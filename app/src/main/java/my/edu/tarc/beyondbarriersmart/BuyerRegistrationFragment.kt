package my.edu.tarc.beyondbarriersmart

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class BuyerRegistrationFragment : Fragment() {
    companion object {
        var custID = 1
    }

    private val SELECT_IMAGE_REQUEST_CODE = 10
    private var selectedImageUri: Uri? = null

    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var passwordRetype: EditText
    private lateinit var phoneNumberInput: EditText
    private lateinit var dateOfBirthInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var bankNameInput: Spinner
    private lateinit var cardNoInput: EditText
    private lateinit var profileImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_buyer_registration, container, false)

        usernameInput = view.findViewById(R.id.buyer_username_input)
        emailInput = view.findViewById(R.id.buyer_email_input)
        passwordInput = view.findViewById(R.id.buyer_password_input)
        passwordRetype = view.findViewById(R.id.buyer_password_retype_input)
        phoneNumberInput = view.findViewById(R.id.buyer_phone_number_input)
        dateOfBirthInput = view.findViewById(R.id.buyer_date_of_birth_input)
        addressInput = view.findViewById(R.id.buyer_address_input)
        bankNameInput = view.findViewById(R.id.buyer_bank_name_input)
        cardNoInput = view.findViewById(R.id.buyer_card_no_input)

        val bankNames = resources.getStringArray(R.array.bank_names)
        val adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, bankNames)
        bankNameInput.adapter = adapter

        // create pop out date selector for date of birth
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
                year, month, day)

            datePickerDialog.show()
        }
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
            passwordRetype?.error = getString(R.string.not_same_password)
            result = false
        }

        val phoneRegex = Regex("^\\+?(60|0)(1[0-9]{8}|2[1-9]{1}[0-9]{7})$")
        if (!phoneRegex.matches(phoneNumberInput!!.text.toString())) {
            phoneNumberInput.error = getString(R.string.invalid_phone_number)
            result = false
        }
        else if (phoneNumberInput!!.text.toString().isEmpty()) {
            phoneNumberInput.error = getString(R.string.empty_phone_number)
            result = false
        }

        if (dateOfBirthInput!!.text.toString().isEmpty()) {
            dateOfBirthInput!!.error = getString(R.string.empty_date)
            result = false
        }
        else {
            val currYear = Calendar.getInstance().get(Calendar.YEAR)
            val isInvalidYear = currYear - 18 <= dateOfBirthInput!!.text.toString().split("-").last().toInt()
            if (isInvalidYear) {
                dateOfBirthInput!!.error = getString(R.string.underage)
                result = false
            }
        }

        if (addressInput!!.text.toString().isEmpty()) {
            addressInput!!.error = getString(R.string.empty_address)
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
        Log.d("Buyer Registration", "this is being run")

        var custId = "C" + "${UUID.randomUUID().toString().substring(0, 4)}"
        var username = usernameInput!!.text.toString()
        var email = emailInput!!.text.toString()
        var password = passwordInput!!.text.toString()
        var phoneNo = phoneNumberInput!!.text.toString()
        var dateOfBirth = dateOfBirthInput!!.text.toString()
        var address = addressInput!!.text.toString()
        var bankName = bankNameInput!!.selectedItem.toString()
        var cardNo = cardNoInput!!.text.toString()
        var profileImage = String.format("%s.image", custId)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
        builder.setMessage("Do you confirm that you want to register under these information?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val item = Customer(
                custId,
                username,
                email,
                password,
                phoneNo,
                dateOfBirth,
                address,
                bankName,
                cardNo,
                profileImage
            )

            val collectionRef = db.collection("Customer").document(custId)
            collectionRef.set(item)
            Toast.makeText(context, "Successfully registered!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No") { dialog, which ->
            // do nothing
        }

        ++BuyerRegistrationFragment.custID

        val dialog = builder.create()
        dialog.show()
    }

    private fun setupImg(): String {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val custId = String.format("C%04d", BuyerRegistrationFragment.custID)
        val imageRef = storageRef.child("$custId.image")

        val bitmap = (profileImageButton.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = imageRef.downloadUrl
            } else {
                Log.e("TAG", "Error uploading image: ${task.exception}")
            }
        }

        return custId
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //to show the image on the screen
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data

            // Load the selected image into the ImageView using a library like Glide or Picasso
            Glide.with(this)
                .load(selectedImageUri)
                .into(profileImageButton)
        }
    }
}