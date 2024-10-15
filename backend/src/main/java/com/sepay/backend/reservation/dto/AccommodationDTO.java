package com.sepay.backend.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDTO {
    private Integer accommodationNo;
    private String name;
    private String summary;
    private String description;
    private String address;
    private String thumbnail;
    private String img;
    private Integer pricePerNight;
    private Integer capacity;
    private Time checkInTime;
    private Time checkOutTime;
    private Date createdAt;
    private Date updatedAt;

    List<AmenitiesDTO> amenities;
}
