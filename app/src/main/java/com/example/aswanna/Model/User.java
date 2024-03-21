package com.example.aswanna.Model;

import java.util.HashMap;

public class User {

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_USER_TYPE ="usertype";
    public static final String KEY_EMAIL= "email";
    public static final String KEY_PASSWORD ="password";
    public static final String KEY_PHONE_NO ="phoneno";
    public static final String KEY_CONFIRM_PASSWORD ="confpassword";
    public static final String KEY_PREFERENCE_NAME ="chatAppPreference";
    public static final String KEY_IS_SIGNED_IN ="isSignedIn";
    public static final String KEY_USER_ID ="userId";
    public static final String KEY_IMAGE ="image";
    public static final String KEY_LEVEL="level";
    public static final String KEY_COUNT="count";
    public static final String KEY_FCM_TOKEN ="fcmToken";
    public static final String KEY_USER ="user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID ="senderId";
    public static final String KEY_RECEIVER_id ="receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS ="conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE ="receiverImage";
    public static final String KEY_LAST_MESSAGE ="lastMessage";
    public static final String KEY_AVAILABILITY ="availability";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static HashMap<String, String> remoteMsgHeader = null;
    public static HashMap<String, String> getRemoteMsgHeader(){
        if (remoteMsgHeader == null){
            remoteMsgHeader = new HashMap<>();
            remoteMsgHeader.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAa_-ToOc:APA91bFxzaMgjvlsQU2zIL1qeZy0ZY53JNzCdJcNdtq0sYl_incIexoD-Sr4evqiCHGGQKVAO7RrRq_qqOSNEpA72wdOSBGOvcaF-AANzNT_oPRoeVR_6ZtYZQlD4HhsI4PIFgZHQDjt"
            );
            remoteMsgHeader.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeader;
    }


    public User() {
      
    }


}
