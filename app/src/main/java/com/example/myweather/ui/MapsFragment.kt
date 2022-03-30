package com.example.myweather.ui

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myweather.R
import com.example.myweather.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.next.setOnClickListener {
            val bundle = Bundle()
            bundle.putDouble("lat", lat)
            bundle.putDouble("lon", lon)
            findNavController().previousBackStackEntry?.savedStateHandle?.set("key", bundle)
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val latlan = LatLng(41.33875457507973, 69.29855325165997)
        marker = mMap.addMarker(MarkerOptions().position(latlan).title("marker 1"))!!
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlan, 12.0f))
    }

    var lat = 0.0
    var lon = 0.0

    override fun onMapClick(p0: LatLng) {

        marker.remove()
        binding.next.visibility = View.VISIBLE
        lat = p0.latitude
        lon = p0.longitude
        val latlan = LatLng(lat, lon)

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geocoder.getFromLocation(lat, lon, 1)

        var result = address[0].getAddressLine(0).toString()
        result = result.substring(0, result.indexOf(','))


        Log.d("TAG", "onMapClick: $result")

        marker = mMap.addMarker(MarkerOptions().position(latlan).title(result))!!

    }


    override fun onMarkerClick(p0: Marker): Boolean {

        return false
    }


}