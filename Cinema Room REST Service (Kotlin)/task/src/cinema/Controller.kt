package cinema

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller() {
    private val cinema: Cinema = Cinema(9, 9)

    @GetMapping("/seats")
    fun getInfo(): Cinema {
        return cinema
    }
}