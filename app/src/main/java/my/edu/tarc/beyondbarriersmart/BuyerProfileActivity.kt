package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext

class BuyerProfileActivity : AppCompatActivity() {
    private lateinit var usernameText: TextView
    private lateinit var emailText: TextView
    private lateinit var bankNameText: TextView
    private lateinit var cardNumberText: TextView
    private lateinit var addressText: TextView

    private lateinit var editProfileButton: Button
    private lateinit var editPaymentMethodClickableText: TextView
    private lateinit var editAddressClickableText: TextView
    private lateinit var spendingChartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_profile)

        usernameText = findViewById(R.id.buyer_profile_username)
        emailText = findViewById(R.id.buyer_profile_email)
        bankNameText = findViewById(R.id.buyer_profile_bank_name_text)
        cardNumberText = findViewById(R.id.buyer_profile_card_number)
        addressText = findViewById(R.id.buyer_profile_address)

        editProfileButton = findViewById(R.id.edit_profile_button)
        editPaymentMethodClickableText = findViewById(R.id.edit_payment_method_clickable_text)
        editAddressClickableText = findViewById(R.id.edit_address_clickable_text)
        spendingChartButton = findViewById(R.id.view_spending_chart_button)

        editProfileButton.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val dialog = EditCustomerInfoFragment()
            dialog.show(fragmentManager, "EditCustomerInfoFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        editPaymentMethodClickableText.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val dialog = EditCustomerPaymentInfoFragment()
            dialog.show(fragmentManager, "EditCustomerPaymentInfoFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        editAddressClickableText.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val dialog = EditCustomerAddressFragment()
            dialog.show(fragmentManager, "EditCustomerAddressFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        spendingChartButton.setOnClickListener {
            startActivity(Intent(this@BuyerProfileActivity, SpendingChartActivity::class.java))
        }

        update()

        supportFragmentManager.beginTransaction().apply {
            add(R.id.buyer_profile_fragment_container, BuyerProfileFragment())
            add(R.id.buyer_profile_fragment_container, BottomNavFragment())
            commit()
        }
    }

    private fun update() {
        val sharedPref = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

        // update all possible fields
        usernameText.setText(sharedPref.getString(LoginFragment.username, usernameText!!.text.toString()))
        emailText.setText(sharedPref.getString(LoginFragment.email, emailText!!.text.toString()))
        bankNameText.setText(sharedPref.getString(LoginFragment.bankName, bankNameText!!.text.toString()))
        cardNumberText.setText(sharedPref.getString(LoginFragment.cardNo, cardNumberText!!.text.toString()))
        addressText.setText(sharedPref.getString(LoginFragment.address, addressText!!.text.toString()))
    }
}