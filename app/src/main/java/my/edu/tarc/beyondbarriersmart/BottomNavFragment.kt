package my.edu.tarc.beyondbarriersmart

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
                    //if you need to display a fragment
                    val homeFragment = CartFragment()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                    true
                }

                R.id.profileIcon -> {
                    //if you want to display an activity
                    //note that you have to recopy this entire function in your .kt, since it is a seperate activity.
                    val intent = Intent(activity, SpendingChartActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.cartIcon -> {
                    val cartFragment = CartFragment()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, cartFragment)
                    true
                }

                R.id.logoutIcon -> {
                    true
                }

                else -> false
            }
        }
    }
}