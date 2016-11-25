package com.docgenerici.selfbox.comm;



import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.retrofit.SelfBoxUserApi;


import java.util.List;

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
}
