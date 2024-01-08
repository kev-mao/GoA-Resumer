package com.example.resume_app.main;

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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resume_app.JsonTools;
import com.example.resume_app.R;
import com.example.resume_app.data_model.ResumeData;
import com.example.resume_app.data_model.UserData;
import com.example.resume_app.resume_editor.ResumeEditorActivity;

/**
 * Displays a list of existing resumes from UserData and creates new ones.
 */
public class YourResumesFragment extends Fragment implements YourResumesRecyclerAdapter.IClickListener {

    static final String ID = "YOUR_RESUMES";

    JsonTools jsonTools;
    UserData data = MainActivity.userData;

    YourResumesRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_your_resumes, container, false);
        connectXml(view);
        jsonTools = new JsonTools(getContext());
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        new JsonTools(getContext()).saveUserToJson(data);
    }

    void connectXml(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new YourResumesRecyclerAdapter(getContext(), data.resumeFiles, this);
        recyclerView.setAdapter(adapter);

        Button buttonPlus = view.findViewById(R.id.button_create_resume);
        buttonPlus.setOnClickListener(v -> openCreateResumeDialog());
    }

    void openCreateResumeDialog() {

        Dialog d = new Dialog(getContext());
        d.setCancelable(false);
        d.setContentView(R.layout.dialog_create_resume);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        EditText nameEditText = d.findViewById(R.id.edittext_resume_name);

        ImageButton closeButton = d.findViewById(R.id.close_create_resume_button);
        closeButton.setOnClickListener(v -> d.dismiss());

        Button saveButton = d.findViewById(R.id.resume_name_save_button);
        saveButton.setOnClickListener(v -> {

            String fileName = nameEditText.getText().toString().trim();

            if (fileName.length() == 0) {
                nameEditText.setError(getString(R.string.error_fill_field));
                return;
            }

            if (data.resumeFiles.stream().anyMatch(f -> f.replaceAll("\\s+", "_")
                    .equalsIgnoreCase(fileName.replaceAll("\\s+", "_")))) {
                nameEditText.setError(getString(R.string.error_already_exists));
                return;
            }

            data.resumeFiles.add(fileName);
            ResumeData resumeData = new ResumeData(fileName);
            jsonTools.saveResumeToJson(resumeData);

            d.dismiss();

            Intent intent = new Intent(getContext(), ResumeEditorActivity.class);
            intent.putExtra("FILE_NAME", fileName);
            startActivity(intent);
        });

        d.show();
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(getContext(), ResumeEditorActivity.class);
        intent.putExtra("FILE_NAME", data.resumeFiles.get(position));
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(View view, int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View alert = getLayoutInflater().inflate(R.layout.dialog_confirm_erase_card, null);
        dialogBuilder.setView(alert);
        AlertDialog dialog = dialogBuilder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancelDelete = alert.findViewById(R.id.cancel_delete);
        cancelDelete.setOnClickListener(v -> dialog.dismiss());

        Button eraseButton = alert.findViewById(R.id.erase_card);
        eraseButton.setOnClickListener(v -> {

            if (jsonTools.deleteJson(data.resumeFiles.get(position))) {
                data.resumeFiles.remove(data.resumeFiles.get(position));
                adapter.notifyItemRemoved(position);
            } else {
                Toast.makeText(getContext(), R.string.error_generic, Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
        });
    }
}