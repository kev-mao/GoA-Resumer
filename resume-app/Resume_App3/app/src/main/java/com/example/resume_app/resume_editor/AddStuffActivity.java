package com.example.resume_app.resume_editor;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.resume_app.R;
import com.example.resume_app.data_model.Category;

/**
 * Contains an AddStuffFragment.
 */
public class AddStuffActivity extends AppCompatActivity {

    Fragment currentFragment;

    public static Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stuff);
        category = (Category) getIntent().getSerializableExtra("CATEGORY");
        connectXml();
    }

    void connectXml() {

        Fragment addStuffFragment = new AddStuffFragment();

        TextView title = findViewById(R.id.title);
        switch (category) {
            case EXPERIENCE:
                title.setText(R.string.header_add_experience);
                break;
            case AWARD:
                title.setText(R.string.header_add_awards);
                break;
            case EDUCATION:
                title.setText(R.string.header_add_education);
                break;
            case CERTIFICATION:
                title.setText(R.string.header_add_certifications);
                break;
            case SKILL:
                title.setText(R.string.header_add_skills);
                break;
        }

        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> onBackPressed());

        openFragment(addStuffFragment, AddStuffFragment.ID);
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
