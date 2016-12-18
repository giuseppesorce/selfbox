package com.docgenerici.selfbox.models.products;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * @uthor giuseppesorce
 */

public class Product extends RealmObject{

    @PrimaryKey
    public String aic;
    public String nome;
    public String denominazione_it;
    public String denominazione_en;
    public Scheda scheda;
    public String rcp;
    public String originatore;
    public String noFCDL;
    public String classeSnn;
    public String published;
    @Index
    public String categoria_farmacologica;
}
