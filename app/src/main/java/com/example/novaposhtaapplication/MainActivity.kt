package com.example.novaposhtaapplication

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val img = findViewById<ImageView>(R.id.NovaPoshtaImg)
        //val theme = findViewById<ConstraintLayout>(R.id.BackGroundCL)
        //theme.setBackgroundResource(R.drawable.nature_wallpaperv2)
        //img.setImageResource(R.drawable.nova_poshta)
    }
    fun registerAct(view: View){
        val intent = Intent(this, register_activity::class.java)
        startActivity(intent)
    }
    fun logInAct(view: View) {
        val intent = Intent(this, login_activity::class.java)
        startActivity(intent)
    }
    fun documentationClick(view: View) {
        //val intent = Intent(this, TestAutoAct::class.java)
        //startActivityForResult(intent,1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //----
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            }
            else{
                //----
                //Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG)
                startDownloading()
            }
        }
        else{
            //----
            //Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG)
            startDownloading()
        }

    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null){
            val depPlace = data.getStringExtra("DepPlace")
            val depLatLng = data.getStringExtra("DepLatLng")
            val arrPlace = data.getStringExtra("ArrPlace")
            val arrLatLng = data.getStringExtra("ArrLatLng")
            if (depPlace != null){
                Toast.makeText(this, depPlace, Toast.LENGTH_LONG).show()
            }
        }
    }*/
    private fun startDownloading() {
        //----
        val textUrl = "https://drive.google.com/uc?export=download&id=1acBKhDOk2_CCRiRFcgrXrbZhtx1lJu7X"
        val request = DownloadManager.Request(Uri.parse(textUrl))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        request.setTitle("Nova_Poshta_Documentation")
        request.setDescription("File is downloading...")

        //request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")
        //----
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show()
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //----
                    //Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG)
                    startDownloading()
                }
                else{
                    Toast.makeText(this, "Permission denied!",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
