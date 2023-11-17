package com.dyndyn.urm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.dyndyn.urm.domain.Utility} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean constant;

    @NotNull
    private Boolean predictable;

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

    public Boolean getConstant() {
        return constant;
    }

    public void setConstant(Boolean constant) {
        this.constant = constant;
    }

    public Boolean getPredictable() {
        return predictable;
    }

    public void setPredictable(Boolean predictable) {
        this.predictable = predictable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilityDTO)) {
            return false;
        }

        UtilityDTO utilityDTO = (UtilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", constant='" + getConstant() + "'" +
            ", predictable='" + getPredictable() + "'" +
            "}";
    }
}
