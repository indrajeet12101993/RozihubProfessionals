package rozihub.rozihubprofessionals.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_new_bookings.view.*
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.pojo.newBooking.Result

class AllNewBookingAdapter(var mList: List<Result>, var mlistner: ListnerForCustomDialog) :
    RecyclerView.Adapter<AllNewBookingAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mlistner, mList, position)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_new_bookings, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(listener: ListnerForCustomDialog, list: List<Result>, position: Int) {


            itemView.tv_bookingid.text = "Booking Id:" + " " + list[position].book_id
            itemView.tv_bookingdate.text = "Booking Date:" + " " + list[position].booking_date
            itemView.tv_booker_user_name.text = list[position].name
            itemView.tv_booker_user_mobile.text = list[position].phone
            itemView.tv_servicename.text = list[position].servicename
            itemView.tv_adress.text = list[position].booking_address
            itemView.setOnClickListener {

                listener.selectPosition(position)
            }
        }
    }
}