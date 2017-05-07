package com.netcracker.crm.service.entity;

import com.netcracker.crm.domain.model.Order;
import com.netcracker.crm.domain.request.OrderRowRequest;
import com.netcracker.crm.dto.OrderDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Karpunets
 * @since 03.05.2017
 */
public interface OrderService {

    Order persist(OrderDto orderDto);
    public List<Order> findByCustomerId(Long id);

    Map<String, Object> getOrderRow(OrderRowRequest orderRowRequest) throws IOException;

}
