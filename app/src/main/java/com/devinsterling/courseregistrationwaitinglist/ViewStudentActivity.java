package com.devinsterling.courseregistrationwaitinglist;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.devinsterling.courseregistrationwaitinglist.database.DBContract;
import com.devinsterling.courseregistrationwaitinglist.database.DBHelper;

import java.util.Arrays;
import java.util.Objects;

public class ViewStudentActivity extends AppCompatActivity {
    Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Show back button
        setTitle(R.string.view_student); // Set Title

        int courseId = getIntent().getIntExtra("course", -1);
        int studentId = getIntent().getIntExtra("student", -1);

        returnIntent = new Intent(this, ListStudentsActivity.class);
        returnIntent.putExtra("course", courseId);

        TextView fullName = findViewById(R.id.studentDetails_fullName);
        TextView priority = findViewById(R.id.studentDetails_priority);

        DBHelper dbHelper = new DBHelper(this);

        /* Retrieve student details to display */
        try (SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + DBContract.FeedEntry.COLUMN_STUDENT_FIRST_NAME + ", " + DBContract.FeedEntry.COLUMN_STUDENT_LAST_NAME + ", "  + DBContract.FeedEntry.COLUMN_STUDENT_PRIORITY +
                    " FROM " + DBContract.FeedEntry.TABLE_STUDENTS + " WHERE " + DBContract.FeedEntry._ID + " = " + studentId , null)) {

            /* Check if retrieval is a success and set text */
            if (cursor.moveToFirst()) {
                fullName.setText(getString(R.string.full_name, cursor.getString(0), cursor.getString(1)));
                priority.setText(GradeLevel.getStringGradeLevel(cursor.getInt(2)));
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
            Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
        }

        findViewById(R.id.studentDetails_editStudent).setOnClickListener(v -> {
            Intent intent = new Intent(this, EditStudentActivity.class);
            intent.putExtra("course", courseId);
            intent.putExtra("student", studentId);

            startActivity(intent);
            finish();
        });

        /* Remove student button */
        findViewById(R.id.studentDetails_removeStudent).setOnClickListener(v -> {
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                long result = db.delete(DBContract.FeedEntry.TABLE_STUDENTS, DBContract.FeedEntry._ID + " = ?", new String[]{ Integer.toString(studentId) });

                /* Verify if db delete was successful */
                if (result == 1) {
                    Toast.makeText(this, R.string.success_remove_student, Toast.LENGTH_SHORT).show();

                    startActivity(returnIntent);
                    finish();
                }
                else {
                    Toast.makeText(this, R.string.fail_remove_student, Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
                Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
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
        startActivity(returnIntent);
        finish();
    }
}