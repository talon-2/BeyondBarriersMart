package my.edu.tarc.beyondbarriersmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class BuyerProfileFragment : Fragment() {
    private lateinit var editProfileButton: Button
    private lateinit var editPaymentMethodClickableText: TextView
    private lateinit var editAddressClickableText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_buyer_profile, container, false)

        editProfileButton = view.findViewById(R.id.edit_profile_button)
        editPaymentMethodClickableText = view.findViewById(R.id.edit_payment_method_clickable_text)
        editAddressClickableText = view.findViewById(R.id.edit_address_clickable_text)

        editProfileButton.setOnClickListener {
            val fragmentManager = requireFragmentManager()
            val dialog = EditCustomerInfoFragment()
            dialog.show(fragmentManager, "EditCustomerInfoFragment")
        }

        editPaymentMethodClickableText.setOnClickListener {
            val fragmentManager = requireFragmentManager()
            val dialog = EditCustomerPaymentInfoFragment()
            dialog.show(fragmentManager, "EditCustomerPaymentInfoFragment")
        }

        editAddressClickableText.setOnClickListener {
            val fragmentManager = requireFragmentManager()
            val dialog = EditCustomerAddressFragment()
            dialog.show(fragmentManager, "EditCustomerAddressFragment")
        }

        return view
    }
}