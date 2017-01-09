package com.docgenerici.selfbox.comm;


import com.docgenerici.selfbox.android.home.ServerResponse;
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
 * @author Giuseppe Sorce
 */
class ApiInteractorImpl implements ApiInteractor {

    private CommApi api;

    public ApiInteractorImpl(CommApi api) {
        this.api = api;
    }

    @Override
    public Single<LoginResponse> login(String appVer, String devId, String isfCode, String appLicence) {
        return api.login(appVer, devId, isfCode, appLicence);
    }

    @Override
    public Single<MedicalList> getallMedical(String isf) {
        return api.getallMedical(isf);
    }

    @Override
    public Single<List<Folder>> getAllContents(String isf) {
        return api.getAllContents(isf);
    }

    @Override
    public Single<ResponseBody> getProduct(String date) {
        return api.getProduct(date);
    }

    @Override
    public Single<EmailText> getEmailText() {
        return api.getEmailText();
    }

    @Override
    public Single<ServerResponse> shareData(ShareDataSend shareDataSend) {
        return api.shareData(shareDataSend);
    }

    @Override
   public Single<SessionCounterResponse> sendStatistic(SessionCounter sessionCounter){
        return api.sendStatistic(sessionCounter);
    }
}
