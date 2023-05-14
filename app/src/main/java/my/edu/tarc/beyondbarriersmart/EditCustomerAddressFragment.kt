package my.edu.tarc.beyondbarriersmart

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class EditCustomerAddressFragment : DialogFragment() {
    private lateinit var addressInput: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_edit_customer_address, null)

        addressInput = view.findViewById(R.id.buyer_profile_popout_address_edit)

        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
        addressInput.setText(sharedPref.getString(LoginFragment.address, addressInput!!.text.toString()))

        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle("Edit Information")
            .setPositiveButton("Save") { dialog, _ ->
                val currId = sharedPref.getString(LoginFragment.custId, "")
                val address = LoginFragment.address

                val ref = FirebaseFirestore.getInstance().collection("Customer").document(currId!!)
                val updates = hashMapOf<String, Any>(
                    address to addressInput!!.text.toString()
                )

                val editor = sharedPref.edit()
                editor!!.putString(LoginFragment.address, addressInput!!.text.toString())
                editor!!.apply()

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}