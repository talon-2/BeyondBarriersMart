package my.edu.tarc.beyondbarriersmart

import androidx.fragment.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var bottomNavigationView: BottomNavigationView

    var isSellerRegistration = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_add_products)
        setContentView(R.layout.activity_register)

        registrationThings()

//        val testFragment = SellerRegistrationFragment()

//         Add BottomNavigationFragment
//        val bottomNavigationFragment = BottomNavFragment()

//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragment_container, testFragment)
//            .add(R.id.fragment_container, bottomNavigationFragment)
//            .commit()

        //starting a activity (to be placed in an if else statement.)
        //some Activities can only be seen if you run it instead of looking it from the Design preview.
        //startActivity(Intent(this,MyProductsActivity::class.java))
    }

    private fun registrationThings() {
        val registerAsBuyerOrSellerText: TextView = findViewById(R.id.register_seller_or_buyer_clickable_text)
        val sellerFragment = SellerRegistrationFragment()
        val buyerFragment = BuyerRegistrationFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.register_form_fragment_layout, buyerFragment)
            .commit()

        registerAsBuyerOrSellerText.setOnClickListener {
            isSellerRegistration = !isSellerRegistration

            if (isSellerRegistration) {
                registerAsBuyerOrSellerText.setText(getString(R.string.register_buyer))
                supportFragmentManager.beginTransaction().replace(R.id.register_form_fragment_layout, sellerFragment).commit()
            }
            else {
                registerAsBuyerOrSellerText.setText(getString(R.string.register_seller))
                supportFragmentManager.beginTransaction().replace(R.id.register_form_fragment_layout, buyerFragment).commit()
            }
        }
    }
}