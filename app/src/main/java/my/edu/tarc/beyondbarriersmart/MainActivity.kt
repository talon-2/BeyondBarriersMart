package my.edu.tarc.beyondbarriersmart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_add_products)
        setContentView(R.layout.fragment_category)

        //starting a activity (to be placed in an if else statement.)
        //some Activities can only be seen if you run it instead of looking it from the Design preview.
        startActivity(Intent(this,SpendingChartActivity::class.java))

    }
}