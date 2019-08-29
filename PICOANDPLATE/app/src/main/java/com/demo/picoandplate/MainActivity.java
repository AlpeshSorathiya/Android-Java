package com.demo.picoandplate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.demo.picoandplate.database.DatabaseHelper;
import com.demo.picoandplate.database.model.Log;
import com.demo.picoandplate.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private DatabaseHelper db;

    private Button btnDatePicker, btnTimePicker, btnStatus, btnLogs;
    private EditText edtDate, edtTime, edtPlateNumber;
    private TextView txtStatus, txtTotalFine;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Switch switchDisabilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        btnStatus = (Button) findViewById(R.id.btn_status);
        btnLogs = (Button) findViewById(R.id.btn_logs);
        edtDate = (EditText) findViewById(R.id.edt_date);
        edtTime = (EditText) findViewById(R.id.edt_time);
        edtPlateNumber = (EditText) findViewById(R.id.edt_plate_number);
        txtStatus = (TextView) findViewById(R.id.txt_status);
        txtTotalFine = (TextView) findViewById(R.id.txt_total_fine);
        switchDisabilities = (Switch) findViewById(R.id.switch_disabilities);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnStatus.setOnClickListener(this);
        btnLogs.setOnClickListener(this);

        db = new DatabaseHelper(this);
        db.getAllLogs();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_date:
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.btn_time:
                // Get Current Time
                final Calendar cc = Calendar.getInstance();
                mHour = cc.get(Calendar.HOUR_OF_DAY);
                mMinute = cc.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
                break;
            case R.id.btn_status:
                String plateNumber = edtPlateNumber.getText().toString().trim();
                if (edtDate.getText().toString().trim().length() < 1) {
                    doShowToast(getString(R.string.msg_valid_date));
                } else if (edtTime.getText().toString().trim().length() < 1) {
                    doShowToast(getString(R.string.msg_valid_date));
                } else if (plateNumber.length() > 0 && isValidPlateNumber(plateNumber)) {
                    if (switchDisabilities.isChecked()) {
                        doAddLogs(getString(R.string.txt_status) + ": " + getString(R.string.txt_no), edtPlateNumber.getText().toString().trim(), "0");
                        txtStatus.setText(getString(R.string.txt_status) + ": " + getString(R.string.txt_no));
                    } else {
                        doValidatePP(String.valueOf(plateNumber.charAt(plateNumber.length() - 1)));
                    }

                } else {
                    doShowToast(getString(R.string.msg_plate_number));
                }
                break;
            case R.id.btn_logs:
                if (!isValidPlateNumber(edtPlateNumber.getText().toString().trim())) {
                    doShowToast(getString(R.string.msg_plate_number));
                } else {
                    Intent intent = new Intent(MainActivity.this, LogActivity.class);
                    intent.putExtra("plateNumber", edtPlateNumber.getText().toString().trim());
                    startActivity(intent);
                }
                break;
        }
    }

    private boolean doValidatePP(String lastNumber) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String dayOfTheWeek = "";
        Date date = null;
        try {
            date = sdf.parse(edtDate.getText().toString().trim() + " " + edtTime.getText().toString().trim());
            dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date).toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dayOfPlate = Constants.RULES_DATA.get(Integer.parseInt(lastNumber)).toLowerCase();
        if (dayOfTheWeek.equalsIgnoreCase(dayOfPlate)) {

            // check whether it's come under time range
            if (doCheckTimeSlot(date)) {
                doAddLogs(getString(R.string.txt_status) + ": " + getString(R.string.txt_contravention), edtPlateNumber.getText().toString().trim(), "10");
                txtStatus.setText(getString(R.string.txt_status) + ": " + getString(R.string.txt_contravention));
            } else {
                doAddLogs(getString(R.string.txt_status) + ": " + getString(R.string.txt_no), edtPlateNumber.getText().toString().trim(), "0");
                txtStatus.setText(getString(R.string.txt_status) + ": " + getString(R.string.txt_no));
            }
        } else {
            doAddLogs(getString(R.string.txt_status) + ": " + getString(R.string.txt_no), edtPlateNumber.getText().toString().trim(), "0");
            txtStatus.setText(getString(R.string.txt_status) + ": " + getString(R.string.txt_no));
            return true;
        }
        return true;
    }

    private void doShowToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    public boolean isValidPlateNumber(String str) {
        return str.matches("GST-?\\d+(\\.\\d+)?");
    }

    private boolean doCheckTimeSlot(Date currentDate) {
        try {
            // Fixed date range
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            String time_slot_a_start_time = edtDate.getText().toString().trim() + " " + "7:00";
            String time_slot_a_end_time = edtDate.getText().toString().trim() + " " + "9:30";
            String time_slot_b_start_time = edtDate.getText().toString().trim() + " " + "16:00";
            String time_slot_b_end_time = edtDate.getText().toString().trim() + " " + "19:30";

            Date date_slot_a_start = sdf.parse(time_slot_a_start_time);
            Date date_slot_a_end = sdf.parse(time_slot_a_end_time);
            Date date_slot_b_start = sdf.parse(time_slot_b_start_time);
            Date date_slot_b_end = sdf.parse(time_slot_b_end_time);

            if (currentDate.after(date_slot_a_start) && currentDate.before(date_slot_a_end) ||
                    currentDate.after(date_slot_b_start) && currentDate.before(date_slot_b_end)) {

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void doAddLogs(String data, String plateNumber, String fine) {
        db.insertLog(data, plateNumber, fine);


        txtTotalFine.setText(calculateFine(db.getAllLogs(plateNumber)));
    }

    private String calculateFine(List<Log> logList) {
        int fine = 0;
        for (Log log : logList
        ) {
            fine = fine + Integer.parseInt(log.getFine());
        }

        return getString(R.string.txt_total_fine) + ": $" + fine;
    }

}
