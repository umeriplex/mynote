package com.example.quoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.quoto.databinding.FragmentRegistarBinding

class RegistarFragment : Fragment() {

    // binding variable Start
    private var _binding : FragmentRegistarBinding? = null
    private val binding get() = _binding!!
    // binding variable End

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View? {
        // ----------applying binding Start--------------\\
        _binding = FragmentRegistarBinding.inflate(inflater,container,  false)
        // ----------applying binding End--------------\\

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}