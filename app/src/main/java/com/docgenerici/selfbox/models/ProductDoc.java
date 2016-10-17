package com.docgenerici.selfbox.models;

/**
 * @author Giuseppe Sorce
 */

public class ProductDoc {


    private int typerow;
    private String title;
    private String subtitle;
    private String classe;
    private String noinside;
    public ProductDoc(int typerow, String title,String subtitle, String classe, String noinside) {
        this.typerow=typerow;
        this.title= title;
        this.subtitle= subtitle;
        this.classe= classe;
        this.noinside= noinside;
    }

    public ProductDoc(int typerow, String title) {
        this.typerow=typerow;
        this.title= title;
    }

    public int getTyperow() {
        return typerow;
    }

    public void setTyperow(int typerow) {
        this.typerow = typerow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getNoinside() {
        return noinside;
    }

    public void setNoinside(String noinside) {
        this.noinside = noinside;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
