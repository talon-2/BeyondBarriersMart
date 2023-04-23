package my.edu.tarc.beyondbarriersmart

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class SpendingChartFragment : Fragment() {

    lateinit var barList: ArrayList<BarEntry> //create arraylist for data
    lateinit var barDataSet : BarDataSet
    lateinit var barData: BarData
    lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spending_chart, container, false)
        barChart = view.findViewById(R.id.barchart)

        setBarData()

        barDataSet = BarDataSet(barList, "Bar Chart Data")
        barData = BarData(barDataSet)
        barChart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.color = Color.BLUE
        //barDataSet.setColor(resources.getColor(R.color.purple_200))

        barDataSet.valueTextSize = 16f
        barChart.description.isEnabled = false
        //barChart.visibility = View.VISIBLE

        return view
    }

     private fun setBarData(){
        barList = ArrayList()
        barList.add(BarEntry(1f, 1f))
        barList.add(BarEntry(2f, 2f))
        barList.add(BarEntry(3f, 3f))
        barList.add(BarEntry(4f, 4f))


    }

}