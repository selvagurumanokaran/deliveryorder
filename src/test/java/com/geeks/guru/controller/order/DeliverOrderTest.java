package com.geeks.guru.controller.order;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryOrderRequest;
import com.geeks.guru.dto.order.DeliveryOrderStatus;
import com.geeks.guru.dto.order.DeliveryStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class DeliverOrderTest {
    @Before
    public void injectMocks() {
	MockitoAnnotations.initMocks(this);
    }

    protected List<DeliveryOrder> getMockDeliveryOrdes() {
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

    protected DeliveryOrder getMockOrder() {
	return getMockDeliveryOrdes().get(0);
    }

    protected DeliveryOrderRequest getMockDeliveryRequest() {
	DeliveryOrderRequest orderRequest = new DeliveryOrderRequest();
	Double[] origin = { 32.9697, -96.80322 };
	orderRequest.setOrigin(origin);
	Double[] detination = { 32.9697, -96.80322 };
	orderRequest.setDestination(detination);
	return orderRequest;
    }

    protected DeliveryOrderStatus getMockUpdateStatus(String status) {
	return new DeliveryOrderStatus(status);
    }
}
