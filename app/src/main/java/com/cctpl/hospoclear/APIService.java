package com.cctpl.hospoclear;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAuQCDk-Y:APA91bH73QYmNnsxeWr-gXZqYs7NzH7MeGqzoHKWcPfV-M5CFLO58GntVLh_OMwiMonn3y2Rsz50z0IBS6EPkyT-ruB-bNdEq93rKcaCBr8Uu5hXqNk8YrvJwiGVY9AUni8pXYcWwVWB"
    })

    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationSender body);

}
