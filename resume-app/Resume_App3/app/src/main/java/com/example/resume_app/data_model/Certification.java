package com.example.resume_app.data_model;

public class Certification implements DataObject {

    public String certificationTitle;
    public String issuer;
    public String issuedOn;
    public String expiryDate;
    public boolean selected;

    public Certification() {
        this.certificationTitle = "";
        this.issuer = "";
        this.issuedOn = "";
        this.expiryDate = "";
        selected = false;
    }

    public String toHtmlString() {
        return "<p><b>" + certificationTitle + "</b><br>" + issuer + "<br><i>" + issuedOn + " - " + expiryDate + "</i></p>";
    }

    public boolean equals(DataObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certification that = (Certification) o;
        return certificationTitle.equals(that.certificationTitle) && issuer.equals(that.issuer) && issuedOn.equals(that.issuedOn) && expiryDate.equals(that.expiryDate);
    }
}
