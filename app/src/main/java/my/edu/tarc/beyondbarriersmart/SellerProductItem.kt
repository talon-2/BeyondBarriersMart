package my.edu.tarc.beyondbarriersmart

var productList = mutableListOf<SellerProductItem>()

class SellerProductItem (
    var productId: String,
    var sellerId: String,
    var image : String,
    var name: String,
    var description: String,
    var category: String,
    var stock: Int,
    var sold: Int,
    var rating: Float,
    var cost: Float,
    var shipNeed: Boolean
){

}