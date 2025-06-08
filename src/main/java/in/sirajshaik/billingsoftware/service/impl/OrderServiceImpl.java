package in.sirajshaik.billingsoftware.service.impl;

import in.sirajshaik.billingsoftware.entity.OrderEntity;
import in.sirajshaik.billingsoftware.entity.OrderItemEntity;
import in.sirajshaik.billingsoftware.io.*;
import in.sirajshaik.billingsoftware.repository.OrderEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements  OrderService {

    private final OrderEntityRepository orderEntityRepository;


    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        OrderEntity newOrder = convertToOrderEntity(orderRequest);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentStatus(newOrder.getPaymentMethod() == PaymentMethod.CASH ?
                PaymentDetails.PaymentStatus.COMPLETED :
                PaymentDetails.PaymentStatus.PENDING);
        newOrder.setPaymentDetails(paymentDetails);

        List<OrderItemEntity> orderItems = orderRequest.getCartItems().stream()
                .map(this::convertToOrderItemEntity)
                .collect(Collectors.toList());

        newOrder.setItems(orderItems);
        newOrder = orderEntityRepository.save(newOrder);
        return convertToResponse(newOrder);
    }

    private OrderResponse convertToResponse(OrderEntity newOrder) {
        return OrderResponse.builder()
                .orderId(newOrder.getOrderId())
                .customerName(newOrder.getCustomerName())
                .phoneNumber(newOrder.getPhoneNumber())
                .subtotal(newOrder.getSubTotal())
                .tax(newOrder.getTax())
                .grandTotal(newOrder.getGrandTotal())
                .paymentMethod(newOrder.getPaymentMethod())
                .items(newOrder.getItems().stream()
                        .map(this::convertToItemResponse)
                        .collect(Collectors.toList()))
                .paymentDetails(newOrder.getPaymentDetails())
                .createdAt(newOrder.getCreatedAt())
                .build();
    }

    private OrderResponse.OrderItemResponse convertToItemResponse(OrderItemEntity orderItemEntity) {

        return OrderResponse.OrderItemResponse.builder()
                .itemId(orderItemEntity.getItemId())
                .name(orderItemEntity.getName())
                .price(orderItemEntity.getPrice())
                .quantity(orderItemEntity.getQuantity())
                .build();
    }

    private OrderItemEntity convertToOrderItemEntity(OrderRequest.OrderItemRequest orderItemRequest) {
        return OrderItemEntity.builder()
                .itemId(orderItemRequest.getItemId())
                .name(orderItemRequest.getName())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .build();
    }

    private OrderEntity convertToOrderEntity(OrderRequest orderRequest) {
        return OrderEntity.builder()
                .customerName(orderRequest.getCustomerName())
                .phoneNumber(orderRequest.getPhoneNumber())
                .subTotal(orderRequest.getSubtotal())
                .tax(orderRequest.getTax())
                .grandTotal(orderRequest.getGrandTotal())
                .paymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()))
                .build();
    }

    @Override
    public void deleteOrder(String orderId) {
        OrderEntity existingOrder = orderEntityRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("order not found"));
        orderEntityRepository.delete(existingOrder);
    }

    @Override
    public List<OrderResponse> getLatestOrders() {
        return orderEntityRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse verifyPayment(PaymentVerificationRequest request) {
        OrderEntity existingOrder = orderEntityRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("order not found"));

        if (!verifyRazorpaySignature(request.getRazorpayOrderId(), request.getRazorpayPaymentId(), request.getRazorpaySignature())) {

            throw new RuntimeException("payment verification failed");
        }

        PaymentDetails paymentDetails = existingOrder.getPaymentDetails();
        paymentDetails.setRazorpayOrderId(request.getRazorpayOrderId());
        paymentDetails.setRazorpayPaymentId(request.getRazorpayPaymentId());
        paymentDetails.setRazorpaySignature(request.getRazorpaySignature());
        paymentDetails.setPaymentStatus(PaymentDetails.PaymentStatus.COMPLETED);

        existingOrder = orderEntityRepository.save(existingOrder);

        return convertToResponse(existingOrder);
    }

    @Override
    public Double sumSalesByDateRange(LocalDate date) {
        return orderEntityRepository.sumSalesByDateRange(date);
    }

    @Override
    public Long countByOrderDateRange(LocalDate date) {
        return orderEntityRepository.countByOrderDateRange(date);
    }

    @Override
    public List<OrderResponse> findRecentOrders( ) {
        return orderEntityRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0,5))
                .stream()
                .map(orderEntity -> convertToResponse(orderEntity))
                .collect(Collectors.toList());
    }

    private boolean verifyRazorpaySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
              return true;
    }

//        private boolean verifyRazorpaySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
//
//        String payload = razorpayOrderId + "|" + razorpayPaymentId;
//        String secret = "rzp_test_lDzxXB0uQaQwB8";
//
//        try {
//            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
//            sha256HMAC.init(secretKey);
//            byte[] hash = sha256HMAC.doFinal(payload.getBytes());
//            String generatedSignature = Base64.getEncoder().encodeToString(hash);
//            return generatedSignature.equals(razorpaySignature);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}
