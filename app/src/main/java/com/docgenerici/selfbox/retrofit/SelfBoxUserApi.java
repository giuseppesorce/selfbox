package com.docgenerici.selfbox.retrofit;

import com.docgenerici.selfbox.android.home.ServerResponse;
import com.docgenerici.selfbox.models.EmailText;
import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.SessionCounter;
import com.docgenerici.selfbox.models.SessionCounterResponse;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.shares.ShareData;
import com.docgenerici.selfbox.models.shares.ShareDataSend;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Single;

/**
 * @uthor giuseppesorce
 */

public interface SelfBoxUserApi {

    @FormUrlEncoded
    @POST("/selfbox-login/")
    Single<LoginResponse> login(@Field("appVer") String appVer, @Field("devId") String devId, @Field("isfCode") String isfCode, @Field("appLicence") String appLicence);

    @POST("/send-counters/")
    Single<SessionCounterResponse> sendStatistic(@Body SessionCounter sessionCounter);

    @POST("/isf/isf-selfbox/{isf}")
    Single<MedicalList> getMedicalData(@Path("isf") String isf);

    @POST("/contents/contents-selfbox/{isf}")
    Single<List<Folder>> getAllContents(@Path("isf") String isf);


    @GET
    Single<ResponseBody> getProducts(@Url String url ,  @Query("DT_RIF") String date);

    @GET
    Single<EmailText> getEmailText(@Url String url);


    @POST("/share-contents/")
    Single<ServerResponse> shareData(@Body ShareDataSend shareDataSend);




}
