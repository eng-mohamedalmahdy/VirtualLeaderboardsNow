package com.example.virtualleaderboardsnow.presentation.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.FragmentSignUpBinding
import com.example.virtualleaderboardsnow.domain.isValidEmail
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            signIn.setOnClickListener { findNavController().navigateUp() }
            signUp.setOnClickListener {
                if (firstName.text.isNullOrEmpty()) {
                    firstNameContainer.error = getString(R.string.this_field_is_required)
                } else if (lastName.text.isNullOrEmpty()) {
                    lastNameContainer.error = getString(R.string.this_field_is_required)
                } else if (email.text.isNullOrEmpty()) {
                    emailContainer.error = getString(R.string.this_field_is_required)
                } else if (password.text.isNullOrEmpty()) {
                    password.error = getString(R.string.this_field_is_required)
                } else if (email.text.toString().isValidEmail()) {
                    emailContainer.error = getString(R.string.invalid_email_formation)
                } else {
                    lastNameContainer.error = null
                    firstNameContainer.error = null
                    emailContainer.error = null
                    passwordContainer.error = null
                    Firebase.auth.createUserWithEmailAndPassword(
                        email.text.toString(),
                        password.text.toString()
                    ).addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.email_created_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
                    }.addOnFailureListener {
                        Toasty.error(
                            requireContext(),
                            getString(R.string.invalid_inputs),
                            Toast.LENGTH_SHORT,
                            true
                        ).show();

                    }

                }
            }
        }
    }

}