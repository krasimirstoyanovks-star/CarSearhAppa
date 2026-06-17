package com.example.carsearhapp.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String email;

    public int getId() { return id; }
    public String getFullName() { return username; }
    public String getEmail() { return email; }
}