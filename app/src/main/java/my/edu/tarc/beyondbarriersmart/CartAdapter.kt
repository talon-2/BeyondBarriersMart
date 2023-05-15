package my.edu.tarc.beyondbarriersmart

import android.graphics.BitmapFactory
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    private var cartItem = mutableListOf<Cart>()
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cartItemImage : ImageView = view.findViewById(R.id.cartCardImage)
        val cartItemName : TextView = view.findViewById(R.id.cartCardName)
        val cartItemPrice : TextView = view.findViewById(R.id.cartCardPrice)
        val cartCardAmt : TextView = view.findViewById(R.id.cartCardAmt)
        val cartAddButton : Button = view.findViewById(R.id.cartAddButton)
        val cartRemoveButton : Button = view.findViewById(R.id.cartRemoveButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryReference = FirebaseFirestore.getInstance().collection("SellerProductItem")
        val query = categoryReference.whereEqualTo("productId", cartItem[position].productId)
        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                var currentImage = "${data?.get("image")}"
                val imageRef = storageRef.child(currentImage)
                val ONE_MEGABYTE = 1024 * 1024.toLong()
                var productName = "${data?.get("name")}"
                val tempPrice = data?.get("cost") as Double
                val currentPrice = String.format("RM%,.2f", tempPrice)

                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    holder.cartItemImage.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    //do nothing
                }
                holder.cartItemName.text = productName
                holder.cartItemPrice.text = currentPrice
                holder.cartCardAmt.text = cartItem[position].itemAmt.toString()
                holder.cartAddButton.setOnClickListener{
                    holder.cartCardAmt.text = (holder.cartCardAmt.text.toString().toInt() + 1).toString()
                }
                holder.cartRemoveButton.setOnClickListener{
                    var cartAfterAmt = (holder.cartCardAmt.text.toString().toInt() - 1)
                    if(cartAfterAmt <= 0){
                        //Remove item from cart database
                        cartItem.removeAt(position)
                    }
                    else{
                        holder.cartCardAmt.text = cartAfterAmt.toString()
                    }
                }
            }
        }
    }

    internal fun initialiseAdapter(cartList: List<Cart>){
        cartItem.clear()
        cartItem.addAll(cartList)
         notifyDataSetChanged()
    }
}