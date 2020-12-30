package com.farmdiagnostician.web.service.dto;

import com.farmdiagnostician.web.domain.enumeration.animalKindTypeSearch;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.farmdiagnostician.web.domain.Retina} entity. This class is used
 * in {@link com.farmdiagnostician.web.web.rest.RetinaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /retinas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RetinaCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;
    private LongFilter id;
    private StringFilter name;
    private animalKindTypeSearchFilter animal;

    public RetinaCriteria() {}

    public RetinaCriteria(RetinaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.animal = other.animal == null ? null : other.animal.copy();
    }

    @Override
    public RetinaCriteria copy() {
        return new RetinaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public animalKindTypeSearchFilter getAnimal() {
        return animal;
    }

    public void setAnimal(animalKindTypeSearchFilter animal) {
        this.animal = animal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RetinaCriteria that = (RetinaCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(animal, that.animal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, animal);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RetinaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (animal != null ? "animal=" + animal + ", " : "") +
            "}";
    }

    /**
     * Class for filtering animalKindTypeSearch
     */
    public static class animalKindTypeSearchFilter extends Filter<animalKindTypeSearch> {

        public animalKindTypeSearchFilter() {}

        public animalKindTypeSearchFilter(animalKindTypeSearchFilter filter) {
            super(filter);
        }

        @Override
        public animalKindTypeSearchFilter copy() {
            return new animalKindTypeSearchFilter(this);
        }
    }
}
