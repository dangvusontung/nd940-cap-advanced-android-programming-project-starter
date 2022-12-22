package com.example.android.politicalpreparedness.launch

import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseBindingFragment
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding

class LaunchFragment : BaseBindingFragment<LaunchViewModel, FragmentLaunchBinding>() {

    override val viewModel: LaunchViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.fragment_launch

    override fun getViewModelBindingVariable(): Int = BR.viewModel

    override fun initView() {

    }

    override fun initViewModel() {

    }

}
