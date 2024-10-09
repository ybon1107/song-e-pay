package com.sepay.backend.history.controller;

import com.sepay.backend.common.pagination.Page;
import com.sepay.backend.common.pagination.PageRequest;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.service.HistoryService;
import com.sepay.backend.user.dto.SearchItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/histories")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/filter")
    public ResponseEntity<Page<HistoryDTO>> getHistoryList(
            @RequestBody SearchItem searchItem, // SearchItem으로 요청 값 받음
            @RequestParam int page,
            @RequestParam int amount) {
        log.info("SearchItem received: {}", searchItem);
        log.info("Page: {}", page);
        log.info("Amount: {}", amount);
        log.info("Sort Order: {}", searchItem.getSortOrder());  // SearchItem 내의 sortOrder 확인

        try {
            int pageNumber = Math.max(page - 1, 0); // 0-based 인덱스로 변환
            int validAmount = Math.max(amount, 1);

            PageRequest pageRequest = PageRequest.of(pageNumber, validAmount);

            // 필터된 거래 내역을 페이지네이션과 함께 반환
            Page<HistoryDTO> filteredHistories = historyService.getFilter(searchItem, pageRequest);
            log.info("Filtered Histories: {}", filteredHistories);
            return ResponseEntity.ok(filteredHistories);
        } catch (Exception e) {
            log.error("Error occurred while filtering histories", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//    @GetMapping("/getList")
//    public ResponseEntity<Page<HistoryDTO>> getList(@RequestParam int page,
//                                                    @RequestParam int amount) {
//        log.info("Request received: GET /api/histories/getList");
//        log.info("Page: {}", page);
//        log.info("Amount: {}", amount);
//
//        try {
//            // 페이지 번호가 1 미만일 경우 기본값으로 설정
//            int pageNumber = Math.max(page-1, 0); // 1-based 인덱스를 0-based로 변환
//            int validAmount = Math.max(amount, 1); // 최소 1개 이상의 항목을 받아야 함
//
//            // PageRequest 생성
//            PageRequest pageRequest = PageRequest.of(pageNumber, validAmount);
//
//            // 전체 거래 내역을 페이지네이션과 함께 반환
//            Page<HistoryDTO> histories = historyService.getAllHistories(pageRequest);
//            log.info("Histories: {}", histories);
//            return ResponseEntity.ok(histories);
//        } catch (Exception e) {
//            // 오류 발생 시 상세 로그 출력
//            log.error("Error occurred while retrieving histories", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @PostMapping("/updateMemo")
    public ResponseEntity<String> updateMemo(@RequestBody HistoryDTO historyDTO) {
        log.info("Request received: POST /api/histories/updateMemo");
        log.info("HistoryDTO: {}", historyDTO);

        try {
            historyService.updateMemo(historyDTO);
            log.info("Memo updated successfully for historyNo: {}", historyDTO.getHistoryNo());
            return ResponseEntity.ok("메모가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            log.error("메모 업데이트 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메모 업데이트에 실패했습니다.");
        }
    }
}