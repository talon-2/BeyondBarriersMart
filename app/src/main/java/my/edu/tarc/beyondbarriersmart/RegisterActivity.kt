package my.edu.tarc.beyondbarriersmart

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class RegisterActivity : AppCompatActivity() {
    private var isSellerRegistration = true

    // general components
    private val sellerFragment = SellerRegistrationFragment()
    private val buyerFragment = BuyerRegistrationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // accessing the registration form fragment
        val fragmentManager = supportFragmentManager

        // general components
        val titlePlaceholder: TextView = findViewById(R.id.welcome_registration_text)
        val shortDescriptionPlaceholder: TextView = findViewById(R.id.welcome_short_desc_text)
        val registerAsClickableText: TextView = findViewById(R.id.register_seller_or_buyer_clickable_text)
        val registerButton: Button = findViewById(R.id.register_button)

        // switch between buyer and seller registration
        switchForm()
        registerAsClickableText.setOnClickListener {
            isSellerRegistration = !isSellerRegistration

            val transaction = fragmentManager.beginTransaction()

            if (isSellerRegistration) {
                titlePlaceholder.setText(R.string.welcome_seller)
                shortDescriptionPlaceholder.setText(R.string.seller_description)
                registerAsClickableText.setText(R.string.register_buyer)
            }
            else {
                titlePlaceholder.setText(R.string.welcome_user)
                shortDescriptionPlaceholder.setText(R.string.user_description)
                registerAsClickableText.setText(R.string.register_seller)
            }
            switchForm()
        }

        // register the buyer/seller
        registerButton.setOnClickListener {
            var result: Boolean = true

            if (isSellerRegistration) {
                val registrationFragment = fragmentManager.findFragmentById(R.id.register_form_fragment_layout) as SellerRegistrationFragment
                result = registrationFragment.validate()
            }
            else {
                val registrationFragment = fragmentManager.findFragmentById(R.id.register_form_fragment_layout) as BuyerRegistrationFragment
                result = registrationFragment.validate()
            }
        }
    }

    private fun switchForm() {
        val transaction = supportFragmentManager.beginTransaction()

        if (isSellerRegistration) {
            transaction.replace(R.id.register_form_fragment_layout, sellerFragment)
        } else {
            transaction.replace(R.id.register_form_fragment_layout, buyerFragment)
        }
        transaction.commit()
    }
}