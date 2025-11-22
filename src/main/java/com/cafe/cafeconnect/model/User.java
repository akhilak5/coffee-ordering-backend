package com.cafe.cafeconnect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // stored hashed with BCrypt

    @Column(nullable = false)
    private String role; // ADMIN, CHEF, WAITER, USER

    @Column(name = "invitation_code")
    private String invitationCode;

    @Column(nullable = false)
    private boolean activated = false;

    public User() {}

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.activated = false;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getInvitationCode() { return invitationCode; }
    public void setInvitationCode(String invitationCode) { this.invitationCode = invitationCode; }

    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }
}
