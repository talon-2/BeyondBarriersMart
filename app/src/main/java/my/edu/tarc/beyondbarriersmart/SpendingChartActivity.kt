package my.edu.tarc.beyondbarriersmart

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class SpendingChartActivity : AppCompatActivity() {

    lateinit var barList: ArrayList<BarEntry> //create arraylist for data
    lateinit var barDataSet : BarDataSet
    lateinit var barData: BarData
    lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_chart)
        barChart = findViewById(R.id.barchart)

        getChartData()

        barDataSet = BarDataSet(barList, "Bar Chart Data")
        barData = BarData(barDataSet)
        barChart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        //barDataSet.color = Color.BLUE
        barDataSet.setColor(resources.getColor(R.color.purple_200))

        barDataSet.valueTextSize = 16f
        barChart.description.isEnabled = false
        //barChart.visibility = View.VISIBLE
    }

    private fun getChartData(){
        barList = ArrayList()
        barList.add(BarEntry(1f, 1f))
        barList.add(BarEntry(2f, 2f))
        barList.add(BarEntry(3f, 3f))
        barList.add(BarEntry(4f, 4f))


    }
}