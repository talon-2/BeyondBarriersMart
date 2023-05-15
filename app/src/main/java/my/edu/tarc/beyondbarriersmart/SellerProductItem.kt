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
    var shipNeed: Boolean,
    var isDelisted: Boolean
){
    private var isEmpty = false

    constructor() : this("", "", "", "", "", "", 0, 0, 0f, 0f, false, false) {
        isEmpty = true
    }

    public fun isEmptyProduct(): Boolean {
        return isEmpty
    }

    public fun setEmpty(empty: Boolean) {
        isEmpty = empty
    }
}