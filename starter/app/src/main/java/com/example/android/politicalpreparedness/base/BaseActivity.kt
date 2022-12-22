package com.example.android.politicalpreparedness.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), BaseActivityListener {

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(intent = intent, isNewIntent = false)
        initView()
        initViewModel()
    }

    override fun onStop() {
        super.onStop()
        System.gc()
        Runtime.getRuntime().gc()
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
        Runtime.getRuntime().gc()
    }

    abstract override fun initView()
}

interface BaseActivityListener {
    fun initData(intent: Intent?, isNewIntent: Boolean)
    fun initView()
    fun initViewModel()
}