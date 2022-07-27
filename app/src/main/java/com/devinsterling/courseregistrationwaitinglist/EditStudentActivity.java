package com.devinsterling.courseregistrationwaitinglist;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.devinsterling.courseregistrationwaitinglist.database.DBContract;
import com.devinsterling.courseregistrationwaitinglist.database.DBHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Objects;

public class EditStudentActivity extends AppCompatActivity {
    Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close); // Change Icon
        setTitle(R.string.edit_student); // Set Title

        int studentId = getIntent().getIntExtra("student", -1);

        returnIntent = new Intent(this, ListStudentsActivity.class);
        returnIntent.putExtra("course", getIntent().getIntExtra("course", -1));

        /* Get TextEdit Layout holders */
        TextInputLayout firstName = findViewById(R.id.tl_studentFirstName);
        TextInputLayout lastName = findViewById(R.id.tl_studentLastName);
        Spinner gradeLevel = findViewById(R.id.et_studentGradeLevel);

        /* Listener for email input */
        Objects.requireNonNull(firstName.getEditText()).setOnFocusChangeListener(new VerifyListener(2, "^[a-zA-Z]+$", getString(R.string.name_pattern)));

        /* Listener for password input */
        Objects.requireNonNull(lastName.getEditText()).setOnFocusChangeListener(new VerifyListener(2, "^[a-zA-Z]+$", getString(R.string.name_pattern)));

        DBHelper dbHelper = new DBHelper(this);

        try (SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + DBContract.FeedEntry.COLUMN_STUDENT_FIRST_NAME + ", " + DBContract.FeedEntry.COLUMN_STUDENT_LAST_NAME + ", "  + DBContract.FeedEntry.COLUMN_STUDENT_PRIORITY +
                    " FROM " + DBContract.FeedEntry.TABLE_STUDENTS + " WHERE " + DBContract.FeedEntry._ID + " = " + studentId , null)) {

            /* Check if retrieval is a success and set text */
            if (cursor.moveToFirst()) {
                firstName.getEditText().setText(cursor.getString(0));
                lastName.getEditText().setText(cursor.getString(1));
                gradeLevel.setSelection(cursor.getInt(2)-1);
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
            Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
        }

        /* Validation check upon clicking add student */
        findViewById(R.id.btn_edit_student).setOnClickListener(v -> {
            boolean isVerified; // If user provided input is valid

            /* Clear anything that is focused */
            if (getCurrentFocus() != null) getCurrentFocus().clearFocus();

            /* Extract String to check for empty input */
            String inputFirstName   = firstName.getEditText().getText().toString();
            String inputLastName    = lastName.getEditText().getText().toString();

            isVerified = checkEmpty(firstName, inputFirstName);
            isVerified &= checkEmpty(lastName, inputLastName);

            if (isVerified) {
                // Provide new row details
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBContract.FeedEntry.COLUMN_STUDENT_FIRST_NAME, inputFirstName);
                contentValues.put(DBContract.FeedEntry.COLUMN_STUDENT_LAST_NAME, inputLastName);
                contentValues.put(DBContract.FeedEntry.COLUMN_STUDENT_PRIORITY, gradeLevel.getSelectedItemPosition()+1);

                /* Connect to database to display courses */
                try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
                    long result = db.update(DBContract.FeedEntry.TABLE_STUDENTS, contentValues, DBContract.FeedEntry._ID + " = ?", new String[]{Integer.toString(studentId)}); // Insert new row

                    /* Check if the update was successful */
                    if (result == 1) {
                        Toast.makeText(this, R.string.success_edit_student, Toast.LENGTH_SHORT).show();
                        returnActivity();
                    }
                    else {
                        Toast.makeText(this, R.string.fail_edit_student, Toast.LENGTH_SHORT).show();
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