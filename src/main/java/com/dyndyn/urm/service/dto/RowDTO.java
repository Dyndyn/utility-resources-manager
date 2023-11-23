package com.dyndyn.urm.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RowDTO {

    private Long householdId;
    private Double consumption;
    private Double area;
    private Integer residents;
    private LocalDate month;
    private Double temperature;

    private Double consumptionLag1;
    private Double consumptionLag2;
    private Double monthSin;
    private Double monthCos;
    private Double yearSin;
    private Double yearCos;
    private Double meanDailyConsumption;
    private Double dailyConsumptionDeviation;
    private Double trend;
    private Double tempCorrelation;

    public RowDTO(Long householdId, BigDecimal consumption, BigDecimal area, Integer residents, LocalDate month, BigDecimal temperature) {
        this.householdId = householdId;
        this.consumption = consumption.doubleValue();
        this.area = area.doubleValue();
        this.residents = residents;
        this.month = month;
        this.temperature = temperature.doubleValue();
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(Long householdId) {
        this.householdId = householdId;
    }

    public RowDTO householdId(Long householdId) {
        this.householdId = householdId;
        return this;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    public RowDTO consumption(Double consumption) {
        this.consumption = consumption;
        return this;
    }

    public Double getArea() {
        return area;
    }

    public RowDTO area(Double area) {
        this.area = area;
        return this;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getResidents() {
        return residents;
    }

    public RowDTO residents(Integer residents) {
        this.residents = residents;
        return this;
    }

    public void setResidents(Integer residents) {
        this.residents = residents;
    }

    public LocalDate getMonth() {
        return month;
    }

    public RowDTO month(LocalDate month) {
        this.month = month;
        return this;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getConsumptionLag1() {
        return consumptionLag1;
    }

    public void setConsumptionLag1(Double consumptionLag1) {
        this.consumptionLag1 = consumptionLag1;
    }

    public Double getConsumptionLag2() {
        return consumptionLag2;
    }

    public void setConsumptionLag2(Double consumptionLag2) {
        this.consumptionLag2 = consumptionLag2;
    }

    public Double getMonthSin() {
        return monthSin;
    }

    public void setMonthSin(Double monthSin) {
        this.monthSin = monthSin;
    }

    public Double getMonthCos() {
        return monthCos;
    }

    public void setMonthCos(Double monthCos) {
        this.monthCos = monthCos;
    }

    public Double getYearSin() {
        return yearSin;
    }

    public void setYearSin(Double yearSin) {
        this.yearSin = yearSin;
    }

    public Double getYearCos() {
        return yearCos;
    }

    public void setYearCos(Double yearCos) {
        this.yearCos = yearCos;
    }

    public Double getMeanDailyConsumption() {
        return meanDailyConsumption;
    }

    public void setMeanDailyConsumption(Double meanDailyConsumption) {
        this.meanDailyConsumption = meanDailyConsumption;
    }

    public Double getDailyConsumptionDeviation() {
        return dailyConsumptionDeviation;
    }

    public void setDailyConsumptionDeviation(Double dailyConsumptionDeviation) {
        this.dailyConsumptionDeviation = dailyConsumptionDeviation;
    }

    public void calculate() {
        this.monthSin = Math.sin((2 * Math.PI * this.month.getMonthValue()) / 12);
        this.monthCos = Math.cos((2 * Math.PI * this.month.getMonthValue()) / 12);
    }

    public Double getTrend() {
        return trend;
    }

    public void setTrend(Double trend) {
        this.trend = trend;
    }

    public Double getTempCorrelation() {
        return tempCorrelation;
    }

    public void setTempCorrelation(Double tempCorrelation) {
        this.tempCorrelation = tempCorrelation;
    }
}
