package cinema

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {
    private val cinema: Cinema = Cinema(9, 9)

    @GetMapping("/seats")
    fun getInfo(): Cinema {
        return cinema
    }

    @PostMapping("purchase")
    fun bookTicket(@RequestBody seat: Seat): ResponseEntity<Any> {
        return if (seat.row !in 1..cinema.rows || seat.column !in 1..cinema.columns) {
            ResponseEntity(object {
                val error = "The number of a row or a column is out of bounds!"
            }, HttpStatus.BAD_REQUEST)
        } else {
            val output = cinema.seats.find { it.row == seat.row && it.column == seat.column }
                ?: object {
                    val error = "The ticket has been already purchased!"
                }
            if (output is PricedSeat) {
                cinema.seats.remove(output)
                ResponseEntity(output, HttpStatus.OK)
            } else {
                ResponseEntity(output, HttpStatus.BAD_REQUEST)
            }
        }
    }
}