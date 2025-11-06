package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.error.exception.NoOrderFoundException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.order.CreateNewOrderRequest;
import ru.yandex.practicum.order.OrderDto;
import ru.yandex.practicum.order.ProductReturnRequest;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(String username) {
        return orderRepository.findAllByUsername(username).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDto createOrder(CreateNewOrderRequest request, String username) {
        Order order = Order.builder()
                .order_id(UUID.randomUUID())
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .username(username)
                .products(request.getShoppingCart().getProducts())
                .state(OrderState.NEW)
                .fragile(false)
                .totalPrice(0.0)
                .deliveryPrice(0.0)
                .productPrice(0.0)
                .build();

        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public OrderDto returnProducts(ProductReturnRequest request) {
        Order order = getOrThrow(request.getOrderId());
        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    public OrderDto payment(UUID orderId) {
        return updateState(orderId, OrderState.PAID);
    }

    public OrderDto paymentFailed(UUID orderId) {
        return updateState(orderId, OrderState.PAYMENT_FAILED);
    }

    public OrderDto delivery(UUID orderId) {
        return updateState(orderId, OrderState.DELIVERED);
    }

    public OrderDto deliveryFailed(UUID orderId) {
        return updateState(orderId, OrderState.DELIVERY_FAILED);
    }

    public OrderDto completed(UUID orderId) {
        return updateState(orderId, OrderState.COMPLETED);
    }

    public OrderDto calculateTotal(UUID orderId) {
        Order order = getOrThrow(orderId);
        Double total = (order.getDeliveryPrice() != null ? order.getDeliveryPrice() : 0) +
                       (order.getProductPrice() != null ? order.getProductPrice() : 0);

        order.setTotalPrice(total);
        return orderMapper.toDto(orderRepository.save(order));
    }

    public OrderDto calculateDelivery(UUID orderId) {
        Order order = getOrThrow(orderId);
        order.setDeliveryPrice(1.0);
        return orderMapper.toDto(orderRepository.save(order));
    }

    public OrderDto assembly(UUID orderId) {
        return updateState(orderId, OrderState.ASSEMBLED);
    }

    public OrderDto assemblyFailed(UUID orderId) {
        return updateState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    private Order getOrThrow(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoOrderFoundException::new);
    }

    private OrderDto updateState(UUID orderId, OrderState newState) {
        Order order = getOrThrow(orderId);
        order.setState(newState);
        return orderMapper.toDto(orderRepository.save(order));
    }
}
