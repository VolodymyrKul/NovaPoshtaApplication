package com.example.novaposhtaapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*

class TestAutoAct : AppCompatActivity() {

    lateinit var placcesClient: PlacesClient

    var placeField = Arrays.asList(
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.LAT_LNG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_auto)
        initPlaces()
        setupPlacesClient()
    }
    private fun initPlaces(){
        val apiKey = "AIzaSyBgVaPLjlIAY3m6IZfyeALczfLu0xk-Fg4"
        if(!Places.isInitialized()){
            Places.initialize(applicationContext, apiKey)
            //placcesClient = Places.createClient(this)
        }
        placcesClient = Places.createClient(this)
    }
    private fun setupPlacesClient(){
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.testautocomplete_fragment) as AutocompleteSupportFragment
        val arrAuto =
            supportFragmentManager.findFragmentById(R.id.Arrtestautocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setHint("Depart Place")
        autocompleteFragment.setPlaceFields(placeField)

        arrAuto.setHint("Arr Place")
        arrAuto.setPlaceFields(placeField)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                val latLon = p0.latLng
                if (latLon != null) {
                    findViewById<TextView>(R.id.LatLngTV).text =
                        latLon.latitude.toString() + " " + latLon.longitude.toString()
                }
                val add = p0.address
                if (add != null){
                    findViewById<TextView>(R.id.AddressTV).text = add
                }
                //Toast.makeText(this@AutoComActivity, ""+p0.address,Toast.LENGTH_LONG).show()
            }

            override fun onError(p0: Status) {
                Toast.makeText(this@TestAutoAct, ""+p0.statusMessage,Toast.LENGTH_LONG).show()
            }

        })

        arrAuto.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                val latLon = p0.latLng
                if (latLon != null) {
                    findViewById<TextView>(R.id.ArrLatLngTV).text =
                        latLon.latitude.toString() + " " + latLon.longitude.toString()
                }
                val add = p0.address
                if (add != null){
                    findViewById<TextView>(R.id.ArrAddressTV).text = add
                }
                //Toast.makeText(this@AutoComActivity, ""+p0.address,Toast.LENGTH_LONG).show()
            }

            override fun onError(p0: Status) {
                Toast.makeText(this@TestAutoAct, ""+p0.statusMessage,Toast.LENGTH_LONG).show()
            }

        })
            //Toast.makeText(this, "All func load", Toast.LENGTH_LONG).show()
    }
    fun onPlaceSave(view: View){
        if (placeValid()){
            val depPlace = findViewById<TextView>(R.id.AddressTV)
            val arrPlace = findViewById<TextView>(R.id.ArrAddressTV)
            val depLatLng = findViewById<TextView>(R.id.LatLngTV)
            val arrLatLng = findViewById<TextView>(R.id.ArrLatLngTV)
            val intent = Intent()
            intent.putExtra("DepPlace",depPlace.text.toString())
            intent.putExtra("DepLatLng",depLatLng.text.toString())
            intent.putExtra("ArrPlace",arrPlace.text.toString())
            intent.putExtra("ArrLatLng",arrLatLng.text.toString())
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }
    fun placeValid() : Boolean{
        val depPlace = findViewById<TextView>(R.id.AddressTV)
        val arrPlace = findViewById<TextView>(R.id.ArrAddressTV)
        val depLatLng = findViewById<TextView>(R.id.LatLngTV)
        val arrLatLng = findViewById<TextView>(R.id.ArrLatLngTV)
        val defAdd = "Address field"
        val defLatLng = "LatLng field"

        if (TextUtils.equals(depPlace.text,defAdd)){
            Toast.makeText(this, "Choose place", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.equals(arrPlace.text,defAdd)){
            Toast.makeText(this, "Choose place", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.equals(depLatLng.text,defLatLng)){
            Toast.makeText(this, "Choose place", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.equals(arrLatLng.text,defLatLng)){
            Toast.makeText(this, "Choose place", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}
