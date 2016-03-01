package com.example.ashwin.hajeri.attendance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwin.hajeri.MainActivity;
import com.example.ashwin.hajeri.R;
import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.helperFunctions.RandomColor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AttendanceMain extends AppCompatActivity implements View.OnClickListener {
    final static String BUNDLE_KEY = "BUNDLE_KEY";
    private static String datetimestamp = null;
    private final int requestCode = 50;
    ListView listView;
    ImageButton ibAddStudentsAttendanceMain;
    DatabaseHelper db;
    boolean PA_Flag = true;

    Menu menu;
    ArrayList mSelectedItems;
    private Bundle bundle;
    private String class_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_main);

        class_ID = getIntent().getStringExtra(BUNDLE_KEY);

        listView = (ListView) findViewById(R.id.lvStudentListAttendanceMain);
        listView.setEmptyView(findViewById(R.id.empty));

        db = new DatabaseHelper(this);

        populateList();

        if (findViewById(R.id.empty).getVisibility() != View.GONE) {
            ibAddStudentsAttendanceMain = (ImageButton) findViewById(R.id.ibAddStudentsAttendanceMain);
            ibAddStudentsAttendanceMain.setOnClickListener(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attendance_main, menu);

        this.menu = menu;

        if (findViewById(R.id.empty).getVisibility() != View.GONE) {
            menu.findItem(R.id.action_save).setVisible(false);
            menu.findItem(R.id.action_chooseDate).setVisible(false);
            menu.findItem(R.id.action_present).setVisible(false);
            menu.findItem(R.id.action_absent).setVisible(false);
            menu.findItem(R.id.action_addMoreStudents).setVisible(false);
            menu.findItem(R.id.action_deleteStudents).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                saveAttendance();
                break;

            case R.id.action_present:
                menu.findItem(R.id.action_present).setVisible(false);
                menu.findItem(R.id.action_absent).setVisible(true);

                PA_Flag = false;
                populateList();
                break;

            case R.id.action_absent:
                menu.findItem(R.id.action_present).setVisible(true);
                menu.findItem(R.id.action_absent).setVisible(false);

                PA_Flag = true;
                populateList();
                break;

            case R.id.action_chooseDate:
                showCalendar();
                break;

            case R.id.action_addMoreStudents:
                addStudents();
                break;

            case R.id.action_deleteStudents:
                deleteStudents();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteStudents() {
        Intent intent = new Intent(this, DeleteStudentsOfClass.class);
        intent.putExtra(BUNDLE_KEY, this.class_ID);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        addStudents();
    }


    private void addStudents() {
        Intent intent = new Intent(this, StudentsSelectionDialog.class);
        intent.putExtra(BUNDLE_KEY, class_ID);
//        startActivityForResult(intent, requestCode);
        startActivity(intent);
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == this.requestCode && resultCode == RESULT_OK) {
//            //populateList();
//            onCreate(null);
//        }
//
//
//    }

    private void populateList() {

        setTitle(db.setTitleClassName(class_ID));

        MatrixCursor rollNo = db.displayStudentsOfClass(class_ID);

        SimpleCursorAdapter adaptRollNo = new SimpleCursorAdapter(this, R.layout.row_for_attendance_main, rollNo, new String[]{"rollno", "name"}, new int[]{R.id.tvRoll, R.id.ctvName});

        adaptRollNo.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() != R.id.tvRoll)
                    return false;

                GradientDrawable background = (GradientDrawable) view.getBackground();

                background.setColor(new RandomColor(AttendanceMain.this).getRandomColor(cursor.getString(columnIndex)));

                ((TextView) view).setText(cursor.getString(columnIndex));
                return true;


            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adaptRollNo);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvPAimage = (TextView) view.findViewById(R.id.tvPAimage);
                if (PA_Flag) {

                    if (tvPAimage.getBackground() == null) {
                        tvPAimage.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_check_green));
                    } else {
                        tvPAimage.setBackground(null);
                    }
                } else {
                    if (tvPAimage.getBackground() == null) {
                        tvPAimage.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_cross_red));
                    } else {
                        tvPAimage.setBackground(null);
                    }
                }
            }
        });
    }


    private void showCalendar() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void saveAttendance() {
        long[] selected = listView.getCheckedItemIds();

        if (datetimestamp == null) {
            String time;
            String formatForTime = "yyyy-MM-dd HH:mm:ss";

            Date date = new Date();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatForTime, Locale.US);
            time = simpleDateFormat.format(date);

            datetimestamp = time;
        } else {
            String time;
            String formatForTime = "HH:mm:ss";

            Date date = new Date();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatForTime, Locale.US);
            time = simpleDateFormat.format(date);

            datetimestamp = datetimestamp + " " + time;
        }


        if (selected.length >= 0) {

            if (PA_Flag) {
                //mark present students

                for (int i = 0; i < selected.length; i++)
                    db.markAttendance(class_ID, String.valueOf(selected[i]), datetimestamp, 1);

                db.unmarkedAttendance(class_ID, datetimestamp, 0);

            } else {
                //mark absent students


                for (int i = 0; i < selected.length; i++)
                    db.markAttendance(class_ID, String.valueOf(selected[i]), datetimestamp, 0);

                db.unmarkedAttendance(class_ID, datetimestamp, 1);
            }

        }

        datetimestamp = null;

        Toast.makeText(AttendanceMain.this, "Attendance marked.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            datetimestamp = year + "-" + month + "-" + day;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            datetimestamp = year + "-" + month + "-" + day;
        }
    }



    /*
    public static class StudentSelectionFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
           final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Select students")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(R.array.toppings, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(which);
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            })
                            // Set the action buttons
                    .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            return builder.create();
        }
    }
    */
}