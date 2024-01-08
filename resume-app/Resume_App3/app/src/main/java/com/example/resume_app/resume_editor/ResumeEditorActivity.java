package com.example.resume_app.resume_editor;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.resume_app.JsonTools;
import com.example.resume_app.R;
import com.example.resume_app.data_model.ResumeData;
import com.example.resume_app.data_model.UserData;

/**
 * Contains a ResumeEditorActivity, and manages a shared UserData and ResumeData between fragments.
 */
public class ResumeEditorActivity extends AppCompatActivity {

    Fragment currentFragment;

    JsonTools jsonTools;
    public static UserData userData;
    public static ResumeData resumeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_editor);

        jsonTools = new JsonTools(this);

        View loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        new Thread(() -> {

            userData = jsonTools.loadUserFromJson();
            resumeData = jsonTools.loadResumeFromJson(getIntent().getStringExtra("FILE_NAME"));

            runOnUiThread(() -> {
                connectXml();
                loader.setVisibility(View.GONE);
            });
        }).start();
    }

    void connectXml() {

        Fragment resumeEditorFragment = new ResumeEditorFragment();

        TextView title = findViewById(R.id.title);
        title.setText(resumeData.fileName);

        ImageButton buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(view -> onBackPressed());

        openFragment(resumeEditorFragment, ResumeEditorFragment.ID);
    }

    void openFragment(Fragment fragment, String tag) {

        if (fragment == currentFragment) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        // hide whatever is on screen right now
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        // if the fragment has already been added to the activity, show it
        Fragment f = fragmentManager.findFragmentByTag(tag);
        if (f != null) {
            fragmentTransaction.show(f);
        }

        // otherwise add it to the activity
        else {
            fragmentTransaction.add(R.id.frame, fragment, tag);
        }

        fragmentTransaction.commitNow();
        currentFragment = fragment;
    }
}



