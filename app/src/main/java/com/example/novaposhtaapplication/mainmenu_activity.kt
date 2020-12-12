package com.example.novaposhtaapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mainmenu_activity.*

class mainmenu_activity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var db: FirebaseDatabase? = null
    var images: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu_activity)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        images = db!!.getReference("ProfileImages")

        images!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@mainmenu_activity, "Image not load", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    HideImage()
                    val userImage = p0.getValue(ProfileImgModel::class.java)
                    if (userImage != null){
                        val imageView = findViewById<ImageView>(R.id.MainProfileImage)
                        Picasso.get().load(userImage.imgUrl).resize(140,180).into(imageView)
                    }
                    ShowImage()
                }

            })
    }
    fun HideImage(){
        findViewById<ImageView>(R.id.MainProfileImage).visibility = View.GONE
        findViewById<ProgressBar>(R.id.MenuProgress).visibility = View.VISIBLE
    }
    fun ShowImage(){
        findViewById<ProgressBar>(R.id.MenuProgress).visibility = View.GONE
        findViewById<ImageView>(R.id.MainProfileImage).visibility = View.VISIBLE
    }
    fun departuresClick(view: View){
        val intent = Intent(this, DeparturesActivity::class.java)
        startActivity(intent)
    }
    fun createParcel(view: View){
        val intent = Intent(this, AddDepartActivity::class.java)
        startActivity(intent)
    }
    fun findPostOffices(view: View){
        Toast.makeText(this, "Crash start", Toast.LENGTH_LONG).show()
        try {
            crashMethod()
        }
        catch (e : Exception){
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
    fun crashMethod(){
        throw RuntimeException("Test Crash")
    }
    fun payParcel(view: View)
    {
        //Toast.makeText(this, "This functional is not created", Toast.LENGTH_LONG).show()
        throw RuntimeException("Fatal Test Crash")
    }
}
