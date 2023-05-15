package my.edu.tarc.beyondbarriersmart

import androidx.fragment.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testFragment = LoginFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, testFragment)
            .commit()
//            .add(R.id.fragment_container, bottomNavigationFragment)

        //starting a activity (to be placed in an if else statement.)
        //some Activities can only be seen if you run it instead of looking it from the Design preview.
        startActivity(Intent(this,CategoryActivity::class.java))
//        finish()
    }
}
