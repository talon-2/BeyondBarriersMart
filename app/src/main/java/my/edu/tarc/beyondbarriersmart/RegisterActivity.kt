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

        // create pop out date selector for date of birth
        val dateOfBirthPicker = fragmentManager.findFragmentById(R.id.register_form_fragment_layout)?.view?.findViewById<EditText>(R.id.buyer_date_of_birth_input)
        dateOfBirthPicker?.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val date = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    dateOfBirthPicker.setText(date)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year, month, day)

            datePickerDialog.show()
        }

        // switch between buyer and seller registration
        registerAsClickableText.setOnClickListener {
            isSellerRegistration = !isSellerRegistration

            if (isSellerRegistration) {
                titlePlaceholder.setText(R.string.welcome_seller)
                shortDescriptionPlaceholder.setText(R.string.seller_description)
                registerAsClickableText.setText(R.string.register_buyer)

                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.register_form_fragment_layout, sellerFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            else {
                titlePlaceholder.setText(R.string.welcome_user)
                shortDescriptionPlaceholder.setText(R.string.user_description)
                registerAsClickableText.setText(R.string.register_seller)

                val transaction = fragmentManager.beginTransaction()
                transaction.add(R.id.register_form_fragment_layout, buyerFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
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

    private fun register() {
    }
}