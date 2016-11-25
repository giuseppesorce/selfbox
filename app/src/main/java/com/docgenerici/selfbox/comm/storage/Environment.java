package com.docgenerici.selfbox.comm.storage;


import com.docgenerici.selfbox.BuildConfig;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * @author Giuseppe Sorce @copyright ReMago srl  2016.
 */

@Singleton
public class Environment {


  private String baseUrl;


  @Inject
  public Environment(String baseUrl) {
    this.baseUrl = baseUrl;
   }

  public String getBaseUrl() {
    return BuildConfig.BASE_URL;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }


}
