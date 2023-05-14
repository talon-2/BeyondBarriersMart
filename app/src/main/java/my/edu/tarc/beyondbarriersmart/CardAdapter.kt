package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

data class Card(val image: String, val name: String, val price:String)

class CardAdapter(private val context: Context, private val cards: List<Card>) : BaseAdapter() {

    override fun getCount(): Int {
        return cards.size
    }

    override fun getItem(position: Int): Any {
        return cards[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_items_card, parent, false)

        val card = cards[position]
        view.findViewById<TextView>(R.id.productCardName).text = card.name
        view.findViewById<TextView>(R.id.productCardPrice).text = card.price

        return view
    }
}

