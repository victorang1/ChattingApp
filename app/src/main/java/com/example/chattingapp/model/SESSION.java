package com.example.chattingapp.model;

import java.util.ArrayList;
import java.util.List;

public class SESSION {

    public static String username;
    public static String user_key;
    public static List<Friend> friendList = new ArrayList<>();

    public static void clear() {
        username = null;
        user_key = null;
        friendList = new ArrayList<>();
    }
}
