package com.naris.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Review.
 */

@Document(collection = "review")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("comment")
    private String comment;

    @Field("created")
    private ZonedDateTime created;

    @Field("published")
    private ZonedDateTime published;

    @Field("star")
    private Integer star;

    @Field("pros")
    private String pros;

    @Field("cons")
    private String cons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Review title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public Review comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Review created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getPublished() {
        return published;
    }

    public Review published(ZonedDateTime published) {
        this.published = published;
        return this;
    }

    public void setPublished(ZonedDateTime published) {
        this.published = published;
    }

    public Integer getStar() {
        return star;
    }

    public Review star(Integer star) {
        this.star = star;
        return this;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getPros() {
        return pros;
    }

    public Review pros(String pros) {
        this.pros = pros;
        return this;
    }

    public void setPros(String pros) {
        this.pros = pros;
    }

    public String getCons() {
        return cons;
    }

    public Review cons(String cons) {
        this.cons = cons;
        return this;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Review review = (Review) o;
        if (review.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Review{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", comment='" + comment + "'" +
            ", created='" + created + "'" +
            ", published='" + published + "'" +
            ", star='" + star + "'" +
            ", pros='" + pros + "'" +
            ", cons='" + cons + "'" +
            '}';
    }
}
