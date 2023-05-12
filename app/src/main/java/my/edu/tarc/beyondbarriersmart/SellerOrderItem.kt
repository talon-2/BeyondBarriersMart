package my.edu.tarc.beyondbarriersmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SellerOrderItem.newInstance] factory method to
 * create an instance of this fragment.
 */
class SellerOrderItem : Fragment() {
    private val db = Firebase.firestore
    private val noteRef = db.collection("SellerProductItem")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_order_item, container, false)
    }

    private fun foo() {
        noteRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val querySnapshot = task.result

                for (document in querySnapshot?.documents ?: emptyList()) {

                    val data = document.data

                    val item = SellerProductItem(
                        "${data?.get("productId")}",
                        "${data?.get("sellerId")}",
                        "${data?.get("image")}",
                        "${data?.get("name")}",
                        "${data?.get("description")}",
                        "${data?.get("category")}",
                        "${data?.get("stock")}".toInt(),
                        "${data?.get("sold")}".toInt(),
                        "${data?.get("rating")}".toFloat(),
                        "${data?.get("cost")}".toFloat(),
                        "${data?.get("shipNeed")}".toBoolean()
                    )
                }
            }
        }
    }

    fun newInstance(
        prodName: String,
        prodOrderDate: String,
        prodAmount: Int,
        prodStatus: String,
        prodAddress: String
    ): SellerOrderItem {
        val args = Bundle() // wtf is this
        // idk what to write here
        val fragment = SellerOrderItem()
        fragment.arguments = args
        return fragment
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SellerOrderItem.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SellerOrderItem().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}