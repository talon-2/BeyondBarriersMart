package my.edu.tarc.beyondbarriersmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.checkout_fragment_container, CheckoutFragment())
            add(R.id.checkout_fragment_container, BottomNavFragment())
            commit()
        }
    }
}