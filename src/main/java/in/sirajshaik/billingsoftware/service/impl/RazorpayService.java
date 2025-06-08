package in.sirajshaik.billingsoftware.service.impl;

import com.razorpay.RazorpayException;
import in.sirajshaik.billingsoftware.io.RazorpayOrderResponse;

public interface RazorpayService {

   RazorpayOrderResponse  createOrder(Double amount , String currency) throws RazorpayException;
}
