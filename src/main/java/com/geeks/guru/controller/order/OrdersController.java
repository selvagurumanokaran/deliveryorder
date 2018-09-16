package com.geeks.guru.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryOrderRequest;
import com.geeks.guru.dto.order.DeliveryOrderStatus;
import com.geeks.guru.dto.order.DeliveryStatus;
import com.geeks.guru.dto.order.OrderException;
import com.geeks.guru.service.order.OrdersService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

@RestController
public class OrdersController {

	@Autowired
	private OrdersService ordersService;

	@GetMapping(value = "/orders", produces = { APPLICATION_JSON_UTF8_VALUE })
	public List<DeliveryOrder> getAllOrders(@RequestParam(defaultValue = "0", required = false) Integer page,
			@RequestParam(defaultValue = "20", required = false) Integer limit) {
		return ordersService.getAllOrders(PageRequest.of(page, limit));
	}

	@PostMapping(value = "/order", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = {
			APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> createOrder(@Valid @RequestBody DeliveryOrderRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getOrderException(bindingResult));
		}
		return ResponseEntity.ok(ordersService.createOrder(request));
	}

	@PutMapping(value = "/order/{id}", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = {
			APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<?> updateOrder(@PathVariable String id, @Valid @RequestBody DeliveryOrderStatus updateRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getOrderException(bindingResult));
		}
		try {
			ordersService.findOrderById(id);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderException("Order does not exist."));
		}
		int updatedCount = ordersService.updateStatus(id);
		if (updatedCount > 0) {
			return ResponseEntity.ok(new DeliveryOrderStatus(DeliveryStatus.SUCCESS.name()));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new OrderException("Order already taken."));
	}

	private OrderException getOrderException(BindingResult bindingResult) {
		StringBuilder messageBuilder = new StringBuilder();
		bindingResult.getAllErrors().stream().forEach(fieldError -> {
			messageBuilder.append(fieldError.getDefaultMessage() + " ");
		});
		return new OrderException(messageBuilder.toString());
	}

}
