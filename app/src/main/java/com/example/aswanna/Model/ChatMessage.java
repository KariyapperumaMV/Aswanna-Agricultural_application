package com.example.aswanna.Model;

import com.example.aswanna.SendNotificationPack.Data;

import java.util.Date;

public class ChatMessage {
    public String senderId, receiverId, message,dateTime;
    public Date dateObject;
    public String conversionId, conversionName, conversionImage;
    private boolean updateMessageVisible;

    public boolean isUpdateMessageVisible() {
        return updateMessageVisible;
    }

    public void setUpdateMessageVisible(boolean updateMessageVisible) {
        this.updateMessageVisible = updateMessageVisible;
    }
}
