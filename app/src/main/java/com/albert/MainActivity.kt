package com.albert

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albert.application.lifecycle.annotation.AApplicationLifecycle
import com.albert.application.lifecycle.api.IAApplicationLifecycle
import com.albert.ui.main.MainFragment

@AApplicationLifecycle
class MainActivity : AppCompatActivity(), IAApplicationLifecycle {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}