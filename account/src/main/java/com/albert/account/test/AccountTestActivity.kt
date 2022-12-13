package com.albert.account.test

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.albert.account.api.AccountServerImpl
import com.albert.account.module.R
import com.albert.common.server.account.AccountCallback
import com.albert.common.server.account.AccountPathApi
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter

class AccountTestActivity : AppCompatActivity() {
    @Autowired(name = AccountPathApi.Server)
    lateinit var accountServerImpl: AccountServerImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_test_layout)
        ARouter.getInstance().inject(this)
    }

    fun login(view: View) {
        accountServerImpl.login(object : AccountCallback {
            override fun onSuccess() {

            }

            override fun onFailed() {
            }

        })
    }
}