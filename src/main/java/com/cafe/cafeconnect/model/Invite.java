package com.cafe.cafeconnect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "invites")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // stored hashed using BCrypt
    @Column(name = "code_hash", nullable = false)
    private String codeHash;

    // role assigned (CHEF, WAITER)
    @Column(nullable = false)
    private String role;

    @Column(name = "is_used", nullable = false)
    private boolean used = false;

    public Invite() { }

    // constructor for saving (only hash + role)
    public Invite(String codeHash, String role) {
        this.codeHash = codeHash;
        this.role = role;
        this.used = false;
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodeHash() { return codeHash; }
    public void setCodeHash(String codeHash) { this.codeHash = codeHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
}




