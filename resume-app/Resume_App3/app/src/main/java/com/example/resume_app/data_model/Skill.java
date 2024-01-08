package com.example.resume_app.data_model;

public class Skill implements DataObject {

    public String skillName;
    public boolean selected;

    public Skill() {
        this.skillName = "";
        selected = false;
    }

    public String toHtmlString() {
        return "<p>" + skillName + "</p>";
    }

    public boolean equals(DataObject o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return skillName.equals(skill.skillName);
    }
}
