package com.dyndyn.urm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dyndyn.urm.domain.HouseholdUtility} entity. This class is used
 * in {@link com.dyndyn.urm.web.rest.HouseholdUtilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /household-utilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HouseholdUtilityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter accountId;

    private BooleanFilter active;

    private LongFilter consumptionHistoryId;

    private LongFilter householdId;

    private LongFilter utilityProviderId;

    private Boolean distinct;

    public HouseholdUtilityCriteria() {}

    public HouseholdUtilityCriteria(HouseholdUtilityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.accountId = other.accountId == null ? null : other.accountId.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.consumptionHistoryId = other.consumptionHistoryId == null ? null : other.consumptionHistoryId.copy();
        this.householdId = other.householdId == null ? null : other.householdId.copy();
        this.utilityProviderId = other.utilityProviderId == null ? null : other.utilityProviderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HouseholdUtilityCriteria copy() {
        return new HouseholdUtilityCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAccountId() {
        return accountId;
    }

    public StringFilter accountId() {
        if (accountId == null) {
            accountId = new StringFilter();
        }
        return accountId;
    }

    public void setAccountId(StringFilter accountId) {
        this.accountId = accountId;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getConsumptionHistoryId() {
        return consumptionHistoryId;
    }

    public LongFilter consumptionHistoryId() {
        if (consumptionHistoryId == null) {
            consumptionHistoryId = new LongFilter();
        }
        return consumptionHistoryId;
    }

    public void setConsumptionHistoryId(LongFilter consumptionHistoryId) {
        this.consumptionHistoryId = consumptionHistoryId;
    }

    public LongFilter getHouseholdId() {
        return householdId;
    }

    public LongFilter householdId() {
        if (householdId == null) {
            householdId = new LongFilter();
        }
        return householdId;
    }

    public void setHouseholdId(LongFilter householdId) {
        this.householdId = householdId;
    }

    public LongFilter getUtilityProviderId() {
        return utilityProviderId;
    }

    public LongFilter utilityProviderId() {
        if (utilityProviderId == null) {
            utilityProviderId = new LongFilter();
        }
        return utilityProviderId;
    }

    public void setUtilityProviderId(LongFilter utilityProviderId) {
        this.utilityProviderId = utilityProviderId;
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
        final HouseholdUtilityCriteria that = (HouseholdUtilityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(accountId, that.accountId) &&
            Objects.equals(active, that.active) &&
            Objects.equals(consumptionHistoryId, that.consumptionHistoryId) &&
            Objects.equals(householdId, that.householdId) &&
            Objects.equals(utilityProviderId, that.utilityProviderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountId, active, consumptionHistoryId, householdId, utilityProviderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HouseholdUtilityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (accountId != null ? "accountId=" + accountId + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (consumptionHistoryId != null ? "consumptionHistoryId=" + consumptionHistoryId + ", " : "") +
            (householdId != null ? "householdId=" + householdId + ", " : "") +
            (utilityProviderId != null ? "utilityProviderId=" + utilityProviderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
