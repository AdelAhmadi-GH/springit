package com.adelahmadi.springit.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Comment extends Auditable {

    @Id
    @GeneratedValue
    private Long commentId;
    private String body;

    // link
    @ManyToOne
    @JoinColumn(name = "link_id")
    private Link link;

/* Replaced by Lombok annotations
    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(id, comment.id) && Objects.equals(body, comment.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body);
    }
*/
}
