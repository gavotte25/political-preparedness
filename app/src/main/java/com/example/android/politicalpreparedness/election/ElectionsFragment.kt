package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.ElectionsViewModel.Companion.getDirection
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment: Fragment() {

    private val viewModel: ElectionsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentElectionBinding>(inflater, R.layout.fragment_election, container, false)
        binding.lifecycleOwner = this

        val listener = ElectionListener {
            findNavController().navigate(getDirection(it))
        }

        val upComingAdapter = ElectionListAdapter(listener)
        val savedAdapter = ElectionListAdapter(listener)
        binding.viewModel = viewModel
        binding.upcomingRecycler.adapter = upComingAdapter
        binding.savedRecycler.adapter = savedAdapter
        binding.executePendingBindings()
        return binding.root
    }
}