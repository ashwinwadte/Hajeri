package com.example.ashwin.hajeri.studentMasterList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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

import com.example.ashwin.hajeri.R;
import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.model.StudentModel;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddStudentFragment extends Fragment implements View.OnClickListener {
    EditText etGRNo, etLastName, etFirstName, etMiddleName, etRollNo;
    Spinner spYear, spClassName;
    Spinner spDivision;
    Button bSaveStudent;

    View view;

    String selectedYear, selectedClass, selectedDivision;

    DatabaseHelper db;
    ArrayAdapter adapter1, adapter2, adapter3;

    private OnAddStudentFragmentInteractionListener listener;

    public AddStudentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_student, container, false);

        getActivity().setTitle("Add new student");

        initWidgets();

        db = new DatabaseHelper(getActivity());

        List<String> list1 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_YEAR);
        List<String> list2 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_CLASS_NAME);
        List<String> list3 = db.displayClassColumn(DatabaseHelper.COMMON_COLUMN_DIVISION);

        adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, list1);
        adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, list2);
        adapter3 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, list3);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spYear.setAdapter(adapter1);
        spClassName.setAdapter(adapter2);
        spDivision.setAdapter(adapter3);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnAddStudentFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSaveListner");
        }
    }

    private void initWidgets() {
        etGRNo = (EditText) view.findViewById(R.id.etGRNo);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etMiddleName = (EditText) view.findViewById(R.id.etMiddleName);
        etRollNo = (EditText) view.findViewById(R.id.etRollNo);
        spDivision = (Spinner) view.findViewById(R.id.spDivision);
        spYear = (Spinner) view.findViewById(R.id.spYear);
        spClassName = (Spinner) view.findViewById(R.id.spClassName);

        bSaveStudent = (Button) view.findViewById(R.id.bSaveStudent);

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
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        addRecord();
    }

    public void addRecord() {
        try {
            db.openDB();

            StudentModel studentModel = new StudentModel(etGRNo.getText().toString(), etLastName.getText().toString(), etFirstName.getText().toString(), etMiddleName.getText().toString(), etRollNo.getText().toString(), selectedDivision, selectedYear, selectedClass);

            db.addStudent(studentModel);
            db.closeDB();
            Toast.makeText(getActivity(), "Student added.", Toast.LENGTH_SHORT).show();
            listener.onAddStudentFragmentInteraction();
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

    public interface OnAddStudentFragmentInteractionListener {
        void onAddStudentFragmentInteraction();
    }

//    public static class ArrayAdapterWithHint<T> extends ArrayAdapter<T> {
//
//        public ArrayAdapterWithHint(Context context, int resource, int textViewResourceId, List<T> objects, T hint) {
//            super(context, resource, textViewResourceId, objects);
//
//            objects.add(0, hint);
//        }
//
//        @Override
//        public int getPosition(T item) {
//            return super.getPosition(item);
//        }
//
//        @Override
//        public boolean isEnabled(int position) {
//            return position != 0;
//
//            /*
//             if (position == 0)
//                return false;
//            else
//                return true;
//             */
//        }
//
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            View view = super.getDropDownView(position, convertView, parent);
//            TextView textView = (TextView) view;
//
//            if (position == 0) {
//                textView.setTextColor(Color.GRAY);
//            } else {
//                textView.setTextColor(Color.BLACK);
//            }
//
//            return view;
//        }
//    }
}
