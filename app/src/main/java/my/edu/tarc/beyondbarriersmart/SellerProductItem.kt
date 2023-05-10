package my.edu.tarc.beyondbarriersmart

var productList = mutableListOf<SellerProductItem>()

class SellerProductItem (
    var image : Int,
    var name: String,
    var stock: Int,
    var sold: Int,
    var rating: Float,
    var cost: Float,
)