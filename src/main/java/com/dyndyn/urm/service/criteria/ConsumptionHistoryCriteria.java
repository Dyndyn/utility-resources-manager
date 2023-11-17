package com.dyndyn.urm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dyndyn.urm.domain.ConsumptionHistory} entity. This class is used
 * in {@link com.dyndyn.urm.web.rest.ConsumptionHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consumption-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumptionHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter consumption;

    private BigDecimalFilter cost;

    private LocalDateFilter date;

    private LongFilter householdUtilityId;

    private Boolean distinct;

    public ConsumptionHistoryCriteria() {}

    public ConsumptionHistoryCriteria(ConsumptionHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.consumption = other.consumption == null ? null : other.consumption.copy();
        this.cost = other.cost == null ? null : other.cost.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.householdUtilityId = other.householdUtilityId == null ? null : other.householdUtilityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConsumptionHistoryCriteria copy() {
        return new ConsumptionHistoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getConsumption() {
        return consumption;
    }

    public BigDecimalFilter consumption() {
        if (consumption == null) {
            consumption = new BigDecimalFilter();
        }
        return consumption;
    }

    public void setConsumption(BigDecimalFilter consumption) {
        this.consumption = consumption;
    }

    public BigDecimalFilter getCost() {
        return cost;
    }

    public BigDecimalFilter cost() {
        if (cost == null) {
            cost = new BigDecimalFilter();
        }
        return cost;
    }

    public void setCost(BigDecimalFilter cost) {
        this.cost = cost;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getHouseholdUtilityId() {
        return householdUtilityId;
    }

    public LongFilter householdUtilityId() {
        if (householdUtilityId == null) {
            householdUtilityId = new LongFilter();
        }
        return householdUtilityId;
    }

    public void setHouseholdUtilityId(LongFilter householdUtilityId) {
        this.householdUtilityId = householdUtilityId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConsumptionHistoryCriteria that = (ConsumptionHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(consumption, that.consumption) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(date, that.date) &&
            Objects.equals(householdUtilityId, that.householdUtilityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consumption, cost, date, householdUtilityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumptionHistoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (consumption != null ? "consumption=" + consumption + ", " : "") +
            (cost != null ? "cost=" + cost + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (householdUtilityId != null ? "householdUtilityId=" + householdUtilityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
