package com.example.resume_app.resume_editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.resume_app.JsonTools;
import com.example.resume_app.R;
import com.example.resume_app.data_model.Award;
import com.example.resume_app.data_model.Category;
import com.example.resume_app.data_model.Certification;
import com.example.resume_app.data_model.Education;
import com.example.resume_app.data_model.Experience;
import com.example.resume_app.data_model.ResumeData;
import com.example.resume_app.data_model.Skill;
import com.example.resume_app.data_model.UserData;
import com.google.android.material.card.MaterialCardView;

/**
 * Selects items from UserData and adds them to ResumeData.
 */
public class AddStuffFragment extends Fragment {

    public static final String ID = "RESUME_EDITOR_ADD";

    JsonTools jsonTools;
    UserData userData = ResumeEditorActivity.userData;
    ResumeData resumeData = ResumeEditorActivity.resumeData;
    Category category = AddStuffActivity.category;

    LinearLayout experienceLinearLayout;
    LinearLayout awardsLinearLayout;
    LinearLayout educationLinearLayout;
    LinearLayout certificationsLinearLayout;
    LinearLayout skillsLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_stuff, container, false);
        jsonTools = new JsonTools(getContext());
        connectXml(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        jsonTools.saveResumeToJson(resumeData);
    }

    void connectXml(View view) {
        experienceLinearLayout = view.findViewById(R.id.experience_linear_layout);
        awardsLinearLayout = view.findViewById(R.id.awards_linear_layout);
        educationLinearLayout = view.findViewById(R.id.education_linear_layout);
        certificationsLinearLayout = view.findViewById(R.id.certifications_linear_layout);
        skillsLinearLayout = view.findViewById(R.id.skills_linear_layout);

        switch (category) {
            case EXPERIENCE:
                for (Experience experience : userData.experience) {
                    for (Experience resumeExperience : resumeData.experience) {
                        if (resumeExperience.equals(experience)) {
                            experience.selected = true;
                            break;
                        }
                    }
                    createExperienceCard(experience);
                }
                break;
            case AWARD:
                for (Award award : userData.awards) {
                    for (Award resumeAward : resumeData.awards) {
                        if (resumeAward.equals(award)) {
                            award.selected = true;
                            break;
                        }
                    }
                    createAwardCard(award);
                }
                break;
            case EDUCATION:
                for (Education education : userData.education) {
                    for (Education resumeEducation : resumeData.education) {
                        if (resumeEducation.equals(education)) {
                            education.selected = true;
                            break;
                        }
                    }
                    createEducationCard(education);
                }
                break;
            case CERTIFICATION:
                for (Certification certification : userData.certifications) {
                    for (Certification resumeCertification : resumeData.certifications) {
                        if (resumeCertification.equals(certification)) {
                            certification.selected = true;
                            break;
                        }
                    }
                    createCertificationCard(certification);
                }
                break;
            case SKILL:
                for (Skill skill : userData.skills) {
                    for (Skill resumeSkill : resumeData.skills) {
                        if (resumeSkill.equals(skill)) {
                            skill.selected = true;
                            break;
                        }
                    }
                    createSkillCard(skill);
                }
                break;
        }
    }

    private void createExperienceCard(Experience experience) {
        View card = getLayoutInflater().inflate(R.layout.item_experience_cardview, experienceLinearLayout, false);
        experienceLinearLayout.addView(card);

        MaterialCardView cardView = card.findViewById(R.id.experience_material_cardview);
        TextView positionTitleTextView = card.findViewById(R.id.position_title_textview);
        TextView organizationNameTextView = card.findViewById(R.id.organization_name_textview);
        TextView experienceDescriptionTextView = card.findViewById(R.id.experience_description_textview);
        TextView jobStartEndDateTextView = card.findViewById(R.id.job_start_end_date_textview);

        positionTitleTextView.setText(experience.jobPosition);
        organizationNameTextView.setText(experience.companyName);
        experienceDescriptionTextView.setText(experience.description);
        jobStartEndDateTextView.setText(getString(R.string.start_date_to_end_date, experience.startDate, experience.endDate));

        ImageButton experienceDeleteButton = card.findViewById(R.id.experience_delete_button);
        experienceDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
        experienceDeleteButton.setOnClickListener(v -> card.callOnClick());

        if (experience.selected) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
            experienceDeleteButton.setBackgroundResource(R.drawable.checkmark);
        }

        card.setOnClickListener(v -> {
            if (experience.selected) {
                experienceDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                experience.selected = false;
                for (Experience resumeExperience : resumeData.experience) {
                    if (resumeExperience.equals(experience)) {
                        resumeData.experience.remove(resumeExperience);
                        break;
                    }
                }
            } else {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
                experienceDeleteButton.setBackgroundResource(R.drawable.checkmark);
                experience.selected = true;
                resumeData.experience.add(experience);
            }
        });
    }

    private void createAwardCard(Award award) {
        View card = getLayoutInflater().inflate(R.layout.item_awards_cardview, awardsLinearLayout, false);
        awardsLinearLayout.addView(card);

        MaterialCardView cardView = card.findViewById(R.id.award_material_cardview);
        TextView awardTitleTextView = card.findViewById(R.id.award_title_textview);
        TextView awardIssuerNameTextView = card.findViewById(R.id.award_issuer_name_textview);
        TextView awardDescriptionTextView = card.findViewById(R.id.award_description_textview);
        TextView awardedDateTextView = card.findViewById(R.id.awarded_date_textview);

        awardTitleTextView.setText(award.awardName);
        awardIssuerNameTextView.setText(award.issuer);
        awardDescriptionTextView.setText(award.description);
        awardedDateTextView.setText(award.dateAwarded);

        ImageButton awardDeleteButton = card.findViewById(R.id.award_delete_button);
        awardDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
        awardDeleteButton.setOnClickListener(v -> card.callOnClick());

        if (award.selected) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
            awardDeleteButton.setBackgroundResource(R.drawable.checkmark);
        }

        card.setOnClickListener(v -> {
            if (award.selected) {
                awardDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                award.selected = false;
                for (Award resumeAward : resumeData.awards) {
                    if (resumeAward.equals(award)) {
                        resumeData.awards.remove(resumeAward);
                        break;
                    }
                }
            } else {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
                awardDeleteButton.setBackgroundResource(R.drawable.checkmark);
                award.selected = true;
                resumeData.awards.add(award);
            }
        });
    }

    private void createEducationCard(Education education) {
        View card = getLayoutInflater().inflate(R.layout.item_education_cardview, educationLinearLayout, false);
        educationLinearLayout.addView(card);

        MaterialCardView cardView = card.findViewById(R.id.education_material_cardview);
        TextView schoolNameTextView = card.findViewById(R.id.school_name_textview);
        TextView educationDescriptionTextView = card.findViewById(R.id.education_description_textview);
        TextView educationStartEndDateTextView = card.findViewById(R.id.education_start_end_date_textview);

        schoolNameTextView.setText(education.schoolName);
        educationDescriptionTextView.setText(education.description);
        educationStartEndDateTextView.setText(getString(R.string.start_date_to_end_date, education.startDate, education.endDate));

        ImageButton educationDeleteButton = card.findViewById(R.id.education_delete_button);
        educationDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
        educationDeleteButton.setOnClickListener(v -> card.callOnClick());

        if (education.selected) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
            educationDeleteButton.setBackgroundResource(R.drawable.checkmark);
        }

        card.setOnClickListener(v -> {
            if (education.selected) {
                educationDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                education.selected = false;
                for (Education resumeEducation : resumeData.education) {
                    if (resumeEducation.equals(education)) {
                        resumeData.education.remove(resumeEducation);
                        break;
                    }
                }
            } else {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
                educationDeleteButton.setBackgroundResource(R.drawable.checkmark);
                education.selected = true;
                resumeData.education.add(education);
            }
        });
    }

    private void createCertificationCard(Certification certification) {
        View card = getLayoutInflater().inflate(R.layout.item_certifications_cardview, certificationsLinearLayout, false);
        certificationsLinearLayout.addView(card);

        MaterialCardView cardView = card.findViewById(R.id.certification_material_cardview);
        TextView certificationTitleTextView = card.findViewById(R.id.certification_title_textview);
        TextView certificationIssuerNameTextView = card.findViewById(R.id.certification_issuer_name_textview);
        TextView issuedDateTextView = card.findViewById(R.id.certification_issued_date_textview);

        certificationTitleTextView.setText(certification.certificationTitle);
        certificationIssuerNameTextView.setText(certification.issuer);
        issuedDateTextView.setText(getString(R.string.issued_date_to_expiry_date, certification.issuedOn, certification.expiryDate));

        ImageButton certificationDeleteButton = card.findViewById(R.id.certification_delete_button);
        certificationDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
        certificationDeleteButton.setOnClickListener(v -> card.callOnClick());

        if (certification.selected) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
            certificationDeleteButton.setBackgroundResource(R.drawable.checkmark);
        }

        card.setOnClickListener(v -> {
            if (certification.selected) {
                certificationDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                certification.selected = false;
                for (Certification resumeCertification : resumeData.certifications) {
                    if (resumeCertification.equals(certification)) {
                        resumeData.certifications.remove(resumeCertification);
                        break;
                    }
                }
            } else {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
                certificationDeleteButton.setBackgroundResource(R.drawable.checkmark);
                certification.selected = true;
                resumeData.certifications.add(certification);
            }
        });
    }

    private void createSkillCard(Skill skill) {
        View card = getLayoutInflater().inflate(R.layout.item_skills_cardview, skillsLinearLayout, false);
        skillsLinearLayout.addView(card);

        MaterialCardView cardView = card.findViewById(R.id.skill_material_cardview);
        TextView skillNameTextView = card.findViewById(R.id.skill_name_textview);

        skillNameTextView.setText(skill.skillName);

        ImageButton skillDeleteButton = card.findViewById(R.id.skill_delete_button);
        skillDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
        skillDeleteButton.setOnClickListener(v -> card.callOnClick());

        if (skill.selected) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
            skillDeleteButton.setBackgroundResource(R.drawable.checkmark);
        }

        card.setOnClickListener(v -> {
            if (skill.selected) {
                skillDeleteButton.setBackgroundResource(R.drawable.ic_baseline_add_24);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                skill.selected = false;
                for (Skill resumeSkill : resumeData.skills) {
                    if (resumeSkill.equals(skill)) {
                        resumeData.skills.remove(resumeSkill);
                        break;
                    }
                }
            } else {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
                skillDeleteButton.setBackgroundResource(R.drawable.checkmark);
                skill.selected = true;
                resumeData.skills.add(skill);
            }
        });
    }
}