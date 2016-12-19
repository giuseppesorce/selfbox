package com.docgenerici.selfbox.comm;


import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.models.EmailText;
import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.shares.ShareDataSend;
import com.docgenerici.selfbox.retrofit.SelfBoxUserApi;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Single;

/**
 * @uthor giuseppesorce
 */

class SelfApiImpl implements SelfApi {

    private final SelfBoxUserApi selfBoxUserApi;
    private final Environment environment;

    public SelfApiImpl(SelfBoxUserApi selfBoxUserApi, Environment environment) {
        this.selfBoxUserApi = selfBoxUserApi;
        this.environment = environment;
    }

    @Override
    public Single<LoginResponse> login( String appVer,  String devId,  String isfCode,  String appLicence) {
        return selfBoxUserApi.login(appVer, devId, isfCode, appLicence);
    }

    @Override
    public Single<MedicalList> getallMedical(String isf) {
        return selfBoxUserApi.getMedicalData(isf);
    }

    @Override
    public Single<List<Folder>> getAllContents(String isf) {
        return selfBoxUserApi.getAllContents(isf);
    }

    @Override
    public Single<ResponseBody> getProduct(String date) {
        return selfBoxUserApi.getProducts("http://www.docgenerici.it/app/app.php",date);
    }

    @Override
    public Single<EmailText> getEmailText() {
        return selfBoxUserApi.getEmailText("http://docportal-staging.docgenerici.it/mail-template/");
    }

    @Override
    public Single<ResponseBody> shareData(ShareDataSend shareDataSend) {
        return selfBoxUserApi.shareData(shareDataSend);
    }
}
