package com.adelahmadi.springit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Vote {

    @Id
    @GeneratedValue
    private Long voteId;
    private int voteCounter;

    // user
    // link


/* Replaced by Lombok annotations
    public Vote() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", vote=" + vote +
                '}';
    }
*/
}
