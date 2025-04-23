package com.example.learningAppBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.learningAppBackend.dto.DashboardSummaryDto;
import com.example.learningAppBackend.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

  private final DashboardService dashboardService;

  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }


  @GetMapping("/summary")
  public ResponseEntity<DashboardSummaryDto> DashboardSummary() {
    DashboardSummaryDto summary = dashboardService.geDashboardSummary();
    return ResponseEntity.ok(summary);
  }


}
