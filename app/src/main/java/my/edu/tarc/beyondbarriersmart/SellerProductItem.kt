package my.edu.tarc.beyondbarriersmart

var productList = mutableListOf<SellerProductItem>()

class SellerProductItem (
    var image : Int,
    var name: String,
    var stock: String,
    var sold: String,
    var rating: String,
    var cost: String,
)