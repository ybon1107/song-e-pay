package com.sepay.backend.history.controller;

import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.service.HistoryService;
import com.sepay.backend.user.dto.SearchItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/histories", produces = "application/json; charset=UTF-8" )
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/filter")
    public List<HistoryDTO> getHistoryList(@RequestBody SearchItem searchItem) {
        log.info("SearchItem received: {}", searchItem);
        return historyService.getFilter(searchItem);
    }


    @GetMapping("/getList")
    public List<HistoryDTO> getAllHistories() {
        System.out.println("controller 연결");
        return historyService.getAllHistories();
    }

}
