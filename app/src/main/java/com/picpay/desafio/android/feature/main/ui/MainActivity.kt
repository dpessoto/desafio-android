package com.picpay.desafio.android.feature.main.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.picpay.desafio.android.databinding.ActivityMainBinding
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


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private val adapter: UserListAdapter by inject()
    private var users = ArrayList<User>()
    private val getUser = View.OnClickListener { viewModel.getUser() }
    private var dataRemote = true

    private val observer = Observer<StateView<Pair<List<User>, Boolean>>> { stateView ->
        when (stateView) {
            is StateView.Loading -> {
                binding.scroll.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                binding.clError.visibility = View.GONE
            }
            is StateView.DataLoaded -> {
                binding.progressBar.gone()

                users = ArrayList(stateView.data.first)
                adapter.users = users

                if (stateView.data.second) {
                    binding.txtInformation.invisible()
                    if (!dataRemote) {
                        val snack: Snackbar = Snackbar.make(binding.recyclerView, "Lista atualizada com sucesso!", Snackbar.LENGTH_SHORT)
                        val view = snack.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.TOP
                        view.layoutParams = params
                        snack.show()
                    }
                    dataRemote = true
                } else {
                    if (dataRemote) {
                        binding.txtInformation.visible()
                        binding.txtInformation.text = "Dados carregados localmente, clique aqui para atualizar!"
                        binding.txtInformation.toUnderline()
                        dataRemote = false
                    } else {
                        val snack: Snackbar = Snackbar.make(binding.recyclerView, "Não foi possível atualizar a lista, verifique sua conexão", Snackbar.LENGTH_SHORT)
                        val view = snack.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.TOP
                        view.layoutParams = params
                        snack.show()
                    }
                }
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

        binding.btnTryAgain.setOnClickListener(getUser)
        binding.txtInformation.setOnClickListener(getUser)
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
