package com.dyndyn.urm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.dyndyn.urm.domain.HouseholdUtility} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HouseholdUtilityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String accountId;

    @NotNull
    private Boolean active;

    private HouseholdDTO household;

    private UtilityProviderDTO utilityProvider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public HouseholdDTO getHousehold() {
        return household;
    }

    public void setHousehold(HouseholdDTO household) {
        this.household = household;
    }

    public UtilityProviderDTO getUtilityProvider() {
        return utilityProvider;
    }

    public void setUtilityProvider(UtilityProviderDTO utilityProvider) {
        this.utilityProvider = utilityProvider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HouseholdUtilityDTO)) {
            return false;
        }

        HouseholdUtilityDTO householdUtilityDTO = (HouseholdUtilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, householdUtilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HouseholdUtilityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", active='" + getActive() + "'" +
            ", household=" + getHousehold() +
            ", utilityProvider=" + getUtilityProvider() +
            "}";
    }
}
