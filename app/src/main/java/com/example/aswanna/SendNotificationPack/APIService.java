package com.example.aswanna.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAa_-ToOc:APA91bFxzaMgjvlsQU2zIL1qeZy0ZY53JNzCdJcNdtq0sYl_incIexoD-Sr4evqiCHGGQKVAO7RrRq_qqOSNEpA72wdOSBGOvcaF-AANzNT_oPRoeVR_6ZtYZQlD4HhsI4PIFgZHQDjt" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);

}
