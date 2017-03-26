package com.training.android.selficheck.Datas;


public class AttendanceData {

    private String AttendancePassword, Date;

    public AttendanceData() {
    }

    public AttendanceData(String attendancePassword, String date) {
        AttendancePassword = attendancePassword;
        Date = date;
    }

    public String getAttendancePassword() {
        return AttendancePassword;
    }

    public void setAttendancePassword(String attendancePassword) {
        AttendancePassword = attendancePassword;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
