package cinema

import com.fasterxml.jackson.annotation.JsonProperty

class SalesStats(rows: Int, columns: Int) {
    @JsonProperty("current_income")
    private var currIncome = 0

    @JsonProperty("number_of_available_seats")
    private var noOfSeatsAvailable = rows * columns

    @JsonProperty("number_of_purchased_tickets")
    private var noOfTicketsSold = 0

    fun saleTicket(seat: PricedSeat) {
        currIncome += seat.price
        noOfSeatsAvailable -= 1
        noOfTicketsSold += 1
    }

    fun returnTicket(seat: PricedSeat) {
        currIncome -= seat.price
        noOfSeatsAvailable += 1
        noOfTicketsSold -= 1
    }
}