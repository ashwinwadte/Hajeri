package com.example.ashwin.hajeri.attendance;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class StudentsSelectionDialog extends ActionBarActivity implements View.OnClickListener {
    private static final String BUNDLE_KEY = "BUNDLE_KEY";
    ListView listView;
    Button bSelectStudents;

    DatabaseHelper db;

    String class_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_selection_dialog);

        listView = (ListView) findViewById(R.id.lvStudentList);
        listView.setEmptyView(findViewById(R.id.empty));

        bSelectStudents = (Button) findViewById(R.id.bSelectStudent);
        bSelectStudents.setOnClickListener(this);
/*
        if (findViewById(R.id.empty).getVisibility() == View.GONE) {
            bSelectStudents.setVisibility(View.VISIBLE);
        } else {
            bSelectStudents.setVisibility(View.INVISIBLE);
        }
*/

        class_ID = getIntent().getStringExtra(BUNDLE_KEY);

        db = new DatabaseHelper(this);

        MatrixCursor rollNo = db.displayAllStudentsForClassToSelect(class_ID);

        SimpleCursorAdapter adaptRollNo = new SimpleCursorAdapter(this, R.layout.row_for_attendance_activity, rollNo, new String[]{"division", "name"}, new int[]{R.id.tvRoll, R.id.ctvName});

        adaptRollNo.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() != R.id.tvRoll)
                    return false;

                GradientDrawable background = (GradientDrawable) view.getBackground();

                background.setColor(new RandomColor(StudentsSelectionDialog.this).getRandomColor(cursor.getString(columnIndex)));

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

               // listView.setItemChecked(position, true);

            }
        });
    }

    public void setSelectable(boolean isSelectable) {
//        mIsSelectable = isSelectable;
//        mRecyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_students_selection_dialog, menu);
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
        long[] selected = listView.getCheckedItemIds();

        if (selected.length >= 0) {
            for (int i = 0; i < selected.length; i++)
                db.addClassStudent(class_ID, String.valueOf(selected[i]));

            Intent intent = new Intent(StudentsSelectionDialog.this, AttendanceMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(BUNDLE_KEY, class_ID);
            startActivity(intent);
//            setResult(RESULT_OK); //, new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//            finish();
        }
    }
}
