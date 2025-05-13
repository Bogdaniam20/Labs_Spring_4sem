package com.springlabs.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String emails;
    private String phones;

    @ManyToMany(mappedBy = "info")
    private List<User> users = new ArrayList<>();

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", emails='" + emails + '\'' +
                ", phones='" + phones + '\'' +
                '}';
    }
}