package com.picpay.desafio.android.util.view.component

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.picpay.desafio.android.databinding.EditTextSearchBinding
import com.picpay.desafio.android.util.extension.*
import com.picpay.desafio.android.util.view.KeyboardUtils
import com.picpay.desafio.android.util.view.KeyboardUtils.SoftKeyboardToggleListener

@SuppressLint("ClickableViewAccessibility")
class EditTextSearch : ConstraintLayout {

    interface AddTextChangedListener {
        fun textChanged(text: String)
    }

    private lateinit var binding: EditTextSearchBinding
    private lateinit var cxt: Context
    private var triggerTextChange = false
    private var keyboardVisible = false
    var addTextChangedListener: AddTextChangedListener? = null
        set(value) {
            keyboardListener()
            field = value
        }

    val clSearch by lazy { binding.clSearch }
    val imgSearch by lazy { binding.imgSearch }
    val editSearch by lazy { binding.editSearch }
    val imgClear by lazy { binding.imgClear }
    val imgHide by lazy { binding.imgHide }
    val txtCancel by lazy { binding.txtCancel }

    private fun keyboardListener() {
        KeyboardUtils.addKeyboardToggleListener(cxt, object : SoftKeyboardToggleListener {
            override fun onToggleSoftKeyboard(isVisible: Boolean) {
                keyboardVisible = isVisible
                when {
                    !keyboardVisible -> imgHide.gone()
                    editSearch.text.isNullOrBlank() -> {
                        imgHide.visible()
                    }
                }
            }
        })
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }


    private fun init(context: Context) {
        cxt = context
        binding = EditTextSearchBinding.inflate(LayoutInflater.from(cxt), this, true)

        setEvents()
    }

    private fun setEvents() {
        imgSearch.setOnClickListener {
            editSearch.requestFocus()
            editSearch.showKeyboard()
        }

        txtCancel.setOnClickListener {
            clearAndHideKeyboard(true)
        }

        imgClear.setOnClickListener {
            editSearch.text?.clear()
        }

        imgHide.setOnClickListener {
            clearAndHideKeyboard(false)
        }

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //todo
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //todo
            }

            override fun afterTextChanged(edit: Editable) {
                val text = edit.toString()
                if (text.isNotEmpty()) {
                    txtCancel.visible()
                    imgClear.visible()
                    imgHide.gone()
                } else {
                    txtCancel.gone()
                    imgClear.invisible()
                    if (keyboardVisible)
                        imgHide.visible()
                }

                if (triggerTextChange)
                    addTextChangedListener?.textChanged(text)
            }
        })
    }

    fun clearAndHideKeyboard(triggerTextChange: Boolean) {
        this.triggerTextChange = triggerTextChange
        binding.apply {
            editSearch.text?.clear()
            editSearch.clearFocus()
            txtCancel.hideKeyboard()
        }
        this.triggerTextChange = true
    }
}