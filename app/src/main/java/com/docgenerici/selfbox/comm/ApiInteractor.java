package com.docgenerici.selfbox.comm;




import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.contents.Folder;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Single;

/**
 * Login logic.
 *
 * @author Giuseppe Sorce
 */
public interface ApiInteractor {

  /**
   * The login logic.

   * @return A Single of a AuthData.
   */
  Single<LoginResponse> login(String appVer, String devId, String isfCode, String appLicence);
  Single<MedicalList> getallMedical(String isf);
  Single<List<Folder>> getAllContents(String isf);
  Single<ResponseBody> getProduct(String date);


}
