package com.example.ashwin.hajeri.attendance;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwin.hajeri.R;
import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.helperFunctions.RandomColor;

import java.util.ArrayList;

public class TotalAttendance extends AppCompatActivity {
    final static String CLASS_ID = "CLASS_ID";
    private static String datetimestamp = null;
    private final int requestCode = 50;
    ListView listView;
    ImageButton ibAddStudentsAttendanceMain;
    DatabaseHelper db;
    boolean PA_Flag = true;
    boolean flag1 = true, flag2 = true;
    Menu menu;
    ArrayList mSelectedItems;
    private Bundle bundle;
    private String class_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_main);

        class_ID = getIntent().getStringExtra(CLASS_ID);

        listView = (ListView) findViewById(R.id.lvStudentListAttendanceMain);
        listView.setEmptyView(findViewById(R.id.empty));

        db = new DatabaseHelper(this);

        populateList();
    }

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

                background.setColor(new RandomColor(TotalAttendance.this).getRandomColor(cursor.getString(columnIndex)));

                ((TextView) view).setText(cursor.getString(columnIndex));
                return true;


            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adaptRollNo);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int total;
                total = db.getTotalAttendance(class_ID, String.valueOf(id));

                Toast.makeText(getApplication(), "Total attedance: " + total, Toast.LENGTH_SHORT).show();

            }
        });
    }

}
