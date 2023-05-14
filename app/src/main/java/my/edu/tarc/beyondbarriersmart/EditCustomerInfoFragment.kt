package my.edu.tarc.beyondbarriersmart

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class EditCustomerInfoFragment : DialogFragment() {
    private lateinit var profileImageButton: ImageButton
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneNumberInput: EditText
    private lateinit var dateOfBirthInput: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_edit_customer_info, null)

        profileImageButton = view.findViewById(R.id.buyer_profile_edit_pic)
        usernameInput = view.findViewById(R.id.buyer_profile_username_edit)
        emailInput = view.findViewById(R.id.buyer_profile_email_edit)
        phoneNumberInput = view.findViewById(R.id.buyer_profile_phone_number_edit)
        dateOfBirthInput = view.findViewById(R.id.buyer_profile_birthdate_edit)

        val sharedPref = requireContext().getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)

        usernameInput.setText(
            sharedPref.getString(
                LoginFragment.username,
                usernameInput!!.text.toString()
            )
        )
        emailInput.setText(sharedPref.getString(LoginFragment.email, emailInput!!.text.toString()))
        phoneNumberInput.setText(
            sharedPref.getString(
                LoginFragment.phoneNo,
                phoneNumberInput!!.text.toString()
            )
        )
        dateOfBirthInput.setText(
            sharedPref.getString(
                LoginFragment.dateOfBirth,
                dateOfBirthInput!!.text.toString()
            )
        )

        dateOfBirthInput!!.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this.requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val date = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateOfBirthInput!!.setText(date)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year, month, day
            )

            datePickerDialog.show()
        }

        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle("Edit Information")
            .setPositiveButton("Save") { dialog, _ ->
                val currId = sharedPref.getString(LoginFragment.custId, "")
                val username = LoginFragment.username
                val email = LoginFragment.email
                val phoneNo = LoginFragment.phoneNo
                val dateOfBirth = LoginFragment.dateOfBirth

                val ref = FirebaseFirestore.getInstance().collection("Customer").document(currId!!)
                val updates = hashMapOf<String, Any>(
                    username to usernameInput!!.text.toString(),
                    email to emailInput!!.text.toString(),
                    phoneNo to phoneNumberInput!!.text.toString(),
                    dateOfBirth to dateOfBirthInput!!.text.toString()
                )

                ref.update(updates)

                val editor = sharedPref.edit()
                editor!!.putString(LoginFragment.username, usernameInput!!.text.toString())
                editor!!.putString(LoginFragment.email, emailInput!!.text.toString())
                editor!!.putString(LoginFragment.phoneNo, phoneNumberInput!!.text.toString())
                editor!!.putString(LoginFragment.dateOfBirth, dateOfBirthInput!!.text.toString())
                editor!!.apply()

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}