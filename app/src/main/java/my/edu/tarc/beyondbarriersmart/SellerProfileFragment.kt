package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class SellerProfileFragment : Fragment(), DialogInterface.OnDismissListener {
    private lateinit var usernameText: TextView
    private lateinit var emailText: TextView
    private lateinit var viewProductsButton: Button
    private lateinit var editProfileButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_seller_profile, container, false)

        usernameText = view.findViewById(R.id.seller_profile_username)
        emailText = view.findViewById(R.id.seller_profile_email)
        viewProductsButton = view.findViewById(R.id.view_my_products_button)
        editProfileButton = view.findViewById(R.id.seller_edit_profile_button)

        viewProductsButton.setOnClickListener {

        }

        editProfileButton.setOnClickListener {
            val fragmentManager = requireFragmentManager()
            val dialog = EditSellerInfoFragment()
            dialog.show(fragmentManager, "EditSellerInfoFragment")
            fragmentManager.executePendingTransactions()
            dialog.dialog?.setOnDismissListener {
                update()
            }
        }

        update()

        return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        update()
    }

    private fun update() {
        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

        usernameText.setText(sharedPref.getString(LoginFragment.username, getString(R.string.seller_name_sample)))
        emailText.setText(sharedPref.getString(LoginFragment.email, getString(R.string.email_sample)))
    }
}