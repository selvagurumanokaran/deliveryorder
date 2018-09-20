package com.geeks.guru.controller.order;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCreateOrder() throws NullPointerException, Exception {
	Double[] origin = { 32.9697, -96.80322 };
	Double[] destination = { 32.9697, -96.80322 };
	DeliveryOrderRequest request = getMockDeliveryRequest();
	doReturn("23 km").when(distanceCalculator).calculateDistance(origin, destination);
	doReturn(getMockOrder()).when(repository).save(Mockito.any());
	DeliveryOrder result = subject.createOrder(request);
	assertEquals(result.getDistance(), "23 km");

	expectedException.expect(NullPointerException.class);
	doThrow(NullPointerException.class).when(distanceCalculator).calculateDistance(origin, destination);
	subject.createOrder(request);
    }
}
