    package com.springlabs.model;

    import com.fasterxml.jackson.annotation.JsonIdentityInfo;
    import com.fasterxml.jackson.annotation.ObjectIdGenerators;
    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.ArrayList;
    import java.util.List;

    @Data
    @Entity
    @Table(name = "users")
    @NoArgsConstructor
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String name;
        private String surname;

        @ManyToMany
        @JoinTable(name = "user_info",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "info_id"))
        private List<Info> info = new ArrayList<>();

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", surname='" + surname + '\'' +
                    '}';
        }
    }