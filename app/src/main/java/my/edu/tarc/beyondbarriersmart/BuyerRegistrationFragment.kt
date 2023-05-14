package my.edu.tarc.beyondbarriersmart

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import java.util.Calendar

class BuyerRegistrationFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_buyer_registration, container, false)

        // create pop out date selector for date of birth
        val dateOfBirthPicker = view.findViewById<EditText>(R.id.buyer_date_of_birth_input)
        dateOfBirthPicker!!.setOnClickListener {
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
                    val date = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    dateOfBirthPicker.setText(date)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year, month, day)

            datePickerDialog.show()
        }
        return view
    }

    public fun validate(): Boolean {
        var result = true;
        val username: EditText? = view?.findViewById(R.id.buyer_username_input)
        val email: EditText? = view?.findViewById(R.id.buyer_email_input)
        val password: EditText? = view?.findViewById(R.id.buyer_password_input)
        val passwordRetype: EditText? = view?.findViewById(R.id.buyer_password_retype_input)
        val phoneNumber: EditText? = view?.findViewById(R.id.buyer_phone_number_input)
        val dateOfBirth: EditText? = view?.findViewById(R.id.buyer_date_of_birth_input)

        if (username!!.text.toString().isEmpty()) {
            username!!.error = getString(R.string.empty_username)
            result = false
        }

        if (email!!.text.toString().isEmpty()) {
            email!!.error = getString(R.string.empty_email)
            result = false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email!!.text.toString()).matches()) {
            email!!.error = getString(R.string.invalid_email)
            result = false
        }

        if (password!!.text.toString().isEmpty()) {
            password!!.error = getString(R.string.empty_password)
            result = false
        }
        else if (password!!.text.toString() != passwordRetype!!.text.toString()) {
            password!!.error = getString(R.string.not_same_password)
            passwordRetype?.error = getString(R.string.not_same_password)
            result = false
        }

        val phoneRegex = Regex("^\\+?(60|0)(1[0-9]{8}|2[1-9]{1}[0-9]{7})$")
        if (!phoneRegex.matches(phoneNumber!!.text.toString())) {
            phoneNumber.error = getString(R.string.invalid_phone_number)
            result = false
        }
        else if (phoneNumber!!.text.toString().isEmpty()) {
            phoneNumber.error = getString(R.string.empty_phone_number)
            result = false
        }

        val currYear = Calendar.getInstance().get(Calendar.YEAR)
        val isInvalidYear = currYear - 18 <= dateOfBirth!!.text.toString()!!.split("/").last().toInt()
        if (isInvalidYear) {
            dateOfBirth!!.error = getString(R.string.underage)
            result = false
        }
        else if (dateOfBirth!!.text.toString().isEmpty()) {
            dateOfBirth!!.error = getString(R.string.empty_date)
            result = false
        }

        return result;
    }
}