package com.example.virtualleaderboardsnow.presentation.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.FragmentSignInBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            signUp.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
            logIn.setOnClickListener {
                val mail = email.text.toString()
                val pw = password.text.toString()
                Firebase.auth.signInWithEmailAndPassword(mail, pw).addOnSuccessListener {
                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment())
                }.addOnFailureListener {
                    Toasty.error(requireContext(), R.string.invalid_credrentials).show()
                }
            }
        }
    }

}