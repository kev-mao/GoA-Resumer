package com.example.resume_app;

import android.content.Context;

import com.example.resume_app.data_model.ResumeData;
import com.example.resume_app.data_model.UserData;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Locale;

/**
 * Helper class to keep all JSON saving- /loading-related functions in one place.
 * Also, we use .nfteam as a file extension. How proprietary! :] --arthur
 */
public class JsonTools {

    static final String USER_FILE_NAME = "user_data";
    static final String FILE_EXTENSION = ".nfteam";

    Context context;
    Gson gson;

    public JsonTools(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    /**
     * @param userData The UserData object to save.
     * @return The same UserData that was passed in.
     */
    public UserData saveUserToJson(UserData userData) {
        File file = new File(context.getExternalFilesDir(null), USER_FILE_NAME + FILE_EXTENSION);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(userData, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userData;
    }

    /**
     * @return The UserData that was loaded, or null if the file was not found.
     */
    public UserData loadUserFromJson() {
        File file = new File(context.getExternalFilesDir(null), USER_FILE_NAME + FILE_EXTENSION);
        if (!file.exists() || file.length() == 0) {
            return null;
        }

        UserData u = null;

        try (FileReader reader = new FileReader(file)) {
            u = gson.fromJson(reader, UserData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

    /**
     * @param resumeData The ResumeData object to save.
     * @return The same ResumeData that was passed in.
     */
    public ResumeData saveResumeToJson(ResumeData resumeData) {
        String formattedFileName = resumeData.fileName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");

        File file = new File(context.getExternalFilesDir(null), formattedFileName + FILE_EXTENSION);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(resumeData, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resumeData;
    }

    /**
     * @param fileName The name of the JSON file to load.
     * @return The ResumeData that was loaded.
     */
    public ResumeData loadResumeFromJson(String fileName) {
        String formattedFileName = fileName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");

        File file = new File(context.getExternalFilesDir(null), formattedFileName + FILE_EXTENSION);
        ResumeData r = null;

        try (FileReader reader = new FileReader(file)) {
            r = gson.fromJson(reader, ResumeData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

    /**
     * @param fileName The name of the JSON file to delete.
     * @return Whether the file was deleted successfully or not.
     */
    public boolean deleteJson(String fileName) {
        String formattedFileName = fileName.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");

        File file = new File(context.getExternalFilesDir(null), formattedFileName + FILE_EXTENSION);
        return file.delete();
    }
}
