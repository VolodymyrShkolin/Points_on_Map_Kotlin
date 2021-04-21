package com.bignerdranch.android.kotlin_point.fragment.childFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bignerdranch.android.kotlin_point.R
import com.bignerdranch.android.kotlin_point.api.Api
import com.bignerdranch.android.kotlin_point.api.Request
import com.bignerdranch.android.kotlin_point.data.PlacesItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.*

class MapFragment : Fragment() {
    private  var places: List<PlacesItem> = mutableListOf()           //?


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startRequest()
    }

    private fun mapRequest() {
        val mapFragment = childFragmentManager.
        findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            for (i in places.indices) {
                val lat: Double? = places[i].lat
                val lng: Double? = places[i].lng
                val mLatLng = LatLng(lat!!, lng!!)
                val markerOptions = MarkerOptions()
                markerOptions.position(mLatLng)
                markerOptions.title(places[i].name)
                googleMap.addMarker(markerOptions)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 13f))
            }
        }
    }


    private fun startRequest(){
        val api: Api? = Request.buildRetrofitConfig()

        GlobalScope.launch(Dispatchers.Main){
            val response = api?.getMapResult()?.awaitResponse()                             //Coroutine??
            if (response!!.isSuccessful) {
                    places = ArrayList<PlacesItem>(response.body()?.places)
                    mapRequest()
                } else {
                    Toast.makeText(context, R.string.try_again_error, Toast.LENGTH_LONG).show()
                }
        }
    }
}