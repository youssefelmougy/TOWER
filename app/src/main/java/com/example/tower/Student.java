package com.example.tower;

public class Student {
    private String name;
    private String email;
    private long id;
    private String password;
    private String uID;

    public Student() {

    }

    public String getuID() {
        return uID;
    }

    public Student(String name, String email, long id, String password, String uID) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.password = password;
        this.uID = uID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

}
