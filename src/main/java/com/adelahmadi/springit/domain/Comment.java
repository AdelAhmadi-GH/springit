package com.adelahmadi.springit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String body;

    // link

    /* Replaced by Lombok annotations */
//    public Comment() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    @Override
//    public String toString() {
//        return "Comment{" +
//                "id=" + id +
//                ", body='" + body + '\'' +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof Comment comment)) return false;
//        return Objects.equals(id, comment.id) && Objects.equals(body, comment.body);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, body);
//    }
}
