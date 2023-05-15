package my.edu.tarc.beyondbarriersmart

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.beyondbarriersmart.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity(){

    private lateinit var binding: ActivityCategoryBinding
    val db  = Firebase.firestore
    private lateinit var gridView: GridView
    private lateinit var cards: MutableList<Card>

    private lateinit var fashionBtn: Button
    private lateinit var sportBtn: Button
    private lateinit var dailyBtn: Button
    private lateinit var electronicBtn: Button
    private lateinit var entertainmentBtn: Button
    private lateinit var foodBtn: Button
    private lateinit var bookBtn: Button
    private lateinit var decorationBtn: Button
    private lateinit var filterSelection: Spinner
    var choseCate: Boolean = false
    private var sortAscend: Boolean = false
    private var sortDescend: Boolean = false
    private var currentChosenCate: String = "Fashion"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gridView = findViewById(R.id.productGridView)
        cards = mutableListOf()

        fashionBtn = findViewById(R.id.fashionBtn)
        sportBtn = findViewById(R.id.sportsBtn)
        dailyBtn = findViewById(R.id.dailyBtn)
        electronicBtn = findViewById(R.id.electronicsBtn)
        entertainmentBtn = findViewById(R.id.entertainmentBtn)
        foodBtn = findViewById(R.id.foodBtn)
        bookBtn = findViewById(R.id.booksBtn)
        decorationBtn = findViewById(R.id.decorativeBtn)
        filterSelection = findViewById(R.id.cateFilterSpinner)

        val bottomNavigationFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.category_fragment_container, bottomNavigationFragment)
            .commit()



        filterSelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (position) {
                    //0 ->
                    //1 ->
                    2 -> {
                        sortAscend = true
                        sortDescend = false
                        getDataFromDatabase(currentChosenCate, 1)
                    }
                    3 -> {
                        sortAscend = true
                        sortDescend = false
                        getDataFromDatabase(currentChosenCate, 2)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        fashionBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                choseCate = true
                currentChosenCate = "Fashion"
                getDataFromDatabase(currentChosenCate, 0)
            }
        })
        sportBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                choseCate = true
                currentChosenCate = "Sports"
                getDataFromDatabase(currentChosenCate, 0)
            }
        })
        dailyBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                choseCate = true
                currentChosenCate = "Daily"
                getDataFromDatabase(currentChosenCate, 0)
            }
        })
        electronicBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                choseCate = true
                currentChosenCate = "Electronics"
                getDataFromDatabase(currentChosenCate, 0)
            }
        })
        entertainmentBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                choseCate = true
                currentChosenCate = "Entertainment"
                getDataFromDatabase(currentChosenCate, 0)
            }
        })
        foodBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                choseCate = true
                currentChosenCate = "Food"
                getDataFromDatabase(currentChosenCate, 0)
            }
        })
        bookBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                choseCate = true
                currentChosenCate = "Books"
                getDataFromDatabase(currentChosenCate, 0)
            }
        })
        decorationBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                choseCate = true
                currentChosenCate = "Decorative"
                getDataFromDatabase(currentChosenCate, 0)
            }
        })

        if(!choseCate){
            getDataFromDatabase(currentChosenCate, 0)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getDataFromDatabase(currentCate: String, sorting: Int){
        val categoryReference = FirebaseFirestore.getInstance().collection("SellerProductItem")
            cards.clear()
        val query = categoryReference.whereEqualTo("category", currentCate)

        query.get().addOnSuccessListener { documents ->
            documents?.let{
                var sortedDocuments = it.documents.toList()
                if(sorting == 1){
                     sortedDocuments = it.sortedBy{it["cost"] as Double}
                }else if(sorting == 2){
                    sortedDocuments = it.sortedByDescending { it["cost"] as Double }
                }


                for (document in sortedDocuments){
                    val data = document.data
                    var stockToCheck = "${data?.get("stock")}".toString().toInt()
                    var deListToCheck = "${data?.get("isDelisted")}".toBoolean()

                    if(stockToCheck > 0 && !deListToCheck){

                        var currentImage = "${data?.get("image")}"
                        var currentName =  "${data?.get("name")}"
                        val tempPrice = data?.get("cost") as Double
                        val currentPrice = String.format("RM%,.2f", tempPrice)
                        val currentId = "${data?.get("productId")}"

                        var card = Card(currentImage, currentName, currentPrice, currentCate, currentId)

                        cards.add(card)
                    }
                }

                val cardAdapter = CardAdapter(this@CategoryActivity, cards)
                gridView.adapter = cardAdapter
            }
        }
    }


}