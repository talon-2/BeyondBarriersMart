package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class SellerOrderItem : Fragment() {
    private val db = Firebase.firestore
    private val noteRef = db.collection("SellerProductItem")

    private lateinit var imageView: ImageView
    private lateinit var itemName: TextView
    private lateinit var amount: TextView
    private lateinit var dateOrdered: TextView
    private lateinit var address: TextView
    private lateinit var markAsDoneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_seller_order_item, container, false)

        imageView = view.findViewById(R.id.product_image)
        itemName = view.findViewById(R.id.order_item_name)
        amount = view.findViewById(R.id.order_amount)
        dateOrdered = view.findViewById(R.id.seller_order_item_date)
        address = view.findViewById(R.id.order_received_address)
        markAsDoneButton = view.findViewById(R.id.mark_as_done_button)

        itemName.setText(arguments?.getString("name").toString())
        amount.setText(arguments?.getString("purchaseAmt").toString())
        address!!.setText(arguments?.getString("address").toString())
        dateOrdered!!.setText(arguments?.getString("date").toString())

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child(arguments?.getString("image").toString())
        val ONE_MEGABYTE = 1024 * 1024.toLong()

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // Convert the bytes to a Bitmap
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imageView.setImageBitmap(bitmap)
        }
        markAsDoneButton.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

            val productQuery = FirebaseFirestore.getInstance()
                .collection("Purchase")
                .whereEqualTo("purchaseId", arguments?.getString("purchaseId").toString())

            productQuery.get().addOnSuccessListener { productSnapshot ->
                if (!productSnapshot.isEmpty) {
                    productSnapshot.documents.forEachIndexed { index, document ->
                        document.reference.update("isDone", true)
                    }
                }
            }

            (parentFragment as SellerProfileFragment).readOrders()
        }

        return view
    }

    public fun display(order: SellerProductItem) {
        itemName!!.setText(order.name)
        amount!!.setText(order.stock)
        dateOrdered!!.setText(getString(R.string.seller_order_item_date_sample))
    }
}