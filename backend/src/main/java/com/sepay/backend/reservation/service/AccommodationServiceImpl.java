package com.sepay.backend.reservation.service;

import com.sepay.backend.reservation.dto.AccommodationDTO;
import com.sepay.backend.reservation.mapper.AccommodationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService{

    final AccommodationMapper mapper;

    @Override
    public List<AccommodationDTO> getAccommodations() {
        return mapper.selectAll();
    }

    @Override
    public AccommodationDTO getByNo(Integer no) {
        return mapper.selectByNo(no);
    }
}
