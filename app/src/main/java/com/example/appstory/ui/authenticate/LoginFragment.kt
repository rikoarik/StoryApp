package com.example.appstory.ui.authenticate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.example.appstory.R
import com.example.appstory.ui.dashboard.MainActivity
import com.example.appstory.ui.viewmodel.LoginViewModel
import com.example.appstory.utils.SharedPreferencesHelper


class LoginFragment : Fragment() {

    private lateinit var tvEmailLogin: EditText
    private lateinit var tvPasswordLogin: EditText
    private lateinit var btLogin: Button
    private lateinit var btRegister: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var loginViewModel: LoginViewModel
    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val moveTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = moveTransition
        sharedElementReturnTransition = moveTransition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        tvEmailLogin = view.findViewById(R.id.tvEmailLogin)
        tvPasswordLogin = view.findViewById(R.id.tvPasswordLogin)
        btLogin = view.findViewById(R.id.btLogin)
        btRegister = view.findViewById(R.id.btToRegister)
        progressBar = view.findViewById(R.id.progressBar)

        btLogin.setOnClickListener {
            val email = tvEmailLogin.text.toString()
            val password = tvPasswordLogin.text.toString()

            loginViewModel.login(email, password)
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner) { loginResponse ->
            btLogin.isEnabled = false
            val token = loginResponse.loginResult.token
            loginViewModel.saveLoginStatus(sharedPreferencesHelper, token)
            progressBar.visibility = View.VISIBLE
            val intent = Intent(context, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), btLogin, "transition_login_button")
            context?.startActivity(intent, options.toBundle())
            requireActivity().finishAffinity()
        }

        loginViewModel.error.observe(viewLifecycleOwner) { error ->
            progressBar.visibility = View.GONE
            btLogin.isEnabled = true
        }
        btRegister.setOnClickListener {
            navigateToRegister()
        }


        return view
    }

    private fun navigateToRegister() {
        val registerFragment = RegisterFragment.newInstance()
        val loginToRegisterButtonTransitionName = getString(R.string.transition_login_to_register_button)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, registerFragment)
        transaction.addToBackStack(null)
        transaction.addSharedElement(btLogin, loginToRegisterButtonTransitionName)
        transaction.commit()
    }
}
