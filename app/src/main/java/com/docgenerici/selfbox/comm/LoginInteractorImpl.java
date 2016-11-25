package com.docgenerici.selfbox.comm;


import com.docgenerici.selfbox.models.LoginResponse;

import rx.Single;

/**
 * @author Giuseppe Sorce
 */
class LoginInteractorImpl implements LoginInteractor {

    private LoginApi api;


    public LoginInteractorImpl(LoginApi api) {
        this.api = api;

    }

    @Override
    public Single<LoginResponse> login(String appVer, String devId, String isfCode, String appLicence) {
        return api.login(appVer, devId, isfCode, appLicence);


    }


}
