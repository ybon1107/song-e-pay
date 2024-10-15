package com.sepay.backend.reservation.mapper;

import com.sepay.backend.reservation.dto.AccommodationDTO;

import java.util.List;

public interface AccommodationMapper {

    // 숙소 전체 리스트
    List<AccommodationDTO> selectAll();

    // 특정 숙소
    AccommodationDTO selectByNo(Integer no);
}
