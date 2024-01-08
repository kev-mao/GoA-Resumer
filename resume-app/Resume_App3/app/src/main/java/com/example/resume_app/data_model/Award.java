package com.example.resume_app.data_model;

public class Award implements DataObject {

    public String awardName;
    public String issuer;
    public String description;
    public String dateAwarded;
    public boolean selected;

    public Award() {
        this.awardName = "";
        this.issuer = "";
        this.description = "";
        this.dateAwarded = "";
        selected = false;
    }

    public String toHtmlString() {
        return "<p><b>" + awardName + "</b><br>" + issuer + "<br>" + description + "<br><i>" + dateAwarded + "</i></p>";
    }

    public boolean equals(DataObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Award award = (Award) o;
        return awardName.equals(award.awardName) && issuer.equals(award.issuer) && description.equals(award.description) && dateAwarded.equals(award.dateAwarded);
    }
}
