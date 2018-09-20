package com.geeks.guru.controller.order;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryOrderRequest;
import com.geeks.guru.repository.order.OrdersRepository;
import com.geeks.guru.service.order.DistanceCalculator;
import com.geeks.guru.service.order.OrdersService;

public class OrdersServiceTest extends DeliverOrderTest {

    @InjectMocks
    @Autowired
    private OrdersService subject;

    @Mock
    private OrdersRepository repository;

    @Mock
    private DistanceCalculator distanceCalculator;

    @Test
    public void testCreateOrder() throws NullPointerException, Exception {
	Double[] origin = { 32.9697, -96.80322 };
	Double[] destination = { 32.9697, -96.80322 };
	DeliveryOrderRequest request = new DeliveryOrderRequest(origin, destination);
	doReturn(23L).when(distanceCalculator).calculateDistance(origin, destination);
	doReturn(getMockOrder()).when(repository).save(Mockito.any());
	DeliveryOrder result = subject.createOrder(request);
	assertEquals(result.getDistance(), 23L);

	doReturn(-1L).when(distanceCalculator).calculateDistance(origin, destination);
	assertEquals(subject.createOrder(request), null);
    }
}
