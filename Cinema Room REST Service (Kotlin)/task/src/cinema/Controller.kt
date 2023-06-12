package cinema

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

const val ERROR_MSG_SEAT_OUT_OF_BOUNDS = "The number of a row or a column is out of bounds!"
const val ERROR_MSG_SEAT_TAKEN = "The ticket has been already purchased!"
const val ERROR_MSG_WRONG_TOKEN = "Wrong token!"

@RestController
class Controller {
    private val cinema: Cinema = Cinema(9, 9)
    private val soldSeats: MutableList<SoldSeat> = mutableListOf()

    @GetMapping("/seats")
    fun getInfo(): Cinema {
        return cinema
    }

    @PostMapping("purchase")
    fun bookTicket(@RequestBody seat: Seat): ResponseEntity<Any> {

        // check if request out of bounds
        if (seat.row !in 1..cinema.rows || seat.column !in 1..cinema.columns) {
            return ResponseEntity(
                object {
                    val error = ERROR_MSG_SEAT_OUT_OF_BOUNDS
                }, HttpStatus.BAD_REQUEST
            )
        }

        // check if seat is already taken
        val seat = cinema.seats.find { it.row == seat.row && it.column == seat.column } ?: return ResponseEntity(
            object {
                val error = ERROR_MSG_SEAT_TAKEN
            }, HttpStatus.BAD_REQUEST
        )

        // all seems ok, let's purchase the ticket and generate token
        val token = UUID.randomUUID().toString()
        val soldSeat = SoldSeat(token, seat)
        cinema.seats.remove(seat)
        soldSeats.add(soldSeat)
        return ResponseEntity(soldSeat, HttpStatus.OK)
    }

    @PostMapping("return")
    fun returnTicket(@RequestBody token: SoldSeat): ResponseEntity<Any> {

        // check if token exists
        val soldSeat = soldSeats.find { it.token == token.token && it.ticket != null }
            ?: return ResponseEntity(object {
                val error = ERROR_MSG_WRONG_TOKEN
            }, HttpStatus.BAD_REQUEST)

        soldSeats.remove(soldSeat)
        cinema.seats.add(soldSeat.ticket!!)
        return ResponseEntity(object {
            @JsonProperty("returned_ticket")
            val ticket = soldSeat.ticket
        }, HttpStatus.OK)
    }
}