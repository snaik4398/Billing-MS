package com.project.receipt.generator.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    private String name;
    private String gstNumber;
    private String logoPath;
}
