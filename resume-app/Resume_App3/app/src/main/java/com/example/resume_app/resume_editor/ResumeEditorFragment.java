package com.example.resume_app.resume_editor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

/**
 * Displays a list of selected items from ResumeData.
 */
public class ResumeEditorFragment extends Fragment {

    public static final String ID = "RESUME_EDITOR";

    JsonTools jsonTools;
    UserData userData = ResumeEditorActivity.userData;
    ResumeData resumeData = ResumeEditorActivity.resumeData;

    LinearLayout experienceLinearLayout;
    LinearLayout awardsLinearLayout;
    LinearLayout educationLinearLayout;
    LinearLayout certificationsLinearLayout;
    LinearLayout skillsLinearLayout;

    Dialog editIntroductionDialog;
    Dialog confirmEraseProgressDialog;

    TextView introductionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resume_editor_lists, container, false);
        jsonTools = new JsonTools(getContext());
        connectXml(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        jsonTools.saveResumeToJson(resumeData);
    }

    @Override
    public void onResume() {
        super.onResume();

        experienceLinearLayout.removeAllViews();
        awardsLinearLayout.removeAllViews();
        educationLinearLayout.removeAllViews();
        certificationsLinearLayout.removeAllViews();
        skillsLinearLayout.removeAllViews();

        for (Experience experience : resumeData.experience) {
            createExperienceCard(experience);
        }

        for (Award award : resumeData.awards) {
            createAwardCard(award);
        }

        for (Education education : resumeData.education) {
            createEducationCard(education);
        }

        for (Certification certification : resumeData.certifications) {
            createCertificationCard(certification);
        }

        for (Skill skill : resumeData.skills) {
            createSkillCard(skill);
        }
    }

    void connectXml(View view) {

        experienceLinearLayout = view.findViewById(R.id.experience_linear_layout);
        awardsLinearLayout = view.findViewById(R.id.awards_linear_layout);
        educationLinearLayout = view.findViewById(R.id.education_linear_layout);
        certificationsLinearLayout = view.findViewById(R.id.certifications_linear_layout);
        skillsLinearLayout = view.findViewById(R.id.skills_linear_layout);

        editIntroductionDialog = new Dialog(getContext());
        confirmEraseProgressDialog = new Dialog(getContext());

        introductionTextView = view.findViewById(R.id.introduction_textview);

        if (resumeData.introduction.length() == 0) {
            introductionTextView.setText(R.string.introduction);
        } else {
            introductionTextView.setText(resumeData.introduction);
        }

        ImageButton introductionEditButton = view.findViewById(R.id.introduction_edit_button);
        introductionEditButton.setOnClickListener(v -> openEditIntroductionDialog(resumeData.introduction));

        Button buttonPreview = view.findViewById(R.id.button_preview);
        buttonPreview.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ResumePreviewActivity.class);
            intent.putExtra("FILE_NAME", resumeData.fileName);
            intent.putExtra("TEMPLATE_ID", R.layout.template_resume_classic);
            startActivity(intent);
        });

        // Experience section of the profile

        ImageButton experienceAddButton = view.findViewById(R.id.experience_add_button);
        experienceAddButton.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddStuffActivity.class);
            i.putExtra("CATEGORY", Category.EXPERIENCE);
            startActivity(i);
        });

        // Awards section of the profile

        ImageButton awardsAddButton = view.findViewById(R.id.awards_add_button);
        awardsAddButton.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddStuffActivity.class);
            i.putExtra("CATEGORY", Category.AWARD);
            startActivity(i);
        });

        // Education section of the profile

        ImageButton educationAddButton = view.findViewById(R.id.education_add_button);
        educationAddButton.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddStuffActivity.class);
            i.putExtra("CATEGORY", Category.EDUCATION);
            startActivity(i);
        });

        // Certifications section of the profile

        ImageButton certificationsAddButton = view.findViewById(R.id.certifications_add_button);
        certificationsAddButton.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddStuffActivity.class);
            i.putExtra("CATEGORY", Category.CERTIFICATION);
            startActivity(i);
        });

        // Skills section of the profile

        ImageButton skillsAddButton = view.findViewById(R.id.skills_add_button);
        skillsAddButton.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddStuffActivity.class);
            i.putExtra("CATEGORY", Category.SKILL);
            startActivity(i);
        });
    }

    private void createExperienceCard(Experience experience) {
        View card = getLayoutInflater().inflate(R.layout.item_experience_cardview, experienceLinearLayout, false);
        experienceLinearLayout.addView(card);

        TextView positionTitleTextView = card.findViewById(R.id.position_title_textview);
        TextView organizationNameTextView = card.findViewById(R.id.organization_name_textview);
        TextView experienceDescriptionTextView = card.findViewById(R.id.experience_description_textview);
        TextView jobStartEndDateTextView = card.findViewById(R.id.job_start_end_date_textview);

        positionTitleTextView.setText(experience.jobPosition);
        organizationNameTextView.setText(experience.companyName);
        experienceDescriptionTextView.setText(experience.description);
        jobStartEndDateTextView.setText(getString(R.string.start_date_to_end_date, experience.startDate, experience.endDate));

        ImageButton experienceDeleteButton = card.findViewById(R.id.experience_delete_button);

        for (Experience userExperience : userData.experience) {
            if (userExperience.equals(experience)) {
                experienceDeleteButton.setVisibility(View.GONE);
                return;
            }
        }

        TextView textWarn = card.findViewById(R.id.not_found_warning);
        MaterialCardView cardView = card.findViewById(R.id.experience_material_cardview);

        textWarn.setVisibility(View.VISIBLE);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.yellow));

        experienceDeleteButton.setOnClickListener(v -> confirmEraseCardDialog(resumeData.experience,
                experience, experienceLinearLayout, card));
    }

    private void createAwardCard(Award award) {
        View card = getLayoutInflater().inflate(R.layout.item_awards_cardview, awardsLinearLayout, false);
        awardsLinearLayout.addView(card);

        TextView awardTitleTextView = card.findViewById(R.id.award_title_textview);
        TextView awardIssuerNameTextView = card.findViewById(R.id.award_issuer_name_textview);
        TextView awardDescriptionTextView = card.findViewById(R.id.award_description_textview);
        TextView awardedDateTextView = card.findViewById(R.id.awarded_date_textview);

        awardTitleTextView.setText(award.awardName);
        awardIssuerNameTextView.setText(award.issuer);
        awardDescriptionTextView.setText(award.description);
        awardedDateTextView.setText(award.dateAwarded);

        ImageButton awardDeleteButton = card.findViewById(R.id.award_delete_button);

        for (Award userAward : userData.awards) {
            if (userAward.equals(award)) {
                awardDeleteButton.setVisibility(View.GONE);
                return;
            }
        }

        TextView textWarn = card.findViewById(R.id.not_found_warning);
        MaterialCardView cardView = card.findViewById(R.id.award_material_cardview);

        textWarn.setVisibility(View.VISIBLE);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.yellow));

        awardDeleteButton.setOnClickListener(v -> confirmEraseCardDialog(resumeData.awards,
                award, awardsLinearLayout, card));
    }

    private void createEducationCard(Education education) {
        View card = getLayoutInflater().inflate(R.layout.item_education_cardview, educationLinearLayout, false);
        educationLinearLayout.addView(card);

        TextView schoolNameTextView = card.findViewById(R.id.school_name_textview);
        TextView educationDescriptionTextView = card.findViewById(R.id.education_description_textview);
        TextView educationStartEndDateTextView = card.findViewById(R.id.education_start_end_date_textview);

        schoolNameTextView.setText(education.schoolName);
        educationDescriptionTextView.setText(education.description);
        educationStartEndDateTextView.setText(getString(R.string.start_date_to_end_date, education.startDate, education.endDate));

        ImageButton educationDeleteButton = card.findViewById(R.id.education_delete_button);

        for (Education userEducation : userData.education) {
            if (userEducation.equals(education)) {
                educationDeleteButton.setVisibility(View.GONE);
                return;
            }
        }

        TextView textWarn = card.findViewById(R.id.not_found_warning);
        MaterialCardView cardView = card.findViewById(R.id.education_material_cardview);

        textWarn.setVisibility(View.VISIBLE);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.yellow));

        educationDeleteButton.setOnClickListener(v -> confirmEraseCardDialog(resumeData.education,
                education, educationLinearLayout, card));
    }

    private void createCertificationCard(Certification certification) {
        View card = getLayoutInflater().inflate(R.layout.item_certifications_cardview, certificationsLinearLayout, false);
        certificationsLinearLayout.addView(card);

        TextView certificationTitleTextView = card.findViewById(R.id.certification_title_textview);
        TextView certificationIssuerNameTextView = card.findViewById(R.id.certification_issuer_name_textview);
        TextView issuedDateTextView = card.findViewById(R.id.certification_issued_date_textview);

        certificationTitleTextView.setText(certification.certificationTitle);
        certificationIssuerNameTextView.setText(certification.issuer);
        issuedDateTextView.setText(getString(R.string.issued_date_to_expiry_date, certification.issuedOn, certification.expiryDate));

        ImageButton certificationDeleteButton = card.findViewById(R.id.certification_delete_button);

        for (Certification userCertification : userData.certifications) {
            if (userCertification.equals(certification)) {
                certificationDeleteButton.setVisibility(View.GONE);
                return;
            }
        }

        TextView textWarn = card.findViewById(R.id.not_found_warning);
        MaterialCardView cardView = card.findViewById(R.id.certification_material_cardview);

        textWarn.setVisibility(View.VISIBLE);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.yellow));

        certificationDeleteButton.setOnClickListener(v -> confirmEraseCardDialog(resumeData.certifications,
                certification, certificationsLinearLayout, card));
    }

    private void createSkillCard(Skill skill) {
        View card = getLayoutInflater().inflate(R.layout.item_skills_cardview, skillsLinearLayout, false);
        skillsLinearLayout.addView(card);

        TextView skillNameTextView = card.findViewById(R.id.skill_name_textview);

        skillNameTextView.setText(skill.skillName);

        ImageButton skillDeleteButton = card.findViewById(R.id.skill_delete_button);

        for (Skill userSkill : userData.skills) {
            if (userSkill.equals(skill)) {
                skillDeleteButton.setVisibility(View.GONE);
                return;
            }
        }

        TextView textWarn = card.findViewById(R.id.not_found_warning);
        MaterialCardView cardView = card.findViewById(R.id.skill_material_cardview);

        textWarn.setVisibility(View.VISIBLE);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.yellow));

        skillDeleteButton.setOnClickListener(v -> confirmEraseCardDialog(resumeData.skills,
                skill, skillsLinearLayout, card));
    }

    void openEditIntroductionDialog(String introduction) {
        editIntroductionDialog.setCancelable(false);
        editIntroductionDialog.setContentView(R.layout.dialog_edit_introduction);
        editIntroductionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editIntroductionDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        EditText editText = editIntroductionDialog.findViewById(R.id.edittext_intro);

        editText.setText(introduction);

        Button tips = editIntroductionDialog.findViewById(R.id.button_not_sure);
        ImageButton close = editIntroductionDialog.findViewById(R.id.close_edit_intro_button);
        Button save = editIntroductionDialog.findViewById(R.id.intro_save_button);

        tips.setOnClickListener(v -> introductionWritingTipsDialog());
        close.setOnClickListener(v -> confirmEraseProgressDialog(editIntroductionDialog));

        save.setOnClickListener(v -> {
            if (editText.getText().length() == 0) {
                editText.setError(getString(R.string.error_fill_field));
                return;
            }

            resumeData.introduction = editText.getText().toString();

            introductionTextView.setText(resumeData.introduction);

            editIntroductionDialog.dismiss();
        });

        editIntroductionDialog.show();
    }

    private void confirmEraseProgressDialog(Dialog originalDialog) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View alert = getLayoutInflater().inflate(R.layout.dialog_confirm_erase_progress, null);
        dialogBuilder.setView(alert);
        AlertDialog dialog = dialogBuilder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button continueButton = alert.findViewById(R.id.continue_working);
        continueButton.setOnClickListener(v -> dialog.dismiss());

        Button eraseButton = alert.findViewById(R.id.erase_progress);
        eraseButton.setOnClickListener(v -> {
            originalDialog.dismiss();
            dialog.dismiss();
        });
    }

    private void confirmEraseCardDialog(ArrayList arrayListOfObject, Object objectToDelete, LinearLayout linearLayoutOfCard, View cardToDelete) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View alert = getLayoutInflater().inflate(R.layout.dialog_confirm_erase_card, null);
        dialogBuilder.setView(alert);
        AlertDialog dialog = dialogBuilder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancelDelete = alert.findViewById(R.id.cancel_delete);
        cancelDelete.setOnClickListener(v -> dialog.dismiss());

        Button eraseButton = alert.findViewById(R.id.erase_card);
        eraseButton.setOnClickListener(v -> {
            arrayListOfObject.remove(objectToDelete);
            linearLayoutOfCard.removeView(cardToDelete);
            dialog.dismiss();
        });
    }

    void introductionWritingTipsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View alert = getLayoutInflater().inflate(R.layout.dialog_introduction_writing_tips, null);
        dialogBuilder.setView(alert);
        AlertDialog dialog = dialogBuilder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button continueButton = alert.findViewById(R.id.button_continue);
        continueButton.setOnClickListener(v -> dialog.dismiss());
    }
}