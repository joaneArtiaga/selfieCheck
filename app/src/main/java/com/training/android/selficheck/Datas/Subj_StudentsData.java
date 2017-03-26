package com.training.android.selficheck.Datas;

public class Subj_StudentsData {

    private String CourseCode;


    public Subj_StudentsData() {
    }

    public Subj_StudentsData(String courseCode) {
        CourseCode = courseCode;

    }


    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }
}
