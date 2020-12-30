package com.farmdiagnostician.web.service.dto;

import com.farmdiagnostician.web.domain.enumeration.animalKindTypeSearch;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.farmdiagnostician.web.domain.Retina} entity.
 */
public class RetinaDTO implements Serializable {
    private Long id;

    private String name;

    @Lob
    private byte[] picture;

    private String pictureContentType;
    private animalKindTypeSearch animal;

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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public animalKindTypeSearch getAnimal() {
        return animal;
    }

    public void setAnimal(animalKindTypeSearch animal) {
        this.animal = animal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetinaDTO)) {
            return false;
        }

        return id != null && id.equals(((RetinaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RetinaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", picture='" + getPicture() + "'" +
            ", animal='" + getAnimal() + "'" +
            "}";
    }
}
