package com.example.novaposhtaapplication

class ProfileImgModel {
    lateinit var imgName: String
    lateinit var imgUrl: String

    constructor(){

    }
    constructor(name: String, url: String){
        this.imgName = name
        this.imgUrl = url
    }
}