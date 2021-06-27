package com.example.plazmabankam2;

public class User {
    private String userID;
    private boolean isActive;
    private int role;
    private String displayName;
    private String userName;
    private String password;

    public User(){

    }

    public User(com.example.plazmabankam2.User user){
        this.userID = user.getUserID();
        this.isActive = user.getActive();
        this.role = user.getRole();
        this.displayName = user.getDisplayName();
        this.userName = user.getUserName();
        this.password = user.getPassword();
    }

    public User(String userID, int role, String displayName, String userName, String pass){
        this.userID = userID;
        this.isActive = true;
        this.role = role;
        this.displayName = displayName;
        this.userName = userName;
        this.password = pass;
    }

    public String getUserID(){
        return this.userID;
    }

    public boolean getActive(){
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public int getRole(){
        return this.role;
    }

    public void setRole(int newRole) {
        this.role = newRole;
    }

    public String getDisplayName(){
        return this.displayName;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getPassword(){
        return this.password;
    }
}
