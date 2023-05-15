package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class BuyerProfileFragment : Fragment(), DialogInterface.OnDismissListener {
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_buyer_profile, container, false)

        usernameText = view.findViewById(R.id.buyer_profile_username)
        emailText = view.findViewById(R.id.buyer_profile_email)
        bankNameText = view.findViewById(R.id.buyer_profile_bank_name_text)
        cardNumberText = view.findViewById(R.id.buyer_profile_card_number)
        addressText = view.findViewById(R.id.buyer_profile_address)

        editProfileButton = view.findViewById(R.id.edit_profile_button)
        editPaymentMethodClickableText = view.findViewById(R.id.edit_payment_method_clickable_text)
        editAddressClickableText = view.findViewById(R.id.edit_address_clickable_text)
        spendingChartButton = view.findViewById(R.id.view_spending_chart_button)

        editProfileButton.setOnClickListener {
            val fragmentManager = requireFragmentManager()
            val dialog = EditCustomerInfoFragment()
            dialog.show(fragmentManager, "EditCustomerInfoFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        editPaymentMethodClickableText.setOnClickListener {
            val fragmentManager = requireFragmentManager()
            val dialog = EditCustomerPaymentInfoFragment()
            dialog.show(fragmentManager, "EditCustomerPaymentInfoFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        editAddressClickableText.setOnClickListener {
            val fragmentManager = requireFragmentManager()
            val dialog = EditCustomerAddressFragment()
            dialog.show(fragmentManager, "EditCustomerAddressFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        spendingChartButton.setOnClickListener {
            startActivity(Intent(this.requireActivity(), SpendingChartActivity::class.java))
        }

        update()

        val newFragment = BottomNavFragment()
        val transaction = fragmentManager?.beginTransaction()
        transaction?.add(R.id.fragment_container, newFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()

        return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        update()
    }

    private fun update() {
        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

        // update all possible fields
        usernameText.setText(sharedPref.getString(LoginFragment.username, usernameText!!.text.toString()))
        emailText.setText(sharedPref.getString(LoginFragment.email, emailText!!.text.toString()))
        bankNameText.setText(sharedPref.getString(LoginFragment.bankName, bankNameText!!.text.toString()))
        cardNumberText.setText(sharedPref.getString(LoginFragment.cardNo, cardNumberText!!.text.toString()))
        addressText.setText(sharedPref.getString(LoginFragment.address, addressText!!.text.toString()))
    }
}