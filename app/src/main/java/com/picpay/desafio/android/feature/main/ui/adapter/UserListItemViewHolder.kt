package com.picpay.desafio.android.feature.main.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.util.extension.gone
import com.picpay.desafio.android.util.extension.loadImage
import com.picpay.desafio.android.util.extension.visible

class UserListItemViewHolder(
    private val binding: ListItemUserBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.apply {
            name.text = user.name
            username.text = user.username
            progressBar.visible()
            picture.loadImage(
                user.img,
                R.drawable.ic_round_account_circle,
                { progressBar.gone() },
                { progressBar.gone() })
        }
    }
}