package com.dyndyn.urm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.dyndyn.urm.domain.UtilityProvider} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilityProviderDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String iban;

    private String usreou;

    @NotNull
    private BigDecimal rate;

    private UtilityDTO utility;

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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getUsreou() {
        return usreou;
    }

    public void setUsreou(String usreou) {
        this.usreou = usreou;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public UtilityDTO getUtility() {
        return utility;
    }

    public void setUtility(UtilityDTO utility) {
        this.utility = utility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilityProviderDTO)) {
            return false;
        }

        UtilityProviderDTO utilityProviderDTO = (UtilityProviderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilityProviderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilityProviderDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", iban='" + getIban() + "'" +
            ", usreou='" + getUsreou() + "'" +
            ", rate=" + getRate() +
            ", utility=" + getUtility() +
            "}";
    }
}
