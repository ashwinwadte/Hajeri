package com.example.ashwin.hajeri;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.model.StudentModel;

import java.util.List;


public class AddStudents extends AppCompatActivity implements View.OnClickListener {
    EditText etGRNo, etLastName, etFirstName, etMiddleName, etRollNo, etDivision;
    Spinner spYear, spClassName;
    Button bSaveStudent;

    //String[] from1 = new String[]{DatabaseHelper.COMMON_COLUMN_YEAR};
    //String[] from2 = new String[]{DatabaseHelper.COMMON_COLUMN_CLASS_NAME};
    //int[] to = new int[]{android.R.id.text1};

    String selectedYear, selectedClass;

    DatabaseHelper db;
    ArrayAdapter adapter1, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);
        setTitle("Add new student");
        db = new DatabaseHelper(this);

        initWidgets();

        List<String> list1 = db.displayClassColumn("Year");
        List<String> list2 = db.displayClassColumn("Class");

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, list1);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, list2);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(adapter1);
        spClassName.setAdapter(adapter2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_students, menu);
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

    private void initWidgets() {
        etGRNo = (EditText) findViewById(R.id.etGRNo);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etMiddleName = (EditText) findViewById(R.id.etMiddleName);
        etRollNo = (EditText) findViewById(R.id.spDivision);
        etDivision = (EditText) findViewById(R.id.etRollNo);
        spYear = (Spinner) findViewById(R.id.spYear);
        spClassName = (Spinner) findViewById(R.id.spClassName);

        bSaveStudent = (Button) findViewById(R.id.bSaveStudent);

        bSaveStudent.setOnClickListener(this);

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        addRecord();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        //addRecord();
//    }

    public void addRecord() {
        try {
            db.openDB();
            StudentModel studentModel = new StudentModel(etGRNo.getText().toString(), etLastName.getText().toString(), etFirstName.getText().toString(), etMiddleName.getText().toString(), etRollNo.getText().toString(), etDivision.getText().toString(), selectedYear, selectedClass);

            long id = db.addStudent(studentModel);
            db.closeDB();
            Toast.makeText(this, "Student added.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            if (etGRNo.getText().toString().equals(null) || etGRNo.getText().toString().equals("")) {
                etGRNo.setHint("GR No. Required");
                etGRNo.setHintTextColor(Color.RED);
            }

            if (etLastName.getText().toString().equals(null) || etLastName.getText().toString().equals("")) {
                etLastName.setHint("Last name required");
                etLastName.setHintTextColor(Color.RED);
            }

            if (etFirstName.getText().toString().equals(null) || etFirstName.getText().toString().equals("")) {
                etFirstName.setHint("First name required");
                etFirstName.setHintTextColor(Color.RED);
            }

            if (etRollNo.getText().toString().equals(null) || etRollNo.getText().toString().equals("")) {
                etRollNo.setHint("Roll No. required");
                etRollNo.setHintTextColor(Color.RED);
            }

            if (etDivision.getText().toString().equals(null) || etDivision.getText().toString().equals("")) {
                etDivision.setHint("Division required");
                etDivision.setHintTextColor(Color.RED);
            }
        }

    }
}
