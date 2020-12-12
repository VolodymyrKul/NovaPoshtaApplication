package com.example.novaposhtaapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddDepartActivity : AppCompatActivity() {

    var imageUri: Uri? = null

    var auth: FirebaseAuth? = null
    var db: FirebaseDatabase? = null
    var departures: DatabaseReference? = null

    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var departCount: Long = 0

    var departmentPlace: String? = null
    var departmentLatLng: String? = null
    var arrivalPlace: String? = null
    var arrivalLatLng: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_depart)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        departures = db!!.getReference("Departures")

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()

        departures!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@AddDepartActivity, "Something wrong", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    departCount = p0.childrenCount
                    //Toast.makeText(this@AddDepartActivity, "Count is read", Toast.LENGTH_LONG).show()
                }
            })
    }

    fun onPlaceChange(view: View){
        val intent = Intent(this, TestAutoAct::class.java)
        startActivityForResult(intent, PLACE_CODE)
    }


    fun onParcelImgClick(view: View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                //permission already granted
                pickImageFromGallery();
            }
        }
        else{
            //system OS is < Marshmallow
            pickImageFromGallery();
        }

    }
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
        //Place code
        private val PLACE_CODE = 1002;
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val image_view = findViewById<ImageView>(R.id.DepImage)
            image_view.setImageURI(imageReturnedIntent?.data)
            imageUri=imageReturnedIntent?.data
            if (imageUri != null){
                Toast.makeText(this, "Uri set success", Toast.LENGTH_LONG).show()
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == PLACE_CODE){
            if (imageReturnedIntent != null){
                departmentPlace = imageReturnedIntent.getStringExtra("DepPlace")
                departmentLatLng = imageReturnedIntent.getStringExtra("DepLatLng")
                arrivalPlace = imageReturnedIntent.getStringExtra("ArrPlace")
                arrivalLatLng = imageReturnedIntent.getStringExtra("ArrLatLng")
                if (departmentPlace != null){
                    Toast.makeText(this, departmentPlace, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun onDepartSave(view : View){
        if (!placeValid()){
            return
        }
        val depTitle = findViewById<EditText>(R.id.DepartTitle)
        val depDesc = findViewById<EditText>(R.id.DepartDesc)
        val depLatLng = departmentLatLng//"49.501617 23.23386"
        val arrLatLng = arrivalLatLng//"49.841952 24.0315921"
        val depPlace = departmentPlace//"Ралівка"
        val arrPlace = arrivalPlace//"Львів"

        val code = FirebaseAuth.getInstance().currentUser!!.uid + "_#" + departCount.toString()
        val status = "Not paid"

        val storageChild = FirebaseAuth.getInstance().currentUser!!.uid + "_#" + departCount.toString()

        imageUri?.let { it1->
            storageReference!!.child(storageChild)
                .putFile(it1)
                .addOnSuccessListener { taskSnapshot ->
                    var imgUri = taskSnapshot.metadata!!.reference!!.downloadUrl
                    imgUri.addOnSuccessListener {
                        val imgNewUri = it
                        //Toast.makeText(this, "imgNewUri set", Toast.LENGTH_LONG).show()
                        val newDepart = DepartNewModel(
                            imgNewUri.toString(),
                            depTitle.text.toString(),
                            depDesc.text.toString(),
                            depPlace.toString(),
                            depLatLng.toString(),
                            arrPlace.toString(),
                            arrLatLng.toString(),
                            code,
                            status)
                        departures!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child(departCount.toString())
                            .setValue(newDepart)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Departure added success",Toast.LENGTH_LONG).show()
                            }
                    }
                        .addOnFailureListener {
                            Toast.makeText(this,"Departure not added",Toast.LENGTH_LONG).show()
                        }
                }
        }
    }
    fun placeValid() : Boolean{
        if (TextUtils.isEmpty(findViewById<EditText>(R.id.DepartTitle).text.toString())){
            Toast.makeText(this, "Enter dep title", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(findViewById<EditText>(R.id.DepartDesc).text.toString())){
            Toast.makeText(this, "Enter dep desc", Toast.LENGTH_LONG).show()
            return false
        }
        if (departmentPlace == null){
            Toast.makeText(this, "Enter dep place", Toast.LENGTH_LONG).show()
            return false
        }
        if (departmentLatLng == null){
            Toast.makeText(this, "Enter dep latlng", Toast.LENGTH_LONG).show()
            return false
        }
        if (arrivalPlace == null){
            Toast.makeText(this, "Enter arr place", Toast.LENGTH_LONG).show()
            return false
        }
        if (arrivalLatLng == null){
            Toast.makeText(this, "Enter arr latlng", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}
