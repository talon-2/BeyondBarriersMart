package my.edu.tarc.beyondbarriersmart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
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

        var cartList = mutableListOf<Cart>()
        FirebaseFirestore.getInstance().collection("Cart").
        whereEqualTo("customerId", "C0001").get().addOnSuccessListener {carts->

            carts.forEach{
                val cart = Cart(it.data.get("customerId").toString(), it.data.get("itemAmt").toString().toInt(), it.data.get("productId").toString())
                cartList.add(cart)
            }

            adapter.initialiseAdapter(cartList)


        }.addOnFailureListener{
            Log.d("ERROR",it.message.toString())
        }
        var layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        binding.cartItemList.layoutManager = layoutManager
        adapter.setContext(requireContext())
        //adapter.initialiseAdapter(cartList)
        adapter.setBinding(binding)

        binding.cartItemList.adapter = adapter

        binding.clearCartButton.setOnClickListener(){
            FirebaseFirestore.getInstance().collection("Cart").
            whereEqualTo("customerId", "C0001").get().addOnSuccessListener {
                cartList.clear()
                for (cart in it){
                    adapter.initialiseAdapter(cartList)

                    FirebaseFirestore.getInstance().collection("Cart").
                    document(cart.id).delete()
                }
            }
        }

        binding.cartItemNumber.text = String.format("Items in Cart: ") + adapter.itemCount.toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun changeItemCount(){

    }


}