package my.edu.tarc.beyondbarriersmart

import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.beyondbarriersmart.databinding.SellerProductCardItemBinding

class SellerProductCardViewHolder (
    private val  sellerProductCardItemBinding: SellerProductCardItemBinding
) : RecyclerView.ViewHolder(sellerProductCardItemBinding.root)
{
    fun bindProduct(sellerProductItem: SellerProductItem)
    {
        sellerProductCardItemBinding.sellerProductImageView.setImageResource(sellerProductItem.image)
        sellerProductCardItemBinding.sellerProductNameTextview.text = sellerProductItem.name
        sellerProductCardItemBinding.sellerProductStockTextview.text = "Stock: " + sellerProductItem.stock
        sellerProductCardItemBinding.sellerProductSoldTextview.text = "Sold: " + sellerProductItem.sold
        sellerProductCardItemBinding.sellerProductRatingTextview.text = "Rating: " + sellerProductItem.rating
        sellerProductCardItemBinding.sellerProductCostTextview.text = "Cost: RM" + String.format(sellerProductItem.cost.toString(),"%.2f")
    }

}