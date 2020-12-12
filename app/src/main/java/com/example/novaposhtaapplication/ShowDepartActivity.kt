package com.example.novaposhtaapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ShowDepartActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var db: FirebaseDatabase? = null
    var departures: DatabaseReference? = null

    var departPlace: String? = null
    var departLatLng: String? = null
    var arrivalPlace: String? = null
    var arrivalLatLng: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_depart)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        departures = db!!.getReference("Departures")

        val departImg = findViewById<ImageView>(R.id.ShowDepImage)
        val title = findViewById<TextView>(R.id.TitleTV)
        val desc = findViewById<TextView>(R.id.DescTV)
        val depPlace = findViewById<TextView>(R.id.DepPlaceTV)
        val arrPlace = findViewById<TextView>(R.id.ArrPlaceTV)

        val childName = intent.getIntExtra("DbChild", 0)
        //Toast.makeText(this, childName.toString(), Toast.LENGTH_LONG).show()

        departures!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(childName.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@ShowDepartActivity, "Data not load", Toast.LENGTH_LONG).show()
                }
                override fun onDataChange(p0: DataSnapshot) {
                    HideUI()
                    val depart = p0.getValue(DepartNewModel::class.java)
                    if (depart != null){
                        Picasso.get().load(depart.url).resize(180,180).into(departImg)
                        title.text = depart.title
                        desc.text = depart.desc
                        depPlace.text = depart.departureplace
                        arrPlace.text = depart.arrivalplace
                        departPlace = depart.departureplace
                        departLatLng = depart.departurelatlng
                        arrivalPlace = depart.arrivalplace
                        arrivalLatLng = depart.arrivallatlng
                        //Toast.makeText(this@ShowDepartActivity, "Data all load", Toast.LENGTH_LONG).show()
                    }
                    ShowUI()
                }

            })

    }
    fun HideUI(){
        findViewById<ImageView>(R.id.ShowDepImage).visibility = View.GONE
        findViewById<LinearLayout>(R.id.ShowDepDetail).visibility = View.GONE
        findViewById<LinearLayout>(R.id.ParcelOptionLL).visibility = View.GONE
        findViewById<ProgressBar>(R.id.ShowDepBar).visibility = View.VISIBLE
    }
    fun ShowUI(){
        findViewById<ProgressBar>(R.id.ShowDepBar).visibility = View.GONE
        findViewById<ImageView>(R.id.ShowDepImage).visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.ShowDepDetail).visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.ParcelOptionLL).visibility = View.VISIBLE
    }
    fun MapShow(view: View){
        val intent =  Intent(this, MapsActivity::class.java)
        intent.putExtra("depPlace", departPlace)
        intent.putExtra("depLatLng", departLatLng)
        intent.putExtra("arrPlace", arrivalPlace)
        intent.putExtra("arrLatLng", arrivalLatLng)
        startActivity(intent)
    }
}
