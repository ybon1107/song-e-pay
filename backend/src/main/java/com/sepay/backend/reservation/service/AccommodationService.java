package com.sepay.backend.reservation.service;

import com.sepay.backend.reservation.dto.AccommodationDTO;

import java.util.List;

public interface AccommodationService {

    // 숙소 전체 리스트
    List<AccommodationDTO> getAccommodations();

    // 특정 숙소
    AccommodationDTO getByNo(Integer no);
}
