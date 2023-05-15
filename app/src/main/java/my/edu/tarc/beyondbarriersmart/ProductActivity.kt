package my.edu.tarc.beyondbarriersmart

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.media.Rating
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.processNextEventInCurrentThread
import my.edu.tarc.beyondbarriersmart.databinding.ActivityProductBinding
import org.w3c.dom.Text
import java.util.Locale.Category

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    val db  = Firebase.firestore
    private lateinit var gridView: GridView
    private lateinit var cards: MutableList<Card>

    private lateinit var addToCartBtn: Button
    private lateinit var currentProduct: String
    private lateinit var currentCate:String

    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var productRating: RatingBar
    private lateinit var productDesc: TextView

    private lateinit var sellerName: TextView
    //private lateinit var sellerImage: ImageView

    private lateinit var plusBtn: Button
    private lateinit var minusBtn: Button
    private lateinit var quantityNumber: TextView
    private lateinit var currentCustomerId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //current customer data (TO BE UPDATED)
        val sharedPref = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
        currentCustomerId = sharedPref.getString(LoginFragment.custId, "").toString()

        gridView = findViewById(R.id.dynamicRelatedProductGridView)
        cards = mutableListOf()

        addToCartBtn = findViewById(R.id.addToCartButton)
        productImage = findViewById(R.id.itemImageView)
        productName = findViewById(R.id.itemNameTextView)
        productPrice = findViewById(R.id.itemPriceTextView)
        productRating = findViewById(R.id.itemRatingBar)
        productDesc = findViewById(R.id.itemDescTextView)
        plusBtn = findViewById(R.id.plusAmtButton)
        minusBtn = findViewById(R.id.minusSignButton)
        quantityNumber = findViewById(R.id.quantityNumberText)

        sellerName = findViewById(R.id.sellerNameTextView)

        currentProduct = intent.getStringExtra("id_key").toString()
        currentCate = intent.getStringExtra("cate_key").toString()

        val bottomNavigationFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.product_fragment_container, bottomNavigationFragment)
            .commit()

        plusBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val currentQuantity = quantityNumber.text.toString().toInt()
                val newQuantity = currentQuantity + 1
                quantityNumber.text = newQuantity.toString()
            }
        })
        minusBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val currentQuantity = quantityNumber.text.toString().toInt()
                val newQuantity = currentQuantity - 1
                if(currentQuantity > 1){
                    quantityNumber.text = newQuantity.toString()
                }

            }
        })
        addToCartBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                addDataToCartInDatabase()

                val intent = Intent(this@ProductActivity, CategoryActivity::class.java)
                startActivity(intent)
                //Toast.makeText(this@ProductActivity, "addtoCart btn clicked ", Toast.LENGTH_SHORT).show()
            }
        })

        getProductDataFromDatabase()
        getRelatedDataFromDatabase()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getProductDataFromDatabase(){
        val categoryReference = FirebaseFirestore.getInstance().collection("SellerProductItem")
        val query = categoryReference.whereEqualTo("productId", currentProduct)
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        query.get().addOnSuccessListener { documents ->

            for (document in documents){
                val data = document.data

                var stockToCheck = "${data?.get("stock")}".toString().toInt()
                var deListToCheck = "${data?.get("isDelisted")}".toBoolean()

                if(stockToCheck > 0 && !deListToCheck) {

                    var currentImage = "${data?.get("image")}"
                    val currentName = "${data?.get("name")}"
                    val tempPrice = data?.get("cost") as Double
                    val currentPrice = String.format("RM%,.2f", tempPrice)
                    val currentRating = "${data?.get("rating")}"
                    val currentDesc = "${data?.get("description")}"
                    val currentSellerId = "${data?.get("sellerId")}"

                    val imageRef = storageRef.child(currentImage)
                    val ONE_MEGABYTE = 1024 * 1024.toLong()

                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        productImage.findViewById<ImageView>(R.id.itemImageView)
                            .setImageBitmap(bitmap)
                    }.addOnFailureListener {
                        //do nothing
                    }
                    productName.text = currentName
                    productPrice.text = currentPrice
                    productDesc.text = currentDesc
                    productRating.rating = currentRating.toFloat()

                    val sellerQuery = FirebaseFirestore.getInstance().collection("Seller")
                        .whereEqualTo("sellerId", currentSellerId)
                    sellerQuery.get().addOnSuccessListener { sellerDocuments ->
                        for (sellerDocument in sellerDocuments) {
                            val data = sellerDocument.data

                            val currentSellerName = "${data?.get("username")}"
                            /*
                        val currentSellerImage = "${data?.get("image")}"

                        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            sellerImage.findViewById<ImageView>(R.id.sellerNameTextView).setImageBitmap(bitmap)
                        }.addOnFailureListener {
                            //do nothing
                        }
                        */
                            sellerName.text = currentSellerName
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getRelatedDataFromDatabase(){
        val categoryReference = FirebaseFirestore.getInstance().collection("SellerProductItem")
        cards.clear()
        val query = categoryReference.whereEqualTo("category", currentCate)
        query.get().addOnSuccessListener { documents ->

                for (document in documents){
                    val data = document.data
                    val tempId = "${data?.get("productId")}"

                    if(tempId != currentProduct){

                        var currentImage = "${data?.get("image")}"
                        var currentName =  "${data?.get("name")}"
                        val tempPrice = data?.get("cost") as Double
                        val currentPrice = String.format("RM%,.2f", tempPrice)
                        val currentId = "${data?.get("productId")}"

                        var card = Card(currentImage, currentName, currentPrice, currentCate, currentId)

                        cards.add(card)
                    }
                }

                val cardAdapter = CardAdapter(this@ProductActivity, cards)
                gridView.adapter = cardAdapter

        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun addDataToCartInDatabase(){

        val cartRef = db.collection("Cart")

        val custId = currentCustomerId
        val itemAmt = quantityNumber.text.toString().toInt()
        val prodId = currentProduct

        val cartRecord = hashMapOf(
            "customerId" to custId,
            "itemAmt" to itemAmt,
            "productId" to prodId
        )
        cartRef.document()
            .set(cartRecord)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }
}