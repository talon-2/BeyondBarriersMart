package my.edu.tarc.beyondbarriersmart

import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.beyondbarriersmart.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private val db  = Firebase.firestore
    val productImgs = db.collection("SellerProductItem")

    private lateinit var fashionBtn: Button
    private lateinit var sportBtn: Button
    private lateinit var dailyBtn: Button
    private lateinit var electronicBtn: Button
    private lateinit var entertainmentBtn: Button
    private lateinit var foodBtn: Button
    private lateinit var bookBtn: Button
    private lateinit var decorationBtn: Button

    private lateinit var filterSelection: Spinner

    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)
        gridView = findViewById(R.id.productGridView)

        val productImageList = mutableListOf<String>()
        val productNameList = mutableListOf<String>()
        val productPriceList = mutableListOf<String>()
        val cards = mutableListOf<Card>()

        fashionBtn = findViewById(R.id.fashionBtn)
        sportBtn = findViewById(R.id.sportsBtn)
        dailyBtn = findViewById(R.id.dailyBtn)
        electronicBtn = findViewById(R.id.electronicsBtn)
        entertainmentBtn = findViewById(R.id.entertainmentBtn)
        foodBtn = findViewById(R.id.foodBtn)
        bookBtn = findViewById(R.id.booksBtn)
        decorationBtn = findViewById(R.id.decorativeBtn)

        filterSelection = findViewById(R.id.cateFilterSpinner)
            //get the field data from database
        //db.collection("SellerProductItem").get().addOnSuccessListener { documents ->
            //for(document in documents){
                //productImageList.add()
                //productNameList.add()
                //productPriceList.add()

                //val card = Card(document.get("image") as String, document.get("name") as String,document.get("price") as String)
                //cards.add(card)
            //}
        //}

        //val cardAdapter = CardAdapter(this, cards)
        //gridView.adapter = cardAdapter
        //for(i in 0 until productNameList.size){
            //add each

        //}
    }

}