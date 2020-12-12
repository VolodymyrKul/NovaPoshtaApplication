package com.example.novaposhtaapplication

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class register_activity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var db: FirebaseDatabase? = null
    var users: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activity)

    }
    fun regFun(view: View){
        val FirstNameET = findViewById<TextView>(R.id.Name)
        val LastNameET = findViewById<TextView>(R.id.Surname)
        val EmailET = findViewById<TextView>(R.id.Email)
        val PhoneET = findViewById<TextView>(R.id.Phone)
        val PassET = findViewById<TextView>(R.id.Password)
        val ConfPassET = findViewById<TextView>(R.id.ConsfirmPassword)

        val fName = FirstNameET.text.toString()
        val lName = LastNameET.text.toString()
        val email = EmailET.text.toString()
        val phone = PhoneET.text.toString()
        val pass = PassET.text.toString()
        val confPass = ConfPassET.text.toString()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db!!.getReference("Users")

        if (validateFun()){
            auth!!.createUserWithEmailAndPassword(email,pass)
                .addOnSuccessListener {
                    var newUser: UserDataModel = UserDataModel(
                        fName,
                        lName,
                        pass,
                        phone,
                        email
                    )
                    users!!.child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(newUser)
                        .addOnSuccessListener {
                            val toast: Toast = Toast.makeText(this, "User added success", Toast.LENGTH_LONG)
                            toast.show()
                        }
                }
                .addOnFailureListener {
                    val toast: Toast = Toast.makeText(this, "User didnot add", Toast.LENGTH_LONG)
                    toast.show()
                }
        }
    }
    fun validateFun() : Boolean{
        val FirstNameET = findViewById<TextView>(R.id.Name)
        val LastNameET = findViewById<TextView>(R.id.Surname)
        val EmailET = findViewById<TextView>(R.id.Email)
        val PhoneET = findViewById<TextView>(R.id.Phone)
        val PassET = findViewById<TextView>(R.id.Password)
        val ConfPassET = findViewById<TextView>(R.id.ConsfirmPassword)

        val fName = FirstNameET.text.toString()
        val lName = LastNameET.text.toString()
        val email = EmailET.text.toString()
        val phone = PhoneET.text.toString()
        val pass = PassET.text.toString()
        val confPass = ConfPassET.text.toString()
        if (TextUtils.isEmpty(fName)){
            val toast: Toast = Toast.makeText(this, "Miss first name",Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        else if (TextUtils.isEmpty(lName)){
            val toast: Toast = Toast.makeText(this, "Miss last name",Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        else if (TextUtils.isEmpty(email)){
            val toast: Toast = Toast.makeText(this, "Miss email",Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        else if (TextUtils.isEmpty(phone)){
            val toast: Toast = Toast.makeText(this, "Miss phone",Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        else if (TextUtils.isEmpty(pass)){
            val toast: Toast = Toast.makeText(this, "Miss pass",Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        else if (TextUtils.isEmpty(confPass)){
            val toast: Toast = Toast.makeText(this, "Miss conf pass",Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        else if (!TextUtils.equals(pass, confPass)){
            val toast: Toast = Toast.makeText(this, "Not right conf pass",Toast.LENGTH_LONG)
            toast.show()
            return false
        }
        return true
    }
}
