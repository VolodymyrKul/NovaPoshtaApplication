package com.example.novaposhtaapplication

class DepartureModel {
    var imgName: Int=0
    lateinit var title: String
    lateinit var desc: String
    lateinit var fullDesc: String

    constructor(){

    }

    constructor(imgName: Int, title: String, desc: String, fullDesc: String){
        this.imgName=imgName
        this.title=title
        this.desc=desc
        this.fullDesc=fullDesc
    }
}