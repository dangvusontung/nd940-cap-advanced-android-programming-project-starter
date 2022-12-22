package com.example.android.politicalpreparedness.election

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseBindingFragment
import com.example.android.politicalpreparedness.database.LocalRepository
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.base.EventObserver
import com.example.android.politicalpreparedness.network.RemoteRepository

class ElectionsFragment : BaseBindingFragment<ElectionsViewModel, FragmentElectionBinding>() {

    private lateinit var upcomingElectionAdapter: ElectionListAdapter

    private lateinit var savedElectionAdapter: ElectionListAdapter

    //TODO: Add ViewModel values and create ViewModel
    override val viewModel: ElectionsViewModel by lazy {
        ElectionsViewModelFactory(
            RemoteRepository.getInstance(),
            LocalRepository.getInstance(requireContext())
        ).create(ElectionsViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.fragment_election

    override fun getViewModelBindingVariable(): Int = BR.viewModel

    override fun initView() {
        val clickListener = ElectionListener(
            itemClickListener = {
                viewModel.navToVoteInfo(it)
            }
        )
        upcomingElectionAdapter = ElectionListAdapter(clickListener)
        savedElectionAdapter = ElectionListAdapter(clickListener)
        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters
        binding.rvUpcomingElection.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = upcomingElectionAdapter
            isNestedScrollingEnabled = false
        }
        //TODO: Populate recycler adapters
        binding.rvSavedElection.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = savedElectionAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun initViewModel() {

    }

    //TODO: Refresh adapters when fragment loads
    override fun fetchDataAfterViewCreated() {
        super.fetchDataAfterViewCreated()
        viewModel.getElectionData()
    }

    override fun setEventListeners() {
        super.setEventListeners()
        viewModel.upcomingElections.observe(viewLifecycleOwner, EventObserver {
            upcomingElectionAdapter.submitList(it)
        })

        viewModel.savedElections.observe(viewLifecycleOwner, EventObserver {
            savedElectionAdapter.submitList(it)
        })
    }
}