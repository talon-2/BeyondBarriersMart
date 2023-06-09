package my.edu.tarc.beyondbarriersmart

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.storage.FirebaseStorage

data class Card(val image: String, val name: String, val price:String, val cate: String, val id:String)
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
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child(card.image)
        val ONE_MEGABYTE = 1024 * 1024.toLong()

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            view.findViewById<ImageView>(R.id.productCardImage).setImageBitmap(bitmap)
        }.addOnFailureListener {
            //do nothing
        }
        view.findViewById<TextView>(R.id.productCardName).text = card.name
        view.findViewById<TextView>(R.id.productCardPrice).text = card.price

        val layout = view.findViewById<CardView>(R.id.cateProductCardView)

        layout.setOnClickListener(){
            // Handle click event
            val intent = Intent( context, ProductActivity::class.java)
            intent.putExtra("id_key", card.id)
            intent.putExtra("cate_key", card.cate)
            context.startActivity(intent)
            //Toast.makeText(context, "CardView clicked" + card.name, Toast.LENGTH_SHORT).show()
        }

        return view
    }


}

