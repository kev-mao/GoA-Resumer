package com.example.resume_app.data_model;

public class Education implements DataObject {

    public String schoolName;
    public String description;
    public String startDate;
    public String endDate;
    public boolean selected;

    public Education() {
        this.schoolName = "";
        this.description = "";
        this.startDate = "";
        this.endDate = "";
        selected = false;
    }

    public String toHtmlString() {
        return "<p><b>" + schoolName + "</b><br>" + description + "<br><i>" + startDate + " - " + endDate + "</i></p>";
    }

    public boolean equals(DataObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Education education = (Education) o;
        return schoolName.equals(education.schoolName) && description.equals(education.description) && startDate.equals(education.startDate) && endDate.equals(education.endDate);
    }
}
