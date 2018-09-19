package com.geeks.guru.integrationtest.controller.order;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryStatus;
import com.geeks.guru.dto.order.OrderErrorDetail;
import com.geeks.guru.repository.order.OrdersRepository;
import com.geeks.guru.service.order.OrdersService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
@EnableWebMvc
@AutoConfigureMockMvc
public class OrdersIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private OrdersService ordersService;

    @Autowired
    private OrdersRepository ordersRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllOrders() throws Exception {
	saveDeliveryOrdes();
	mvc.perform(MockMvcRequestBuilders.get("/orders").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk()).andDo(new ResultHandler() {

	    @Override
	    public void handle(MvcResult result) throws Exception {
		final List<DeliveryOrder> deliveryOrders = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<DeliveryOrder>>() {
		});
		assertEquals(deliveryOrders.size(), 2);
	    }
	});
    }

    @Test
    public void testGetAllOrdersWithOnlyPageSize() throws Exception {
	saveDeliveryOrdes();
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
    }

    @Test
    public void testGetAllOrdersWithSizeAndLimit() throws Exception {
	saveDeliveryOrdes();
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
    }

    @Test
    public void testCreateOrder() {
	//mvc.perform(MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).);
    }
    private void saveDeliveryOrdes() {
	final DeliveryOrder order1 = new DeliveryOrder();
	order1.setId(1);
	order1.setDistance("23 km");
	order1.setStatus(DeliveryStatus.UNASSIGN);
	ordersRepo.save(order1);

	final DeliveryOrder order2 = new DeliveryOrder();
	order2.setId(2);
	order2.setDistance("93.3 km");
	order2.setStatus(DeliveryStatus.UNASSIGN);
	ordersRepo.save(order2);
    }
}
