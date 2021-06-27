package com.example.plazmabankam2;

import android.content.Context;
import android.content.SharedPreferences;

class Session {
    private SharedPreferences prefs;
    private static final String MyPREFERENCES = "LoggedInUser";
    private static final String LoggedInUserID = "userIdKey";
    private static final String LoggedInUserRole = "userRoleKey";
    private static final String LoggedInUserName = "userNameKey";
    private static final String LoggedInUserDisplayName = "displayNameKey";
    private static final String LoggedInUserPassword = "passwordNameKey";

    Session(Context ctx) {
        prefs = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    void saveLoggedInUser(User loggedInUser){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LoggedInUserID, loggedInUser.getUserID());
        editor.putInt(LoggedInUserRole, loggedInUser.getRole());
        editor.putString(LoggedInUserName, loggedInUser.getUserName());
        editor.putString(LoggedInUserDisplayName, loggedInUser.getDisplayName());
        editor.putString(LoggedInUserPassword, loggedInUser.getPassword());
        editor.apply();
    }

    User reloadLoggedInUser(){
        String userID = prefs.getString(LoggedInUserID,"0");
        int role = prefs.getInt(LoggedInUserRole, 0);
        String userName = prefs.getString(LoggedInUserName,"guest");
        String displayName = prefs.getString(LoggedInUserDisplayName,"guest");
        String userPass = prefs.getString(LoggedInUserPassword,"nopass");

        return new User(userID, role, displayName, userName, userPass);
    }
}
