package com.aau.storagemanagment.model;

import java.util.Map;
import java.util.HashMap;

public class User extends AbstractModel {
    private int id;
    private String username;
    private String password;
    private String role;

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    @Override
    public Object getIdentifier() { return id; }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", id);
        m.put("username", username);
        m.put("password", password);
        m.put("role", role);
        return m;
    }
}
