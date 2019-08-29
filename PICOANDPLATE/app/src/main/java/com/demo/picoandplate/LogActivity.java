package com.demo.picoandplate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.demo.picoandplate.Adapter.LogAdapter;
import com.demo.picoandplate.database.DatabaseHelper;
import com.demo.picoandplate.database.model.Log;
import com.demo.picoandplate.utils.MyDividerItemDecoration;
import com.demo.picoandplate.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    private LogAdapter mAdapter;
    private List<Log> notesList = new ArrayList<>();
    //    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noLogsView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        String plateNumber = getIntent().getExtras().getString("plateNumber");
        recyclerView = findViewById(R.id.recycler_view);
        noLogsView = findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllLogs(plateNumber));

        mAdapter = new LogAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyLogs();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateNote(String note, int position) {
        Log n = notesList.get(position);
        // updating note text
        n.setLog(note);

        // updating note in db
        db.updateLog(n);

        // refreshing the list
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyLogs();
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteLog(int position) {
        // deleting the note from db
        db.deleteLog(notesList.get(position));

        // removing the note from the list
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyLogs();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLog(position);
            }
        });
        builder.show();
    }


    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyLogs() {
        // you can check notesList.size() > 0

        if (db.getLogsCount() > 0) {
            noLogsView.setVisibility(View.GONE);
        } else {
            noLogsView.setVisibility(View.VISIBLE);
        }
    }
}
