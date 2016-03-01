package com.example.ashwin.hajeri.studentMasterList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ashwin on 11/15/2015.
 */
public class DetailStudentFragment extends Fragment implements Dialog.OnClickListener {
    View rootView;
    TextView tvGRNo, tvName, tvRollNo, tvDivision, tvClassYear;

    DatabaseHelper db;
    Cursor cursor;
    String id;

    OnDetailStudentFragmentInteractionListener listener;

    public DetailStudentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        getActivity().finish();
        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().onCreate(savedInstanceState, null);
        */
        getActivity().setTheme(R.style.AppThemeNoBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_detail_student, container, false);


        //getActivity().setTitle("");

        initWidgets();

        showDetails();

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_students, menu);

        menu.findItem(R.id.action_edit).setVisible(true);
        menu.findItem(R.id.action_delete).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit:
                listener.onDetailStudentFragmentInteraction(this.id);
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Do you want to delete?").setPositiveButton("Delete", this).setNegativeButton("Cancel", null);

                AlertDialog deleteDialog = builder.create();
                deleteDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnDetailStudentFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDetailStudentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void initWidgets() {
        tvGRNo = (TextView) rootView.findViewById(R.id.tvGRNo);
        tvName = (TextView) rootView.findViewById(R.id.ctvName);
        tvRollNo = (TextView) rootView.findViewById(R.id.tvRollNo);
        tvDivision = (TextView) rootView.findViewById(R.id.tvDivision);
        tvClassYear = (TextView) rootView.findViewById(R.id.tvClassYear);
    }

    private void showDetails() {
        Bundle args = getArguments();
        id = args.getString(Students.BUNDLE_KEY);

        db = new DatabaseHelper(getActivity());
        cursor = db.displayStudentDetail(id);
        cursor.moveToFirst();

        String GRNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_GRNO));
        String Name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_COLUMN_LASTNAME)) + " " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_COLUMN_FIRSTNAME)) + " " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_COLUMN_MIDDLENAME));
        String RollNo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_ROLLNO));
        String Division = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_DIVISION));
        String ClassYear = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_YEAR)) + " " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_CLASS_NAME)) + currentYear();

        tvGRNo.setText(GRNo);
        tvName.setText(Name);
        tvRollNo.setText(RollNo);
        tvDivision.setText(Division);
        tvClassYear.setText(ClassYear);
    }

    private String currentYear() {
        String month;
        String year;
        int tempYear;
        Date date = new Date();
        String formatForMonth = "M";
        String formatForYear = "yy";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatForMonth, Locale.US);
        month = simpleDateFormat.format(date);

        if (Integer.parseInt(month) <= 5) {

            simpleDateFormat = new SimpleDateFormat(formatForYear, Locale.US);
            year = simpleDateFormat.format(date);
            tempYear = Integer.parseInt(year);

            year = " (20" + String.valueOf(tempYear - 1) + "-" + String.valueOf(tempYear) + ")";
        } else {
            simpleDateFormat = new SimpleDateFormat(formatForYear, Locale.US);
            year = simpleDateFormat.format(date);
            tempYear = Integer.parseInt(year);

            year = " (20" + String.valueOf(tempYear) + "-" + String.valueOf(tempYear + 1) + ")";
        }

        return year;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        db.deleteStudent(this.id);
        Toast.makeText(getActivity(), "Deleted.", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    public interface OnDetailStudentFragmentInteractionListener {
        void onDetailStudentFragmentInteraction(String id);
    }
}
