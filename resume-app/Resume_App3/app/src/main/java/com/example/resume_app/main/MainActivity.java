package com.example.resume_app.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.resume_app.JsonTools;
import com.example.resume_app.R;
import com.example.resume_app.data_model.UserData;

/**
 * Contains YourResumesFragment and ProfileFragment, and manages a shared UserData between fragments.
 */
public class MainActivity extends AppCompatActivity {

    Fragment currentFragment;

    JsonTools jsonTools;
    static UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jsonTools = new JsonTools(this);

        View loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        new Thread(() -> {

            userData = jsonTools.loadUserFromJson();
            if (userData == null) {

                userData = jsonTools.saveUserToJson(new UserData());
                // maybe let that soak in a little bit...? just for some flair --arthur
                try {
                    Thread.sleep(2500);
                } catch (Exception e) {
                    // do nothing
                }
            }

            runOnUiThread(() -> {
                connectXml();
                loader.setVisibility(View.GONE);
            });
        }).start();
    }

    void connectXml() {

        Fragment yourResumesFragment = new YourResumesFragment();
        Fragment profileFragment = new ProfileFragment();

        TextView title = findViewById(R.id.title);

        Button buttonResumeBuilder = findViewById(R.id.button_resume_builder);
        buttonResumeBuilder.setOnClickListener(view -> {
            title.setText(R.string.title_your_resumes);
            openFragment(yourResumesFragment, YourResumesFragment.ID);
        });

        Button buttonProfile = findViewById(R.id.button_profile);
        buttonProfile.setOnClickListener(view -> {
            title.setText(R.string.title_profile);
            openFragment(profileFragment, ProfileFragment.ID);
        });

        // by default open the your resumes fragment
        buttonResumeBuilder.callOnClick();
    }

    /**
     * Takes care of opening and/or lazily adding fragments to the activity.
     *
     * @param fragment The fragment to open and/or add.
     * @param tag      A String used to identify the fragment.
     */
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

        fragmentTransaction.commit();
        currentFragment = fragment;
    }
}