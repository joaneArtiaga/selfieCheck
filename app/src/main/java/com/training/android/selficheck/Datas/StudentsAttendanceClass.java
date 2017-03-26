package com.training.android.selficheck.Datas;

public class StudentsAttendanceClass {


    private String StudentID, Time, imgURL;

    public StudentsAttendanceClass() {
    }

    public StudentsAttendanceClass(String studentID, String time, String imgURL) {
        StudentID = studentID;
        Time = time;
        this.imgURL = imgURL;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
