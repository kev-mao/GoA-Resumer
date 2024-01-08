package com.example.resume_app.data_model;

import java.util.ArrayList;

public class ResumeData {

    public String fileName;
    public String introduction;
    public ArrayList<Award> awards;
    public ArrayList<Certification> certifications;
    public ArrayList<Education> education;
    public ArrayList<Experience> experience;
    public ArrayList<Skill> skills;

    public ResumeData(String fileName) {
        this.fileName = fileName;
        this.introduction = "";
        this.awards = new ArrayList<>();
        this.certifications = new ArrayList<>();
        this.education = new ArrayList<>();
        this.experience = new ArrayList<>();
        this.skills = new ArrayList<>();
    }
}
