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
import com.picpay.desafio.android.model.StateView
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.util.extension.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException

class MainActivity : BaseActivity(), MainActivityView {

    private val viewModel: MainViewModel by viewModel()
    private val adapterUserList: UserListAdapter by inject()
    private val manager: LinearLayoutManager by inject()
    private lateinit var binding: ActivityMainBinding
    private var users = listOf<User>()
    private var dataRemote = true

    private val observerData = Observer<StateView<Pair<List<User>, Boolean>>> { stateView ->
        when (stateView) {
            is StateView.DataLoaded -> {
                setVisibilitySwipeAndError(swipeVisibility = View.VISIBLE, errorVisibility = View.GONE)
                stateDataLoaded(stateView.data.first, stateView.data.second)
            }
            is StateView.Error -> {
                setVisibilitySwipeAndError(swipeVisibility = View.GONE, errorVisibility = View.VISIBLE)
                stateError(stateView.e)
            }
            else -> {
                setVisibilitySwipeAndError(swipeVisibility = View.GONE, errorVisibility = View.VISIBLE)
                stateError(Exception())
            }
        }
    }

    private val observerLoading = Observer<Boolean> { loading ->
        if (loading) showLoading() else stopLoading()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setRecyclerView()
        setEvents()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        if (users.isNullOrEmpty()) {
            viewModel.getUser()
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
            swipe.isRefreshing = false
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

    override fun stateDataLoaded(list: List<User>, remote: Boolean) {
        if (remote) {
            binding.txtInformation.invisible()
            if (!dataRemote || binding.swipe.isRefreshing)
                showSnackBarMessage(message = getString(R.string.update_list), viewContext = binding.root)

            dataRemote = true
        } else {
            if (dataRemote && users.isNullOrEmpty()) {
                binding.apply {
                    txtInformation.visible()
                    txtInformation.setTextToUnderline(getString(R.string.locally_loaded_data))
                }
                dataRemote = false
            } else {
                showSnackBarMessage(message = getString(R.string.could_not_update_list), viewContext = binding.root)
            }
        }

        binding.swipe.isRefreshing = false

        users = list
        adapterUserList.users = users
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
                viewModel.getUser()
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
                    viewModel.getUser()
                    swipe.isRefreshing = true
                }
            }

            swipe.setOnRefreshListener {
                viewModel.getUser()
            }

            clMain.setTransitionBackgroundDrawable(R.drawable.transition_main_activity, 2000)
            swipe.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        }
    }

    override fun setObservers() {
        viewModel.stateView.observe(this, observerData)
        viewModel.showLoading.observe(this, observerLoading)
    }

    override fun removeObservers() {
        viewModel.stateView.removeObserver(observerData)
        viewModel.showLoading.removeObserver(observerLoading)
    }
}
