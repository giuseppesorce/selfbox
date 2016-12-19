package com.docgenerici.selfbox.models.products;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * @uthor giuseppesorce
 */

public class Product extends RealmObject{

    @PrimaryKey
    private String aic;
    private String nome;
    public String denominazione_it;
    public String denominazione_en;
    private Scheda scheda;
    public RisorsaDb risorsa;
    public String rcp;
    public String originatore;
    public String noFCDL;
    public String classeSnn;
    public String published;
    private String uriPdf;
    private String filename;
    @Index
    public String categoria_farmacologica;

    public String getUriPdf() {
        return uriPdf;
    }

    public void setUriPdf(String uriPdf) {
        this.uriPdf = uriPdf;
    }

    public String getAic() {
        return aic;
    }

    public void setAic(String aic) {
        this.aic = aic;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Scheda getScheda() {
        return scheda;
    }

    public void setScheda(Scheda scheda) {
        this.scheda = scheda;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
