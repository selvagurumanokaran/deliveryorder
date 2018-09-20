package com.geeks.guru.integrationtest.controller.order;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeks.guru.controller.order.DeliverOrderTest;
import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryOrderRequest;
import com.geeks.guru.dto.order.DeliveryOrderStatus;
import com.geeks.guru.dto.order.DeliveryStatus;
import com.geeks.guru.dto.order.OrderErrorDetail;
import com.geeks.guru.repository.order.OrdersRepository;
import com.geeks.guru.service.order.OrdersService;

@Transactional
@Rollback(true)
@EnableWebMvc
public class OrdersIntegrationTest extends DeliverOrderTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private OrdersService ordersService;

    @Autowired
    private OrdersRepository ordersRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDeliveryOrders() throws Exception {
	saveDeliveryOrdes();
	mvc.perform(MockMvcRequestBuilders.get("/orders").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final List<DeliveryOrder> deliveryOrders = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<DeliveryOrder>>() {
		});
		assertEquals(deliveryOrders.size(), 2);
	    }
	});

	// Test fetching orders with only page size
	mvc.perform(MockMvcRequestBuilders.get("/orders?page=0").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final List<DeliveryOrder> deliveryOrders = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<DeliveryOrder>>() {
		});
		assertEquals(deliveryOrders.size(), 2);
	    }
	});

	mvc.perform(MockMvcRequestBuilders.get("/orders?page=1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final List<DeliveryOrder> deliveryOrders = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<DeliveryOrder>>() {
		});
		assertEquals(deliveryOrders.size(), 0);
	    }
	});

	mvc.perform(MockMvcRequestBuilders.get("/orders?page=-1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isInternalServerError()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final OrderErrorDetail errorDetail = objectMapper.readValue(result.getResponse().getContentAsByteArray(), OrderErrorDetail.class);
		assertEquals(errorDetail.getError(), "Failed to fetch orders.");
	    }
	});

	// Test fetching orders with page size and limit
	mvc.perform(MockMvcRequestBuilders.get("/orders?page=0&limit=5").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final List<DeliveryOrder> deliveryOrders = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<DeliveryOrder>>() {
		});
		assertEquals(deliveryOrders.size(), 2);
	    }
	});

	mvc.perform(MockMvcRequestBuilders.get("/orders?page=1&limit=1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final List<DeliveryOrder> deliveryOrders = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<DeliveryOrder>>() {
		});
		assertEquals(deliveryOrders.size(), 1);
		assertEquals(deliveryOrders.get(0).getId(), 2);
	    }
	});

	mvc.perform(MockMvcRequestBuilders.get("/orders?page=1&limit=5").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final List<DeliveryOrder> deliveryOrders = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<DeliveryOrder>>() {
		});
		assertEquals(deliveryOrders.size(), 0);
	    }
	});

	mvc.perform(MockMvcRequestBuilders.get("/orders?page=0&limit=0").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isInternalServerError()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final OrderErrorDetail errorDetail = objectMapper.readValue(result.getResponse().getContentAsByteArray(), OrderErrorDetail.class);
		assertEquals(errorDetail.getError(), "Failed to fetch orders.");
	    }
	});

	// Test creating orders
	final DeliveryOrderRequest orderRequest = getMockDeliveryRequest();
	mvc.perform(MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(orderRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk())
		.andDo(new ResultHandler() {
		    @Override
		    public void handle(MvcResult result) throws Exception {
			final DeliveryOrder oder = objectMapper.readValue(result.getResponse().getContentAsByteArray(), DeliveryOrder.class);
			assertEquals(oder.getId(), 3);
		    }
		});

	// Test creating orders with invalid payload
	orderRequest.setDestination(new Double[] { 12.90 });
	mvc.perform(MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(orderRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isInternalServerError());

	orderRequest.setDestination(null);
	mvc.perform(MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(orderRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isInternalServerError());

	orderRequest.setOrigin(null);
	mvc.perform(MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(orderRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isInternalServerError());

	orderRequest.setDestination(new Double[] { 12.90, 23.90 });
	orderRequest.setOrigin(null);
	mvc.perform(MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(orderRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isInternalServerError());

	// Invalid orgin
	orderRequest.setOrigin(new Double[] { 89.17, 98.90 });
	mvc.perform(MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(orderRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isInternalServerError());

	DeliveryOrderStatus updateRequest = getMockUpdateStatus("taken");
	// Test update order
	mvc.perform(MockMvcRequestBuilders.put("/order/1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(updateRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk())
		.andDo(new ResultHandler() {

		    @Override
		    public void handle(MvcResult result) throws Exception {
			final DeliveryOrderStatus updateResult = objectMapper.readValue(result.getResponse().getContentAsByteArray(), DeliveryOrderStatus.class);
			assertEquals(updateResult.getStatus(), DeliveryStatus.SUCCESS.name());
		    }
		});

	mvc.perform(MockMvcRequestBuilders.put("/order/1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(updateRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isConflict())
		.andDo(new ResultHandler() {

		    @Override
		    public void handle(MvcResult result) throws Exception {
			final OrderErrorDetail errorDetail = objectMapper.readValue(result.getResponse().getContentAsByteArray(), OrderErrorDetail.class);
			assertEquals(errorDetail.getError(), "Order already taken.");
		    }
		});

	mvc.perform(MockMvcRequestBuilders.put("/order/13").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(updateRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isBadRequest())
		.andDo(new ResultHandler() {

		    @Override
		    public void handle(MvcResult result) throws Exception {
			final OrderErrorDetail errorDetail = objectMapper.readValue(result.getResponse().getContentAsByteArray(), OrderErrorDetail.class);
			assertEquals(errorDetail.getError(), "Order does not exist.");
		    }
		});

	updateRequest.setStatus(null);
	mvc.perform(MockMvcRequestBuilders.put("/order/13").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(objectMapper.writeValueAsBytes(updateRequest)).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isBadRequest());

    }

    private void saveDeliveryOrdes() {
	final DeliveryOrder order1 = new DeliveryOrder();
	order1.setId(1);
	order1.setDistance("23 km");
	order1.setStatus(DeliveryStatus.UNASSIGN);
	ordersRepo.saveAndFlush(order1);

	final DeliveryOrder order2 = new DeliveryOrder();
	order2.setId(2);
	order2.setDistance("93.3 km");
	order2.setStatus(DeliveryStatus.UNASSIGN);
	ordersRepo.saveAndFlush(order2);
    }
}
