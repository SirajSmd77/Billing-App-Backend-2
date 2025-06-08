package in.sirajshaik.billingsoftware.controller;

import in.sirajshaik.billingsoftware.io.OrderRequest;
import in.sirajshaik.billingsoftware.io.OrderResponse;
import in.sirajshaik.billingsoftware.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

        private final OrderService orderService;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public OrderResponse createOrder(@RequestBody OrderRequest request){
            try{
                return  orderService.createOrder(request);
            }catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unable to Create Order"+e.getMessage());
            }
        }

        @GetMapping("/latest")
        public List<OrderResponse> getLatestOrders(){
            return orderService.getLatestOrders();
        }

        @DeleteMapping("/{orderId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteOrder(@PathVariable String orderId){
            try{
                orderService.deleteOrder(orderId);
            }catch (Exception e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found");
            }
        }
    }

