package com.tomward.membershield.status

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ServiceStatus(
    val service: String,
    val status: String,
)

@RestController
@RequestMapping("/api/v1/status")
class StatusController {

    @GetMapping
    fun getStatus(): ServiceStatus {
        return ServiceStatus(
            service = "membershield-api",
            status = "UP",
        )
    }
}