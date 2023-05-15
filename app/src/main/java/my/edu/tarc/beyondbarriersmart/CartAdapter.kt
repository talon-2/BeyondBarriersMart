package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import my.edu.tarc.beyondbarriersmart.databinding.FragmentCartBinding
import org.w3c.dom.Text

class CartAdapter() : RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    private var cartItem = mutableListOf<Cart>()
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    var contextAdapter: Context?=null

    private lateinit var binding: FragmentCartBinding



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

    fun setContext(context: Context){
        this.contextAdapter = context
    }

    fun setBinding(binding: FragmentCartBinding){
        this.binding = binding
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
                var sharedPref = contextAdapter!!.getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    holder.cartItemImage.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    //do nothing
                }
                holder.cartItemName.text = productName
                holder.cartItemPrice.text = currentPrice
                holder.cartCardAmt.text = cartItem[position].itemAmt.toString()
                Log.d("CART",cartItem[position].productId)
                holder.cartAddButton.setOnClickListener {
                    cartItem[position].itemAmt+= 1
                    FirebaseFirestore.getInstance().collection("Cart").
                    whereEqualTo("customerId", "C0001").
                        whereEqualTo("productId", cartItem[position].productId).get().addOnSuccessListener {

                        for (cart in it){

                            val cartHashmap = hashMapOf(
                                "customerId" to cartItem[position].customerId,
                                "itemAmt" to cartItem[position].itemAmt,
                                "productId" to cartItem[position].productId
                            )
                            FirebaseFirestore.getInstance().collection("Cart").document(cart.id).set(cartHashmap)

                        }
                    }
                    holder.cartCardAmt.text =  cartItem[position].itemAmt.toString()
                }
                holder.cartRemoveButton.setOnClickListener{
                    cartItem[position].itemAmt-= 1
                    FirebaseFirestore.getInstance().collection("Cart").
                    whereEqualTo("customerId", sharedPref.getString(LoginFragment.custId, "")).
                    whereEqualTo("productId", cartItem[position].productId).get().addOnSuccessListener {

                        for (cart in it){
                            if(cartItem[position].itemAmt <= 0){
                                //Remove item from cart database
                                cartItem.removeAt(position)
                                notifyDataSetChanged()
                                changeItemCount()
                                FirebaseFirestore.getInstance().collection("Cart").
                                        document(cart.id).delete()
                            }
                            else{
                                holder.cartCardAmt.text = cartItem[position].itemAmt.toString()
                                val cartHashmap = hashMapOf(
                                    "customerId" to cartItem[position].customerId,
                                    "itemAmt" to cartItem[position].itemAmt,
                                    "productId" to cartItem[position].productId
                                )
                                FirebaseFirestore.getInstance().collection("Cart").document(cart.id).set(cartHashmap)

                            }

                        }
                    }


                }
            }
        }
    }

    private fun changeItemCount(){
        binding.cartItemNumber.text = String.format("Items in Cart: %d", cartItem.size)
    }

    internal fun initialiseAdapter(cartList: List<Cart>){
        cartItem = cartList.toMutableList()
        changeItemCount()
        notifyDataSetChanged()
    }
}