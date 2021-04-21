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
import com.bignerdranch.android.kotlin_point.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_register_user.*



class RegisterUserFragment : Fragment() {
    var mAuth: FirebaseAuth? = null
    var navController: NavController? = null
    var mBundle: Bundle? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        mBundle = Bundle()
        progressBarReg.visibility = View.GONE

        regUser.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val userEmail : String = editTextRegEmailAddress.text.toString().trim()
        val userPassword : String = editTextRegPassword.text.toString().trim()

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

        progressBarReg.visibility = View.VISIBLE
        mAuth?.createUserWithEmailAndPassword(userEmail, userPassword)
                ?.addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val user = User(userEmail, userPassword)
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().currentUser.uid)
                                .setValue(user).addOnCompleteListener { task1 ->
                                    if (task1.isSuccessful) {
                                        val email: String = editTextRegEmailAddress.text.toString()
                                        mBundle?.putString("TitleArg", email)
                                        navController?.navigate(R.id.mapScreenFragment, mBundle)
                                        Toast.makeText(context, R.string.user_registered,
                                                Toast.LENGTH_LONG).show()
                                        progressBarReg.visibility = View.GONE
                                    } else {
                                        Toast.makeText(context, R.string.not_registered,
                                                Toast.LENGTH_LONG).show()
                                        progressBarReg.visibility = View.GONE
                                    }
                                }
                                     }else {
                                         Toast.makeText(context, R.string.not_registered,
                                         Toast.LENGTH_LONG).show()
                                         progressBarReg.visibility = View.GONE
                                   }
                                }
                    }
                }
