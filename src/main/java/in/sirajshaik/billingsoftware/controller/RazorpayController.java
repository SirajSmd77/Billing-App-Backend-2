package in.sirajshaik.billingsoftware.controller;

import in.sirajshaik.billingsoftware.io.*;
import in.sirajshaik.billingsoftware.service.impl.OrderService;
import in.sirajshaik.billingsoftware.service.impl.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class RazorpayController {

        private final OrderService orderService;

        private final RazorpayService razorpayService;

        @PostMapping("/create-order")
        @ResponseStatus(HttpStatus.CREATED)
        public RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request){
            try{
                return  razorpayService.createOrder(request.getAmount(), request.getCurrency());
            }catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unable to Create Order"+e.getMessage());
            }
        }


    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse verifyPayment(@RequestBody PaymentVerificationRequest request){
        try{
            return  orderService.verifyPayment(request);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unable to verify payment"+e.getMessage());
        }
    }

    }
