package com.dyndyn.urm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utility.
 */
@Entity
@Table(name = "utility")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "constant", nullable = false)
    private Boolean constant;

    @NotNull
    @Column(name = "predictable", nullable = false)
    private Boolean predictable;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utility")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "householdUtilities", "utility" }, allowSetters = true)
    private Set<UtilityProvider> utilityProviders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utility id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Utility name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getConstant() {
        return this.constant;
    }

    public Utility constant(Boolean constant) {
        this.setConstant(constant);
        return this;
    }

    public void setConstant(Boolean constant) {
        this.constant = constant;
    }

    public Boolean getPredictable() {
        return this.predictable;
    }

    public Utility predictable(Boolean predictable) {
        this.setPredictable(predictable);
        return this;
    }

    public void setPredictable(Boolean predictable) {
        this.predictable = predictable;
    }

    public Set<UtilityProvider> getUtilityProviders() {
        return this.utilityProviders;
    }

    public void setUtilityProviders(Set<UtilityProvider> utilityProviders) {
        if (this.utilityProviders != null) {
            this.utilityProviders.forEach(i -> i.setUtility(null));
        }
        if (utilityProviders != null) {
            utilityProviders.forEach(i -> i.setUtility(this));
        }
        this.utilityProviders = utilityProviders;
    }

    public Utility utilityProviders(Set<UtilityProvider> utilityProviders) {
        this.setUtilityProviders(utilityProviders);
        return this;
    }

    public Utility addUtilityProvider(UtilityProvider utilityProvider) {
        this.utilityProviders.add(utilityProvider);
        utilityProvider.setUtility(this);
        return this;
    }

    public Utility removeUtilityProvider(UtilityProvider utilityProvider) {
        this.utilityProviders.remove(utilityProvider);
        utilityProvider.setUtility(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utility)) {
            return false;
        }
        return getId() != null && getId().equals(((Utility) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utility{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", constant='" + getConstant() + "'" +
            ", predictable='" + getPredictable() + "'" +
            "}";
    }
}
