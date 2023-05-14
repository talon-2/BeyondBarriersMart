package my.edu.tarc.beyondbarriersmart

import android.app.AlertDialog
import android.app.Dialog
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

        val customerQuery = FirebaseFirestore.getInstance()
            .collection("Customer")
            .whereEqualTo("address", addressInput!!.text.toString())

        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle("Edit Information")
            .setPositiveButton("Save") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}