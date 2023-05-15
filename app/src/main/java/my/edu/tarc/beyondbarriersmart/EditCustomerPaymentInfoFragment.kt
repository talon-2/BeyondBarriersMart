package my.edu.tarc.beyondbarriersmart

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class EditCustomerPaymentInfoFragment : DialogFragment() {
    private lateinit var bankNameInput: Spinner
    private lateinit var cardNoInput: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_edit_customer_payment_info, null)

        bankNameInput = view.findViewById(R.id.buyer_profile_bank_name_edit)
        cardNoInput = view.findViewById(R.id.buyer_profile_card_number_edit)

        val bankNames = resources.getStringArray(R.array.bank_names)
        val adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, bankNames)
        bankNameInput.adapter = adapter

        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
        val userBank = sharedPref.getString(LoginFragment.bankName, bankNameInput!!.selectedItem.toString())
        var index = 0
        for (bank in bankNames) {
            if (bank.equals(userBank)) {
                break
            }
            ++index
        }
        bankNameInput.setSelection(index)
        cardNoInput.setText(sharedPref.getString(LoginFragment.cardNo, cardNoInput!!.text.toString()))

        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle("Edit Information")
            .setPositiveButton("Save") { dialog, _ ->
                val currId = sharedPref.getString(LoginFragment.custId, "")
                val bankName = LoginFragment.bankName
                val cardNo = LoginFragment.cardNo

                val ref = FirebaseFirestore.getInstance().collection("Customer").document(currId!!)
                val updates = hashMapOf<String, Any>(
                    bankName to bankNameInput!!.selectedItem.toString(),
                    cardNo to cardNoInput!!.text.toString()
                )

                ref.update(updates)

                val editor = sharedPref.edit()
                editor!!.putString(LoginFragment.bankName, bankNameInput!!.selectedItem.toString())
                editor!!.putString(LoginFragment.cardNo, cardNoInput!!.text.toString())
                editor!!.apply()

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}