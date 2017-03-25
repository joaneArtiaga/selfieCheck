package com.training.android.selficheck.Datas;

/**
 * Created by Dyste on 3/24/2017.
 */

public class Subj_StudentsData {

    private String CourseCode;
    private String DailyPassword;

    public Subj_StudentsData() {
    }

    public Subj_StudentsData(String courseCode, String dailyPassword) {
        CourseCode = courseCode;
        DailyPassword = dailyPassword;
    }


    public String getDailyPassword() {
        return DailyPassword;
    }

    public void setDailyPassword(String dailyPassword) {
        DailyPassword = dailyPassword;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }
}
