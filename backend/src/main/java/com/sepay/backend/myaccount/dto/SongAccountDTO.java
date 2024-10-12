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
public class SongAccountDTO {
    private String songNo;
    private Integer countryCode;
    private Double balance;
    private Date createdAt;
    private Date updatedAt;
}