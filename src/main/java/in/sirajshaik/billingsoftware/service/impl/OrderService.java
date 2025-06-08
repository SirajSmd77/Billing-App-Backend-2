package in.sirajshaik.billingsoftware.service.impl;

import in.sirajshaik.billingsoftware.entity.OrderEntity;
import in.sirajshaik.billingsoftware.io.OrderRequest;
import in.sirajshaik.billingsoftware.io.OrderResponse;
import in.sirajshaik.billingsoftware.io.PaymentVerificationRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {


    OrderResponse createOrder(OrderRequest orderRequest);

    void deleteOrder(String orderId);

    List<OrderResponse> getLatestOrders();

    OrderResponse verifyPayment(PaymentVerificationRequest request);


    Double sumSalesByDateRange(LocalDate date);


    Long countByOrderDateRange(LocalDate date);


    List<OrderResponse> findRecentOrders();
}
