package com.ism.Infrastructure;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;

import com.andreabaccega.widget.FormEditText;

public class FormTextWatcher implements TextWatcher {

    private FormEditText field; // field which is associated with text wather
    private ImageView pdf; // pdf icon (optional)
    private ImageView www; // web icon (optional)

    public FormTextWatcher(FormEditText field) {
        this.field = field; // initialize field
    }

    // initialization, when used with pdf and web icon
    public FormTextWatcher(FormEditText field, ImageView pdf, ImageView www) {
        this.field = field;
        this.pdf = pdf;
        this.www = www;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (pdf != null) {
            // when website field option with enable/disable icons
            if (field.testValidity()) {
                pdf.setEnabled(true);
                www.setEnabled(true);
            } else {
                pdf.setEnabled(false);
                www.setEnabled(false);
            }
        } else {
            // normal data field
            field.testValidity();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
