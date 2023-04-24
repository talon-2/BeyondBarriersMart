package my.edu.tarc.beyondbarriersmart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_products)

        //starting a activity (to be placed in an if else statement.)
        //some Activities can only be seen if you run it instead of looking it from the Design preview.
        //startActivity(Intent(this,MyProductsActivity::class.java))

    }
}