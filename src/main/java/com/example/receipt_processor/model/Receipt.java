package com.example.receipt_processor.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Auto-generated UUID

    @NotBlank(message = "Retailer is required")
    private String retailer;

    @NotNull(message = "Purchase date is mandatory")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format (YYYY-MM-DD)")
    private String purchaseDate;

    @NotNull(message = "Purchase time is mandatory")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Invalid time format (HH:MM)")
    private String purchaseTime;


    @ElementCollection
    @CollectionTable(name = "receipt_item", joinColumns = @JoinColumn(name = "receipt_id"))
    private List<Item> items = new ArrayList<>();

    @NotBlank(message = "Total is required")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Total must be a valid decimal (e.g., 10.00)")
    private String total;
}
