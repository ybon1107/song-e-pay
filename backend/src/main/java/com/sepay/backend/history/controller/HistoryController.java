package com.sepay.backend.history.controller;

import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.service.HistoryService;
import com.sepay.backend.user.dto.SearchItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/updateMemo")
    public ResponseEntity<String> updateMemo(@RequestBody HistoryDTO historyDTO) {
        try {
            historyService.updateMemo(historyDTO);
            return ResponseEntity.ok("메모가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            log.error("메모 업데이트 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메모 업데이트에 실패했습니다.");
        }
    }

}
