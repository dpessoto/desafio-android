package com.picpay.desafio.android.feature.main.ui.view

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.feature.main.ui.adapter.UserListAdapter
import com.picpay.desafio.android.feature.main.viewModel.MainViewModel
import com.picpay.desafio.android.model.StateView
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.util.extension.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException

class MainActivity : AppCompatActivity(), MainActivityView {

    private val viewModel: MainViewModel by viewModel()
    private val adapter: UserListAdapter by inject()
    private lateinit var binding: ActivityMainBinding
    private var users = listOf<User>()
    private var dataRemote = true

    private val observer = Observer<StateView<Pair<List<User>, Boolean>>> { stateView ->
        when (stateView) {
            is StateView.Loading -> {
                setVisibilitySwipeAndError(swipeVisibility = View.VISIBLE, errorVisibility = View.GONE)
            }
            is StateView.DataLoaded -> {
                stateDataLoaded(stateView.data.first, stateView.data.second)
            }
            is StateView.Error -> {
                setVisibilitySwipeAndError(swipeVisibility = View.GONE, errorVisibility = View.VISIBLE)
                stateError(stateView.e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setRecylerView()
        setEvents()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        if (users.isNullOrEmpty())
            viewModel.getUser()
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

    override fun showSnackBarMessage(message: String) {
        val snack: Snackbar = Snackbar.make(binding.recyclerView, message, Snackbar.LENGTH_SHORT)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snack.show()
    }

    override fun stateError(e: Throwable) {
        binding.apply {
            progressBar.gone()
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
        binding.progressBar.gone()

        users = list
        adapter.users = users

        if (remote) {
            binding.txtInformation.invisible()
            if (!dataRemote || binding.swipe.isRefreshing)
                showSnackBarMessage(message = getString(R.string.update_list))

            dataRemote = true
        } else {
            if (dataRemote) {
                binding.apply {
                    txtInformation.visible()
                    txtInformation.text = getString(R.string.locally_loaded_data)
                    txtInformation.toUnderline()
                }
                dataRemote = false
            } else {
                showSnackBarMessage(message = getString(R.string.could_not_update_list))
            }
        }
        binding.swipe.isRefreshing = false
    }

    override fun setRecylerView() {
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun setEvents() {
        binding.apply {
            includeError.btnTryAgain.setOnClickListener {
                viewModel.getUser()
                includeError.btnTryAgain.gone()
                includeError.progressBar.visible()
                progressBar.visible()

                Handler().postDelayed({
                    includeError.btnTryAgain.visible()
                    includeError.progressBar.gone()
                }, 2000)
            }

            txtInformation.setOnClickListener {
                viewModel.getUser()
            }

            swipe.setOnRefreshListener {
                viewModel.getUser()
            }

            clMain.setTransitionBackgroundDrawble(R.drawable.transition_main_activity, 2000)
        }
    }

    override fun setObservers() {
        viewModel.stateView.observe(this, observer)
    }

    override fun removeObservers() {
        viewModel.stateView.removeObserver(observer)
    }
}
