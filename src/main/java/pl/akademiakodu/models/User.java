package pl.akademiakodu.models;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private boolean isActive;
}
