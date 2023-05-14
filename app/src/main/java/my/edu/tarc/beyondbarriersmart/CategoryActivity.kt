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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.beyondbarriersmart.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

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

        filterSelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    //0 ->
                    //1 ->
                    2 ->{
                        sortAscend = true
                        sortDescend = false
                        //getDataFromDatabase(currentChosenCate)
                    }
                    3 ->{
                         sortAscend = true
                        sortDescend = false
                        //getDataFromDatabase(currentChosenCate)
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        fashionBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                choseCate = true
                currentChosenCate = "Fashion"
                getDataFromDatabase(currentChosenCate)
            }
        })
        sportBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                choseCate = true
                currentChosenCate = "Sports"
                getDataFromDatabase(currentChosenCate)
            }
        })
        dailyBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                choseCate = true
                currentChosenCate = "Daily"
                getDataFromDatabase(currentChosenCate)
            }
        })
        electronicBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                choseCate = true
                currentChosenCate = "Electronics"
                getDataFromDatabase(currentChosenCate)
            }
        })
        entertainmentBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                choseCate = true
                currentChosenCate = "Entertainment"
                getDataFromDatabase(currentChosenCate)
            }
        })
        foodBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                choseCate = true
                currentChosenCate = "Food"
                getDataFromDatabase(currentChosenCate)
            }
        })
        bookBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                choseCate = true
                currentChosenCate = "Books"
                getDataFromDatabase(currentChosenCate)
            }
        })
        decorationBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                choseCate = true
                currentChosenCate = "Decorative"
                getDataFromDatabase(currentChosenCate)
            }
        })

        getDataFromDatabase(currentChosenCate)

        //intent.putExtra("data_key", data)

        gridView.onItemClickListener = AdapterView.OnItemClickListener{parent, view,position,id->
            val clickedCard = cards[position]

            Log.d(TAG, "aaaaaaaaaaaaaaaaaaa")

            val intent = Intent(this, ProductActivity::class.java)
            intent.putExtra("cardName", clickedCard.name)

            startActivity(intent)
        }


    }

    @SuppressLint("SuspiciousIndentation")
    private fun getDataFromDatabase(currentCate: String){
        val categoryReference = FirebaseFirestore.getInstance().collection("SellerProductItem")
            cards.clear()
        val query = categoryReference.whereEqualTo("category", currentCate)

        query.get().addOnSuccessListener { documents ->
            for (document in documents){
                val data = document.data

                var currentImage = "${data?.get("image")}"
                var currentName =  "${data?.get("name")}"
                val price = data?.get("cost") as Double
                val currentPrice = String.format("RM%,.2f", price)

                var card = Card(currentImage, currentName, currentPrice)

                cards.add(card)

                if(sortAscend){
                    cards.sortBy { card.price }
                }else if(sortDescend){
                    cards.sortByDescending { card.price }
                }
            }
            val cardAdapter = CardAdapter(this@CategoryActivity, cards)
            gridView.adapter = cardAdapter
        }
    }


}