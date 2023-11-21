package com.dyndyn.urm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HouseholdUtility.
 */
@Entity
@Table(name = "household_utility")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HouseholdUtility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private String accountId;

    @NotNull
    @Column(name = "rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal rate;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "householdUtility")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "householdUtility" }, allowSetters = true)
    private Set<ConsumptionHistory> consumptionHistories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "householdUtility")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "householdUtility" }, allowSetters = true)
    private Set<ConsumptionPrediction> consumptionPredictions = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "householdUtilities", "users", "city" }, allowSetters = true)
    private Household household;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "householdUtilities", "utility" }, allowSetters = true)
    private UtilityProvider utilityProvider;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HouseholdUtility id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HouseholdUtility name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public HouseholdUtility accountId(String accountId) {
        this.setAccountId(accountId);
        return this;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public HouseholdUtility rate(BigDecimal rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public HouseholdUtility active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<ConsumptionHistory> getConsumptionHistories() {
        return this.consumptionHistories;
    }

    public void setConsumptionHistories(Set<ConsumptionHistory> consumptionHistories) {
        if (this.consumptionHistories != null) {
            this.consumptionHistories.forEach(i -> i.setHouseholdUtility(null));
        }
        if (consumptionHistories != null) {
            consumptionHistories.forEach(i -> i.setHouseholdUtility(this));
        }
        this.consumptionHistories = consumptionHistories;
    }

    public HouseholdUtility consumptionHistories(Set<ConsumptionHistory> consumptionHistories) {
        this.setConsumptionHistories(consumptionHistories);
        return this;
    }

    public HouseholdUtility addConsumptionHistory(ConsumptionHistory consumptionHistory) {
        this.consumptionHistories.add(consumptionHistory);
        consumptionHistory.setHouseholdUtility(this);
        return this;
    }

    public HouseholdUtility removeConsumptionHistory(ConsumptionHistory consumptionHistory) {
        this.consumptionHistories.remove(consumptionHistory);
        consumptionHistory.setHouseholdUtility(null);
        return this;
    }

    public Set<ConsumptionPrediction> getConsumptionPredictions() {
        return consumptionPredictions;
    }

    public void setConsumptionPredictions(Set<ConsumptionPrediction> consumptionPredictions) {
        this.consumptionPredictions = consumptionPredictions;
    }

    public Household getHousehold() {
        return this.household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    public HouseholdUtility household(Household household) {
        this.setHousehold(household);
        return this;
    }

    public UtilityProvider getUtilityProvider() {
        return this.utilityProvider;
    }

    public void setUtilityProvider(UtilityProvider utilityProvider) {
        this.utilityProvider = utilityProvider;
    }

    public HouseholdUtility utilityProvider(UtilityProvider utilityProvider) {
        this.setUtilityProvider(utilityProvider);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HouseholdUtility)) {
            return false;
        }
        return getId() != null && getId().equals(((HouseholdUtility) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HouseholdUtility{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", rate=" + getRate() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
