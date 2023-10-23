package com.no3.game.controller;

import com.no3.game.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class StatisticsController {
    private final OrderService orderService;

    public StatisticsController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("admin/sales")
    public String showTotalSales(Model model) {
        int totalSales = orderService.calculateTotalSales();
        model.addAttribute("totalSales", totalSales);
        return "order/totalSalesPage";
    }

    @PostMapping("/admin/sales")
    public String generateSalesStatistics(@RequestParam String startDate, @RequestParam String endDate, Model model) {
        LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");

        // calculateTotalSales 메서드를 사용하여 매출 계산
        double totalSales = orderService.calculateTotalSales(startDateTime, endDateTime);
        model.addAttribute("totalSales", totalSales);

        return "order/totalSalesPage";
    }
}
