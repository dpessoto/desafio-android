package com.picpay.desafio.android.util.view.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.picpay.desafio.android.databinding.EditTextSearchBinding
import com.picpay.desafio.android.util.extension.gone
import com.picpay.desafio.android.util.extension.hideKeyboard
import com.picpay.desafio.android.util.extension.visible

class EditTextSearch : ConstraintLayout {

    interface AddTextChangedListener {
        fun textChanged(text: String)
    }

    private lateinit var binding: EditTextSearchBinding
    private var triggerTextChange = false
    var addTextChangedListener: AddTextChangedListener? = null

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
        binding = EditTextSearchBinding.inflate(LayoutInflater.from(context), this, true)

        binding.apply {
            txtCancel.setOnClickListener {
                clearAndHideKeyboard(true)
            }

            imgClear.setOnClickListener {
                editSearch.text?.clear()
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
                    } else {
                        txtCancel.gone()
                    }

                    if (triggerTextChange)
                        addTextChangedListener?.textChanged(text)
                }
            })
        }
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