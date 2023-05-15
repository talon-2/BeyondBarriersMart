package my.edu.tarc.beyondbarriersmart

class OrderReceive(
    var purchaseId: String,
    var image: String,
    var itemName: String,
    var address: String,
    var purchaseAmt: String,
    var date: String,
    var isDone: Boolean
) {
    constructor() : this("", "","", "", "", "", true) {}
}