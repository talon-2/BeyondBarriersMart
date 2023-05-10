package my.edu.tarc.beyondbarriersmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import my.edu.tarc.beyondbarriersmart.databinding.ActivityMyProductsBinding
import my.edu.tarc.beyondbarriersmart.databinding.SellerProductCardItemBinding
import org.intellij.lang.annotations.JdkConstants.BoxLayoutAxis

class MyProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProductsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dummydata() //to be removed once database is implemented

        val sellerProductItem = this
        binding.sellerProductsRecyclerView.apply{
            layoutManager = GridLayoutManager(applicationContext, 1)
            adapter = SellerProductCardAdapter(productList)
        }

        val bottomNavigationFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.my_products_fragment_container, bottomNavigationFragment)
            .commit()
        
    }

    //to be removed once database is implemented
    private fun dummydata() {
        val item1 = SellerProductItem(
            R.drawable.baseline_person_24,
            "Prod1",
            10,
            10,
            5f,
            10f,
        )
        productList.add(item1)

        val item2 = SellerProductItem(
            R.drawable.baseline_person_24,
            "Prod1",
            10,
            10,
            5f,
            10f,
        )
        productList.add(item2)

        val item3 = SellerProductItem(
            R.drawable.baseline_person_24,
            "Prod1",
            10,
            10,
            5f,
            10f,
        )
        productList.add(item3)
    }
}