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
import com.picpay.desafio.android.util.extension.gone
import com.picpay.desafio.android.util.extension.invisible
import com.picpay.desafio.android.util.extension.toUnderline
import com.picpay.desafio.android.util.extension.visible
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
                setVisibilityScrollAndError(scroll = View.VISIBLE, error = View.GONE)
            }
            is StateView.DataLoaded -> {
                stateDataLoaded(stateView.data.first, stateView.data.second)
            }
            is StateView.Error -> {
                setVisibilityScrollAndError(scroll = View.GONE, error = View.VISIBLE)
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
        setClicks()

        viewModel.stateView.observe(this, observer)
    }

    override fun onResume() {
        super.onResume()
        if (users.isNullOrEmpty())
            viewModel.getUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stateView.removeObserver(observer)
    }

    override fun setVisibilityScrollAndError(scroll: Int, error: Int) {
        binding.scroll.visibility = scroll
        binding.progressBar.visibility = scroll
        binding.recyclerView.visibility = scroll
        binding.clError.visibility = error
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
        binding.includeError.txtMessage.text = when (e) {
            is UnknownHostException -> {
                getString(R.string.verify_conection)
            }
            else -> {
                getString(R.string.unexpected_error)
            }
        }
    }

    override fun stateDataLoaded(list: List<User>, remote: Boolean) {
        binding.progressBar.gone()

        users = list
        adapter.users = users

        if (remote) {
            binding.txtInformation.invisible()
            if (!dataRemote)
                showSnackBarMessage(message = getString(R.string.update_list))

            dataRemote = true
        } else {
            if (dataRemote) {
                binding.txtInformation.visible()
                binding.txtInformation.text = getString(R.string.locally_loaded_data)
                binding.txtInformation.toUnderline()
                dataRemote = false
            } else {
                showSnackBarMessage(message = getString(R.string.could_not_update_list))
            }
        }
    }

    override fun setRecylerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun setClicks() {
        binding.includeError.btnTryAgain.setOnClickListener {
            viewModel.getUser()
            binding.includeError.btnTryAgain.gone()
            binding.includeError.progressBar.visible()

            Handler().postDelayed({
                binding.includeError.btnTryAgain.visible()
                binding.includeError.progressBar.gone()
            }, 2000)
        }

        binding.txtInformation.setOnClickListener {
            viewModel.getUser()
        }
    }
}
