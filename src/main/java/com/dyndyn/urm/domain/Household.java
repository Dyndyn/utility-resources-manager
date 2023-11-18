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
 * A Household.
 */
@Entity
@Table(name = "household")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Household implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "area", precision = 21, scale = 2, nullable = false)
    private BigDecimal area;

    @NotNull
    @Column(name = "residents", nullable = false)
    private Integer residents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "household")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "consumptionHistories", "household", "utilityProvider" }, allowSetters = true)
    private Set<HouseholdUtility> householdUtilities = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "household_user",
        joinColumns = @JoinColumn(name = "household_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "households", "region" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Household id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public Household address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getArea() {
        return this.area;
    }

    public Household area(BigDecimal area) {
        this.setArea(area);
        return this;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getResidents() {
        return this.residents;
    }

    public Household residents(Integer residents) {
        this.setResidents(residents);
        return this;
    }

    public void setResidents(Integer residents) {
        this.residents = residents;
    }

    public Set<HouseholdUtility> getHouseholdUtilities() {
        return this.householdUtilities;
    }

    public void setHouseholdUtilities(Set<HouseholdUtility> householdUtilities) {
        if (this.householdUtilities != null) {
            this.householdUtilities.forEach(i -> i.setHousehold(null));
        }
        if (householdUtilities != null) {
            householdUtilities.forEach(i -> i.setHousehold(this));
        }
        this.householdUtilities = householdUtilities;
    }

    public Household householdUtilities(Set<HouseholdUtility> householdUtilities) {
        this.setHouseholdUtilities(householdUtilities);
        return this;
    }

    public Household addHouseholdUtility(HouseholdUtility householdUtility) {
        this.householdUtilities.add(householdUtility);
        householdUtility.setHousehold(this);
        return this;
    }

    public Household removeHouseholdUtility(HouseholdUtility householdUtility) {
        this.householdUtilities.remove(householdUtility);
        householdUtility.setHousehold(null);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Household users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Household addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Household removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Household city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Household)) {
            return false;
        }
        return getId() != null && getId().equals(((Household) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Household{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", area=" + getArea() +
            ", residents=" + getResidents() +
            "}";
    }
}
