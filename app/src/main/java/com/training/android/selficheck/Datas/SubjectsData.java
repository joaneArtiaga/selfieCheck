package com.training.android.selficheck.Datas;

public class SubjectsData extends Subj_StudentsData {


    private String CourseCode, CourseName, CourseSchedule, CourseRoom, SubjectPassword;
    private long TeacherID;

    public SubjectsData() {
    }

    public SubjectsData(String courseCode, String courseName, String courseSchedule, String courseRoom, String subjectPassword, long teacherID) {
        CourseCode = courseCode;
        CourseName = courseName;
        CourseSchedule = courseSchedule;
        CourseRoom = courseRoom;
        SubjectPassword = subjectPassword;
        TeacherID = teacherID;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public String getSubjectPassword() {
        return SubjectPassword;
    }

    public void setSubjectPassword(String subjectPassword) {
        SubjectPassword = subjectPassword;
    }

    public long getTeacherID() {
        return TeacherID;
    }

    public void setTeacherID(long teacherID) {
        TeacherID = teacherID;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getCourseSchedule() {
        return CourseSchedule;
    }

    public void setCourseSchedule(String courseSchedule) {
        CourseSchedule = courseSchedule;
    }

    public String getCourseRoom() {
        return CourseRoom;
    }

    public void setCourseRoom(String courseRoom) {
        CourseRoom = courseRoom;
    }

    @Override
    public String getDailyPassword() {
        return super.getDailyPassword();
    }

    @Override
    public void setDailyPassword(String dailyPassword) {
        super.setDailyPassword(dailyPassword);
    }
}
