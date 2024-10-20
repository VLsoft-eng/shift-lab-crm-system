package ru.shift.task.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shift.task.api.dto.BestSellerInPeriodRequest;
import ru.shift.task.api.dto.BestSellersPeriodRequest;
import ru.shift.task.api.dto.SellersWithSumLessThanRequest;
import ru.shift.task.domain.service.AnalyticService;

import static ru.shift.task.api.Paths.ANALYTIC;

@Controller
@RequestMapping(ANALYTIC)
public class AnalyticController {
    AnalyticService analyticService;

    @Autowired
    public void setAnalyticService(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping("/best-seller")
    public ResponseEntity<?> bestSeller(@RequestBody @Valid BestSellerInPeriodRequest request) {
        return ResponseEntity.ok(analyticService.findBestSellerInPeriod(request));
    }

    @GetMapping("/sellers-sum-less")
    public ResponseEntity<?> sellersSumLess(@RequestBody @Valid SellersWithSumLessThanRequest request) {
        return ResponseEntity.ok(analyticService.findSellersWithSumLessThan(request));
    }

    @GetMapping("/best-seller-period")
    public ResponseEntity<?> bestSellerPeriod(@RequestBody @Valid BestSellersPeriodRequest request) {
        return ResponseEntity.ok(analyticService.findBestSellersPeriod(request));
    }

}
