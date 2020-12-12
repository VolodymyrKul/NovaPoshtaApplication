package com.example.novaposhtaapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import com.google.maps.android.PolyUtil

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //var depPlace: String? = null
    //var depLatLng: String? = null
    //var arrPlace: String? = null
    //var arrLatLng: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        //depPlace = intent.getStringExtra("depPlace")
        //depLatLng = intent.getStringExtra("depLatLng")
        //arrPlace = intent.getStringExtra("arrPlace")
        //arrLatLng = intent.getStringExtra("arrLatLng")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val depPlace = intent.getStringExtra("depPlace")
        val depLatLng = intent.getStringExtra("depLatLng")
        val arrPlace = intent.getStringExtra("arrPlace")
        val arrLatLng = intent.getStringExtra("arrLatLng")

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        //val test = "1.24 1.35"
        //val rez = test.substringBefore(' ')
        //val dbl = rez.toDoubleOrNull()

        var depTitle: String = "Ayala"
        var arrTitle: String = "SM City"
        var latLngOrigin = LatLng(10.3181466, 123.9029382) // Ayala
        var latLngDestination = LatLng(10.311795,123.915864) // SM City
        if (depLatLng != null){
            val depLat = depLatLng.substringBefore(' ').toDoubleOrNull()
            val depLng = depLatLng.substringAfter(' ').toDoubleOrNull()
            val arrLat = arrLatLng.substringBefore(' ').toDoubleOrNull()
            val arrLng = arrLatLng.substringAfter(' ').toDoubleOrNull()
            if (depLat != null && depLng != null){
                latLngOrigin = LatLng(depLat, depLng)
            }
            if (arrLat != null && arrLng != null){
                latLngDestination = LatLng(arrLat, arrLng)
            }
        }
        if (depPlace != null){
            depTitle = depPlace
        }
        if (arrPlace != null){
            arrTitle = arrPlace
        }
        //val latLngOrigin = LatLng(10.3181466, 123.9029382) // Ayala
        //val latLngDestination = LatLng(10.311795,123.915864) // SM City
        mMap.addMarker(MarkerOptions().position(latLngOrigin).title(depTitle))
        mMap.addMarker(MarkerOptions().position(latLngDestination).title(arrTitle))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))

        val apiKey = "AIzaSyBWasbZ771Pnm21sHPVarQX2iT2pTRmwRw"
        val path: MutableList<List<LatLng>> = ArrayList()
        val originurl = latLngOrigin.latitude.toString()+","+latLngOrigin.longitude.toString()
        val desturl= latLngDestination.latitude.toString()+","+latLngDestination.longitude.toString()
        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=${originurl}&destination=${desturl}&key=${apiKey}"
        val directionsRequest = object : StringRequest(Request.Method.GET, urlDirections, Response.Listener<String> {
                response ->
            val jsonResponse = JSONObject(response)
            // Get routes
            //Toast.makeText(this, jsonResponse.toString(),Toast.LENGTH_LONG).show()
            val routes = jsonResponse.getJSONArray("routes")
            val legs = routes.getJSONObject(0).getJSONArray("legs")
            val steps = legs.getJSONObject(0).getJSONArray("steps")

            for (i in 0 until steps.length()) {
                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                path.add(PolyUtil.decode(points))
            }
            for (i in 0 until path.size) {
                mMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
                //this.googleMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
            }
        }, Response.ErrorListener {
                _ ->
        }){}
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)

    }
}
