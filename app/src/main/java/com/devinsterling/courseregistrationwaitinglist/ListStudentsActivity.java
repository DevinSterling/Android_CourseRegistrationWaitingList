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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devinsterling.courseregistrationwaitinglist.database.DBContract;
import com.devinsterling.courseregistrationwaitinglist.database.DBHelper;

import java.util.Arrays;
import java.util.Objects;

public class ListStudentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_students);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Show back button
        setTitle(R.string.available_students); // Set Title

        int courseId = getIntent().getIntExtra("course", -1);

        /* Add new student button event */
        findViewById(R.id.fab).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            intent.putExtra("course", courseId);

            startActivity(intent);
            finish();
        });

        /* Layout which encompasses all displayed courses */
        LinearLayout layout = findViewById(R.id.students);

        /* Used to help connect to the database */
        DBHelper dbHelper = new DBHelper(this);

        /* Connect to database to display students */
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.FeedEntry.TABLE_STUDENTS + " WHERE " + DBContract.FeedEntry.COLUMN_STUDENT_COURSE_ID + " = ? ORDER BY " + DBContract.FeedEntry.COLUMN_STUDENT_PRIORITY + " DESC", new String[] { Integer.toString(getIntent().getIntExtra("course", 0)) })) {

            while (cursor.moveToNext()) {
                int studentId = cursor.getInt(0);

                /* Display stacked button for course */
                getLayoutInflater().inflate(R.layout.stacked_button, layout);

                /* Get linearlayout */
                LinearLayout stackedButton = (LinearLayout) layout.getChildAt(layout.getChildCount()-1);

                /* Get Text and Button inner layout */
                LinearLayout InnerStackedButton = (LinearLayout) stackedButton.getChildAt(0);

                Button imageButton = (Button) stackedButton.getChildAt(1);

                /* Set Text */
                Button button = (Button) InnerStackedButton.getChildAt(0);
                button.setText(getString(R.string.full_name, cursor.getString(2), cursor.getString(3)));

                TextView textView = (TextView) InnerStackedButton.getChildAt(1);
                textView.setText(GradeLevel.getStringGradeLevel(cursor.getInt(4)));

                /* Button event */
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(this, ViewStudentActivity.class);
                    intent.putExtra("course", courseId);
                    intent.putExtra("student", studentId);

                    startActivity(intent);
                    finish();
                });

                imageButton.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                    final MenuItem editItem = menu.add(R.string.edit_student);
                    final MenuItem deleteItem = menu.add(R.string.remove_student);

                    /* Create Context Menu */
                    editItem.setOnMenuItemClickListener(item -> {
                        Intent intent = new Intent(this, EditStudentActivity.class);
                        intent.putExtra("course", courseId);
                        intent.putExtra("student", studentId);

                        startActivity(intent);
                        finish();

                        return true;
                    });
                    deleteItem.setOnMenuItemClickListener(item -> {
                        try (SQLiteDatabase db2 = dbHelper.getReadableDatabase()) {
                            long result = db2.delete(DBContract.FeedEntry.TABLE_STUDENTS, DBContract.FeedEntry._ID + " = ?", new String[]{ Integer.toString(studentId) });

                            /* Verify if db delete was successful */
                            if (result == 1) {
                                Toast.makeText(this, R.string.success_remove_student, Toast.LENGTH_SHORT).show();
                                layout.removeView(stackedButton);
                            }
                            else {
                                Toast.makeText(this, R.string.fail_remove_student, Toast.LENGTH_SHORT).show();
                            }
                        } catch (SQLException e) {
                            Log.e(getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
                            Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
                        }

                        return true;
                    });
                });
                imageButton.setOnClickListener(View::showContextMenu);
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
            Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
        }
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
}