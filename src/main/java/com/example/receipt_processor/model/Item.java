package com.example.receipt_processor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @NotBlank
    private String shortDescription;

    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String price;

//    @ManyToOne
//    @JoinColumn(name = "receipt_id")
//    private Receipt receipt;
}