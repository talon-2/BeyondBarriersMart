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
        sellerProductCardItemBinding.sellerProductStockTextview.text = sellerProductItem.stock
        sellerProductCardItemBinding.sellerProductSoldTextview.text = sellerProductItem.sold
        sellerProductCardItemBinding.sellerProductRatingTextview.text = sellerProductItem.rating
        sellerProductCardItemBinding.sellerProductCostTextview.text = sellerProductItem.cost
    }

}