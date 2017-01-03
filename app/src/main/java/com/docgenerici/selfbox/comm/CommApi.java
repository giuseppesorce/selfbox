package com.docgenerici.selfbox.comm;


import com.docgenerici.selfbox.models.EmailText;
import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.SessionCounter;
import com.docgenerici.selfbox.models.SessionCounterResponse;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.shares.ShareDataSend;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Single;

/**
 * Provides the login's http API.
 *
 * @author Giuseppe Sorce
 */
public interface CommApi {

    /**
     * Remote login.
     *
     * @return A Single of a AuthData.
     */
    Single<LoginResponse> login(String appVer, String devId, String isfCode, String appLicence);

    Single<MedicalList> getallMedical(String isf);

    Single<List<Folder>> getAllContents(String isf);


    Single<ResponseBody> getProduct(String date);

    Single<SessionCounterResponse> sendStatistic(SessionCounter sessionCounter);

    Single<EmailText> getEmailText();

    Single<ResponseBody> shareData(ShareDataSend shareDataSend);
}
