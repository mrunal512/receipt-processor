package com.example.receipt_processor.controller;

import com.example.receipt_processor.exception.ReceiptNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.receipt_processor.model.Receipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.receipt_processor.service.ReceiptService;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processReceipt(@Valid @RequestBody Receipt receipt) {
        try {
            String newId = receiptService.processReceipt(receipt);
            return ResponseEntity.ok(Collections.singletonMap("id", newId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, Integer>> getPoints(@PathVariable UUID id) {
        try {
            Integer points = receiptService.getPoint(id);
            return ResponseEntity.ok(Collections.singletonMap("points", points));
        } catch (ReceiptNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
