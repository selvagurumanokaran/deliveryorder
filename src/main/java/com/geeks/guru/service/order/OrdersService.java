package com.geeks.guru.service.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryOrderRequest;
import com.geeks.guru.dto.order.DeliveryStatus;
import com.geeks.guru.repository.order.OrdersRepository;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private DistanceCalculator distanceCalculator;

    public List<DeliveryOrder> getAllOrders(Pageable pageable) {
	return ordersRepository.findAll(pageable).getContent();
    }

    public DeliveryOrder createOrder(DeliveryOrderRequest request) throws Exception {
	Double[] origins = request.getOrigin();
	Double[] destinations = request.getDestination();
	long distance = distanceCalculator.calculateDistance(origins, destinations);
	if (distance > -1)
	    return ordersRepository.save(new DeliveryOrder(distance, DeliveryStatus.UNASSIGN));
	return null;
    }

    public int updateStatus(String id) {
	return ordersRepository.updateOrderStatus(Integer.parseInt(id), DeliveryStatus.SUCCESS);
    }

    public DeliveryOrder findOrderById(String id) {
	return ordersRepository.findById(Integer.parseInt(id)).get();
    }
}
