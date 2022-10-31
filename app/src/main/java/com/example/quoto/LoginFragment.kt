package com.example.quoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quoto.databinding.FragmentLoginBinding
import com.example.quoto.databinding.FragmentRegistarBinding
import com.example.quoto.models.UserRequest
import com.example.quoto.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    // binding variable Start
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    // binding variable End

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // ----------applying binding Start--------------\\
        _binding = FragmentLoginBinding.inflate(inflater,container,  false)
        // ----------applying binding End--------------\\

        if (tokenManager.getToken() !=null){
            findNavController().navigate(R.id.action_registarFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.btnLogin.setOnClickListener {
            val validInput = validateInput()
            if (validInput.first){
                authViewModel.loginUser(getUserRequest())
            }else{
                binding.txtError.text = validInput.second
            }
        }
        bindObserver()
    }

    private fun getUserRequest() : UserRequest {
        val email = binding.txtEmail.text.toString().trim()
        val password = binding.txtPassword.text.toString().trim()
        return UserRequest(email,password,"")
    }

    private fun validateInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.email,userRequest.username,userRequest.password,true)
    }

    private fun bindObserver() {
        authViewModel.userLivedata.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Error -> {
                    binding.txtError.text = it.msg
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}