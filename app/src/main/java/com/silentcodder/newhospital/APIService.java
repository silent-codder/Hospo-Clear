package com.silentcodder.newhospital;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAx9LR7sY:APA91bF0rSCkVzUXnWit8cShqBOQ3Kn1Zv0iNy8ft4EmB26_63sBI0M9TjQJjbFCFG1FB2Q1-a29Vq6dzMLVGRI0DK5_HZ_liqKGXxnDqGo72HQZ3lOlXvQJOEKSndlvctyMjuHo95Bd"
    })

    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationSender body);

}
