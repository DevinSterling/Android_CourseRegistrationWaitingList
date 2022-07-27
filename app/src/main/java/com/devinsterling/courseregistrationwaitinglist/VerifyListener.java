package com.devinsterling.courseregistrationwaitinglist;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

/*
    Input validation for textEdit fields
*/
public class VerifyListener implements View.OnFocusChangeListener {
    private final int minLength;
    private final String regex;
    private final String patternWarning;

    public VerifyListener(int minLength, String regex, String patternWarning) {
        this.minLength = minLength;
        this.regex = regex;
        this.patternWarning = patternWarning;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        /* Validate input when the field is no longer focused */
        if (!hasFocus) {
            TextInputLayout parent = (TextInputLayout) v.getParent().getParent(); // Get layout
            String input = parent.getEditText().getText().toString(); // Get string

            /* Check if string is empty */
            if (input.isEmpty()) {
                parent.setError(App.getResource().getString(R.string.required, parent.getHint()));
            }
            /* Check if string does not match requested pattern */
            else if (!input.matches(regex)) {
                parent.setError(patternWarning);
            }
            /* Check if string is less than minimum length */
            else if (input.length() < minLength) {
                parent.setError(App.getResource().getString(R.string.required_length, parent.getHint(), minLength));
            }
            else {
                parent.setError(null); // Passes validation
            }
        }
    }
}

