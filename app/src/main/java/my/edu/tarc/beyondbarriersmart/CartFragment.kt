package my.edu.tarc.beyondbarriersmart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import my.edu.tarc.beyondbarriersmart.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding : FragmentCartBinding
    val adapter = CartAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        var cartList = mutableListOf<Cart>()
        FirebaseFirestore.getInstance().collection("Cart").
        whereEqualTo("customerId", "C0001").get().addOnSuccessListener {carts->

            carts.forEach{
                val cart = Cart(it.data.get("customerId").toString(), it.data.get("itemAmt").toString().toInt(), it.data.get("productId").toString())
                cartList.add(cart)
            }

            adapter.initialiseAdapter(cartList)
            adapter.notifyDataSetChanged()
            //adapter.initialiseAdapter(cartList)
            binding.cartItemList.adapter = adapter
        }.addOnFailureListener{
            Log.d("ERROR",it.message.toString())
        }

    }



    /*data class Cart(
        val customerId: String,
        val itemAmt: Int,
        val productId: String
    )*/
    /*fun retrieveCart(){
        val cartRef = Firebase.database.reference.child("Cart")
        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = mutableListOf<Cart>()
                for (itemSnapshot in snapshot.children) {
                    val cartItem = itemSnapshot.getValue(Cart::class.java)
                    cartItems.add(cartItem)
                }
                // Display the cart items in the UI
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }*/
}