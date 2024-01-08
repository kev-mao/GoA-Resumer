package com.example.resume_app.resume_editor;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.resume_app.JsonTools;
import com.example.resume_app.R;
import com.example.resume_app.data_model.Award;
import com.example.resume_app.data_model.Certification;
import com.example.resume_app.data_model.Education;
import com.example.resume_app.data_model.Experience;
import com.example.resume_app.data_model.ResumeData;
import com.example.resume_app.data_model.Skill;
import com.example.resume_app.data_model.UserData;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Displays a print preview of the resume given the appropriate data via Intent:
 * <p>
 * - An int corresponding to the XML template to use,
 * - A String corresponding to the JSON file to load resume data from.
 * <p>
 * Currently NOT null-safe! Incomplete ResumeData will cause a CRASH!
 */
public class ResumePreviewActivity extends AppCompatActivity {

    View template;

    JsonTools jsonTools;
    UserData userData;
    ResumeData resumeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_preview);

        Intent intent = getIntent();
        jsonTools = new JsonTools(this);

        View loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        new Thread(() -> {

            userData = jsonTools.loadUserFromJson();
            resumeData = jsonTools.loadResumeFromJson(intent.getStringExtra("FILE_NAME"));

            runOnUiThread(() -> {

                FrameLayout frame = findViewById(R.id.frame);
                frame.addView(getLayoutInflater().inflate(intent.getIntExtra("TEMPLATE_ID",
                        R.layout.template_resume_classic), frame, false));

                connectXml();
                connectTemplate();

                loader.setVisibility(View.GONE);
            });
        }).start();
    }

    void connectXml() {

        ImageButton buttonShare = findViewById(R.id.button_share);
        buttonShare.setOnClickListener(view -> {

            // create a hidden temporary file
            File file = new File(getCacheDir(), resumeData.fileName + ".pdf");
            renderToPdf(file);
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

            // open the android share sheet
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("application/pdf");

            // the default files app should show up here... why doesn't it?
            // my best guess is that it's an emulator restriction --arthur
            startActivity(Intent.createChooser(shareIntent, null));
        });

        ImageButton buttonDownload = findViewById(R.id.button_download);
        buttonDownload.setOnClickListener(view -> {

            // create a permanent file
            File file = new File(getExternalFilesDir(null), resumeData.fileName + ".pdf");
            renderToPdf(file);

            Toast.makeText(this, R.string.toast_file_saved, Toast.LENGTH_LONG).show();
        });

        ImageButton buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(view -> onBackPressed());
    }

    void connectTemplate() {

        // Regrettable...
        template = findViewById(R.id.template);
        TextView textHeaderName = findViewById(R.id.resume_header_name);
        TextView textIntroduction = findViewById(R.id.resume_introduction);
        TextView textEmail = findViewById(R.id.resume_email);
        TextView textPhone = findViewById(R.id.resume_phone);
        TextView textHeaderExperience = findViewById(R.id.resume_header_experience);
        TextView textExperience = findViewById(R.id.resume_experience);
        TextView textHeaderEducation = findViewById(R.id.resume_header_education);
        TextView textEducation = findViewById(R.id.resume_education);
        TextView textHeaderCertifications = findViewById(R.id.resume_header_certifications);
        TextView textCertifications = findViewById(R.id.resume_certifications);
        TextView textHeaderAwards = findViewById(R.id.resume_header_awards);
        TextView textAwards = findViewById(R.id.resume_awards);
        TextView textHeaderSkills = findViewById(R.id.resume_header_skills);
        TextView textSkills = findViewById(R.id.resume_skills);

        textHeaderName.setText(userData.username);
        textIntroduction.setText(resumeData.introduction);
        textEmail.setText(userData.email);
        textPhone.setText(userData.phone);

        if (resumeData.experience.size() > 0) {
            StringBuilder s = new StringBuilder();
            for (Experience e : resumeData.experience) {
                s.append(e.toHtmlString());
            }
            textExperience.setText(Html.fromHtml(s.toString(), 0));
        } else {
            textHeaderExperience.setVisibility(View.GONE);
            textExperience.setVisibility(View.GONE);
        }

        if (resumeData.education.size() > 0) {
            StringBuilder s = new StringBuilder();
            for (Education e : resumeData.education) {
                s.append(e.toHtmlString());
            }
            textEducation.setText(Html.fromHtml(s.toString(), 0));
        } else {
            textHeaderEducation.setVisibility(View.GONE);
            textEducation.setVisibility(View.GONE);
        }

        if (resumeData.certifications.size() > 0) {
            StringBuilder s = new StringBuilder();
            for (Certification e : resumeData.certifications) {
                s.append(e.toHtmlString());
            }
            textCertifications.setText(Html.fromHtml(s.toString(), 0));
        } else {
            textHeaderCertifications.setVisibility(View.GONE);
            textCertifications.setVisibility(View.GONE);
        }

        if (resumeData.awards.size() > 0) {
            StringBuilder s = new StringBuilder();
            for (Award e : resumeData.awards) {
                s.append(e.toHtmlString());
            }
            textAwards.setText(Html.fromHtml(s.toString(), 0));
        } else {
            textHeaderAwards.setVisibility(View.GONE);
            textAwards.setVisibility(View.GONE);
        }

        if (resumeData.skills.size() > 0) {
            StringBuilder s = new StringBuilder();
            for (Skill e : resumeData.skills) {
                s.append(e.toHtmlString());
            }
            textSkills.setText(Html.fromHtml(s.toString(), 0));
        } else {
            textHeaderSkills.setVisibility(View.GONE);
            textSkills.setVisibility(View.GONE);
        }

        float scaleFactor = Resources.getSystem().getDisplayMetrics().widthPixels / 612f;
        template.setScaleX(scaleFactor);
        template.setScaleY(scaleFactor);
    }

    void renderToPdf(File file) {

        PdfDocument pdfDoc = new PdfDocument();
        PdfDocument.PageInfo pdfInfo = new PdfDocument.PageInfo.Builder(612, 792, 1).create();
        PdfDocument.Page pdf = pdfDoc.startPage(pdfInfo);
        template.draw(pdf.getCanvas());
        pdfDoc.finishPage(pdf);

        try {
            pdfDoc.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        pdfDoc.close();
    }
}