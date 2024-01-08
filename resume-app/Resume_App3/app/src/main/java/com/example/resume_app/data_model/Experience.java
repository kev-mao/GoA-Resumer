package com.example.resume_app.data_model;

public class Experience implements DataObject {

    public String jobPosition;
    public String companyName;
    public String description;
    public String startDate;
    public String endDate;
    public boolean selected;

    public Experience() {
        this.jobPosition = "";
        this.companyName = "";
        this.description = "";
        this.startDate = "";
        this.endDate = "";
        selected = false;
    }

    public String toHtmlString() {
        return "<p><b>" + jobPosition + " • " + companyName + "</b><br>" + description + "<br><i>" + startDate + " - " + endDate + "</i></p>";
    }

    public boolean equals(DataObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return jobPosition.equals(that.jobPosition) && companyName.equals(that.companyName) && description.equals(that.description) && startDate.equals(that.startDate) && endDate.equals(that.endDate);
    }
}
