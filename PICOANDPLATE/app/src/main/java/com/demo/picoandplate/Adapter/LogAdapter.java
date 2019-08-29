package com.demo.picoandplate.Adapter;

/**
 * Created by Alpesh Sorathiya.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.picoandplate.R;
import com.demo.picoandplate.database.model.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class LogAdapter extends RecyclerView.Adapter<LogAdapter.MyViewHolder> {

    private Context context;
    private List<Log> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView log;
        public TextView plateNumber;
        public TextView fine;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            log = view.findViewById(R.id.log);
            plateNumber = view.findViewById(R.id.plateNumber);
            fine = view.findViewById(R.id.fine);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public LogAdapter(Context context, List<Log> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log log = notesList.get(position);

        holder.log.setText(log.getLog());

        holder.fine.setText("$"+log.getFine());

        holder.plateNumber.setText(log.getPlateNumber());

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(log.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
