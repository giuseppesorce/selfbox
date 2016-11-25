package com.docgenerici.selfbox.comm;




import com.docgenerici.selfbox.models.LoginResponse;

import rx.Single;

/**
 * Login logic.
 *
 * @author Giuseppe Sorce
 */
public interface LoginInteractor {

  /**
   * The login logic.

   * @return A Single of a AuthData.
   */
  Single<LoginResponse> login(String appVer, String devId, String isfCode, String appLicence);


}
