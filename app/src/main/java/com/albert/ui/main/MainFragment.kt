package com.albert.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.albert.R
import com.albert.common.server.account.AccountCallback
import com.albert.common.server.account.AccountPathApi
import com.albert.common.server.account.IAccountServer
import com.albert.databinding.FragmentMainBinding
import com.albert.log.local.ALog
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    @Autowired(name = AccountPathApi.Server)
    lateinit var accountServer: IAccountServer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ARouter.getInstance().inject(this)
        binding.vm = viewModel
        binding.btAccount.setOnClickListener {
            accountServer.login(object : AccountCallback {
                override fun onSuccess() {

                }

                override fun onFailed() {
                }

            })
        }
    }

}