package com.dyndyn.urm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.dyndyn.urm.domain.Household} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HouseholdDTO implements Serializable {

    private Long id;

    @NotNull
    private String address;

    @NotNull
    private BigDecimal area;

    @NotNull
    private Integer residents;

    private Set<UserDTO> users = new HashSet<>();

    private CityDTO city;

    private GraphDataDTO costs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getResidents() {
        return residents;
    }

    public void setResidents(Integer residents) {
        this.residents = residents;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public GraphDataDTO getCosts() {
        return costs;
    }

    public void setCosts(GraphDataDTO costs) {
        this.costs = costs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HouseholdDTO)) {
            return false;
        }

        HouseholdDTO householdDTO = (HouseholdDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, householdDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HouseholdDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", area=" + getArea() +
            ", residents=" + getResidents() +
            ", users=" + getUsers() +
            ", city=" + getCity() +
            "}";
    }
}
