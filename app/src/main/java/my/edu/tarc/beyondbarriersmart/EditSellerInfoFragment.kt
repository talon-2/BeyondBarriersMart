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
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class EditSellerInfoFragment : DialogFragment() {
    private lateinit var usernameTextInput: TextView
    private lateinit var emailTextInput: TextView
    private lateinit var bankNameInput: Spinner
    private lateinit var cardNoTextInput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_edit_seller_info, null)

        usernameTextInput = view.findViewById(R.id.seller_username_edit)
        emailTextInput = view.findViewById(R.id.seller_email_edit)
        bankNameInput = view.findViewById(R.id.seller_bank_name_edit)
        cardNoTextInput = view.findViewById(R.id.seller_card_number_edit)

        val bankNames = resources.getStringArray(R.array.bank_names)
        val adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, bankNames)
        bankNameInput.adapter = adapter

        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

        usernameTextInput.setText(sharedPref.getString(LoginFragment.username, getString(R.string.username_sample)))
        emailTextInput.setText(sharedPref.getString(LoginFragment.email, getString(R.string.email_sample)))
        cardNoTextInput.setText(sharedPref.getString(LoginFragment.cardNo, getString(R.string.credit_card_number_sample)))

        val userBank = sharedPref.getString(LoginFragment.bankName, bankNameInput!!.selectedItem.toString())
        var index = 0
        for (bank in bankNames) {
            if (bank.equals(userBank)) {
                break
            }
            ++index
        }
        bankNameInput.setSelection(index)

        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle("Edit Information")
            .setPositiveButton("Save") { dialog, _ ->
                val currId = sharedPref.getString(LoginFragment.sellerId, "")
                val username = LoginFragment.username
                val email = LoginFragment.email
                val bankName = LoginFragment.bankName
                val cardNo = LoginFragment.cardNo

                val ref = FirebaseFirestore.getInstance().collection("Seller").document(currId!!)
                val updates = hashMapOf<String, Any>(
                    username to usernameTextInput!!.text.toString(),
                    email to emailTextInput!!.text.toString(),
                    bankName to bankNameInput!!.selectedItem.toString(),
                    cardNo to cardNoTextInput!!.text.toString()
                )

                ref.update(updates)

                val editor = sharedPref.edit()
                editor!!.putString(LoginFragment.username, usernameTextInput!!.text.toString())
                editor!!.putString(LoginFragment.email, emailTextInput!!.text.toString())
                editor!!.putString(LoginFragment.bankName, bankNameInput!!.selectedItem.toString())
                editor!!.putString(LoginFragment.cardNo, cardNoTextInput!!.text.toString())
                editor!!.apply()

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}