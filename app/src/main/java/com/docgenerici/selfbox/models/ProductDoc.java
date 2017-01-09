package com.docgenerici.selfbox.models;

/**
 * @author Giuseppe Sorce
 */

public class ProductDoc {


    private String aic;
    private int typerow;
    private String title;
    private String subtitle;
    private String classe;
    private String noinside;
    private String scheda;
    private String rpc;
    private int color= 0;
    private String uriPdf;
    private String uriSchedaPdf;

    public ProductDoc(String aic, int typerow, String title,String subtitle, String classe, String noinside, int color, String scheda, String rpc, String uriPdf, String uriSchedaPdf) {
        this.aic=aic;
        this.typerow=typerow;
        this.title= title;
        this.subtitle= subtitle;
        this.classe= classe;
        this.noinside= noinside;
        this.scheda= scheda;
        this.rpc= rpc;
        this.color= color;
        this.uriPdf= uriPdf;
        this.uriSchedaPdf= uriSchedaPdf;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getScheda() {
        return scheda;
    }

    public void setScheda(String scheda) {
        this.scheda = scheda;
    }

    public String getRpc() {
        return rpc;
    }

    public void setRpc(String rpc) {
        this.rpc = rpc;
    }

    public String getUriPdf() {
        return uriPdf;
    }

    public void setUriPdf(String uriPdf) {
        this.uriPdf = uriPdf;
    }

    public String getUriSchedaPdf() {
        return uriSchedaPdf;
    }

    public void setUriSchedaPdf(String uriSchedaPdf) {
        this.uriSchedaPdf = uriSchedaPdf;
    }

    public String getAic() {
        return aic;
    }

    public void setAic(String aic) {
        this.aic = aic;
    }
}
