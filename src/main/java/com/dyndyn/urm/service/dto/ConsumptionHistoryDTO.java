package com.dyndyn.urm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dyndyn.urm.domain.ConsumptionHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumptionHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal consumption;

    private LocalDate date;

    private HouseholdUtilityDTO householdUtility;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HouseholdUtilityDTO getHouseholdUtility() {
        return householdUtility;
    }

    public void setHouseholdUtility(HouseholdUtilityDTO householdUtility) {
        this.householdUtility = householdUtility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumptionHistoryDTO)) {
            return false;
        }

        ConsumptionHistoryDTO consumptionHistoryDTO = (ConsumptionHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consumptionHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumptionHistoryDTO{" +
            "id=" + getId() +
            ", consumption=" + getConsumption() +
            ", date='" + getDate() + "'" +
            ", householdUtility=" + getHouseholdUtility() +
            "}";
    }
}
