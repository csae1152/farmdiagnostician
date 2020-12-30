package com.farmdiagnostician.web.domain;

import com.farmdiagnostician.web.domain.enumeration.animalKindTypeSearch;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Retina.
 */
@Entity
@Table(name = "retina")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Retina implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "animal")
    private animalKindTypeSearch animal;

    // jhipster-needle-entity-add-field - JHipster will add fields here
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

    public Retina name(String name) {
        this.name = name;
        return this;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Retina picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Retina pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public animalKindTypeSearch getAnimal() {
        return animal;
    }

    public void setAnimal(animalKindTypeSearch animal) {
        this.animal = animal;
    }

    public Retina animal(animalKindTypeSearch animal) {
        this.animal = animal;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Retina)) {
            return false;
        }
        return id != null && id.equals(((Retina) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Retina{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", animal='" + getAnimal() + "'" +
            "}";
    }
}
