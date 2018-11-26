package com.sciasv.asv.network;

/**
 * Created by Geek Nat on 5/16/2016.
 */

public class Connect {

    public static String HOST = "geeknat.com";
    public static String url = "http://" + HOST + "/elvis/api/";

    public static String login = "login";

    public static String fetchAsset = "asset";

    public static String fetchUserHistory(String userId) {
        return "history/" + userId;
    }

    public static String tag = "HTTP_CALL";


}
