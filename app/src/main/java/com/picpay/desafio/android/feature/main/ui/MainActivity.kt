package com.picpay.desafio.android.feature.main.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.feature.main.viewModel.MainViewModel
import com.picpay.desafio.android.model.StateView
import com.picpay.desafio.android.model.User
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private val adapter: UserListAdapter by inject()
    private var users = ArrayList<User>()

    private val observer = Observer<StateView<List<User>>> { stateView ->
        when (stateView) {
            is StateView.Loading -> {
                binding.scroll.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                binding.clError.visibility = View.GONE
            }
            is StateView.DataLoaded -> {
                binding.progressBar.visibility = View.GONE

                users = ArrayList(stateView.data)
                adapter.users = users
            }
            is StateView.Error -> {
                binding.scroll.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.clError.visibility = View.VISIBLE
                stateError(stateView)
            }
        }
    }

    private fun stateError(stateView: StateView.Error) {
        binding.txtMessage.text = when (stateView.e) {
            is UnknownHostException -> {
                "Verifique sua conexão, por favor"
            }
            else -> {
                "Ocorreu um erro inesperado.\nPor favor, tente novamente."
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.stateView.observe(this, observer)
        viewModel.getUser()

        binding.btnTryAgain.setOnClickListener {
            viewModel.getUser()
        }
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
}
