package com.bignerdranch.android.kotlin_point.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.kotlin_point.R
import com.bignerdranch.android.kotlin_point.api.Api
import com.bignerdranch.android.kotlin_point.api.Request
import com.bignerdranch.android.kotlin_point.data.PlacesItem
import com.bignerdranch.android.kotlin_point.fragment.childFragment.MapFragment
import com.bignerdranch.android.kotlin_point.recyclerview.RecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_map_screen.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.ArrayList


class MapScreenFragment : Fragment() {

    var navController: NavController? = null
    private var places1 = mutableListOf<PlacesItem>()               // так можно или лучше как-то по другому?


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.layoutManager = GridLayoutManager(activity, 1) //activity??
        recyclerview.adapter = RecyclerViewAdapter(places1)
        navController = Navigation.findNavController(view)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navController?.navigate(R.id.mainFragment)
        }

        val fragment: Fragment = MapFragment()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.map_layout, fragment)
            .commit()

        val iEmail: String = arguments?.get("TitleArg") as String               //нужно явно указывать что принимаем String?
        val cleanTitle = iEmail.substring(0, iEmail.indexOf('@'))
        email_title.text = cleanTitle
        startRequest()
    }

    private fun startRequest() {
        val api: Api? = Request.buildRetrofitConfig()

        GlobalScope.launch(Dispatchers.Main){
           val response = api?.getMapResult()?.awaitResponse()                             //Coroutine??
            if (response!!.isSuccessful) {
                places1 = ArrayList<PlacesItem>(response.body()?.places)
                recyclerview.adapter = RecyclerViewAdapter(places1)
            } else {
                Toast.makeText(context, R.string.try_again_error, Toast.LENGTH_LONG).show()
            }
        }
    }
}