package com.yash.calllogdemo;

import android.database.Cursor;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    Cursor managedCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text);
        textView.setText(getCallDetails());
    }

    private String getCallDetails() {

        StringBuffer sb = new StringBuffer();
        managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);// for name
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            String callName = managedCursor.getString(name);
            if (TextUtils.isEmpty(callName))
                callName = "Unknown";

            Date callDayTime = new Date(Long.valueOf(callDate));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
            String dateString = formatter.format(callDayTime);

            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dirCode = Integer.parseInt(callType);
            switch (dirCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat compareFormatterCallDate = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat compareFormatterCurrentDate = new SimpleDateFormat("dd-MM-yy");
            String currentDateString = compareFormatterCurrentDate.format(currentTime);
            String callDateString = compareFormatterCallDate.format(callDayTime);

            if (callDateString.equals(currentDateString)) {
                sb.append("\nName:--- " + callName + "\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                        + dir + " \nCall Date:--- " + dateString
                        + " \nCall duration in sec :--- " + callDuration);
                sb.append("\n----------------------------------");
            } else {
                System.out.println("hh yahsal no call for today is available");
            }

        }
        return sb.toString();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (managedCursor != null)
            managedCursor.close();
    }
}
