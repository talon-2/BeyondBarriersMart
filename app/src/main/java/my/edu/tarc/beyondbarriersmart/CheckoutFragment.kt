package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.beyondbarriersmart.databinding.FragmentCartBinding
import my.edu.tarc.beyondbarriersmart.databinding.FragmentCheckoutBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID.randomUUID

class CheckoutFragment : Fragment() {
    private lateinit var binding : FragmentCheckoutBinding
    val adapter = CheckoutAdapter()



    val db = Firebase.firestore
    val cartRef = db.collection("Cart")
    val purchaseRef = db.collection("Purchase")
    val productRef = db.collection("SellerProductItem")
    //get current date
    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
    val dateToBeSaved = dateFormat.format(currentDate).toString()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
        val currentCustomer = sharedPref.getString(LoginFragment.custId, "").toString()

        var orderList = mutableListOf<Cart>()
        FirebaseFirestore.getInstance().collection("Cart").
        whereEqualTo("customerId", currentCustomer).get().addOnSuccessListener {carts->

            carts.forEach{
                val cart = Cart(it.data.get("customerId").toString(), it.data.get("itemAmt").toString().toInt(), it.data.get("productId").toString())
                orderList.add(cart)
            }

            adapter.initialiseAdapter(orderList)


        }.addOnFailureListener{
            Log.d("ERROR",it.message.toString())
        }
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        binding.checkoutOrderList.layoutManager = layoutManager

        binding.checkoutOrderList.adapter = adapter

        binding.paymentMethodBackground.setOnClickListener(){
            val fragmentManager = requireFragmentManager()
            val dialog = EditCustomerPaymentInfoFragment()
            dialog.show(fragmentManager, "EditCustomerPaymentInfoFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                //binding.paymentMethodDetail.setText(sharedPref.getString(LoginFragment.bankName, "") + sharedPref.getString(LoginFragment.cardNo, ""))
            }
        }

        binding.checkoutBackButton.setOnClickListener(){
            //go back to cart
            val intent = Intent(activity, CartActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.checkoutProceedButton.setOnClickListener() {
            cartRef.get().addOnCompleteListener { task ->
                //get everything from your cart
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    for (document in querySnapshot?.documents ?: emptyList()) {
                        if ("${document?.get("customerId")}" == currentCustomer) { //check if it is the current customer
                            //for every item in your cart
                            val cartData = document.data
                            val prodId = "${cartData?.get("productId")}"
                            val custId = "${cartData?.get("customerId")}"
                            val itemAmt = "${cartData?.get("itemAmt")}".toString().toInt()

                            //add to purchase.
                            purchaseRef.get().addOnCompleteListener { task ->
                                //create a hashmap to store the record

                                //create a new purchaseId
                                val newPurchaseId =
                                    "P" + "${randomUUID().toString().substring(0, 8)}"

                                val cartRecord = hashMapOf(
                                    "productId" to prodId,
                                    "custId" to custId,
                                    "date" to dateToBeSaved,
                                    "purchaseAmt" to itemAmt,
                                    "purchaseId" to newPurchaseId,
                                    "isDone" to false

                                    //might need to double check, idk if this works
                                )

                                //with the record saved in the hashmap, add it to database.
                                purchaseRef.document()
                                    .set(cartRecord)

                                //change the stock number from the productSellerItem according to the productId
                                val product = productRef.document(prodId)

                                //read what stock and sold value is
                                product.get().addOnSuccessListener { document ->
                                    if (document != null) {
                                        val data = document.data
                                        val getProductStock = "${data?.get("stock")}"
                                        val getSoldStock = "${data?.get("sold")}"

                                        //then update it.
                                        val updateStock = hashMapOf<String, Any>(
                                            "stock" to (getProductStock.toString()
                                                .toInt() - itemAmt),
                                            "sold" to (getSoldStock.toString().toInt() + 1)
                                        )

                                        product.update(updateStock)
                                    }
                                }
                            }


                            //once that record is added, delete the record from your cart
                            if ("${document?.get("customerId")}" == currentCustomer) {
                                document.reference.delete()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val checkoutText = "Checkout has been completed!"
                                            Toast.makeText(
                                                context,
                                                checkoutText,
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        } else {

                                        }
                                    }
                            }
                        }
                    }
                }
                //head back to category
                val intent = Intent(activity, CategoryActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
        return binding.root
    }
}