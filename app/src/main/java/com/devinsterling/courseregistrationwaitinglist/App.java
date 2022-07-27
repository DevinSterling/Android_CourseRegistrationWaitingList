package com.devinsterling.courseregistrationwaitinglist;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

import android.app.Application;
import android.content.res.Resources;

/* Allows access to resources to other classes that have no Context */
public class App extends Application {
    private static Resources resource;

    /* Method assigns resource variable */
    @Override
    public void onCreate() {
        super.onCreate();
        resource = getResources();
    }

    /* Returns resource */
    public static Resources getResource() {
        return resource;
    }
}

