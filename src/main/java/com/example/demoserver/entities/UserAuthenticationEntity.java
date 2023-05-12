package com.example.demoserver.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "demo_app_user")
@AllArgsConstructor
@Data
public class UserAuthenticationEntity {

    @Column(name = "user_id")
    private Long userId;
    @Id
    private String email;
    @Column(name = "user_pass")
    private String password;
    private boolean enabled;
    @Column(name = "account_not_expired")
    private boolean accountNonExpired;
    @Column(name = "credentials_not_expired")
    private boolean credentialsNonExpired;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    public UserAuthenticationEntity(String email, String password) {
        this.email = email;
        this.password = password;
        this.enabled = false;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
    }

    public UserAuthenticationEntity() {
        this.enabled = false;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
    }
}
