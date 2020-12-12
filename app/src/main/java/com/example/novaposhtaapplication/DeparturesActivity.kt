package com.example.novaposhtaapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DeparturesActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    //var db: FirebaseDatabase? = null
    //var departures: DatabaseReference? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departures)

        auth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance()
        val departures = db.getReference("Departures")

        var recView = findViewById<RecyclerView>(R.id.recyclerView)
        recView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        departures.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@DeparturesActivity, "Something wrong", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    recView.visibility = View.GONE
                    findViewById<ProgressBar>(R.id.RecProgress).visibility = View.VISIBLE
                    var depList = ArrayList<DepartNewModel>()
                    for (it in p0.children){
                        val dep = it.getValue(DepartNewModel::class.java)
                        if (dep != null) {
                            depList.add(dep)
                        }
                    }
                    var adapter = recyclerViewAdapter(depList, this@DeparturesActivity)

                    recView.setAdapter(adapter)
                    findViewById<ProgressBar>(R.id.RecProgress).visibility = View.GONE
                    recView.visibility = View.VISIBLE
                }
            })
    }
}
