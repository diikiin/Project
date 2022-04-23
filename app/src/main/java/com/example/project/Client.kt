package com.example.project

data class Client(val firstName: String? = null,
                  val lastName: String? = null,
                  var age: Int? = null,
                  var card: Card? = null,
                  var bonus: Int? = 0,
                  var loanId: String? = null,
                  var depositId: String? = null,
                  var frequentTransfer: ArrayList<FrequentTransfer>? = null,
                  var favouritePayment: ArrayList<FavouritePayment>? = null)
