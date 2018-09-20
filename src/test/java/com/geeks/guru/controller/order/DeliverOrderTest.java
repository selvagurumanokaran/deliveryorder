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

	final DeliveryOrder order1 = new DeliveryOrder(1, 23, DeliveryStatus.UNASSIGN);
	orders.add(order1);

	final DeliveryOrder order2 = new DeliveryOrder(2, 93, DeliveryStatus.UNASSIGN);
	orders.add(order2);

	return orders;
    }

    protected DeliveryOrder getMockOrder() {
	return getMockDeliveryOrdes().get(0);
    }

    protected DeliveryOrderStatus getMockUpdateStatus(String status) {
	return new DeliveryOrderStatus(status);
    }
}
