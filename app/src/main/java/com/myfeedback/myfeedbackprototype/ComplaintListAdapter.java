package com.myfeedback.myfeedbackprototype;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ComplaintListAdapter extends RecyclerView.Adapter<ComplaintListAdapter.ComplaintViewHolder> {

    View view;
    private Context mCtx;
    private List<ComplaintList> complaintList;

    public ComplaintListAdapter(Context mCtx, List<ComplaintList> complaintList) {
        this.mCtx = mCtx;
        this.complaintList = complaintList;
    }

    @Override
    public ComplaintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.complaint_list, null);
        return new ComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComplaintViewHolder holder, int position) {
        ComplaintList cl = complaintList.get(position);

        holder.tvId.setText(String.valueOf(cl.getId()));
        holder.tvTitle.setText(String.valueOf(cl.getTitle()));
        holder.tvDesc.setText(String.valueOf(cl.getDescription()));
        if ((cl.getStatus().equals("0"))) {
            holder.tvStatus.setText(String.valueOf("pending"));
            holder.tvStatus.setBackgroundColor(Color.rgb(255, 51, 51));
        } else {
            holder.tvStatus.setText(String.valueOf("resolved"));
            holder.tvStatus.setBackgroundColor(Color.rgb(0, 153, 0));
        }
        //holder.tvStatus.setText(String.valueOf(cl.getStatus()));
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    class ComplaintViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvTitle, tvDesc, tvStatus;
        ImageView imageView;

        public ComplaintViewHolder(View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.textViewId);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            tvDesc = itemView.findViewById(R.id.textViewDesc);
            tvStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}