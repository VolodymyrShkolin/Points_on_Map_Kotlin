package com.bignerdranch.android.kotlin_point.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bignerdranch.android.kotlin_point.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    var mAuth: FirebaseAuth? = null
    var navController: NavController? = null
    var mBundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        mBundle = Bundle()

        progressBarMain.visibility = View.GONE

        tvRegister.setOnClickListener {navController?.navigate(R.id.registerUserFragment)}
        log.setOnClickListener {userLogin()}
        tvForgot_PasswordMain.setOnClickListener {navController?.navigate(R.id.forgotPasswordFragment)}
    }

    private fun userLogin(){
            val userEmail : String = editTextEmailAddress.text.toString().trim()
            val userPassword : String = editTextPassword.text.toString().trim()

        if (userEmail.isEmpty()) {
            editTextEmailAddress.error = getString(R.string.enter_email)
            editTextEmailAddress.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            editTextEmailAddress.error = getString(R.string.enter_existing_email)
            editTextEmailAddress.requestFocus()
            return
        }

        if (userPassword.isEmpty()) {
            editTextPassword.error = getString(R.string.enter_pass)
            editTextPassword.requestFocus()
            return
        }

        if (userPassword.length < 6) {
            editTextPassword.error = getString(R.string.short_pass)
            editTextPassword.requestFocus()
            return
        }

        progressBarMain.visibility = View.VISIBLE

        mAuth?.signInWithEmailAndPassword(userEmail, userPassword)
            ?.addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val user = FirebaseAuth.getInstance().currentUser
                    if(user.isEmailVerified){
                        mBundle?.putString("TitleArg", editTextEmailAddress.text.toString())
                        navController?.navigate(R.id.mapScreenFragment, mBundle)
                    }else{
                        user.sendEmailVerification()
                        Toast.makeText(context, R.string.verify_account, Toast.LENGTH_LONG).show()
                    }
                    progressBarMain.visibility = View.GONE
                }else{
                    Toast.makeText(context, R.string.error_pass_email, Toast.LENGTH_LONG).show()
                    progressBarMain.visibility = View.GONE
                }
            }
    }
}