package com.example.novaposhtaapplication

//import android.os.Parcelable
//import kotlinx.android.parcel.Parcelize

class UserDataModel {
    lateinit var firstname: String
    lateinit var lastname: String
    lateinit var pass: String
    lateinit var phone: String
    lateinit var email: String

    constructor(){

    }

    constructor(fName: String, lName: String, pass: String, phone: String, email: String){
        this.firstname=fName
        this.lastname=lName
        this.pass=pass
        this.phone=phone
        this.email=email
    }
}