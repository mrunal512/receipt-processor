package com.example.receipt_processor.controller;

import com.example.receipt_processor.exception.ReceiptNotFoundException;
import com.example.receipt_processor.model.Receipt;
import com.example.receipt_processor.service.ReceiptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReceiptService receiptService;

    @Autowired
    private ObjectMapper objectMapper;
    private Receipt receipt;
    private UUID receiptId;

    @BeforeEach
    void setUp() {
        receiptId = UUID.randomUUID();
        receipt = new Receipt();
        receipt.setId(receiptId);
        receipt.setRetailer("Retailer");
        receipt.setTotal("100.00");
        receipt.setPurchaseDate("2023-10-10");
        receipt.setPurchaseTime("15:00");
        receipt.setItems(Collections.emptyList());
    }

    @Test
    void testProcessReceiptSuccessfully() throws Exception {
        Mockito.when(receiptService.processReceipt(Mockito.any(Receipt.class))).thenReturn(receiptId.toString());

        mockMvc.perform(post("/receipts/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(receipt)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + receiptId.toString() + "\"}"));
    }

    @Test
    void testGetPoints() throws Exception {
        int points = 100;
        Mockito.when(receiptService.getPoint(receiptId)).thenReturn(points);

        mockMvc.perform(MockMvcRequestBuilders.get("/receipts/{id}/points", receiptId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"points\":" + points + "}"));
    }

    @Test
    void testProcessReceiptWithInvalidData() throws Exception {
        Receipt invalidReceipt = new Receipt();
        invalidReceipt.setRetailer("");
        invalidReceipt.setTotal("invalid_total");

        mockMvc.perform(post("/receipts/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReceipt)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPointsReceiptNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        Mockito.when(receiptService.getPoint(nonExistentId)).thenThrow(new ReceiptNotFoundException("Receipt not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/receipts/{id}/points", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testProcessReceiptWithMissingFields() throws Exception {
        Receipt incompleteReceipt = new Receipt();
        incompleteReceipt.setRetailer("RetailerName");

        mockMvc.perform(post("/receipts/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteReceipt)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testProcessReceiptWithEmptyItemsList() throws Exception {
        Receipt receiptWithEmptyItems = new Receipt();
        receiptWithEmptyItems.setRetailer("RetailerName");
        receiptWithEmptyItems.setPurchaseDate("2023-10-10");
        receiptWithEmptyItems.setPurchaseTime("15:30");
        receiptWithEmptyItems.setTotal("20.00");
        receiptWithEmptyItems.setItems(Collections.emptyList());

        mockMvc.perform(post("/receipts/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(receiptWithEmptyItems)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPointsWithInvalidUUIDFormat() throws Exception {
        String invalidUUID = "invalid-uuid-format";

        mockMvc.perform(MockMvcRequestBuilders.get("/receipts/{id}/points", invalidUUID))
                .andExpect(status().isBadRequest());
    }
}