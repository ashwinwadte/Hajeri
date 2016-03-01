package com.example.ashwin.hajeri.attendance;

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
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.ashwin.hajeri.R;
import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.helperFunctions.RandomColor;

public class DeleteStudentsOfClass extends AppCompatActivity implements View.OnClickListener {
    final static String BUNDLE_KEY = "BUNDLE_KEY";

    ListView listView;
    Button bDeleteSelectedStudents;

    DatabaseHelper db;
    private String class_ID;
    private String student_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_students_of_class);

        class_ID = getIntent().getStringExtra(BUNDLE_KEY);

        listView = (ListView) findViewById(R.id.lvStudentListAttendanceMain);
        listView.setEmptyView(findViewById(R.id.empty));

        bDeleteSelectedStudents = (Button) findViewById(R.id.bDeleteSelectedStudents);
        bDeleteSelectedStudents.setOnClickListener(this);

        db = new DatabaseHelper(this);

        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_students_of_class, menu);
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

    private void populateList() {
        MatrixCursor rollNo = db.displayStudentsOfClass(class_ID);

        SimpleCursorAdapter adaptRollNo = new SimpleCursorAdapter(this, R.layout.row_for_attendance_activity, rollNo, new String[]{"rollno", "name"}, new int[]{R.id.tvRoll, R.id.ctvName});

        adaptRollNo.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() != R.id.tvRoll)
                    return false;

                GradientDrawable background = (GradientDrawable) view.getBackground();

                background.setColor(new RandomColor(DeleteStudentsOfClass.this).getRandomColor(cursor.getString(columnIndex)));

                ((TextView) view).setText(cursor.getString(columnIndex));
                return true;


            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adaptRollNo);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.ctvName);
                checkedTextView.toggle();
            }
        });
    }

    @Override
    public void onClick(View v) {
        long[] selected = listView.getCheckedItemIds();

        if (selected.length >= 0) {
            for (int i = 0; i < selected.length; i++)
                db.deleteStudentOfClass(class_ID, String.valueOf(selected[i]));

            Intent intent = new Intent(DeleteStudentsOfClass.this, AttendanceMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(BUNDLE_KEY, class_ID);
            startActivity(intent);
        }
    }
}
