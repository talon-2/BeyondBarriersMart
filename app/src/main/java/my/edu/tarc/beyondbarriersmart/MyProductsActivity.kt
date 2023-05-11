package my.edu.tarc.beyondbarriersmart

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.beyondbarriersmart.databinding.ActivityMyProductsBinding
import my.edu.tarc.beyondbarriersmart.databinding.SellerProductCardItemBinding
import org.intellij.lang.annotations.JdkConstants.BoxLayoutAxis

class MyProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProductsBinding
    private val db = Firebase.firestore
    private val noteRef = db.collection("sellerProduct")
    private val myObjects = mutableListOf<SellerProductItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProductsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        readData() //prayge
        dummydata() //to be removed once database is implemented

        val sellerProductItem = this
        binding.sellerProductsRecyclerView.apply{
            layoutManager = GridLayoutManager(applicationContext, 1)
            adapter = SellerProductCardAdapter(productList)
        }

        val bottomNavigationFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.my_products_fragment_container, bottomNavigationFragment)
            .commit()
        
    }

    //to be removed once database is implemented


    private fun readData(){
        noteRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){

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
                productList.add(item)

                }

                val adapter = SellerProductCardAdapter(productList)
                val recyclerView = findViewById<RecyclerView>(R.id.sellerProductsRecyclerView)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
//
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG, "There is an error with reading the database (Source: MyProductsActivity).", exception)
        }

    }


    private fun dummydata() {
//        val item1 = SellerProductItem(
//            myObjects.elementAt(0).productId,
//            "tempSeller1",
//            R.drawable.baseline_person_24,
//            "Prod1",
//            "desc1",
//            "Fashion",
//            10,
//            10,
//            5f,
//            10f,
//            false
//        )
//        productList.add(item1)
//
//        val item2 = SellerProductItem(
//            "productID1",
//            "tempSeller1",
//            R.drawable.baseline_person_24,
//            "Prod1",
//            "desc1",
//            "Fashion",
//            10,
//            10,
//            5f,
//            10f,
//            false
//        )
//        productList.add(item2)
//
//        val item3 = SellerProductItem(
//            "productID1",
//            "tempSeller1",
//            R.drawable.baseline_person_24,
//            "Prod1",
//            "desc1",
//            "Fashion",
//            10,
//            10,
//            5f,
//            10f,
//            false
//        )
//        productList.add(item3)
    }
}