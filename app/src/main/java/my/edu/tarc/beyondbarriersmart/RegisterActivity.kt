package my.edu.tarc.beyondbarriersmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    private var isSellerRegistration = true

    // general components
    private val sellerFragment = SellerRegistrationFragment()
    private val buyerFragment = BuyerRegistrationFragment()
    private val title_placeholder: TextView = findViewById(R.id.welcome_registration_text)
    private val short_description_placeholder: TextView = findViewById(R.id.welcome_short_desc_text)
    private val register_as_clickable_text: TextView = findViewById(R.id.register_seller_or_buyer_clickable_text)
    private val register_button: Button = findViewById(R.id.register_button)

    // form components

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_as_clickable_text.setOnClickListener {
            isSellerRegistration = !isSellerRegistration
            switchRegistration()
        }

        register_button.setOnClickListener {
            checkRegisterValidation()
            register()
        }

        switchRegistration()
    }

    private fun checkRegisterValidation() {

    }

    private fun register() {

    }

    private fun switchRegistration() {
        if (isSellerRegistration) {
            title_placeholder.setText(R.string.welcome_seller)
            short_description_placeholder.setText(R.string.seller_description)
            register_as_clickable_text.setText(R.string.register_buyer)

            supportFragmentManager.beginTransaction()
                .replace(R.id.register_form_fragment_layout, sellerFragment)
                .commit()
        }
        else {
            title_placeholder.setText(R.string.welcome_user)
            short_description_placeholder.setText(R.string.user_description)
            register_as_clickable_text.setText(R.string.register_buyer)

            supportFragmentManager.beginTransaction()
                .replace(R.id.register_form_fragment_layout, buyerFragment)
                .commit()
        }
    }
}