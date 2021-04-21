package com.bignerdranch.android.kotlin_point.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.bignerdranch.android.kotlin_point.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_main.*

class ForgotPasswordFragment : Fragment() {
    var navController: NavController? = null
    var mAuth: FirebaseAuth? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarReset.visibility = View.GONE
        mAuth = FirebaseAuth.getInstance()

        btnResetEmail.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {
        val userEmail : String = resetEmail.text.toString().trim()


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

        progressBarReset.visibility = View.VISIBLE

        mAuth?.sendPasswordResetEmail(userEmail)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context,
                                R.string.check_email_change_pass, Toast.LENGTH_LONG).show()
                        navController?.navigate(R.id.mainFragment)
                        progressBarReset.visibility = View.GONE
                    } else {
                        Toast.makeText(context,
                                R.string.try_again_error, Toast.LENGTH_LONG).show()
                        progressBarReset.visibility = View.GONE
                    }
                }
    }
}