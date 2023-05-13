package my.edu.tarc.beyondbarriersmart

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

    public fun validate(): Boolean {
        var result = true;
        val username: EditText? = view?.findViewById(R.id.buyer_username_input)
        val email: EditText? = view?.findViewById(R.id.buyer_email_input)
        val password: EditText? = view?.findViewById(R.id.buyer_password_input)
        val passwordRetype: EditText? = view?.findViewById(R.id.buyer_password_retype_input)
        val phoneNumber: EditText? = view?.findViewById(R.id.buyer_phone_number_input)
        val dateOfBirth: EditText? = view?.findViewById(R.id.buyer_date_of_birth_input)

        if (username?.text.toString()?.length == 0) {
            username?.error = getString(R.string.empty_username)
            result = false
        }

        if (email?.text.toString()?.length == 0) {
            email?.error = getString(R.string.empty_email)
            result = false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email?.text.toString()).matches()) {
            email?.error = getString(R.string.invalid_email)
            result = false
        }

        if (password?.text.toString()?.length == 0) {
            password?.error = getString(R.string.empty_password)
            result = false
        }
        else if (password?.text.toString() != passwordRetype?.text.toString()) {
            password?.error = getString(R.string.not_same_password)
            passwordRetype?.error = getString(R.string.not_same_password)
            result = false
        }

        val currYear = Calendar.getInstance().get(Calendar.YEAR)
        val isInvalidYear = currYear - 18 > dateOfBirth!!.text.toString()!!.split("/").last().toInt()
        if (isInvalidYear) {
            dateOfBirth!!.error = getString(R.string.underage)
            result = false
        }

        return result;
    }
}