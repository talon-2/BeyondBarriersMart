package my.edu.tarc.beyondbarriersmart

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class AddProductsFragment : Fragment() {

    //input fields
    private lateinit var productImageButton: ImageButton
    private lateinit var productNameInput: EditText
    private lateinit var productDescriptionInput: EditText
    private lateinit var productPriceInput: EditText
    private lateinit var productStockInput: EditText
    private lateinit var productCategoryInput: Spinner
    private lateinit var shipNeedsInput: CheckBox
    private lateinit var submitButton: Button
    private var selectedImageUri : Uri? = null

    //image input field
    private val SELECT_IMAGE_REQUEST_CODE = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_products, container, false)
        productImageButton = view.findViewById(R.id.addPhotoImgButton)
        productNameInput = view.findViewById(R.id.enterProductNameText)
        productDescriptionInput = view.findViewById(R.id.enterProductDescText)
        productPriceInput = view.findViewById(R.id.enterPriceText)
        productStockInput = view.findViewById(R.id.enterStockText)
        productCategoryInput = view.findViewById(R.id.categorySpinner)
        shipNeedsInput = view.findViewById(R.id.shipServiceRequiredCb)
        submitButton = view.findViewById(R.id.addProductButton)

        submitButton.setOnClickListener{
            saveProduct()
        }

        productImageButton.setOnClickListener{

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)

            //val REQUEST_IMAGE_PICK = 0
            //intent.type = "image/*"

            //startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //to show the image on the screen
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data

            // Load the selected image into the ImageView using a library like Glide or Picasso
            Glide.with(this)
                .load(selectedImageUri)
                .into(productImageButton)
        }
    }

    fun setupImg(): String {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val randomId = "${UUID.randomUUID()}"
        val imageRef = storageRef.child("$randomId.jpg")

        val bitmap = (productImageButton.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = imageRef.downloadUrl
            } else {
                Log.e("TAG", "Error uploading image: ${task.exception}")
            }
        }

        return randomId
    }

    fun saveProduct(){
        val db = (activity as MainActivity).db

        val generatedString = setupImg()
        val productId = "PROD$generatedString"
        val productImage = "$generatedString.jpg"
        val productName = productNameInput.text.toString()
        val productDescription = productDescriptionInput.text.toString()
        val productPrice = productPriceInput.text.toString().toFloat()
        val productStock = productStockInput.text.toString().toInt()
        val productCategory = productCategoryInput.selectedItem.toString()
        val shipNeeds = shipNeedsInput.isChecked

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to add this item? Items are automatically set up for sale once you have confirmed.")

        builder.setPositiveButton("Yes") { dialog, which ->
            val item = SellerProductItem(
                productId,
                "tempSellerID1",
                productImage,
                productName,
                productDescription,
                productCategory,
                productStock,
                0,
                0f,
                productPrice,
                shipNeeds
            )

            val collectionRef = db.collection("sellerProduct").document(productId)
            collectionRef.set(item)
            Toast.makeText(context, "Record Successfully added!", Toast.LENGTH_SHORT).show()

            //clear everything
        }

        builder.setNegativeButton("No") { dialog, which ->
            // do nothing.
        }

        val dialog = builder.create()
        dialog.show()
    }

}