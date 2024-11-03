package com.sepay.backend.myaccount.dto;

import com.sepay.backend.history.dto.HistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DTORequest {
    private String targetHistoryContent;
    private String isMember;
    private double amount;
    private AccountDTO accountDTO;
    private SongAccountDTO songAccountDTO;
    private KrwAccountDTO krwAccountDTO;
    private HistoryDTO historyDTO;
    private Double exchangeRate;
    private String target_krwNo;
//    // Getter와 Setter
//    public String getTargetHistoryContent() {
//        return targetHistoryContent;
//    }
//
//    public void setTargetHistoryContent(String targetHistoryContent) {
//        this.targetHistoryContent = targetHistoryContent;
//    }
//    public String getIsMember() {
//        return isMember;
//    }
//    public void setIsMember(String isMember) {
//        this.isMember = isMember;
//    }
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public AccountDTO getAccountDTO() {
//        return accountDTO;
//    }
//
//    public void setAccountDTO(AccountDTO accountDTO) {
//        this.accountDTO = accountDTO;
//    }
//
//    public SongAccountDTO getSongAccountDTO() {
//        return songAccountDTO;
//    }
//
//    public void setSongAccountDTO(SongAccountDTO songAccountDTO) {
//        this.songAccountDTO = songAccountDTO;
//    }
//    public KrwAccountDTO getKrwAccountDTO() {
//        return krwAccountDTO;
//    }
//    public void setKrwAccountDTO(KrwAccountDTO krwAccountDTO) {
//        this.krwAccountDTO = krwAccountDTO;
//    }
//
//    public Double getExchangeRate() {
//        return exchangeRate;
//    }
//    public void setExchangeRate(Double exchangeRate) {
//        this.exchangeRate = exchangeRate;
//    }
//
//    public String getTarget_krwNo() {
//        return target_krwNo;
//    }
//    public void setTarget_krwNo(String target_krwNo) {
//        this.target_krwNo = target_krwNo;
//    }
//    public HistoryDTO getHistoryDTO() {
//        return historyDTO;
//    }
//    public void setHistoryDTO(HistoryDTO historyDTO) {
//        this.historyDTO = historyDTO;
//    }
}
