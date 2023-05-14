package my.edu.tarc.beyondbarriersmart

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class SellerRegistrationFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_registration, container, false)
    }

    public fun validate(): Boolean {
        var result = true;
        val username: EditText? = view?.findViewById(R.id.seller_username_input)
        val email: EditText? = view?.findViewById(R.id.seller_email_input)
        val password: EditText? = view?.findViewById(R.id.seller_password_input)
        val passwordRetype: EditText? = view?.findViewById(R.id.seller_password_retype_input)

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

        return result;
    }
}
