package rozihub.rozihubprofessionals.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_custom_dialog.view.*
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog

class CustomDialogRecyclerAdapter(var mList:ArrayList<String>, var mlistner: ListnerForCustomDialog) : RecyclerView.Adapter<CustomDialogRecyclerAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind( mlistner, mList,position)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_dialog, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(  listener: ListnerForCustomDialog,list: List<String>, position: Int) {



           itemView.tv_item.text=list[position]
            itemView.setOnClickListener {
                listener.selectPosition(position)
            }
        }
    }

}