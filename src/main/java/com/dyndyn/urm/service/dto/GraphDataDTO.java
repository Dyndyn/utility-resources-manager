package com.dyndyn.urm.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GraphDataDTO implements Serializable {

    private List<LocalDate> month;
    private List<BigDecimal> data;

    public GraphDataDTO(List<LocalDate> month, List<BigDecimal> data) {
        this.month = month;
        this.data = data;
    }

    public List<LocalDate> getMonth() {
        return month;
    }

    public void setMonth(List<LocalDate> month) {
        this.month = month;
    }

    public List<BigDecimal> getData() {
        return data;
    }

    public void setData(List<BigDecimal> data) {
        this.data = data;
    }
}
