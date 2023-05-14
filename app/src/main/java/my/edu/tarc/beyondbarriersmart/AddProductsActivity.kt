package my.edu.tarc.beyondbarriersmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AddProductsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_products)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container, AddProductsFragment())
            add(R.id.fragment_container, SellerBottomNavFragment())
            commit()
        }
    }
}