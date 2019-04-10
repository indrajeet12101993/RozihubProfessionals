package rozihub.rozihubprofessionals.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_venues.view.*
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.callBackInterface.ListnerForNaviagtionItem
import rozihub.rozihubprofessionals.pojo.detailedBooking.All


class AllVenueAdapterAll(var mList: List<All>, var mlistner: ListnerForNaviagtionItem) :
    RecyclerView.Adapter<AllVenueAdapterAll.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mList, mlistner,  position)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venues, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(list: List<All>, listener: ListnerForNaviagtionItem, position: Int) {
            itemView.tv_bookingid.text = "Booking Id:" + " " + list[position].book_id
            itemView.tv_bookingdate.text = "Booking Date:" + " " + list[position].booking_date
            itemView.tv_shopname.text = list[position].shop_name
            itemView.tv_credit.text = list[position].servicename

            itemView.setOnClickListener {
                listener.itemSelcectPosition(position)
            }
        }
    }
}