package com.example.novaposhtaapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//import com.example.novaposhtaapplication.register_activity.Companion.RegistrationMes
import kotlin.random.Random.Default.Companion

class login_activity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var db: FirebaseDatabase? = null
    var users: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)

    }

    fun OnLogIn(view: View){
        val LoginET = findViewById<EditText>(R.id.EmailField)
        val PassET = findViewById<EditText>(R.id.PassField)
        val login = LoginET.text.toString()
        val pass = PassET.text.toString()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db!!.getReference("Users")

        if (validateFun()){
            auth!!.signInWithEmailAndPassword(login, pass)
                .addOnSuccessListener {
                    val intent = Intent(this, profile_activity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    val toast: Toast = Toast.makeText(this, "Not right data", Toast.LENGTH_LONG)
                    toast.show()
                }
        }
    }

    fun validateFun() : Boolean{
        val LoginET = findViewById<EditText>(R.id.EmailField)
        val PassET = findViewById<EditText>(R.id.PassField)
        val login = LoginET.text.toString()
        val pass = PassET.text.toString()
        if (TextUtils.isEmpty(login)){
            val toast: Toast = Toast.makeText(this, "Miss login data", Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        else if (TextUtils.isEmpty(pass)){
            val toast: Toast = Toast.makeText(this, "Miss pass data", Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        return true
    }
}
