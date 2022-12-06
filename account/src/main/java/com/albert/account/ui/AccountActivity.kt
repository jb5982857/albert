package com.albert.account.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.albert.account.config.RoutePath
import com.albert.account.module.R
import com.albert.account.module.databinding.AccountLayoutBinding
import com.albert.common.compoents.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = RoutePath.ACCOUNT_ACTIVITY_PATH)
class AccountActivity : BaseActivity() {
    private lateinit var dataBinding: AccountLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.account_layout)
        setContentView(dataBinding.root)
    }
}