package my.edu.tarc.beyondbarriersmart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

class SellerBottomNavFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_bottom_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.seller_bottom_navigation_view)

        //make everything unchecked, we do this by adding an invisible element to be checked.
        bottomNavigationView.menu.findItem(R.id.sellerProductIcon).isChecked = false
        bottomNavigationView.menu.findItem(R.id.sellerProfileIcon).isChecked = false
        bottomNavigationView.menu.findItem(R.id.sellerLogoutIcon).isChecked = false
        bottomNavigationView.menu.findItem(R.id.sellerInvisibleIcon).isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.sellerProfileIcon -> {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    val newFragment = LoginFragment()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.add(R.id.fragment_container, newFragment)
                    transaction?.addToBackStack(null)
                    transaction?.commit()

                    true
                }

                R.id.sellerProductIcon -> {
                    //if you want to display an activity
                    //note that you have to recopy this entire function in your .kt, since it is a seperate activity.
                    val intent = Intent(activity, MyProductsActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.sellerLogoutIcon -> {
                    //pop up
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Warning")
                    builder.setMessage("Are You Sure you Want to LogOut?.\n")

                    builder.setPositiveButton("Yes") { dialog, which ->
                        //navigate to logout
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        requireActivity().startActivity(intent)
                    }

                    builder.setNegativeButton("No") { dialog, which ->
                        // do nothing.
                    }

                    val dialog = builder.create()
                    dialog.show()
                    true
                }

                else -> false
            }
        }
    }
}