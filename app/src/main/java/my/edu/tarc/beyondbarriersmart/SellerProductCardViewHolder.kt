package my.edu.tarc.beyondbarriersmart

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent.getIntent
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import my.edu.tarc.beyondbarriersmart.databinding.SellerProductCardItemBinding


class SellerProductCardViewHolder (
    private val  sellerProductCardItemBinding: SellerProductCardItemBinding
) : RecyclerView.ViewHolder(sellerProductCardItemBinding.root)
{
    private val context = itemView.context
    private val delistButton: Button = itemView.findViewById(R.id.delistButton)
    private var adapter : SellerProductCardAdapter = SellerProductCardAdapter(productList)

    fun bindProduct(sellerProductItem: SellerProductItem) {
        //reference to firebase storage
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child(sellerProductItem.image)
        val ONE_MEGABYTE = 1024 * 1024.toLong()

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // Convert the bytes to a Bitmap
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            sellerProductCardItemBinding.sellerProductImageView.setImageBitmap(bitmap)
            sellerProductCardItemBinding.sellerProductNameTextview.text = sellerProductItem.name
            sellerProductCardItemBinding.sellerProductStockTextview.text =
                "Stock: " + sellerProductItem.stock
            sellerProductCardItemBinding.sellerProductSoldTextview.text =
                "Sold: " + sellerProductItem.sold
            sellerProductCardItemBinding.sellerProductRatingTextview.text =
                "Rating: " + sellerProductItem.rating
            sellerProductCardItemBinding.sellerProductCostTextview.text =
                "Cost: RM" + String.format(sellerProductItem.cost.toString(), "%.2f")
        }.addOnFailureListener {

        }

        delistButton.tag = sellerProductItem.productId

        delistButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to delist this item? You cannot reList this back.")

            builder.setPositiveButton("Yes") { dialog, which ->
                // Get the product ID from the tag of the delete button
                val productId = it.tag as String

                // Call the deleteProduct method with the product ID
                delistProduct(productId)
            }

            builder.setNegativeButton("No") { dialog, which ->
                // do nothing.
            }

            val dialog = builder.create()
            dialog.show()


        }
    }
    private fun delistProduct(productId: String) {

        //get db
        val db = Firebase.firestore

        //get the product's isDelisted Attribute
        db.collection("SellerProductItem").document(productId)
            .update("isDelisted", true)
            .addOnSuccessListener {
                Toast.makeText(context, "Record Successfully deListed!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting product with ID $productId from Firestore", e)
            }

//        //firebase storage
//        val storage = FirebaseStorage.getInstance()
//        val storageRef = storage.reference
//
//        //firestore
//        val db = Firebase.firestore
//
//        //Delete record (from firebase Storage first)
//        val productToBeDeleted = storageRef.child(productId.substring(4) + ".jpg")
//        productToBeDeleted.delete()
//            .addOnSuccessListener {
//                Log.d(TAG, "Product image with ID $productId deleted from Firebase Storage")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error deleting product image with ID $productId from Firebase Storage", e)
//            }
//
//        //Delete record (from firestore)
//        db.collection("SellerProductItem").document(productId)
//            .delete()
//            .addOnSuccessListener {
//                Log.d(TAG, "Product info with ID $productId deleted from Firestore")
//                Toast.makeText(context, "Record Successfully Deleted!", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error deleting product with ID $productId from Firestore", e)
//            }
    }
}
