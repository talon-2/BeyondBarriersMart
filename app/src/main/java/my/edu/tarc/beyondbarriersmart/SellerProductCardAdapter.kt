package my.edu.tarc.beyondbarriersmart

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.beyondbarriersmart.databinding.SellerProductCardItemBinding

class SellerProductCardAdapter(private val products: List<SellerProductItem>)
    : RecyclerView.Adapter<SellerProductCardViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = SellerProductCardItemBinding.inflate(from, parent, false)
        return SellerProductCardViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: SellerProductCardViewHolder, position: Int) {
        holder.bindProduct(products[position])
    }


}