package com.example.quoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.quoto.adapters.NoteAdapter
import com.example.quoto.databinding.FragmentMainBinding
import com.example.quoto.databinding.FragmentRegistarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    // binding variable Start
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    // binding variable End

    private val noteViewModel by viewModels<NotesViewModel>()

    private lateinit var noteAdapter : NoteAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // ----------applying binding Start--------------\\
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        // ----------applying binding End--------------\\

        noteAdapter = NoteAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        noteViewModel.getNote()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = noteAdapter
    }

    private fun bindObservers() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Error -> Toast.makeText(requireContext(),it.msg.toString(),Toast.LENGTH_LONG).show()
                is NetworkResult.Loading -> binding.progressBar.isVisible = true
                is NetworkResult.Success -> noteAdapter.submitList(it.data)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}