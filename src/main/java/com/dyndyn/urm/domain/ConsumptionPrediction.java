package com.dyndyn.urm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConsumptionPrediction.
 */
@Entity
@Table(name = "consumption_prediction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumptionPrediction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "consumption", precision = 21, scale = 2, nullable = false)
    private BigDecimal consumption;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "consumptionHistories", "consumptionPredictions", "household", "utilityProvider" }, allowSetters = true)
    private HouseholdUtility householdUtility;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConsumptionPrediction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getConsumption() {
        return this.consumption;
    }

    public ConsumptionPrediction consumption(BigDecimal consumption) {
        this.setConsumption(consumption);
        return this;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ConsumptionPrediction date(LocalDate date) {
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

    public ConsumptionPrediction householdUtility(HouseholdUtility householdUtility) {
        this.setHouseholdUtility(householdUtility);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumptionPrediction)) {
            return false;
        }
        return getId() != null && getId().equals(((ConsumptionPrediction) o).getId());
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
            ", date='" + getDate() + "'" +
            "}";
    }
}
