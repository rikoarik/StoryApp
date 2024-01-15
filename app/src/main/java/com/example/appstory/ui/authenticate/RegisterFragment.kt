package com.example.appstory.ui.authenticate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.example.appstory.R
import com.example.appstory.data.api.ApiService
import com.example.appstory.data.model.UserBodyModel
import com.example.appstory.ui.viewmodel.RegisterViewModel

class RegisterFragment : Fragment(), OnBackPressedListener {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvToLogin: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var registerViewModel: RegisterViewModel

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    override fun onBackPressed() {
        val authActivity = requireActivity() as AuthActivity
        authActivity.navigateToAuthActivity()
    }

    private fun navigateToLoginFragment() {
        val loginFragment = LoginFragment.newInstance()
        val loginToRegisterButtonTransitionName = getString(R.string.transition_register_to_login_button)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, loginFragment)
        transaction.addToBackStack(null)
        transaction.addSharedElement(tvToLogin, loginToRegisterButtonTransitionName)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val moveTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = moveTransition
        sharedElementReturnTransition = moveTransition
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        registerViewModel.registerResult.observe(this, Observer { registerResponse ->
            val message = registerResponse.message
            progressBar.visibility = View.GONE
            navigateToLoginFragment()
            Toast.makeText(requireContext(), "Register $message", Toast.LENGTH_SHORT).show()
        })
        registerViewModel.error.observe(this, Observer { _ ->
            progressBar.visibility = View.GONE
        })

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        tvToLogin = view.findViewById(R.id.tvToLogin)
        progressBar = view.findViewById(R.id.progressBar)

        btnRegister.setOnClickListener {
            register()
        }

        tvToLogin.setOnClickListener {
            navigateToLoginFragment()
        }

        return view
    }

    private fun register() {
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (name.isEmpty()) {
            etName.error = "Please enter your name"
            return
        }

        val registerModel = UserBodyModel(name, email, password)
        progressBar.visibility = View.VISIBLE
        registerViewModel.register(registerModel)
    }
}

interface OnBackPressedListener {
    fun onBackPressed()
}
