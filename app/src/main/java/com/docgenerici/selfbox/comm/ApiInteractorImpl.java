package com.docgenerici.selfbox.comm;


import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.contents.Folder;

import java.util.List;

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


}
