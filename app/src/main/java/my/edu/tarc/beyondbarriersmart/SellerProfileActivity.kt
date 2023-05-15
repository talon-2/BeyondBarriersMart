package my.edu.tarc.beyondbarriersmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SellerProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_profile)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.seller_profile_fragment_container, SellerProfileFragment())
            add(R.id.seller_profile_fragment_container, SellerBottomNavFragment())
            commit()
        }
    }
}