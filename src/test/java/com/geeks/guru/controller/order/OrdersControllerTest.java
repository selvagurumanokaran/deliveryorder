package com.geeks.guru.controller.order;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryOrderRequest;
import com.geeks.guru.dto.order.DeliveryOrderStatus;
import com.geeks.guru.dto.order.DeliveryStatus;
import com.geeks.guru.dto.order.OrderErrorDetail;
import com.geeks.guru.service.order.OrdersService;

public class OrdersControllerTest extends DeliverOrderTest {

    @InjectMocks
    @Autowired
    private OrdersController subject;

    @Mock
    private OrdersService ordersService;

    @Mock
    private BindingResult bindingResult;

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllOrders() {
	List<DeliveryOrder> orders = getMockDeliveryOrdes();
	doReturn(orders).when(ordersService).getAllOrders(PageRequest.of(0, 20));
	ResponseEntity<List<DeliveryOrder>> responseEntity = (ResponseEntity<List<DeliveryOrder>>) subject.getAllOrders(0, 20);
	assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	assertEquals(responseEntity.getBody().size(), 2);

	doThrow(NullPointerException.class).when(ordersService).getAllOrders(PageRequest.of(0, 20));
	ResponseEntity<OrderErrorDetail> responseError = (ResponseEntity<OrderErrorDetail>) subject.getAllOrders(0, 20);
	assertEquals(responseError.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	assertEquals(responseError.getBody().getError(), "Failed to fetch orders.");

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateOrder() throws Exception {
	Double[] origin = { 32.9697, -96.80322 };
	Double[] destination = { 32.9697, -96.80322 };
	DeliveryOrderRequest orderRequest = new DeliveryOrderRequest(origin, destination);
	doReturn(false).when(bindingResult).hasErrors();
	doReturn(getMockOrder()).when(ordersService).createOrder(orderRequest);
	ResponseEntity<DeliveryOrder> responseEntity = (ResponseEntity<DeliveryOrder>) subject.createOrder(orderRequest, bindingResult);
	assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	assertEquals(responseEntity.getBody().getId(), 1);

	doReturn(null).when(ordersService).createOrder(orderRequest);
	ResponseEntity<OrderErrorDetail> responseError = (ResponseEntity<OrderErrorDetail>) subject.createOrder(orderRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	assertEquals(responseError.getBody().getError(), "Failed to calculate distance. Please provide valid coordinates.");

	doThrow(NumberFormatException.class).when(ordersService).createOrder(orderRequest);
	responseError = (ResponseEntity<OrderErrorDetail>) subject.createOrder(orderRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	assertEquals(responseError.getBody().getError(), "Failed to create an order.");

	doReturn(true).when(bindingResult).hasErrors();
	responseError = (ResponseEntity<OrderErrorDetail>) subject.createOrder(orderRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateOrder() {
	DeliveryOrderStatus updateRequest = getMockUpdateStatus("taken");
	doReturn(false).when(bindingResult).hasErrors();
	doReturn(getMockOrder()).when(ordersService).findOrderById("1");
	doReturn(1).when(ordersService).updateStatus("1");
	ResponseEntity<DeliveryOrderStatus> responseEntity = (ResponseEntity<DeliveryOrderStatus>) subject.updateOrder("1", updateRequest, bindingResult);
	assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	assertEquals(responseEntity.getBody().getStatus(), DeliveryStatus.SUCCESS.name());

	doReturn(0).when(ordersService).updateStatus("1");
	ResponseEntity<OrderErrorDetail> responseError = (ResponseEntity<OrderErrorDetail>) subject.updateOrder("1", updateRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.CONFLICT);
	assertEquals(responseError.getBody().getError(), "Order already taken.");

	doThrow(NullPointerException.class).when(ordersService).updateStatus("1");
	responseError = (ResponseEntity<OrderErrorDetail>) subject.updateOrder("1", updateRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	assertEquals(responseError.getBody().getError(), "Failed to update the order");

	doThrow(NoSuchElementException.class).when(ordersService).findOrderById("1");
	responseError = (ResponseEntity<OrderErrorDetail>) subject.updateOrder("1", updateRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.BAD_REQUEST);
	assertEquals(responseError.getBody().getError(), "Order does not exist.");

	doReturn(true).when(bindingResult).hasErrors();
	responseError = (ResponseEntity<OrderErrorDetail>) subject.updateOrder("1", updateRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

}
