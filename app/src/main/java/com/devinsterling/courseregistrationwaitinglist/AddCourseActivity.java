package com.devinsterling.courseregistrationwaitinglist;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.devinsterling.courseregistrationwaitinglist.database.DBContract;
import com.devinsterling.courseregistrationwaitinglist.database.DBHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Objects;

public class AddCourseActivity extends AppCompatActivity {
    private Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close); // Change Icon
        setTitle(R.string.add_course); // Set Title

        returnIntent = new Intent(this, MainCourseActivity.class);


        /* Get TextEdit Layout holders */
        TextInputLayout courseCode = findViewById(R.id.tl_courseCode);
        TextInputLayout courseName = findViewById(R.id.tl_courseName);

        /* Listener for email input */
        Objects.requireNonNull(courseCode.getEditText()).setOnFocusChangeListener(new VerifyListener(3, "^[A-Z]+-[0-9]+$", getString(R.string.course_code_pattern)));

        /* Listener for password input */
        Objects.requireNonNull(courseName.getEditText()).setOnFocusChangeListener(new VerifyListener(2, "^.*$", null));

        /* Validation check upon clicking add course */
        findViewById(R.id.btn_add_course).setOnClickListener(v -> {
            boolean isVerified; // If user provided input is valid

            /* Clear anything that is focused */
            if (getCurrentFocus() != null) getCurrentFocus().clearFocus();

            /* Extract String to check for empty input */
            String inputCourseCode  = courseCode.getEditText().getText().toString();
            String inputCourseName  = courseName.getEditText().getText().toString();

            isVerified = checkEmpty(courseCode, inputCourseCode);
            isVerified &= checkEmpty(courseName, inputCourseName);

            if (isVerified) {
                DBHelper dbHelper = new DBHelper(this);

                // Provide new row details
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBContract.FeedEntry.COLUMN_COURSE_CODE, inputCourseCode);
                contentValues.put(DBContract.FeedEntry.COLUMN_COURSE_NAME, inputCourseName);

                /* Connect to database to display courses */
                try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
                    long result = db.insert(DBContract.FeedEntry.TABLE_COURSES, null, contentValues); // Insert new row

                    /* Check if the insert was successful */
                    if (result >= 1) {
                        Toast.makeText(this, R.string.success_add_course, Toast.LENGTH_SHORT).show();
                        returnActivity();
                    }
                    else {
                        Toast.makeText(this, R.string.fail_add_course, Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    Log.e(getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
                    Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /* Make homeAsUp button work the same as device back button */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Alter back button to work similar to home up button */
    @Override
    public void onBackPressed() {
        returnActivity();
    }

    /* Checks for empty input and marks highlights specified input box */
    public boolean checkEmpty(TextInputLayout view, String input) {
        /* Check if box is marked red or input is empty */
        if (view.getError() != null || input.isEmpty()) {
            if (input.isEmpty() ) view.setError(getString(R.string.required, view.getHint())); // Notify user that input is empty

            return false;
        }

        return true;
    }

    public void returnActivity() {
        startActivity(returnIntent);
        finish();
    }
}