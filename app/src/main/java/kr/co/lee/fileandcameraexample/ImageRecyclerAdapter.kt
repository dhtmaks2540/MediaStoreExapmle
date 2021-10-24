package kr.co.lee.fileandcameraexample

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageRecyclerAdapter(val imageList: ArrayList<Uri>, val context: Context): RecyclerView.Adapter<ImageRecyclerAdapter.ImageRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageRecyclerViewHolder, position: Int) {
        val imageUrl = imageList[position]
        Glide.with(context).load(imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ImageRecyclerViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById<ImageView>(R.id.item_image)
    }
}