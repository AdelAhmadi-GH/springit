package com.adelahmadi.springit.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.ocpsoft.prettytime.PrettyTime;

import com.adelahmadi.springit.service.BeanUtil;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

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
    @NonNull
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    @NonNull
    private Link link;

    // Convenience constructor for creating a comment with body and link
    public Comment(String body, Link link) {
        this.body = body;
        this.link = link;
    }

    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreationDate()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

    // All prerequisites for this class are memorized through the Lombok annotations
    // written at the beginning of the class.
}
