package com.example.novaposhtaapplication

class DepartNewModel {
    lateinit var url: String
    lateinit var title: String
    lateinit var desc: String
    lateinit var departureplace: String
    lateinit var departurelatlng: String
    lateinit var arrivalplace: String
    lateinit var arrivallatlng: String
    lateinit var departcode: String
    lateinit var status: String
    //var lat: Double = 0.0
    constructor(){

    }
    constructor(url: String, title: String, desc: String, dep: String, depll: String, arr: String, arrll:String, code: String, status: String){
        this.url = url
        this.title = title
        this.desc = desc
        this.departureplace = dep
        this.departurelatlng = depll
        this.arrivalplace = arr
        this.arrivallatlng = arrll
        this.departcode = code
        this.status = status
    }

}