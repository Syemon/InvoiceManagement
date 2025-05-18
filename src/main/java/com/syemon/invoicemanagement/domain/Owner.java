package com.syemon.invoicemanagement.domain;

import lombok.Data;
import lombok.ToString;

@Data
public class Owner {
    private String username;
    @ToString.Exclude
    private String password;

    public Owner(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
