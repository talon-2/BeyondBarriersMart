package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class SellerProfileFragment : Fragment() {
//    private lateinit var usernameText: TextView
//    private lateinit var emailText: TextView
//    private lateinit var viewProductsButton: Button
//    private lateinit var editProfileButton: Button
//    private lateinit var ordersReceivedList: LinearLayout
//    private val ordersList = MutableList(50) { OrderReceive() }
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_seller_profile, container, false)

//        usernameText = view.findViewById(R.id.seller_profile_username)
//        emailText = view.findViewById(R.id.seller_profile_email)
//        viewProductsButton = view.findViewById(R.id.view_my_products_button)
//        editProfileButton = view.findViewById(R.id.seller_edit_profile_button)
//        ordersReceivedList = view.findViewById(R.id.orders_received_list)
//
//        viewProductsButton.setOnClickListener {
//            startActivity(Intent(this.requireActivity(), MyProductsActivity::class.java))
//        }
//
//        editProfileButton.setOnClickListener {
//            val fragmentManager = requireFragmentManager()
//            val dialog = EditSellerInfoFragment()
//            dialog.show(fragmentManager, "EditSellerInfoFragment")
//            fragmentManager.executePendingTransactions()
//            dialog.dialog?.setOnDismissListener {
//                update()
//            }
//        }
//
//        update()
//        readOrders()
//
//        val newFragment = SellerBottomNavFragment()
//        val transaction = fragmentManager?.beginTransaction()
//        transaction?.add(R.id.fragment_container, newFragment)
//        transaction?.addToBackStack(null)
//        transaction?.commit()

        return view
    }

//    override fun onDismiss(dialog: DialogInterface?) {
//        update()
//    }
//
//    private fun update() {
//        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
//
//        usernameText.setText(sharedPref.getString(LoginFragment.username, getString(R.string.seller_name_sample)))
//        emailText.setText(sharedPref.getString(LoginFragment.email, getString(R.string.email_sample)))
//    }
//
//    public fun readOrders() {
//        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
//        val sellerId = sharedPref.getString(LoginFragment.sellerId, "")
//
//        val productQuery = FirebaseFirestore.getInstance()
//            .collection("SellerProductItem")
//            .whereEqualTo(LoginFragment.sellerId, sellerId)
//
//        productQuery.get().addOnSuccessListener { prodSnapshot ->
//            if (!prodSnapshot.isEmpty) {
//                prodSnapshot.documents.forEachIndexed { _, productDocument ->
//                    val purchaseQuery = FirebaseFirestore.getInstance()
//                        .collection("Purchase")
//                        .whereEqualTo("productId", productDocument.get("productId").toString())
//
//                    purchaseQuery.get().addOnSuccessListener { purchaseSnapshot ->
//                        if (!purchaseSnapshot.isEmpty) {
//                            purchaseSnapshot.documents.forEachIndexed { index, purchaseDocument ->
//                                val purchaseId = purchaseDocument.get("purchaseId").toString()
//                                val image = productDocument.get("image").toString()
//                                val itemName = productDocument.get("name").toString()
//                                val amount = purchaseDocument.get("purchaseAmt").toString()
//                                val dateOrdered = purchaseDocument.get("date").toString()
//                                val isDone = purchaseDocument.get("isDone").toString().toBoolean()
//
//                                val customerQuery = FirebaseFirestore.getInstance()
//                                    .collection("Customer")
//                                    .whereEqualTo("custId", purchaseDocument.get("custId").toString())
//
//                                customerQuery.get().addOnSuccessListener { customerSnapshot ->
//                                    if (!customerSnapshot.isEmpty) {
//                                        val address = customerSnapshot.documents[0].get("address").toString()
//                                        ordersList[index] = OrderReceive(purchaseId, image, itemName, address, amount, dateOrdered, isDone)
//                                    }
//                                    displayOrders()
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun displayOrders() {
//        // it doesnt work when using supportFragmentManager
//        // theres probably a reason, but as long as it works
//        val fragmentManager = childFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//
//        for (fragment in fragmentManager.fragments) {
//            transaction.remove(fragment)
//            Log.d("Fragment remove", "It has been removed")
//        }
//
//        ordersList.filter({ item -> !item.isDone}).forEachIndexed { index, order ->
//            val bundle = Bundle()
//
//            bundle.putString("purchaseId", order.purchaseId)
//            bundle.putString("image", order.image)
//            bundle.putString("name", order.itemName)
//            bundle.putString("address", order.address)
//            bundle.putString("purchaseAmt", order.purchaseAmt.toString())
//            bundle.putString("date", order.date.toString())
//            bundle.putBoolean("isDone", order.isDone)
//
//            val orderItem = SellerOrderItem()
//            orderItem.arguments = bundle
//            transaction.add(R.id.orders_received_list, orderItem)
//            Log.d("Fragment add", "It has been added")
//        }
//        transaction.commit()
//    }
}