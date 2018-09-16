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

	public List<DeliveryOrder> getAllOrders(Pageable pageable) {
		return (List<DeliveryOrder>) ordersRepository.findAllPagedOrder(pageable);
	}

	public DeliveryOrder createOrder(DeliveryOrderRequest request) {
		Double[] origins = request.getOrigin();
		Double[] destinations = request.getDestination();
		//double distance = DistanceCalculator.distance(Double.parseDouble(origins[0]), Double.parseDouble(origins[1]),
		//		Double.parseDouble(destinations[0]), Double.parseDouble(destinations[1]));
		double distance = DistanceCalculator.distance(origins[0], origins[1],
				destinations[0], destinations[1]);
		return ordersRepository.save(new DeliveryOrder(distance, DeliveryStatus.UNASSIGN));
	}

	public int updateStatus(String id) {
		return ordersRepository.updateOrderStatus(Integer.parseInt(id), DeliveryStatus.SUCCESS);
	}

	public DeliveryOrder findOrderById(String id) {
		return ordersRepository.findById(Integer.parseInt(id)).get();
	}
}
