package com.docgenerici.selfbox.models.products;

import io.realm.RealmObject;

/**
 * @uthor giuseppesorce
 */

public class Scheda extends RealmObject {

    public String published;
    public String status;
    private String uri;
    public String tipo;
    public String filename;
    public String uriPdf;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
