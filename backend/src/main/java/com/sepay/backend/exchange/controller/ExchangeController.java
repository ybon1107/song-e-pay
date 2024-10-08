package com.sepay.backend.exchange.controller;

import com.sepay.backend.exchange.dto.ExchangeDTO;
import com.sepay.backend.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange")
public class ExchangeController {

    final ExchangeService exchangeService;

    @GetMapping
    public ResponseEntity<List<ExchangeDTO>> getExchange(
            @RequestParam Integer baseCode,
            @RequestParam Integer targetCode) {
        List<ExchangeDTO> exchanges = exchangeService.getExchange(baseCode, targetCode);
        return ResponseEntity.ok(exchanges);
    }

    @PostMapping("/rates")
    public ResponseEntity<String> updateExchangeRates(@RequestBody List<ExchangeDTO> rates) {
        System.out.println("update exchange rates" + rates.toString());
        try {
            for (ExchangeDTO rate : rates) {
                System.out.println("for" + rate);
                int updated = exchangeService.updateExchange(rate);
                if (updated == 0) {  // 0은 업데이트된 행이 없음을 의미합니다
                    return ResponseEntity.badRequest().body("환율 정보 업데이트에 실패했습니다: " + rate.toString());
                }
            }
            return ResponseEntity.ok("환율 정보가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("환율 정보 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}