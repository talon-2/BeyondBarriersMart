package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.content.DialogInterface
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

class SellerProfileFragment : Fragment(), DialogInterface.OnDismissListener {
    private lateinit var usernameText: TextView
    private lateinit var emailText: TextView
    private lateinit var viewProductsButton: Button
    private lateinit var editProfileButton: Button
    private lateinit var ordersReceivedList: LinearLayout
    private val ordersList = MutableList(50) { OrderReceive() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_seller_profile, container, false)

        usernameText = view.findViewById(R.id.seller_profile_username)
        emailText = view.findViewById(R.id.seller_profile_email)
        viewProductsButton = view.findViewById(R.id.view_my_products_button)
        editProfileButton = view.findViewById(R.id.seller_edit_profile_button)
        ordersReceivedList = view.findViewById(R.id.orders_received_list)

        viewProductsButton.setOnClickListener {
        }

        editProfileButton.setOnClickListener {
            val fragmentManager = requireFragmentManager()
            val dialog = EditSellerInfoFragment()
            dialog.show(fragmentManager, "EditSellerInfoFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        update()
        readOrders()

        return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        update()
    }

    private fun update() {
        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

        usernameText.setText(sharedPref.getString(LoginFragment.username, getString(R.string.seller_name_sample)))
        emailText.setText(sharedPref.getString(LoginFragment.email, getString(R.string.email_sample)))
    }

    private fun readOrders() {
        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
        val sellerId = sharedPref.getString(LoginFragment.sellerId, "")

        val productQuery = FirebaseFirestore.getInstance()
            .collection("SellerProductItem")
            .whereEqualTo(LoginFragment.sellerId, sellerId)

        productQuery.get().addOnSuccessListener { prodSnapshot ->
            if (!prodSnapshot.isEmpty) {
                prodSnapshot.documents.forEachIndexed { index, productDocument ->
                    val purchaseQuery = FirebaseFirestore.getInstance()
                        .collection("Purchase")
                        .whereEqualTo("productId", productDocument.get("productId").toString())

                    purchaseQuery.get().addOnSuccessListener { purchaseSnapshot ->
                        if (!purchaseSnapshot.isEmpty) {
                            purchaseSnapshot.documents.forEachIndexed { index2, purchaseDocument ->
                                val image = productDocument.get("image").toString()
                                val itemName = productDocument.get("name").toString()
                                val amount = purchaseDocument.get("purchaseAmt").toString()
                                val dateOrdered = purchaseDocument.get("date").toString()
                                val isDone = purchaseDocument.get("isDone").toString().toBoolean()

                                ordersList[index2] = OrderReceive(image, itemName, amount, dateOrdered, isDone)
                            }
                            displayOrders()
                        }
                    }
                }
            }
        }
    }

    public fun displayOrders() {
        // it doesnt work when using supportFragmentManager
        // theres probably a reason, but as long as it works
        val transaction = childFragmentManager.beginTransaction()
        ordersList.filter({ item -> !item.isDone}).forEachIndexed { index, order ->
            val bundle = Bundle()

            bundle.putString("image", order.image)
            bundle.putString("name", order.itemName)
            bundle.putString("purchaseAmt", order.purchaseAmt.toString())
            bundle.putString("date", order.date.toString())
            bundle.putBoolean("isDone", order.isDone)

            val orderItem = SellerOrderItem()
            orderItem.arguments = bundle
            transaction.add(R.id.orders_received_list, orderItem)
        }
        transaction.commit()
    }
}