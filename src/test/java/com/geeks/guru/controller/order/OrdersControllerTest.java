package com.geeks.guru.controller.order;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryOrderRequest;
import com.geeks.guru.dto.order.DeliveryOrderStatus;
import com.geeks.guru.dto.order.DeliveryStatus;
import com.geeks.guru.dto.order.OrderErrorDetail;
import com.geeks.guru.service.order.OrdersService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrdersControllerTest {

    @InjectMocks
    @Autowired
    private OrdersController subject;

    @Mock
    private OrdersService ordersService;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void injectMocks() {
	MockitoAnnotations.initMocks(this);
    }

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
	DeliveryOrderRequest orderRequest = getMockDeliveryRequest();
	doReturn(false).when(bindingResult).hasErrors();
	doReturn(getMockOrder()).when(ordersService).createOrder(orderRequest);
	ResponseEntity<DeliveryOrder> responseEntity = (ResponseEntity<DeliveryOrder>) subject.createOrder(orderRequest, bindingResult);
	assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	assertEquals(responseEntity.getBody().getId(), 1);

	doThrow(NullPointerException.class).when(ordersService).createOrder(orderRequest);
	ResponseEntity<OrderErrorDetail> responseError = (ResponseEntity<OrderErrorDetail>) subject.createOrder(orderRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	assertEquals(responseError.getBody().getError(), "Failed to calculate distance. Please provide valid coordinates.");

	doThrow(NumberFormatException.class).when(ordersService).createOrder(orderRequest);
	responseError = (ResponseEntity<OrderErrorDetail>) subject.createOrder(orderRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	assertEquals(responseError.getBody().getError(), "Failed to create an order.");

	doReturn(true).when(bindingResult).hasErrors();
	responseError = (ResponseEntity<OrderErrorDetail>) subject.createOrder(orderRequest, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    private DeliveryOrderStatus getMockUpdateStatus(String status) {
	return new DeliveryOrderStatus(status);
    }

    private DeliveryOrderRequest getMockDeliveryRequest() {
	DeliveryOrderRequest orderRequest = new DeliveryOrderRequest();
	Double[] origin = { 32.9697, -96.80322 };
	orderRequest.setOrigin(origin);
	Double[] detination = { 32.9697, -96.80322 };
	orderRequest.setDestination(detination);
	return orderRequest;
    }

    private List<DeliveryOrder> getMockDeliveryOrdes() {
	List<DeliveryOrder> orders = new ArrayList<>();

	final DeliveryOrder order1 = new DeliveryOrder();
	order1.setId(1);
	order1.setDistance("23 km");
	order1.setStatus(DeliveryStatus.UNASSIGN);
	orders.add(order1);

	final DeliveryOrder order2 = new DeliveryOrder();
	order2.setId(2);
	order2.setDistance("93.3 km");
	order2.setStatus(DeliveryStatus.UNASSIGN);
	orders.add(order2);

	return orders;
    }

    private DeliveryOrder getMockOrder() {
	return getMockDeliveryOrdes().get(0);
    }

}
