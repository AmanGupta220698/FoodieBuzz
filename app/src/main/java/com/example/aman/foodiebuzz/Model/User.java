package com.example.aman.foodiebuzz.Model;

/**
 * Created by Aman on 4/25/2019.
 */

public class User {
    private String Name;
    private String password;
    private String phone;
    private String isStaff;

    public User() {
    }

    public User(String name, String password, String phone, String isStaff) {
        Name = name;
        this.password = password;
        this.phone = phone;
        this.isStaff = isStaff;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}
