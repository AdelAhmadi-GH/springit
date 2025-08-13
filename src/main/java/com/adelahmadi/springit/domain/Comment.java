package com.adelahmadi.springit.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "link")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "body", nullable = false, length = 1000)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    private Link link;

//  All prerequisites for this class are memorized through the Lombok annotations written at the beginning of the class. 
}
