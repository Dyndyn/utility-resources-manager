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
 * A UtilityProvider.
 */
@Entity
@Table(name = "utility_provider")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilityProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "iban")
    private String iban;

    @Column(name = "usreou")
    private String usreou;

    @NotNull
    @Column(name = "rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal rate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utilityProvider")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "consumptionHistories", "household", "utilityProvider" }, allowSetters = true)
    private Set<HouseholdUtility> householdUtilities = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "utilityProviders" }, allowSetters = true)
    private Utility utility;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UtilityProvider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UtilityProvider name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIban() {
        return this.iban;
    }

    public UtilityProvider iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getUsreou() {
        return this.usreou;
    }

    public UtilityProvider usreou(String usreou) {
        this.setUsreou(usreou);
        return this;
    }

    public void setUsreou(String usreou) {
        this.usreou = usreou;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public UtilityProvider rate(BigDecimal rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Set<HouseholdUtility> getHouseholdUtilities() {
        return this.householdUtilities;
    }

    public void setHouseholdUtilities(Set<HouseholdUtility> householdUtilities) {
        if (this.householdUtilities != null) {
            this.householdUtilities.forEach(i -> i.setUtilityProvider(null));
        }
        if (householdUtilities != null) {
            householdUtilities.forEach(i -> i.setUtilityProvider(this));
        }
        this.householdUtilities = householdUtilities;
    }

    public UtilityProvider householdUtilities(Set<HouseholdUtility> householdUtilities) {
        this.setHouseholdUtilities(householdUtilities);
        return this;
    }

    public UtilityProvider addHouseholdUtility(HouseholdUtility householdUtility) {
        this.householdUtilities.add(householdUtility);
        householdUtility.setUtilityProvider(this);
        return this;
    }

    public UtilityProvider removeHouseholdUtility(HouseholdUtility householdUtility) {
        this.householdUtilities.remove(householdUtility);
        householdUtility.setUtilityProvider(null);
        return this;
    }

    public Utility getUtility() {
        return this.utility;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

    public UtilityProvider utility(Utility utility) {
        this.setUtility(utility);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilityProvider)) {
            return false;
        }
        return getId() != null && getId().equals(((UtilityProvider) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilityProvider{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", iban='" + getIban() + "'" +
            ", usreou='" + getUsreou() + "'" +
            ", rate=" + getRate() +
            "}";
    }
}
