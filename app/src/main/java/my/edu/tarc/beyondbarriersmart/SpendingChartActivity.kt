package my.edu.tarc.beyondbarriersmart

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*

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
    private lateinit var refreshButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_chart)

        //get the spinner
        selectedMonth = findViewById(R.id.monthSpinner)
        selectedYear = findViewById(R.id.yearSpinner)

        selectedMonth.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                getChartData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                //do nothing
            }
        })

        selectedYear.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                getChartData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                //do nothing
            }
        })

        //initialize the values in the spinner
        currentMonthValue = selectedMonth.selectedItem.toString()
        currentYearValue = selectedYear.selectedItem.toString()

        //get the textViews
        highSpent = findViewById(R.id.highestSpendingAmtTextview)
        totalSpent = findViewById(R.id.totalSpendingAmtTextview)

        //get Button
        refreshButton = findViewById(R.id.spendingChartRefreshButton)

        //get barchart
        barChart = findViewById(R.id.barchart)

        //load the barChart as a co-routine.
        getChartData()

        val bottomNavigationFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.spending_chart_fragment_container, bottomNavigationFragment)
            .commit()

        refreshButton.setOnClickListener(){
            //refresh the current activity.
            finish()
            startActivity(getIntent())
        }


    }

    private fun getChartData() {
        currentMonthValue = selectedMonth.selectedItem.toString()
        currentYearValue = selectedYear.selectedItem.toString()

        //initialize the database
        val db = Firebase.firestore
        val purchaseRef = db.collection("Purchase")
        val productRef = db.collection("SellerProductItem")

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
        //var totalSpentInDay = 0f //store the total amount spent in day.
        var day = 1

        barList = ArrayList()

        for (day in 1..daysInMonth) {
            getNumberOfDays(currentYearValue.toInt(), getMonthInInt(currentMonthValue)).toString()

            barList.add(BarEntry(day.toFloat(), 0f)) //set the entire table first
            var totalSpentInDay = 0f

            //initialize current date
            val dateToCheck = "${day}-${getMonthInInt(currentMonthValue)}-$currentYearValue"

            val query = purchaseRef.whereEqualTo("custId", custId).whereEqualTo("date", dateToCheck)

            //look through the database with query
            query.get().addOnSuccessListener { documents ->

                if (documents.isEmpty){ //if it is empty, set up the barList record.
                    barList.add(
                        BarEntry(
                            day.toFloat(),
                            0f
                        )
                    )

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

                    //check if it is the highest Spending day
                    if (highestSpentInDay < totalSpentInDay) {
                        highestSpentInDay = totalSpentInDay
                        highestSpentDay = day
                    }

                    //add up to total
                    totalSpentInMonth += totalSpentInDay

                    //set the textviews
                    highSpent.text =
                        "RM " + highestSpentInDay + " (Day " + highestSpentDay + " " +selectedMonth.selectedItem.toString()+ " "+ selectedYear.selectedItem.toString()+")"
                    totalSpent.text =
                        "RM " + totalSpentInMonth + " (" + selectedMonth.selectedItem.toString() + " " + selectedYear.selectedItem.toString() + ")"
                }

                for (document in documents) { //for every purchase made on that date

                    val purchaseData = document.data

                    //assume that purchaseRef has productId, have a query requesting productId and their cost
                    var currentProduct = "${purchaseData?.get("productId")}" //store the current productId
                    var totalSpentInRecord = 0f
                    var prodQuantity = "${purchaseData?.get("purchaseAmt")}" //get the quantity bought of that product.

                    val prodQuery = productRef.whereEqualTo("productId", currentProduct) //find the related product

                    //loop through the database with that query
                    prodQuery.get().addOnSuccessListener { documents ->


                        for (document in documents) { //for every purchase made on that date
                            val productData = document.data

                            Log.d(TAG, "TRYING TO GET :" + "${productData?.get("cost")}" + " WITH AMOUNT: " + prodQuantity)
                            totalSpentInRecord += ("${productData?.get("cost")}".toFloat() * prodQuantity.toFloat())
                            Log.d(TAG, "DAY :" + day + " ACCUMULATED: " + totalSpentInRecord)

                            totalSpentInDay += totalSpentInRecord
                            Log.d(TAG, "DAY :" + day + " TOTALED: " + totalSpentInDay)
                        }

                        barList.add(
                            BarEntry(
                                day.toFloat(),
                                totalSpentInDay
                            )
                        )

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

                        //check if it is the highest Spending day
                        if (highestSpentInDay < totalSpentInDay) {
                            highestSpentInDay = totalSpentInDay
                            highestSpentDay = day
                        }

                        //add up to total
                        totalSpentInMonth += totalSpentInDay

                        //set the textviews
                        highSpent.text =
                            "RM " + highestSpentInDay + " (Day " + highestSpentDay + " " +selectedMonth.selectedItem.toString()+ " "+ selectedYear.selectedItem.toString()+")"
                        totalSpent.text =
                            "RM " + totalSpentInMonth + " (" + selectedMonth.selectedItem.toString() + " " + selectedYear.selectedItem.toString() + ")"
                    }
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

