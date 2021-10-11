package com.picpay.desafio.android.feature.base.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ActivityBaseBinding
import com.picpay.desafio.android.util.extension.gone
import com.picpay.desafio.android.util.extension.visible

open class BaseActivity : AppCompatActivity(), BaseActivityView {
    private lateinit var binding: ActivityBaseBinding
    private var goingToBackground = true

    private val animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.animation_loading)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
    }

    override fun showLoading() {
        try {
            addView()
            binding.imageView.startAnimation(animation)
        } catch (e: Exception) {
            Log.e("showLoading", e.message + "")
        }
    }

    override fun stopLoading() {
        try {
            removeView()
            binding.imageView.clearAnimation()
        } catch (e: Exception) {
            Log.e("stopLoading", e.message + "")
        }
    }

    private fun addView() {
        (window.decorView as ViewGroup).addView(binding.clMain)
        binding.imageView.visible()
    }

    private fun removeView() {
        (window.decorView as ViewGroup).removeView(binding.clMain)
        binding.imageView.gone()
    }

    override fun showSnackBarMessage(viewContext: View, message: String, postion: Int) {
        val snack: Snackbar = Snackbar.make(viewContext, message, Snackbar.LENGTH_SHORT)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snack.show()
    }

    override fun onResume() {
        super.onResume()
        goingToBackground = true
        try {
            removeView()
        } catch (e: Exception) {
            Log.e("onResume", e.message + "")
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            if (goingToBackground)
                addView()
        } catch (e: Exception) {
            Log.e("onPause", e.message + "")
        }
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        goingToBackground = false
    }

    override fun finish() {
        super.finish()
        goingToBackground = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goingToBackground = false
    }

}