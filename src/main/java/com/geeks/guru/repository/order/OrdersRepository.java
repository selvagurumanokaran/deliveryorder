package com.geeks.guru.repository.order;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.geeks.guru.dto.order.DeliveryOrder;
import com.geeks.guru.dto.order.DeliveryStatus;

@Repository
public interface OrdersRepository extends PagingAndSortingRepository<DeliveryOrder, Integer> {

    @Query("SELECT o FROM DeliveryOrder o")
    List<DeliveryOrder> findAllPagedOrder(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE DeliveryOrder SET status = :status  WHERE id = :id and status != :status")
    int updateOrderStatus(@Param("id") Integer id, @Param("status") DeliveryStatus status);
}
