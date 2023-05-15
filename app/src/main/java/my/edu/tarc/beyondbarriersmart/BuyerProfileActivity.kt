package my.edu.tarc.beyondbarriersmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BuyerProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_profile)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.buyer_profile_fragment_container, BuyerProfileFragment())
            add(R.id.buyer_profile_fragment_container, BottomNavFragment())
            commit()
        }
    }
}