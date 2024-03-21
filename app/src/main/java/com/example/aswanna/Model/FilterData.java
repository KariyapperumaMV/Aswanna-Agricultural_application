package com.example.aswanna.Model;

import java.io.Serializable;

public class FilterData implements Serializable {
    private String spinner1Value;
    private String spinner2Value;
    private String spinner3Value;

    public void setSpinner2Value(String spinner2Value) {
        this.spinner2Value = spinner2Value;
    }

    public void setSpinner3Value(String spinner3Value) {
        this.spinner3Value = spinner3Value;
    }

    public void setEditText1Value(String editText1Value) {
        this.editText1Value = editText1Value;
    }

    public void setEditText2Value(String editText2Value) {
        this.editText2Value = editText2Value;
    }

    public void setSpinner1Value(String spinner1Value) {
        this.spinner1Value = spinner1Value;
    }

    private String editText1Value;

    public String getSpinner1Value() {
        return spinner1Value;
    }

    public String getSpinner2Value() {
        return spinner2Value;
    }

    public String getSpinner3Value() {
        return spinner3Value;
    }

    public String getEditText1Value() {
        return editText1Value;
    }

    public String getEditText2Value() {
        return editText2Value;
    }

    public FilterData() {
    }

    public FilterData(String spinner1Value, String spinner2Value, String spinner3Value, String editText1Value, String editText2Value) {
        this.spinner1Value = spinner1Value;
        this.spinner2Value = spinner2Value;
        this.spinner3Value = spinner3Value;
        this.editText1Value = editText1Value;
        this.editText2Value = editText2Value;
    }

    private String editText2Value;

    // Constructors, getters, and setters
}
