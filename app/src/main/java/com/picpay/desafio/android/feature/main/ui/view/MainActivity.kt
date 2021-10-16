package com.picpay.desafio.android.feature.main.ui.view

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.feature.base.ui.view.BaseActivity
import com.picpay.desafio.android.feature.main.ui.adapter.UserListAdapter
import com.picpay.desafio.android.feature.main.viewModel.MainViewModel
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.util.extension.*
import com.picpay.desafio.android.util.view.component.EditTextSearch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException

class MainActivity : BaseActivity(), MainActivityView {

    private val viewModel: MainViewModel by viewModel()
    private val adapterUserList: UserListAdapter by inject()
    private val manager: LinearLayoutManager by inject()
    private lateinit var binding: ActivityMainBinding
    private var users = ArrayList<User>()

    private val observerDataLoaded = Observer<Pair<ArrayList<User>, Boolean>> { dataLoaded ->
        setVisibilitySwipeAndError(swipeVisibility = View.VISIBLE, errorVisibility = View.GONE)
        stateDataLoaded(dataLoaded.first, dataLoaded.second)
    }

    private val observerSearchContacts = Observer<ArrayList<User>> { list ->
        stateSearchLoaded(list)
    }

    private val observerError = Observer<Exception> { error ->
        setVisibilitySwipeAndError(swipeVisibility = View.GONE, errorVisibility = View.VISIBLE)
        stateError(error)
    }

    private val observerLoading = Observer<Boolean> { loading ->
        if (loading) showLoading() else stopLoading()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setRecyclerView()
        setEvents()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        if (users.isNullOrEmpty()) {
            viewModel.getUsers()
            setVisibilitySwipeAndError(swipeVisibility = View.GONE, errorVisibility = View.GONE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    override fun setVisibilitySwipeAndError(swipeVisibility: Int, errorVisibility: Int) {
        binding.apply {
            swipe.visibility = swipeVisibility
            clError.visibility = errorVisibility
        }
    }

    override fun stateError(e: Throwable) {
        binding.apply {
            includeError.txtMessage.text = when (e) {
                is UnknownHostException -> {
                    getString(R.string.verify_conection)
                }
                else -> {
                    getString(R.string.unexpected_error)
                }
            }
        }
    }

    override fun stateDataLoaded(list: ArrayList<User>, remote: Boolean) {
        if (remote) {
            binding.txtInformation.gone()
            if (binding.swipe.isRefreshing)
                showSnackBarMessage(message = getString(R.string.update_list))

        } else {
            if (users.isNullOrEmpty()) {
                binding.apply {
                    txtInformation.visible()
                    txtInformation.setTextToUnderline(getString(R.string.locally_loaded_data))
                }
            } else {
                showSnackBarMessage(message = getString(R.string.could_not_update_list))
                return
            }
        }

        binding.apply {
            editTextSearch.clearAndHideKeyboard(triggerTextChange = false)
            binding.txtNotFound.gone()
            progressBar.gone()
            editTextSearch.clSearch.setBackgroundDrawable(R.drawable.backgroud_border_search_grey)
        }

        users = list
        adapterUserList.users = users
    }

    override fun stateSearchLoaded(list: ArrayList<User>) {
        adapterUserList.users = list
        binding.apply {
            recyclerView.scrollToPosition(0)
            Handler().postDelayed({ progressBar.gone() }, 500)

            if (list.isEmpty()) {
                txtNotFound.visible()
                editTextSearch.clSearch.setBackgroundDrawable(R.drawable.backgroud_border_search_red)
            } else {
                txtNotFound.gone()
                if (editTextSearch.editSearch.text.toString().isNotEmpty())
                    editTextSearch.clSearch.setBackgroundDrawable(R.drawable.backgroud_border_search_green)
                else
                    editTextSearch.clSearch.setBackgroundDrawable(R.drawable.backgroud_border_search_grey)
            }
        }
    }

    override fun setRecyclerView() {
        binding.recyclerView.apply {
            adapter = adapterUserList
            layoutManager = manager
        }
    }

    override fun setEvents() {
        binding.apply {
            includeError.btnTryAgain.setOnClickListener {
                viewModel.getUsers()
                setVisibilitySwipeAndError(swipeVisibility = View.GONE, errorVisibility = View.GONE)
                includeError.btnTryAgain.gone()
                includeError.progressBar.visible()

                Handler().postDelayed({
                    includeError.btnTryAgain.visible()
                    includeError.progressBar.gone()
                }, 2000)
            }

            txtInformation.setOnClickListener {
                if (!swipe.isRefreshing) {
                    viewModel.getUsers()
                    swipe.isRefreshing = true
                }
            }

            swipe.setOnRefreshListener {
                viewModel.getUsers()
            }

            editTextSearch.addTextChangedListener = object : EditTextSearch.AddTextChangedListener {
                override fun textChanged(text: String) {
                    progressBar.visible()
                    viewModel.searchContacts(text)
                }
            }

            clMain.setTransitionBackgroundDrawable(R.drawable.transition_main_activity, 2000)
            swipe.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        }
    }

    override fun setObservers() {
        viewModel.listUser.observe(this, observerDataLoaded)
        viewModel.listSearchUser.observe(this, observerSearchContacts)
        viewModel.error.observe(this, observerError)
        viewModel.showLoading.observe(this, observerLoading)
    }

    override fun removeObservers() {
        viewModel.listUser.removeObserver(observerDataLoaded)
        viewModel.listSearchUser.removeObserver(observerSearchContacts)
        viewModel.error.removeObserver(observerError)
        viewModel.showLoading.removeObserver(observerLoading)
    }

    override fun stopLoading() {
        super.stopLoading()
        binding.swipe.isRefreshing = false
    }

    private fun showSnackBarMessage(message: String) {
        showSnackBarMessage(message = message, viewContext = binding.root)
    }
}
