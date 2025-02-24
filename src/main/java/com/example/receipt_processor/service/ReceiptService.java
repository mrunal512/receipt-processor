package com.example.receipt_processor.service;

import com.example.receipt_processor.exception.ReceiptNotFoundException;
import com.example.receipt_processor.model.Item;
import com.example.receipt_processor.model.Receipt;
import com.example.receipt_processor.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public String processReceipt(Receipt receipt) {
        Receipt savedReceipt = receiptRepository.save(receipt);
        return savedReceipt.getId().toString();
    }

    public int getPoint(UUID id){
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new ReceiptNotFoundException("Receipt not found for ID: " + id));
        return calculatePoints(receipt);
    }

    private int calculatePoints(Receipt receipt) {
        int points = 0;

        // Rule 1: Alphanumeric characters in retailer name
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();

        // Rule 2: 50 points if total is a round dollar amount
        BigDecimal total = new BigDecimal(receipt.getTotal());
        if (total.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            points += 50;
        }

        // Rule 3: 25 points if total is a multiple of 0.25
        if (total.remainder(new BigDecimal("0.25")).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
        }

        // Rule 4: 5 points per two items
        int itemCount = receipt.getItems().size();
        points += (itemCount / 2) * 5;

        // Rule 5: Points for item descriptions
        for (Item item : receipt.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                BigDecimal price = new BigDecimal(item.getPrice());
                points += price.multiply(new BigDecimal("0.2")).setScale(0, RoundingMode.UP).intValue();
            }
        }

        // Rule 6: 6 points if purchase day is odd
        LocalDate purchaseDate = LocalDate.parse(receipt.getPurchaseDate());
        if (purchaseDate.getDayOfMonth() % 2 != 0) {
            points += 6;
        }

        // Rule 7: 10 points if time is between 2:00 PM and 4:00 PM
        LocalTime purchaseTime = LocalTime.parse(receipt.getPurchaseTime());
        if (purchaseTime.isAfter(LocalTime.of(14, 0)) && purchaseTime.isBefore(LocalTime.of(16, 0))) {
            points += 10;
        }

        return points;
    }
}