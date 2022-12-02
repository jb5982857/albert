package com.albert.account.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.albert.account.module.R
import com.albert.account.module.databinding.AccountLayoutBinding

class AccountActivity : AppCompatActivity() {
    private lateinit var dataBinding: AccountLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.account_layout)
        setContentView(dataBinding.root)
    }
}