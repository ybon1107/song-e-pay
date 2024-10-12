package com.sepay.backend.myaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KrwAccountDTO {
    private String krwNo;
    private Double balance;
    private Date createdAt;
    private Date updatedAt;
}