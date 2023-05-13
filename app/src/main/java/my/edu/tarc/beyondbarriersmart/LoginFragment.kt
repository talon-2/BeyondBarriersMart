package my.edu.tarc.beyondbarriersmart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class LoginFragment : Fragment() {
    private val email_input: EditText? = getView()?.findViewById(R.id.login_username_edit)
    private val password_input: EditText? = getView()?.findViewById(R.id.login_password_edit)
    private val forgot_password_clickable_text: TextView? = getView()?.findViewById(R.id.forgot_password_clickable_text)
    private val create_new_account_clickable_text: TextView? = getView()?.findViewById(R.id.create_account_clickable_text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun validateLogin() {
        val email: String = email_input?.text.toString()
        // what is love, baby dont hurt me
    }

    private fun forgotPassword() {

    }

    private fun createAccount() {

    }
}