package rozihub.rozihubprofessionals.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_item.view.*
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.pojo.imageList.Result


class ImageAdapter(var mList: List<Result>, var mlistner: ListnerForCustomDialog) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mlistner, mList, position)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(listener: ListnerForCustomDialog, list: List<Result>, position: Int) {


            Glide.with(itemView.context).load("https://rozihub.com/local/images/gallery/"+list.get(position).image)
                .into(itemView.iv_galleryimge)
            itemView.setOnClickListener {

                listener.selectPosition(position)
            }
        }
    }
}