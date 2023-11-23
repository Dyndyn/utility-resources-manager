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
 * A Temperature.
 */
@Entity
@Table(name = "temperature")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Temperature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "temperature", nullable = false)
    private BigDecimal temperature;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "households", "region" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Temperature id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public Temperature temperature(BigDecimal temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public City getCity() {
        return city;
    }

    public Temperature city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public LocalDate getDate() {
        return date;
    }

    public Temperature date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Temperature)) {
            return false;
        }
        return getId() != null && getId().equals(((Temperature) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Temperature {" +
            "id=" + getId() +
            ", value='" + getTemperature() + "'" +
            ", name='" + getDate() + "'" +
            "}";
    }
}
