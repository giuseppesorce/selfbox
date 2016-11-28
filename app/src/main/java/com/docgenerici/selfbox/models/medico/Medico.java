
package com.docgenerici.selfbox.models.medico;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Medico extends RealmObject{

    @PrimaryKey
    public int id;
    public String code;
    public String csvLineCode;
    public String csvLineDescr;
    public Line line;
    public String type;
    public String fullname;
    public String visitTarget;
    public String address;
    public String cap;
    public String city;
    public String provinceCode;
    public String provinceDescr;
    public String provinceLabel;
    public String csvCodMicrobrick;
    public String csvDescrMicrobrick;
    public Microbrick microbrick;
    public String email;
    public String name;
    public String surname;
    public String spec;
    public String specUff1;
    public String specUff2;
    public String specUff3;
    public String specUff4;
    public String specUff5;
    public String privacy;
    public String csvStatus;
    public boolean status;
    public String csvPrivacyUpdate;
    public String privacyUpdate;


}
