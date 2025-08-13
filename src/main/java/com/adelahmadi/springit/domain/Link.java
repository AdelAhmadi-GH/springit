package com.adelahmadi.springit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "link")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "comments")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Link extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    @EqualsAndHashCode.Include
    private Long linkId;

    @NonNull
    @Column(nullable = false)
    private String title;

    @NonNull
    @Column(nullable = false)
    private String url;

    // comments
    @OneToMany(mappedBy = "link")
    private List<Comment> comments = new ArrayList<>();

    /**
     * Adds a comment to the link.
     *
     * @param comment the comment to be added
     *                This method adds a comment to the link's list of comments.
     *                It is typically used to maintain the relationship between a
     *                link and its comments.
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    // All prerequisites for this class are memorized through the Lombok annotations
    // written at the beginning of the class.
}
