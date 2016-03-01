package com.example.ashwin.hajeri.savedClassList;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwin.hajeri.attendance.Attendance;
import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.model.ClassListModel;
import com.example.ashwin.hajeri.R;

import java.util.List;

public class AddClass extends AppCompatActivity implements View.OnClickListener {
    private static final String BUNDLE_KEY = "BUNDLE_KEY";
    EditText etStrength;
    Spinner spYear, spClassName, spClassType, spDivision, spClassBatch;
    Button bSaveClass;
    TextView tvSelectBatch;

    String selectedYear, selectedClassName, selectedClassType, selectedDivision, selectedClassBatch = null;

    DatabaseHelper db;
    ArrayAdapter adaptYear, adaptClassName, adaptClassType, adaptDivision, adaptClassBatch;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        initWidgets();

        db = new DatabaseHelper(this);

        List<String> list1 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_YEAR);
        List<String> list2 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_CLASS_NAME);
        List<String> list3 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_CLASS_TYPE);
        List<String> list4 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_DIVISION);
        List<String> list5 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_BATCH);

        adaptYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1, list1);
        adaptClassName = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1, list2);
        adaptClassType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1, list3);
        adaptDivision = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1, list4);
        adaptClassBatch = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1, list5);

        adaptYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptClassName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptClassType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptDivision.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptClassBatch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spYear.setAdapter(adaptYear);
        spClassName.setAdapter(adaptClassName);
        spClassType.setAdapter(adaptClassType);
        spDivision.setAdapter(adaptDivision);
        spClassBatch.setAdapter(adaptClassBatch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_class, menu);
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
        tvSelectBatch = (TextView) findViewById(R.id.tvSelectBatch);

        etStrength = (EditText) findViewById(R.id.etStrength);

        spYear = (Spinner) findViewById(R.id.spYear);
        spClassName = (Spinner) findViewById(R.id.spClassName);
        spClassType = (Spinner) findViewById(R.id.spClassType);
        spDivision = (Spinner) findViewById(R.id.spDivision);
        spClassBatch = (Spinner) findViewById(R.id.spClassBatch);

        bSaveClass = (Button) findViewById(R.id.bSaveClass);

        bSaveClass.setOnClickListener(this);


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
                selectedClassName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spClassType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClassType = parent.getItemAtPosition(position).toString();
                if (selectedClassType.equals("Theory")) {
                    tvSelectBatch.setVisibility(View.INVISIBLE);
                    spClassBatch.setVisibility(View.INVISIBLE);
                    selectedClassBatch = null;
                } else {
                    tvSelectBatch.setVisibility(View.VISIBLE);
                    spClassBatch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDivision = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spClassBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClassBatch = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        addRecord();
        if (flag) {
            String callingActivity = getIntent().getStringExtra(BUNDLE_KEY);

            if (callingActivity == null) {
                Intent intent = new Intent(this, ClassList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);

            } else if (callingActivity.equals("Attendance")) {
                Intent intent = new Intent(this, Attendance.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }

        }
    }

    public void addRecord() {
        try {
            db.openDB();
            if (selectedClassType.equals("Theory"))
                selectedClassBatch = null;

            ClassListModel classListModel = new ClassListModel(etStrength.getText().toString(), selectedYear, selectedClassName, selectedClassType, selectedDivision, selectedClassBatch);

            db.addClass(classListModel);
            db.closeDB();
            Toast.makeText(this, "Class added.", Toast.LENGTH_SHORT).show();
            flag = true;

        } catch (Exception e) {
            if (etStrength.getText().toString().equals("")) {
                etStrength.setHint("No. of students required!");
                etStrength.setHintTextColor(Color.RED);
            }
        }
    }
}
