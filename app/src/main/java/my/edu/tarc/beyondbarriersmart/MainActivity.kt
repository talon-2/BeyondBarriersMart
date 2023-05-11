package my.edu.tarc.beyondbarriersmart

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_add_products)
        setContentView(R.layout.activity_main)

        //call in a fragment
        val testFragment = AddProductsFragment()

        //Add BottomNavigationFragment
        val bottomNavigationFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, testFragment)
            .add(R.id.fragment_container, bottomNavigationFragment)
            .commit()

        //starting a activity (to be placed in an if else statement.)
        //some Activities can only be seen if you run it instead of looking it from the Design preview.
        //startActivity(Intent(this,MyProductsActivity::class.java))


    }
}