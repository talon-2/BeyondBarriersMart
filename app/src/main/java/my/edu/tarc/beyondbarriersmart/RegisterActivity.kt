package my.edu.tarc.beyondbarriersmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RegisterActivity", "onCreate has been run in RegisterActivity")
//        setContentView(R.layout.activity_register)

//        val registerFragment = SellerRegistrationFragment()
//
//        supportFragmentManager.beginTransaction()
//            .add(R.id.register_form_fragment_layout, registerFragment)
//            .commit()
    }

    override fun onStart() {
        super.onStart()

        Log.d("RegisterActivity", "onStart has been run in RegisterActivity")
        val registerFragment = SellerRegistrationFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.register_form_fragment_layout, registerFragment)
            .commit()
    }
}