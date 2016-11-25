package com.docgenerici.selfbox.comm;




import com.docgenerici.selfbox.models.LoginResponse;

import rx.Single;

/**
 * Provides the login's http API.
 *
 * @author Giuseppe Sorce
 */
public interface LoginApi {

  /**
   * Remote login.
   * @return A Single of a AuthData.
   */
  Single<LoginResponse> login(String appVer, String devId, String isfCode, String appLicence);


}
