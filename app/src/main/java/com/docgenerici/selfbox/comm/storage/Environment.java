package com.docgenerici.selfbox.comm.storage;


import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * @author Giuseppe Sorce @copyright
 */

@Singleton
public class Environment {


  private String baseUrl;


  @Inject
  public Environment(String baseUrl) {
    this.baseUrl = baseUrl;
   }

  public String getBaseUrl() {

    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }


}
