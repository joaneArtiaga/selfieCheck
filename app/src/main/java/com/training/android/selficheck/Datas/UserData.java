package com.training.android.selficheck.Datas;

/**
 * Created by Joane14 on 27/03/2017.
 */

public class UserData {

    private String Email;
    private String Name;
    private String Role;

    public UserData(){

    }

    public UserData(String email, String name, String role) {
        Email = email;
        Name = name;
        Role = role;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    public String getRole() {
        return Role;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setRole(String role) {
        Role = role;
    }
}
