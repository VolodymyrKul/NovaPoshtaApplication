package com.example.novaposhtaapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class recyclerViewAdapter(val DepList: ArrayList<DepartNewModel>, val context: Context) :
    RecyclerView.Adapter<recyclerViewAdapter.ViewHolder>() {
    //lateinit var actContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        //actContext = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return DepList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var DepItem = DepList[position]
        Picasso.get().load(DepItem.url).resize(100,100).into(holder.Img)
        holder.Title.text = DepItem.title
        holder.Desc.text = DepItem.arrivalplace + "->" + DepItem.departureplace
        holder.ItemLL.setOnClickListener{
            val intent = Intent(context, ShowDepartActivity::class.java)
            intent.putExtra("DbChild", position)
            context.startActivity(intent)

        }
        //holder.Img.setImageResource(DepItem.imgName)
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var Img: ImageView = item.findViewById(R.id.DepImg)
        var Title: TextView = item.findViewById(R.id.DepTitle)
        var Desc: TextView = item.findViewById(R.id.DepDesc)
        var ItemLL: LinearLayout = item.findViewById(R.id.itemLL)
    }
}