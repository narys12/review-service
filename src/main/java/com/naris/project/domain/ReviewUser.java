package com.naris.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReviewUser.
 */

@Document(collection = "review_user")
public class ReviewUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nick_name")
    private String nickName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public ReviewUser nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewUser reviewUser = (ReviewUser) o;
        if (reviewUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reviewUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReviewUser{" +
            "id=" + id +
            ", nickName='" + nickName + "'" +
            '}';
    }
}
