package com.aurea.zbw.api.model;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static javax.persistence.EnumType.STRING;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Table(name = "users")
@Entity
public class User implements Serializable {

    public enum Status { ONLINE, OFFLINE }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Email
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String username;
    @JsonProperty(access = WRITE_ONLY)
    private String password;
    private String firstName;
    private String lastName;
    private String image;

    @Enumerated(STRING)
    private Status status;

    private boolean enabled;

    @JsonIgnore
    private String resetKey;

    @OneToMany(fetch = EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }
    )
    private List<Role> roles;

}
