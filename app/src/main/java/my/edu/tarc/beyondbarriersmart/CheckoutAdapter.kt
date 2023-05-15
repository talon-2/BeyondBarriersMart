package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CheckoutAdapter() : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {
    private var orderList = mutableListOf<Cart>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderItemName : TextView = view.findViewById(R.id.orderItemName)
        val orderItemPrice : TextView = view.findViewById(R.id.orderItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkout_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryReference = FirebaseFirestore.getInstance().collection("SellerProductItem")
        val query = categoryReference.whereEqualTo("productId", orderList[position].productId)
        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                var productName = "${data?.get("name")}"
                val tempPrice = data?.get("cost") as Double
                val currentPrice = String.format("RM%,.2f", tempPrice * orderList[position].itemAmt)

                holder.orderItemName.text = productName
                holder.orderItemPrice.text = currentPrice

            }
        }
    }

    internal fun initialiseAdapter(orderList: List<Cart>){
        this.orderList = orderList.toMutableList()
        notifyDataSetChanged()
    }
}