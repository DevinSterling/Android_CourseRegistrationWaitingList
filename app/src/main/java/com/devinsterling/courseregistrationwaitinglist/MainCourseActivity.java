package com.devinsterling.courseregistrationwaitinglist;
/*
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

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

import androidx.appcompat.app.AppCompatActivity;

import com.devinsterling.courseregistrationwaitinglist.database.DBContract;
import com.devinsterling.courseregistrationwaitinglist.database.DBHelper;

import java.util.Arrays;

public class MainCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_course);
        setTitle(R.string.available_courses); // Set Title

        /* Add new course button event */
        findViewById(R.id.fab).setOnClickListener(v -> startActivity(new Intent(MainCourseActivity.this, AddCourseActivity.class)));

        /* Layout which encompasses all displayed courses */
        LinearLayout layout = findViewById(R.id.courses);

        /* Used to help connect to the database */
        DBHelper dbHelper = new DBHelper(this);

        /* Connect to database to display courses */
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.FeedEntry.TABLE_COURSES, null)) {

            while (cursor.moveToNext()) {
                int courseId = cursor.getInt(0);

                /* Display button for course */
                getLayoutInflater().inflate(R.layout.stacked_button, layout);

                /* Get linearlayout */
                LinearLayout stackedButton = (LinearLayout) layout.getChildAt(layout.getChildCount()-1);

                /* Get Text and Button inner layout */
                LinearLayout InnerStackedButton = (LinearLayout) stackedButton.getChildAt(0);

                Button imageButton = (Button) stackedButton.getChildAt(1);

                /* Set Text */
                Button button = (Button) InnerStackedButton.getChildAt(0);
                button.setText(cursor.getString(1));

                TextView textView = (TextView) InnerStackedButton.getChildAt(1);
                textView.setText(cursor.getString(2));

                /* Button event */
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(this, ListStudentsActivity.class);
                    intent.putExtra("course", courseId);

                    startActivity(intent);
                });

                /* Create Context Menu */
                imageButton.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                    final MenuItem editItem = menu.add(R.string.edit_course);
                    final MenuItem deleteItem = menu.add(R.string.remove_course);

                    editItem.setOnMenuItemClickListener(item -> {
                        Intent intent = new Intent(this, EditCourseActivity.class);
                        intent.putExtra("course", courseId);

                        startActivity(intent);
                        finish();

                        return true;
                    });
                    deleteItem.setOnMenuItemClickListener(item -> {
                        try (SQLiteDatabase db2 = dbHelper.getReadableDatabase()) {
                            db2.execSQL("PRAGMA foreign_keys=ON");
                            long result = db2.delete(DBContract.FeedEntry.TABLE_COURSES, DBContract.FeedEntry._ID + " = ?", new String[]{ Integer.toString(courseId) });

                            /* Verify if db delete was successful */
                            if (result == 1) {
                                Toast.makeText(this, R.string.success_remove_course, Toast.LENGTH_SHORT).show();
                                layout.removeView(stackedButton);
                            }
                            else {
                                Toast.makeText(this, R.string.fail_remove_course, Toast.LENGTH_SHORT).show();
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
}