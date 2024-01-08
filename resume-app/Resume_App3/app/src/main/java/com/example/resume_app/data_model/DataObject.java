package com.example.resume_app.data_model;

/**
 * Implemented by data objects to give them a shared type.
 */
public interface DataObject {
    String toHtmlString();

    boolean equals(DataObject dataObject);
}
