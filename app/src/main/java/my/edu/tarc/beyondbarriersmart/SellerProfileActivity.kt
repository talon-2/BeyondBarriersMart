package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.transaction
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore

class SellerProfileActivity : AppCompatActivity() {
    private lateinit var usernameText: TextView
    private lateinit var emailText: TextView
    private lateinit var viewProductsButton: Button
    private lateinit var editProfileButton: Button

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_profile)

        usernameText = findViewById(R.id.seller_profile_username)
        emailText = findViewById(R.id.seller_profile_email)
        viewProductsButton = findViewById(R.id.view_my_products_button)
        editProfileButton = findViewById(R.id.seller_edit_profile_button)

        viewProductsButton.setOnClickListener {
            startActivity(Intent(this@SellerProfileActivity, MyProductsActivity::class.java))
        }

        editProfileButton.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val dialog = EditSellerInfoFragment()
            dialog.show(fragmentManager, "EditSellerInfoFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        update()
        readOrders()

        fragmentManager.beginTransaction().apply {
            add(R.id.seller_profile_fragment_container, SellerProfileFragment())
            add(R.id.seller_profile_fragment_container, SellerBottomNavFragment())
            commit()
        }
    }

    private fun update() {
        val sharedPref = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

        usernameText.setText(sharedPref.getString(LoginFragment.username, getString(R.string.seller_name_sample)))
        emailText.setText(sharedPref.getString(LoginFragment.email, getString(R.string.email_sample)))
    }

    public fun readOrders() {
        val sharedPref = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
        val sellerId = sharedPref.getString(LoginFragment.sellerId, "")

        val productQuery = FirebaseFirestore.getInstance()
            .collection("SellerProductItem")
            .whereEqualTo(LoginFragment.sellerId, sellerId).get()

        clearFragments()

        var index = 0
        productQuery.addOnSuccessListener { prodSnapshot ->
            if (!prodSnapshot.isEmpty) {
                prodSnapshot.documents.forEachIndexed { _, productDocument ->
                    val purchaseQuery = FirebaseFirestore.getInstance()
                        .collection("Purchase")
                        .whereEqualTo("productId", productDocument.get("productId").toString())

                    purchaseQuery.get().addOnSuccessListener { purchaseSnapshot ->
                        if (!purchaseSnapshot.isEmpty) {
                            purchaseSnapshot.documents.forEachIndexed { _, purchaseDocument ->
                                val purchaseId = purchaseDocument.get("purchaseId").toString()
                                val image = productDocument.get("image").toString()
                                val itemName = productDocument.get("name").toString()
                                val amount = purchaseDocument.get("purchaseAmt").toString()
                                val dateOrdered = purchaseDocument.get("date").toString()
                                val isDone = purchaseDocument.get("isDone").toString().toBoolean()

                                val customerQuery = FirebaseFirestore.getInstance()
                                    .collection("Customer")
                                    .whereEqualTo("custId", purchaseDocument.get("custId").toString())

                                customerQuery.get().addOnSuccessListener { customerSnapshot ->
                                    if (!customerSnapshot.isEmpty) {
                                        val address = customerSnapshot.documents[0].get("address").toString()
                                        displayItem(OrderReceive(purchaseId, image, itemName, address, amount, dateOrdered, isDone))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun clearFragments() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        for (fragment in fragmentManager.fragments) {
            transaction.remove(fragment)
        }

        transaction.commit()
    }

    private fun displayItem(order: OrderReceive) {
        val bundle = Bundle()

        bundle.putString("purchaseId", order.purchaseId)
        bundle.putString("image", order.image)
        bundle.putString("name", order.itemName)
        bundle.putString("address", order.address)
        bundle.putString("purchaseAmt", order.purchaseAmt.toString())
        bundle.putString("date", order.date.toString())
        bundle.putBoolean("isDone", order.isDone)

        val orderItem = SellerOrderItem()
        orderItem.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.orders_received_list, orderItem)
        transaction.commit()
    }
}