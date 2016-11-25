package com.docgenerici.selfbox.retrofit;

import com.docgenerici.selfbox.models.LoginResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Single;

/**
 * @uthor giuseppesorce
 */

public interface SelfBoxUserApi {

    @FormUrlEncoded
    @POST("/selfbox-login/")
    Single<LoginResponse> login(@Field("appVer") String appVer, @Field("devId") String devId, @Field("isfCode") String isfCode, @Field("appLicence") String appLicence);
}
