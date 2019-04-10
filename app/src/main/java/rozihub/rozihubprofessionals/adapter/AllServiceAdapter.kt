package rozihub.rozihubprofessionals.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_services.view.*
import rozihub.rozihubprofessionals.AddServiceActvity
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.pojo.servicelist.Result


class AllServiceAdapter(var mList: List<Result>, var mlistner: ListnerForCustomDialog): RecyclerView.Adapter<AllServiceAdapter.ViewHolder>()  {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mlistner, mList,  position)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_services, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(listener: ListnerForCustomDialog, list: List<Result>, position: Int) {


            itemView.tv_servicename.text=list[position].servicename
            itemView.tv_price.text="Rs:"+" "+list[position].price

            itemView.setOnClickListener {

                listener.selectPosition(position)




            }
        }
    }
}