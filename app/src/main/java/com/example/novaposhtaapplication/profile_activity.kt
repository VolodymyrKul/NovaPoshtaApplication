package com.example.novaposhtaapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class profile_activity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var db: FirebaseDatabase? = null
    var users: DatabaseReference? = null
    var imageUri: Uri? = null
    var images: DatabaseReference? = null
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_activity)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db!!.getReference("Users")
        images = db!!.getReference("ProfileImages")
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()

        users!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    val toast : Toast = Toast.makeText(this@profile_activity, "Data load failed", Toast.LENGTH_LONG)
                    toast.show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    HideUI()
                    val userData = p0.getValue(UserDataModel::class.java)
                    if (userData != null){
                        findViewById<TextView>(R.id.FirstNameTV).text = userData.firstname
                        findViewById<TextView>(R.id.LastNameTV).text = userData.lastname
                        findViewById<TextView>(R.id.EmailTV).text = userData.email
                        findViewById<TextView>(R.id.PhoneTV).text = userData.phone
                        findViewById<TextView>(R.id.PassTV).text = userData.pass
                        findViewById<EditText>(R.id.FirstNameET).setText(userData.firstname)
                        findViewById<EditText>(R.id.LastNameET).setText(userData.lastname)
                        findViewById<EditText>(R.id.EmailET).setText(userData.email)
                        findViewById<EditText>(R.id.PhoneET).setText(userData.phone)
                        findViewById<EditText>(R.id.PassET).setText(userData.pass)
                    }
                    ShowUI()
                }

            })

        images!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    val toast : Toast = Toast.makeText(this@profile_activity, "Image load failed", Toast.LENGTH_LONG)
                    toast.show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    HideImg()
                    val userImage = p0.getValue(ProfileImgModel::class.java)
                    if (userImage!=null){
                        val imageView = findViewById<ImageView>(R.id.ProfileImage)
                        Picasso.get().load(userImage.imgUrl).resize(140,180).into(imageView)
                    }
                    ShowImg()
                }

            })
    }

    fun ShowUI(){
        //val img = findViewById<ImageView>(R.id.ProfileImage)
        val data = findViewById<LinearLayout>(R.id.UserDataLL)
        val menu = findViewById<Button>(R.id.MainMenuBtn)
        val save = findViewById<Button>(R.id.SaveDataBtn)
        val bar = findViewById<ProgressBar>(R.id.Progress)
        //img.visibility = View.VISIBLE
        data.visibility = View.VISIBLE
        menu.visibility = View.VISIBLE
        save.visibility = View.VISIBLE
        bar.visibility = View.GONE
    }
    fun HideUI(){
        //val img = findViewById<ImageView>(R.id.ProfileImage)
        val data = findViewById<LinearLayout>(R.id.UserDataLL)
        val menu = findViewById<Button>(R.id.MainMenuBtn)
        val save = findViewById<Button>(R.id.SaveDataBtn)
        val bar = findViewById<ProgressBar>(R.id.Progress)
        //img.visibility = View.GONE
        data.visibility = View.GONE
        menu.visibility = View.GONE
        save.visibility = View.GONE
        bar.visibility = View.VISIBLE
    }
    fun ShowImg(){
        val bar = findViewById<ProgressBar>(R.id.ImageProgress)
        val img = findViewById<ImageView>(R.id.ProfileImage)
        bar.visibility = View.GONE
        img.visibility = View.VISIBLE
    }
    fun HideImg(){
        val bar = findViewById<ProgressBar>(R.id.ImageProgress)
        val img = findViewById<ImageView>(R.id.ProfileImage)
        img.visibility = View.GONE
        bar.visibility = View.VISIBLE
    }

    fun mainMenuClick(view: View){
        val intent = Intent(this, mainmenu_activity::class.java)
        startActivity(intent)
    }
    fun firstNameClick(view: View){
        val fname = findViewById<TextView>(R.id.FirstNameTV)
        val fnameET = findViewById<EditText>(R.id.FirstNameET)
        fname.visibility = View.GONE
        fnameET.visibility = View.VISIBLE
    }
    fun lastNameClick(view: View){
        val lname = findViewById<TextView>(R.id.LastNameTV)
        val lnameET = findViewById<EditText>(R.id.LastNameET)
        lname.visibility = View.GONE
        lnameET.visibility = View.VISIBLE
    }
    fun emailClick(view: View){
        val email = findViewById<TextView>(R.id.EmailTV)
        val emailET = findViewById<EditText>(R.id.EmailET)
        email.visibility = View.GONE
        emailET.visibility = View.VISIBLE
    }
    fun phoneClick(view: View){
        val phone = findViewById<TextView>(R.id.PhoneTV)
        val phoneET = findViewById<EditText>(R.id.PhoneET)
        phone.visibility = View.GONE
        phoneET.visibility = View.VISIBLE
    }
    fun passClick(view: View){
        val pass = findViewById<TextView>(R.id.PassTV)
        val passET = findViewById<EditText>(R.id.PassET)
        pass.visibility = View.GONE
        passET.visibility = View.VISIBLE
    }
    fun saveDataClick(view: View){
        val fname = findViewById<TextView>(R.id.FirstNameTV)
        val fnameET = findViewById<EditText>(R.id.FirstNameET)
        val lname = findViewById<TextView>(R.id.LastNameTV)
        val lnameET = findViewById<EditText>(R.id.LastNameET)
        val email = findViewById<TextView>(R.id.EmailTV)
        val emailET = findViewById<EditText>(R.id.EmailET)
        val phone = findViewById<TextView>(R.id.PhoneTV)
        val phoneET = findViewById<EditText>(R.id.PhoneET)
        val pass = findViewById<TextView>(R.id.PassTV)
        val passET = findViewById<EditText>(R.id.PassET)

        if (!TextUtils.equals(email.text.toString(), emailET.text.toString()))
        {
            FirebaseAuth.getInstance().currentUser!!.updateEmail(emailET.text.toString())
                .addOnSuccessListener {
                    val toast: Toast = Toast.makeText(this, "User email changed", Toast.LENGTH_LONG)
                    toast.show()
                }
                .addOnFailureListener {
                    val toast: Toast = Toast.makeText(this, "User email not changed", Toast.LENGTH_LONG)
                    toast.show()
                }
        }

        if (!TextUtils.equals(pass.text.toString(), passET.text.toString())){
            FirebaseAuth.getInstance().currentUser!!.updatePassword(passET.text.toString())
                .addOnSuccessListener {
                    val toast: Toast = Toast.makeText(this, "User pass changed", Toast.LENGTH_LONG)
                    toast.show()
                }
                .addOnFailureListener {
                    val toast: Toast = Toast.makeText(this, "User pass not changed", Toast.LENGTH_LONG)
                    toast.show()
                }
        }

        fname.text = fnameET.text
        lname.text = lnameET.text
        email.text = emailET.text
        phone.text = phoneET.text
        pass.text = passET.text

        val newUserData: UserDataModel = UserDataModel(
            fname.text.toString(),
            lname.text.toString(),
            pass.text.toString(),
            phone.text.toString(),
            email.text.toString())

        users!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(newUserData)
            .addOnSuccessListener {
                val toast: Toast = Toast.makeText(this, "User data changed", Toast.LENGTH_LONG)
                toast.show()
            }
            .addOnFailureListener {
                val toast: Toast = Toast.makeText(this, "User data not changed", Toast.LENGTH_LONG)
                toast.show()
            }

        fname.visibility = View.VISIBLE
        fnameET.visibility = View.GONE
        lname.visibility = View.VISIBLE
        lnameET.visibility = View.GONE
        email.visibility = View.VISIBLE
        emailET.visibility = View.GONE
        phone.visibility = View.VISIBLE
        phoneET.visibility = View.GONE
        pass.visibility = View.VISIBLE
        passET.visibility = View.GONE
    }
    fun onPictureClick(view: View){
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
            val image_view = findViewById<ImageView>(R.id.ProfileImage)
            image_view.setImageURI(imageReturnedIntent?.data)
            imageUri=imageReturnedIntent?.data
            onPictureSave()
        }
    }
    fun onPictureSave(){
        val StorageChild = "ProfileUser_"+ FirebaseAuth.getInstance().currentUser!!.uid
        imageUri?.let { it1->
            storageReference!!.child(StorageChild)
                .putFile(it1)
                .addOnSuccessListener { taskSnapshot ->
                    var imgUri = taskSnapshot.metadata!!.reference!!.downloadUrl
                    imgUri.addOnSuccessListener {
                        val imgNewUri = it
                        val profImg = ProfileImgModel(StorageChild, imgNewUri.toString())
                        images!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(profImg)
                            .addOnSuccessListener {
                                val toast = Toast.makeText(this, "Image added success", Toast.LENGTH_LONG)
                                toast.show()
                            }
                    }.addOnFailureListener {
                            val toast = Toast.makeText(this, "Unknown URL", Toast.LENGTH_LONG)
                            toast.show()
                    }
                }
        }
    }
}
