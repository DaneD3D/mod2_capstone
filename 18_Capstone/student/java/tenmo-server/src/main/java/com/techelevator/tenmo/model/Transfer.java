package com.techelevator.tenmo.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class Transfer {

    private Integer id;
    @NotNull
    @Positive
    private Integer from;
    @NotNull
    @Positive
    private Integer to;
    private Integer type;
    private Integer status;
    @DecimalMin(value = "1", message = "Amount to transfer must be a positive amount.")
    private BigDecimal amount;

    public Transfer() {};

    public Transfer(Integer from, Integer to, Integer type, Integer status, BigDecimal amount) {
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

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
