package my.edu.tarc.beyondbarriersmart

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SpendingChartActivity : AppCompatActivity() {

    lateinit var barList: ArrayList<BarEntry> //create arraylist for data
    lateinit var barDataSet: BarDataSet
    lateinit var barData: BarData
    lateinit var barChart: BarChart
    private lateinit var selectedMonth: Spinner
    private lateinit var currentMonthValue: String
    private lateinit var selectedYear: Spinner
    private lateinit var currentYearValue: String
    private lateinit var highSpent: TextView
    private lateinit var totalSpent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_chart)

        //get the spinner
        selectedMonth = findViewById(R.id.monthSpinner)
        selectedYear = findViewById(R.id.yearSpinner)

        //initialize the values in the spinner
        currentMonthValue = selectedMonth.selectedItem.toString()
        currentYearValue = selectedYear.selectedItem.toString()

        //get the textViews
        highSpent = findViewById(R.id.highestSpendingAmtTextview)
        totalSpent = findViewById(R.id.totalSpendingAmtTextview)

        barChart = findViewById(R.id.barchart)

        //load the barChart as a co-routine.
        lifecycleScope.launch {
            getChartData()
        }

//        barDataSet = BarDataSet(barList, "Spendings")
//        barDataSet.setDrawValues(false)
//        barChart.xAxis.setDrawLabels(false)
//        barChart.xAxis.setDrawGridLinesBehindData(false)
//        barData = BarData(barDataSet)
//        barChart.data = barData
//        barDataSet.valueTextColor = Color.BLACK
//        barDataSet.color = Color.BLUE
//        barChart.animateY(1000)
//
//        barDataSet.valueTextSize = 16f
//        barChart.setDrawGridBackground(false)
//        barChart.description.isEnabled = false

        val bottomNavigationFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.spending_chart_fragment_container, bottomNavigationFragment)
            .commit()
    }

    private suspend fun getChartData() {
        //initialize the database
        val db = Firebase.firestore
        val purchaseRef = db.collection("Purchase")
        //val productRef = db.collection("SellerProductItem")

        //initialize variables
        val currentMonth = getMonthInInt(currentMonthValue)
        val currentYear = currentYearValue.toInt()
        var highestSpentInDay = 0f
        var highestSpentDay = 0
        var totalSpentInMonth = 0f
        val daysInMonth = getNumberOfDays(currentMonth, currentYear)

        var rowcount = 1f
        var amtSpentInDay = 0f

        //who is the CustomerId using this app? (TO BE CHANGED)
        val custId = "C0001"

        //variable for saving a purchase record.
        var productCost = 0f

        var day = 1

        barList = ArrayList()

        for (day in 1..daysInMonth) {
            getNumberOfDays(currentYearValue.toInt(), getMonthInInt(currentMonthValue)).toString()

            //initialize current date
            val dateToCheck = "${day}-${getMonthInInt(currentMonthValue)}-$currentYearValue"

            val query = purchaseRef.whereEqualTo("custId", custId).whereEqualTo("date", dateToCheck)

            //look through the database, loop through every record
            query.get().addOnSuccessListener { documents ->

                for (document in documents) {
                    val purchaseData = document.data

                    barList.add(BarEntry(day.toFloat(), "${purchaseData?.get("purchaseAmt")}".toFloat()))

                    barDataSet = BarDataSet(barList, "Spendings")
                    barDataSet.setDrawValues(false)
                    barChart.xAxis.setDrawLabels(false)
                    barChart.xAxis.setDrawGridLinesBehindData(false)
                    barData = BarData(barDataSet)
                    barChart.data = barData
                    barDataSet.valueTextColor = Color.BLACK
                    barDataSet.color = Color.BLUE
                    barChart.animateY(1000)

                    barDataSet.valueTextSize = 16f
                    barChart.setDrawGridBackground(false)
                    barChart.description.isEnabled = false

                    amtSpentInDay = "${purchaseData?.get("purchaseAmt")}".toFloat()

                    //check if it is the highest Spending day
                    if (highestSpentInDay < amtSpentInDay) {
                    highestSpentInDay = amtSpentInDay
                    highestSpentDay = day
                    }

                    //add up to total
                    totalSpentInMonth += amtSpentInDay

                    //reset amtSpentInDay
                    amtSpentInDay = 0f

                    //set the textviews
                    highSpent.text = "RM " + highestSpentInDay + " (Day " + highestSpentDay + ")"
                    totalSpent.text = "RM " + totalSpentInMonth + " (" + selectedMonth.selectedItem.toString() + ")"

                }
            }.addOnFailureListener { exception ->
                Log.w(
                    ContentValues.TAG,
                    "There is an error with reading the database (Source: MyProductsActivity).",
                    exception
                )
            }

        }


    }

    private fun getNumberOfDays(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // set the calendar to the first day of the month
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) // return the number of days in the month
    }

    private fun getMonthInInt(month: String): Int {
        return when (month) {
            "January" -> 1
            "February" -> 2
            "March" -> 3
            "April" -> 4
            "May" -> 5
            "June" -> 6
            "July" -> 7
            "August" -> 8
            "September" -> 9
            "October" -> 10
            "November" -> 11
            "December" -> 12
            else -> 0
        }
    }
}

