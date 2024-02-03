package com.practice.spring.api.controller

import com.practice.spring.api.common.ApiResponse
import com.practice.spring.api.controller.request.OrderRequest
import com.practice.spring.api.service.OrderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping("/v1/orders")
    fun order(
        @RequestBody orderRequest: OrderRequest
    ): ApiResponse<Unit> {
        orderService.create(orderRequest.toOrderEvent())
        return ApiResponse.success()
    }
}
