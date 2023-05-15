package my.edu.tarc.beyondbarriersmart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_bottom_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        //make everything unchecked, we do this by adding an invisible element to be checked.
        bottomNavigationView.menu.findItem(R.id.homeIcon).isChecked = false
        bottomNavigationView.menu.findItem(R.id.profileIcon).isChecked = false
        bottomNavigationView.menu.findItem(R.id.cartIcon).isChecked = false
        bottomNavigationView.menu.findItem(R.id.logoutIcon).isChecked = false
        bottomNavigationView.menu.findItem(R.id.invisibleIcon).isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeIcon -> {
                    val intent = Intent(activity, CategoryActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    true
                }

                R.id.profileIcon -> {
                    //if you want to display an activity
                    //note that you have to recopy this entire function in your .kt, since it is a seperate activity.
                    val intent = Intent(activity, BuyerProfileActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    true
                }

                R.id.cartIcon -> {
                    val intent = Intent(activity, CartActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    true
                }

                R.id.logoutIcon -> {
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