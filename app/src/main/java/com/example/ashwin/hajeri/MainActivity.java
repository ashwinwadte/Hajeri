package com.example.ashwin.hajeri;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ashwin.hajeri.attendance.Attendance;
import com.example.ashwin.hajeri.savedClassList.ClassList;
import com.example.ashwin.hajeri.studentMasterList.Students;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    final static String BUNDLE_KEY = "BUNDLE_KEY";

    Button bAttendence;
    Button bTotalAttendence;
    Button bStudentsList;
    Button bClassList;
    Button bDBManager;

    Button bImportStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bAttendence:
                intent = new Intent(this, Attendance.class);
                startActivity(intent);
                break;
            case R.id.bTotalAttendence:
                intent = new Intent(this, Attendance.class);
                intent.putExtra(BUNDLE_KEY, "TotalAttendance");
                startActivity(intent);
                break;
            case R.id.bStudentsList:
                intent = new Intent(this, Students.class);
                startActivity(intent);
                break;
            case R.id.bClassList:
                intent = new Intent(this, ClassList.class);
                startActivity(intent);
                break;
            case R.id.bDBManager:
                intent = new Intent(this, AndroidDatabaseManager.class);
                startActivity(intent);
                break;
            case R.id.bImportStudents:
                intent = new Intent(this, ImportCSV.class);
                startActivity(intent);
                break;
        }
    }

    public void initWidgets() {
        bAttendence = (Button) findViewById(R.id.bAttendence);
        bTotalAttendence = (Button) findViewById(R.id.bTotalAttendence);
        bStudentsList = (Button) findViewById(R.id.bStudentsList);
        bClassList = (Button) findViewById(R.id.bClassList);
        bDBManager = (Button) findViewById(R.id.bDBManager);
        bImportStudents = (Button) findViewById(R.id.bImportStudents);

        bAttendence.setOnClickListener(this);
        bTotalAttendence.setOnClickListener(this);
        bStudentsList.setOnClickListener(this);
        bClassList.setOnClickListener(this);
        bDBManager.setOnClickListener(this);
        bImportStudents.setOnClickListener(this);
    }
}
