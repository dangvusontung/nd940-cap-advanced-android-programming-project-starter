package com.example.android.politicalpreparedness.base

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.DialogProgressLoadingBinding
import com.example.android.politicalpreparedness.navigator.NavigationCommand
import com.example.android.politicalpreparedness.navigator.NavigatorImpl
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
abstract class BaseBindingFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {
    protected abstract val viewModel: VM
    protected lateinit var binding: DB
    private val navigator by lazy { NavigatorImpl() }

    private val progressDialogBinding: DialogProgressLoadingBinding by lazy {
        DialogProgressLoadingBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private val progressDialog: Dialog by lazy {
        Dialog(requireContext()).apply {
            window?.setBackgroundDrawableResource(R.color.transparent)
            setContentView(progressDialogBinding.root)
            setCancelable(false)
        }
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int
    protected abstract fun getViewModelBindingVariable(): Int
    protected abstract fun initView()
    protected abstract fun initViewModel()
    protected open fun initViewInstance() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.setContext(requireContext())
        viewModel.setNavigator(navigator)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(getViewModelBindingVariable(), viewModel)
        }
        initViewInstance()
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        fetchDataAfterViewCreated()
        setEventListeners()
        viewModel.navigateEvent.observeSingle(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                it?.let { command ->
                    navigate(command)
                }
            }
        }

        viewModel.loadingEvent.observeSingle(viewLifecycleOwner) {
            it?.let { show ->
                if (show) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            }
        }
    }

    protected open fun fetchDataAfterViewCreated() {

    }

    protected open fun setEventListeners() {
        viewModel.loadingEvent.observeSingle(viewLifecycleOwner) {
            it?.let { show ->
                if (show) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            }
        }
    }

    fun navigate(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.To -> {
                with(findNavController()) {
                    val actionId = command.directions.actionId
                    val action =
                        currentDestination?.getAction(actionId) ?: graph.getAction(actionId)
                    if (action != null && currentDestination?.id != action.destinationId) {
                        navigate(command.directions)
                    }
                }
            }
            is NavigationCommand.Back -> {
                with(findNavController()) { popBackStack() }
            }
            is NavigationCommand.ToRoot -> {
                findNavController().popBackStack(
                    findNavController().graph.startDestination,
                    false
                )
            }
            else -> {
                Log.e("BaseBindingFragment","unknown define command")
            }
        }
    }
}
