package my.edu.tarc.beyondbarriersmart

import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomnavigation.BottomNavigationView

class SpendingChartActivity : AppCompatActivity() {

    lateinit var barList: ArrayList<BarEntry> //create arraylist for data
    lateinit var barDataSet : BarDataSet
    lateinit var barData: BarData
    lateinit var barChart: BarChart
    lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_chart)
        calendar = Calendar.getInstance()
        barChart = findViewById(R.id.barchart)

        getChartData()

        barDataSet = BarDataSet(barList, "Spendings")
        barData = BarData(barDataSet)
        barChart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.color = Color.BLUE
        barChart.animateY(1000)

        barDataSet.valueTextSize = 16f
        barChart.setDrawGridBackground(false)
        barChart.description.isEnabled = false
        //barChart.visibility = View.VISIBLE

        val bottomNavigationFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.spending_chart_fragment_container, bottomNavigationFragment)
            .commit()
    }

    private fun getChartData(){
        barList = ArrayList()
        barList.add(BarEntry(1f, 10.00f))
        barList.add(BarEntry(2f, 200.00f))
        barList.add(BarEntry(3f, 33.33f))
        barList.add(BarEntry(4f, 0f))
    }

}