package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class VoterInfoFragment : Fragment() {
    private val viewModel: VoterInfoViewModel by viewModel()
    private val args by navArgs<VoterInfoFragmentArgs>()
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentVoterInfoBinding>(inflater, R.layout.fragment_voter_info, container, false)
        binding.lifecycleOwner = this
        viewModel.getVoterInfo(args.argElectionId, args.argDivision)
        binding.viewModel = viewModel
        viewModel.isSaved.observe(viewLifecycleOwner, Observer{
            binding.button.setText(if(it) R.string.unfollow_election else R.string.follow_election)
        })
        binding.stateLocations.setOnClickListener{onClickVotingLocations()}
        binding.stateBallot.setOnClickListener{onClickBallotInformation()}
        return binding.root
    }

    fun onClickVotingLocations() {
        viewModel.voterInfo?.votingLocationFinderUrl?.let {
            startActivity(requireActivity(), Intent(Intent.ACTION_VIEW, Uri.parse(it)), null)
        }
    }

    fun onClickBallotInformation() {
        viewModel.voterInfo?.ballotInfoUrl?.let {
            startActivity(requireActivity(), Intent(Intent.ACTION_VIEW, Uri.parse(it)), null)
        }
    }

}