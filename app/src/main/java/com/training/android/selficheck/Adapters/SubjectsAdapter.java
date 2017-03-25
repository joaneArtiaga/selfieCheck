package com.training.android.selficheck.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.training.android.selficheck.Datas.SubjectsData;
import com.training.android.selficheck.R;
import com.training.android.selficheck.Subject_details;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private Context mContext;
    private ViewHolder holder;
    private List<SubjectsData> mSubjects;

    public SubjectsAdapter(Context applicationContext, ArrayList<SubjectsData> mData) {
        mContext = applicationContext;
        mSubjects = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        final View apartmentsLayout = LayoutInflater.from(mContext)
                .inflate(R.layout.student_subject_layout, null);

        holder = new ViewHolder(apartmentsLayout);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final SubjectsData subjectsData = mSubjects.get(position);

        if (subjectsData != null){
            holder.mtvCourseCode.setText(subjectsData.getCourseCode());
            holder.mtvCourseName.setText(subjectsData.getCourseName());
            holder.mtvSchedule.setText(subjectsData.getCourseSchedule());
            holder.mtvRoom.setText(subjectsData.getCourseRoom());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, Subject_details.class);
                i.putExtra("CourseCode", subjectsData.getCourseCode());
                i.putExtra("CourseName", subjectsData.getCourseName());
                i.putExtra("CourseSchedule", subjectsData.getCourseSchedule());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubjects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mtvCourseCode, mtvCourseName, mtvSchedule, mtvRoom;

        public ViewHolder(View itemView) {
            super(itemView);

            mtvCourseCode = (TextView) itemView.findViewById(R.id.tvCourseCode);
            mtvCourseName = (TextView) itemView.findViewById(R.id.tvCourseName);
            mtvSchedule = (TextView) itemView.findViewById(R.id.tvSchedule);
            mtvRoom = (TextView) itemView.findViewById(R.id.tvRoom);
        }
    }
}
