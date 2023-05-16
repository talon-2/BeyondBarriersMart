package my.edu.tarc.beyondbarriersmart

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SellerRegistrationFragment : Fragment() {
    private val REQUEST_CODE_PICK_FILE = 69
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var passwordRetype: EditText
    private lateinit var bankNameInput: Spinner
    private lateinit var cardNoInput: EditText
    private lateinit var certButton: ImageButton
    private var selectedFileUri: Uri? = null

    companion object {
        var sellerID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_seller_registration, container, false)

        usernameInput = view.findViewById(R.id.seller_username_input)
        emailInput = view.findViewById(R.id.seller_email_input)
        passwordInput = view.findViewById(R.id.seller_password_input)
        passwordRetype = view.findViewById(R.id.seller_password_retype_input)
        bankNameInput = view.findViewById(R.id.bank_name_input)
        cardNoInput = view.findViewById(R.id.card_no_input)
        certButton = view.findViewById(R.id.seller_upload_cert_button)

        certButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE)
        }

        val bankNames = resources.getStringArray(R.array.bank_names)
        val adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, bankNames)
        bankNameInput.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    public fun validate(): Boolean {
        var result = true;

        if (usernameInput!!.text.toString().isEmpty()) {
            usernameInput!!.error = getString(R.string.empty_username)
            result = false
        }

        if (emailInput!!.text.toString().isEmpty()) {
            emailInput!!.error = getString(R.string.empty_email)
            result = false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput!!.text.toString()).matches()) {
            emailInput!!.error = getString(R.string.invalid_email)
            result = false
        }

        if (passwordInput!!.text.toString().isEmpty()) {
            passwordInput!!.error = getString(R.string.empty_password)
            result = false
        }
        else if (passwordInput!!.text.toString() != passwordRetype!!.text.toString()) {
            passwordInput!!.error = getString(R.string.not_same_password)
            passwordRetype!!.error = getString(R.string.not_same_password)
            result = false
        }

        if (cardNoInput!!.text.toString().isEmpty()) {
            cardNoInput!!.error = getString(R.string.empty_card)
            result = false
        }
        else {
            val visaPattern = "^4[0-9]{12}(?:[0-9]{3})?\$".toRegex()
            val isVisa = visaPattern.matches(cardNoInput!!.text.toString())
            if (!isVisa) {
                cardNoInput!!.error = getString(R.string.invalid_card_no)
                result = false
            }
        }

        return result;
    }

    public fun register() {
        val db = Firebase.firestore

        var sellerId = "S" + "${UUID.randomUUID().toString().substring(0, 4)}"
        var username = usernameInput!!.text.toString()
        var email = emailInput!!.text.toString()
        var password = passwordInput!!.text.toString()
        var bankName = bankNameInput!!.selectedItem.toString()
        var cardNo = cardNoInput!!.text.toString()
        var certImg = String.format("%s.image", sellerId)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
        builder.setMessage("Do you confirm that you want to register under these information?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val item = Seller(
                sellerId,
                username,
                email,
                password,
                bankName,
                cardNo,
                certImg
            )

            val collectionRef = db.collection("Seller").document(sellerId)
            collectionRef.set(item)
            Toast.makeText(context, "Successfully registered!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No") { dialog, which ->
            // do nothing
        }

        ++SellerRegistrationFragment.sellerID

        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK && data != null) {
            selectedFileUri = data.data
            if (selectedFileUri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, selectedFileUri)
                certButton.setImageBitmap(bitmap)
            }
        }
    }}
