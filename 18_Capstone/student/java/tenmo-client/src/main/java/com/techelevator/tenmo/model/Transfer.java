package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private Long id;
    private Long from;
    private Long to;
    private String type;
    private String status;
    private BigDecimal amount;

    public Transfer(Long from, Long to, String type, String status, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.status = status;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
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
