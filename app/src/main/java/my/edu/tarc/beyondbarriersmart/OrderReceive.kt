package my.edu.tarc.beyondbarriersmart

class OrderReceive(
    var image: String,
    var itemName: String,
    var purchaseAmt: String,
    var date: String,
    var isDone: Boolean
) {
    constructor() : this("", "", "", "", true) {}
}