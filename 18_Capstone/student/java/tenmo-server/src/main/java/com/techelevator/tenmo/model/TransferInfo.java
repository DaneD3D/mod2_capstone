package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferInfo {

    private Integer id;
    private String from;
    private String to;
    private String type;
    private String status;
    private BigDecimal amount;

    public TransferInfo() {};

    public TransferInfo(String from, String to, String type, String status, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.status = status;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
