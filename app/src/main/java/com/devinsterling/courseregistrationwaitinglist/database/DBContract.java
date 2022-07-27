package com.devinsterling.courseregistrationwaitinglist.database;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract() {}

    /* Table Contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_COURSES = "course";
        public static final String COLUMN_COURSE_CODE = "course_code";
        public static final String COLUMN_COURSE_NAME = "course_name";
        public static final String TABLE_STUDENTS = "student";
        public static final String COLUMN_STUDENT_COURSE_ID = "course_id";
        public static final String COLUMN_STUDENT_FIRST_NAME = "first_name";
        public static final String COLUMN_STUDENT_LAST_NAME = "last_name";
        public static final String COLUMN_STUDENT_PRIORITY = "priority";
    }

    /* Database Table Creation */
    public static final String SQL_CREATE_COURSES =
            "CREATE TABLE " + FeedEntry.TABLE_COURSES + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_COURSE_CODE + " TEXT," +
                    FeedEntry.COLUMN_COURSE_NAME + " TEXT);";
    public static final String SQL_CREATE_STUDENTS =
            "CREATE TABLE " + FeedEntry.TABLE_STUDENTS + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_STUDENT_COURSE_ID + " INTEGER," +
                    FeedEntry.COLUMN_STUDENT_FIRST_NAME + " TEXT," +
                    FeedEntry.COLUMN_STUDENT_LAST_NAME + " TEXT," +
                    FeedEntry.COLUMN_STUDENT_PRIORITY + " INTEGER," +
                    "FOREIGN KEY(" + FeedEntry.COLUMN_STUDENT_COURSE_ID +
                    ") REFERENCES " + FeedEntry.TABLE_COURSES + "(" + FeedEntry._ID + ") " +
                    "ON DELETE CASCADE);";

    /* Insert course sample entries */
    public static final String SQL_INSERT_SAMPLE_COURSES =
            "INSERT INTO " + FeedEntry.TABLE_COURSES + " ("+ FeedEntry.COLUMN_COURSE_CODE + ", " + FeedEntry.COLUMN_COURSE_NAME +
                    ") VALUES ('CSIT-551', 'Mobile Computing')," +
                    "('CSIT-415', 'Software Engineering II')," +
                    "('CSIT-101', 'Intro to Computer Science');";
    /* Insert student sample entries */
    public static final String SQL_INSERT_SAMPLE_STUDENTS =
            "INSERT INTO " + FeedEntry.TABLE_STUDENTS + " (" + FeedEntry.COLUMN_STUDENT_COURSE_ID + ", " + FeedEntry.COLUMN_STUDENT_FIRST_NAME + ", " + FeedEntry.COLUMN_STUDENT_LAST_NAME + ", " + FeedEntry.COLUMN_STUDENT_PRIORITY +
                    ") VALUES (1, 'Wasyl', 'Leonel', 3)," +
                    "(1, 'August', 'Nadia', 3)," +
                    "(1, 'Vili', 'Heidrun', 5)," +
                    "(1, 'Cirila', 'Arsênio', 5)," +
                    "(1, 'Gjorgji', 'Gordana', 4)," +
                    "(1, 'Hu', 'Tao', 3)," +
                    "(1, 'Érika', 'Malati', 1)," +
                    "(1, 'Saulius', 'Servaos', 2)," +
                    "(1, 'Ravindra', 'Charlotta', 5)," +
                    "(1, 'Salvador', 'Othmar', 2)," +
                    "(1, 'Michael', 'Fernandez', 4)," +
                    "(1, 'Nick', 'Mascobi', 4)," +
                    "(1, 'Karl', 'Wetzel', 5)," +
                    "(2, 'Mohamed', 'Mirthe', 1)," +
                    "(2, 'Melanija', 'Ève', 4)," +
                    "(2, 'Kazuha', 'Haruta', 2)," +
                    "(2, 'Harald', 'Marica', 5)," +
                    "(2, 'Dwayne', 'Johnson', 3)," +
                    "(2, 'Tracee', 'Kári', 2)," +
                    "(2, 'Owen', 'Tercero', 3)," +
                    "(3, 'Luciano', 'Rudolf', 4)," +
                    "(3, 'Mona', 'Lisa', 2)," +
                    "(3, 'Ælfflæd', 'Noè', 1);";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_COURSES + ";" +
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_STUDENTS;
}