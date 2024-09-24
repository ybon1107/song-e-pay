package com.sepay.backend.myaccount.dto;

import com.sepay.backend.history.dto.HistoryDTO;

public class DTORequest {
    private double amount;
    private AccountDTO accountDTO;
    private SongAccountDTO songAccountDTO;
    private KrwAccountDTO krwAccountDTO;
    private HistoryDTO historyDTO;
    private double exchageRate;
    private String target_krwNo;
    // Getter와 Setter
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public SongAccountDTO getSongAccountDTO() {
        return songAccountDTO;
    }

    public void setSongAccountDTO(SongAccountDTO songAccountDTO) {
        this.songAccountDTO = songAccountDTO;
    }
    public KrwAccountDTO getKrwAccountDTO() {
        return krwAccountDTO;
    }
    public void setKrwAccountDTO(KrwAccountDTO krwAccountDTO) {
        this.krwAccountDTO = krwAccountDTO;
    }

    public Double getExchangeRate() {
        return exchageRate;
    }
    public void setExchangeRate(Double exchageRate) {
        this.exchageRate = exchageRate;
    }

    public String getTarget_krwNo() {
        return target_krwNo;
    }
    public void setTarget_krwNo(String target_krwNo) {
        this.target_krwNo = target_krwNo;
    }
    public HistoryDTO getHistoryDTO() {
        return historyDTO;
    }
    public void setHistoryDTO(HistoryDTO historyDTO) {
        this.historyDTO = historyDTO;
    }
}
