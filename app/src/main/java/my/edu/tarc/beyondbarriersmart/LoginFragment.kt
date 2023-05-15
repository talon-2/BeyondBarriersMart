package my.edu.tarc.beyondbarriersmart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    companion object {
        val sellerId = "sellerId"
        val cert = "cert"
        val custId = "custId"
        val address = "address"
        val bankName = "bankName"
        val cardNo = "cardNo"
        val dateOfBirth = "dateOfBirth"
        val email = "email"
        val password = "password"
        val phoneNo = "phoneNo"
        val profileImage = "profileImage"
        val username = "username"
    }

    private lateinit var username_input: EditText
    private lateinit var password_input: EditText
    private lateinit var forgot_password_clickable_text: TextView
    private lateinit var create_new_account_clickable_text: TextView
    private lateinit var loginButton : Button
    val db = Firebase.firestore
    val customerRef = db.collection("Customer")
    val sellerRef = db.collection("Seller")
    var currentSellerId = ""
    var currentCustId = ""
    var currentAddress = ""
    var currentBankName = ""
    var currentCardNo = ""
    var currentDateOfBirth = ""
    var currentEmail = ""
    var currentPassword = ""
    var currentPhoneNo = ""
    var currentProfileImage = ""
    var currentUsername = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        username_input = view.findViewById(R.id.login_username_edit)
        password_input = view.findViewById(R.id.login_password_edit)
        create_new_account_clickable_text = view.findViewById(R.id.create_account_clickable_text)
        loginButton = view.findViewById(R.id.login_button)

        loginButton.setOnClickListener(){
            if(validationCheck()){
                validateLogin()
            }
        }

        create_new_account_clickable_text.setOnClickListener(){
//            val intent = Intent(activity, RegisterActivity::class.java)
//            startActivity(intent)

            activity?.startActivity(Intent(activity, RegisterActivity::class.java))

        }

        return view
    }

    private fun validationCheck(): Boolean{
        if (username_input.text.isEmpty() || password_input.text.isEmpty()){
            //pop up
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Warning")
            builder.setMessage("Please Fill Your Username and Password.\n")

            builder.setNegativeButton("Ok") { dialog, which ->
                // do nothing.
            }

            val dialog = builder.create()
            dialog.show()
            return false
        }
        return true
    }

    @SuppressLint("SuspiciousIndentation")
    private fun validateLogin() {
        val fetchUsername = username_input.text.toString()
        val fetchPassword = password_input.text.toString()
        val customerQuery = FirebaseFirestore.getInstance()
            .collection("Customer")
            .whereEqualTo("username", fetchUsername)
            .whereEqualTo("password", fetchPassword)

        customerQuery.get().addOnSuccessListener { customerSnapshot ->
            if (customerSnapshot.isEmpty) {
                //if no customer found, find through the seller collection.
                val sellerQuery = FirebaseFirestore.getInstance()
                    .collection("Seller")
                    .whereEqualTo("username", fetchUsername)
                    .whereEqualTo("password", fetchPassword)

                    Log.d(TAG, "username: " + fetchUsername + " password: " + fetchPassword)

                sellerQuery.get().addOnSuccessListener { sellerSnapshot ->
                    if (sellerSnapshot.isEmpty) {
                        Log.d(TAG, "username: " + fetchUsername + " password: " + fetchPassword)
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Warning")
                        builder.setMessage("Could not find account with given Username and Password.\n")

                        builder.setNegativeButton("Ok") { dialog, which ->
                            // do nothing.
                        }

                        val dialog = builder.create()
                        dialog.show()
                    } else {
                        //initialize shared Pref
                        val seller = sellerSnapshot.documents[0].data

                        val sharedPreferences =
                            context?.getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()

                        editor?.putString(LoginFragment.sellerId, "${seller?.get("sellerId")}")
                        editor?.putString(LoginFragment.username, "${seller?.get("username")}")
                        editor?.putString(LoginFragment.email, "${seller?.get("email")}")
                        editor?.putString(LoginFragment.password, "${seller?.get("password")}")
                        editor?.putString(LoginFragment.bankName, "${seller?.get("bankName")}")
                        editor?.putString(LoginFragment.cardNo, "${seller?.get("cardNo")}")
                        editor?.putString(LoginFragment.cert, "${seller?.get("cert")}")

                        editor?.apply()

//                        requireActivity().supportFragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, SellerProfileFragment())
//                            .commit()

                        val intent = Intent(activity, SellerProfileActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                        //navigate where the seller should go
                    }
                }
            }
            else{
                //initialize shared Pref
                val customer = customerSnapshot.documents[0].data

                val sharedPreferences =
                    context?.getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()

                Log.d("Login Section", customer?.get("custId").toString())

                editor?.putString(LoginFragment.custId, customer?.get("custId").toString())
                editor?.putString(LoginFragment.username, customer?.get("username").toString())
                editor?.putString(LoginFragment.password, customer?.get("password").toString())
                editor?.putString(LoginFragment.email, customer?.get("email").toString())
                editor?.putString(LoginFragment.phoneNo, customer?.get("phoneNo").toString())
                editor?.putString(LoginFragment.dateOfBirth, customer?.get("dateOfBirth").toString())
                editor?.putString(LoginFragment.address, customer?.get("address").toString())
                editor?.putString(LoginFragment.bankName, customer?.get("bankName").toString())
                editor?.putString(LoginFragment.cardNo, customer?.get("cardNo").toString())
//                editor?.putString(LoginFragment.profileImage, customer?.get("profileImage").toString())

                editor?.apply()

                val intent = Intent(activity, CategoryActivity::class.java)
                startActivity(intent)
                activity?.finish()
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, BuyerProfileFragment())
//                    .commit()

                //navigate where the customer should go
            }
        }
    }
}