package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseBindingFragment
import com.example.android.politicalpreparedness.database.LocalRepository
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.RemoteRepository


class VoterInfoFragment : BaseBindingFragment<VoterInfoViewModel, FragmentVoterInfoBinding>() {

    //TODO: Add ViewModel values and create ViewModel
    override val viewModel: VoterInfoViewModel by lazy {
        VoterInfoViewModelFactory(
            MyApplication.instance,
            RemoteRepository.getInstance(),
            LocalRepository.getInstance(requireContext()))
            .create(VoterInfoViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.fragment_voter_info

    override fun getViewModelBindingVariable(): Int = BR.viewModel

    override fun initView() {
    }

    override fun initViewModel() {
    }

    override fun setEventListeners() {
        super.setEventListeners()
        //TODO: Handle loading of URLs

        viewModel.urlClickEvent.observe(viewLifecycleOwner) {
            loadUrl(it)
        }

        viewModel.databaseFailureEvent.observe(viewLifecycleOwner) { isRemove ->
            Toast.makeText(requireContext(), if (isRemove) R.string.remove_election_failure else R.string.save_election_failure, Toast.LENGTH_SHORT).show()
        }

        viewModel.databaseSuccessEvent.observe(viewLifecycleOwner) { isRemove ->
            Toast.makeText(requireContext(), if (isRemove) R.string.remove_election_success else R.string.save_election_success, Toast.LENGTH_SHORT).show()
        }
    }

    override fun fetchDataAfterViewCreated() {
        super.fetchDataAfterViewCreated()
        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        val args = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val election = args.argElection
        viewModel.getVoterInfo(election)
    }

    //TODO: Create method to load URL intents
    private fun loadUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}