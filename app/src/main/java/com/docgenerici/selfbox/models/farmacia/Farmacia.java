
package com.docgenerici.selfbox.models.farmacia;


import com.docgenerici.selfbox.models.medico.Microbrick;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Farmacia extends RealmObject {

    @PrimaryKey
    public int id;
    public String ente;
    public String ims;
    public String fullname;
    public String type;
    public String subType;
    public String vatNumber;
    public String address;
    public String cap;
    public String city;
    public String provinceCode;
    public String provinceDescr;
    public String provinceLabel;
    public Microbrick microbrick;
    public Cluster cluster;
    public String qmDoc;


}
