package com.dyndyn.urm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConsumptionHistory.
 */
@Entity
@Table(name = "consumption_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumptionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "consumption", precision = 21, scale = 2, nullable = false)
    private BigDecimal consumption;

    @Column(name = "cost", precision = 21, scale = 2)
    private BigDecimal cost;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "consumptionHistories", "household", "utilityProvider" }, allowSetters = true)
    private HouseholdUtility householdUtility;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConsumptionHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getConsumption() {
        return this.consumption;
    }

    public ConsumptionHistory consumption(BigDecimal consumption) {
        this.setConsumption(consumption);
        return this;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public ConsumptionHistory cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ConsumptionHistory date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HouseholdUtility getHouseholdUtility() {
        return this.householdUtility;
    }

    public void setHouseholdUtility(HouseholdUtility householdUtility) {
        this.householdUtility = householdUtility;
    }

    public ConsumptionHistory householdUtility(HouseholdUtility householdUtility) {
        this.setHouseholdUtility(householdUtility);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumptionHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ConsumptionHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumptionHistory{" +
            "id=" + getId() +
            ", consumption=" + getConsumption() +
            ", cost=" + getCost() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
