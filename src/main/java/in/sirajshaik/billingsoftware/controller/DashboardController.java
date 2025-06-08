package in.sirajshaik.billingsoftware.controller;

import in.sirajshaik.billingsoftware.io.DashboardResponse;
import in.sirajshaik.billingsoftware.io.OrderResponse;
import in.sirajshaik.billingsoftware.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    OrderService orderService;


    @GetMapping
    public DashboardResponse getDashboardData(){
        LocalDate today = LocalDate.now();
        Double todaySale = orderService.sumSalesByDateRange(today);
        Long todayOrderCount = orderService.countByOrderDateRange(today);
        List<OrderResponse> recentOrders = orderService.findRecentOrders();
        return  new DashboardResponse(
               todaySale !=null?todaySale:0.0,
               todayOrderCount!=null?todayOrderCount:0,
               recentOrders
       );
    }
}
