package com.docgenerici.selfbox.models.contents;

import java.util.List;

/**
 * Created by giuseppesorce on 24/11/16.
 */

public class Content {

    public int id;
    public String name;
    public String descrFull;
    public String descrShort;
    public String textEmail;
    public String filename;
    public String checksum;
    public String thumbnailCover;
    public String keywords;
    public long creationDate;
    public long lastUpdate;
    public long expirationDate;
    public boolean alertHighlight;
    public boolean approved;
    public boolean visible;
    public boolean active;
    public Editor editor;
    public TypeContent type;
    public List<Target>targets;


    /*
    "id": 1,
        "name": "Test1",
        "descrFull": "Test PDF 1 Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1Test PDF 1",
        "descrShort": "Test PDF 1",
        "textEmail": null,
        "filename": "test1.pdf",
        "resourcePath": "",
        "checksum": null,
        "thumbnailCover": null,
        "keywords": "test",
        "creationDate": 1479651060000,
        "lastUpdate": 1479658260000,
        "expirationDate": 1482675060000,
        "alertHighlight": false,
        "approved": true,
        "visible": true,
        "active": true,
     */

}
