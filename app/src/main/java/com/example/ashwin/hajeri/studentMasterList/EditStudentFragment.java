package com.example.ashwin.hajeri.studentMasterList;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.model.StudentModel;
import com.example.ashwin.hajeri.R;

import java.util.List;

/**
 * Created by Ashwin on 11/15/2015.
 */
public class EditStudentFragment extends Fragment implements View.OnClickListener {
    EditText etGRNo, etLastName, etFirstName, etMiddleName, etRollNo;
    Spinner spDivision, spYear, spClassName;
    Button bSaveStudent;

    String selectedDivision, selectedYear, selectedClass;
    String id;

    Bundle args;

    DatabaseHelper db;
    ArrayAdapter adapter1, adapter2, adapter3;
    Cursor cursor;

    View rootView;

    OnEditStudentFragmentInteractionListener listener;

    public EditStudentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_add_student, container, false);
        getActivity().setTitle("Edit student");

        db = new DatabaseHelper(getActivity());

        initWidgets();
        previousRecord();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnEditStudentFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnEditStudentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void initWidgets() {
        etGRNo = (EditText) rootView.findViewById(R.id.etGRNo);
        etLastName = (EditText) rootView.findViewById(R.id.etLastName);
        etFirstName = (EditText) rootView.findViewById(R.id.etFirstName);
        etMiddleName = (EditText) rootView.findViewById(R.id.etMiddleName);
        etRollNo = (EditText) rootView.findViewById(R.id.etRollNo);
        spDivision = (Spinner) rootView.findViewById(R.id.spDivision);
        spYear = (Spinner) rootView.findViewById(R.id.spYear);
        spClassName = (Spinner) rootView.findViewById(R.id.spClassName);

        bSaveStudent = (Button) rootView.findViewById(R.id.bSaveStudent);

        bSaveStudent.setOnClickListener(this);

        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDivision = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        updateRecord();
        //getActivity().onBackPressed();
    }

    public void updateRecord() {
        try {
            db.openDB();
            StudentModel studentModel = new StudentModel(etGRNo.getText().toString(), etLastName.getText().toString(), etFirstName.getText().toString(), etMiddleName.getText().toString(), etRollNo.getText().toString(), selectedDivision, selectedYear, selectedClass);

            db.updateStudent(studentModel, this.id);
            db.closeDB();
            Toast.makeText(getActivity(), "Changes saved.", Toast.LENGTH_SHORT).show();
            listener.onEditStudentFragmentInteraction(String.valueOf(this.id));
        } catch (Exception e) {
            if (etGRNo.getText().toString().equals("")) {
                etGRNo.setHint("GR No. required!");
                etGRNo.setHintTextColor(Color.RED);
            }

            if (etLastName.getText().toString().equals("")) {
                etLastName.setHint("Last name required!");
                etLastName.setHintTextColor(Color.RED);
            }

            if (etFirstName.getText().toString().equals("")) {
                etFirstName.setHint("First name required!");
                etFirstName.setHintTextColor(Color.RED);
            }

            if (etRollNo.getText().toString().equals("")) {
                etRollNo.setHint("Roll No. required!");
                etRollNo.setHintTextColor(Color.RED);
            }
        }

    }

    public void previousRecord() {
        args = getArguments();
        this.id = args.getString(Students.BUNDLE_KEY);

        db = new DatabaseHelper(getActivity());
        cursor = db.displayStudentDetail(id);
        cursor.moveToFirst();

        String GRNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_GRNO));
        String LastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_COLUMN_LASTNAME));
        String FirstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_COLUMN_FIRSTNAME));
        String MiddleName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_COLUMN_MIDDLENAME));
        String RollNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_ROLLNO));
        String Division = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_DIVISION));
        String Year = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_YEAR));
        String Class = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_CLASS_NAME));

        etGRNo.setText(GRNo);
        etLastName.setText(LastName);
        etFirstName.setText(FirstName);
        etMiddleName.setText(MiddleName);
        etRollNo.setText(RollNo);

        populateSpinner(Division, Year, Class);
    }

    private void populateSpinner(String currentDivision, String currentYear, String currentClass) {
        List<String> list1 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_YEAR);
        List<String> list2 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_CLASS_NAME);
        List<String> list3 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_DIVISION);

        /*
        list1 = rearrangeList(list1, currentYear);
        list2 = rearrangeList(list2, currentClass);
        list3 = rearrangeList(list3, currentDivision);
        */


        adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, list1);
        adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, list2);
        adapter3 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, list3);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spYear.setAdapter(adapter1);
        spClassName.setAdapter(adapter2);
        spDivision.setAdapter(adapter3);

        spDivision.setSelection(list3.indexOf(currentDivision));
        spYear.setSelection(list1.indexOf(currentYear));
        spClassName.setSelection(list2.indexOf(currentClass));
    }

    private List<String> rearrangeList(List<String> list, String object) {
        int index = list.indexOf(object);
        if (index != -1) {
            list.remove(object);
            list.add(0, object);
        }
        return list;
    }

    public interface OnEditStudentFragmentInteractionListener {
        void onEditStudentFragmentInteraction(String id);
    }

}
