package com.example.quoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quoto.databinding.FragmentRegistarBinding
import com.example.quoto.models.UserRequest
import com.example.quoto.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistarFragment : Fragment() {

    // binding variable Start
    private var _binding: FragmentRegistarBinding? = null
    private val binding get() = _binding!!
    // binding variable End

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // ----------applying binding Start--------------\\
        _binding = FragmentRegistarBinding.inflate(inflater, container, false)
        // ----------applying binding End--------------\\
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registarFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val validationRes = validateInput()
            if (validationRes.first){
                authViewModel.resUser(getUserRequest())
            }else{
                binding.txtError.text = validationRes.second
            }
        }
        bindObserver()
    }

    private fun getUserRequest() : UserRequest{
        val email = binding.txtEmail.text.toString().trim()
        val username = binding.txtUsername.text.toString().trim()
        val password = binding.txtPassword.text.toString().trim()
        return UserRequest(email,password,username)
    }

    private fun validateInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.email,userRequest.username,userRequest.password,false)
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.pBar.isVisible = false
            when (it) {
                is NetworkResult.Error -> {
                    binding.txtError.text = it.msg
                }
                is NetworkResult.Loading -> {
                    binding.pBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_registarFragment_to_mainFragment)
                }//token remaining
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}