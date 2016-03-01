package com.example.ashwin.hajeri.savedClassList;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.R;
import com.example.ashwin.hajeri.helperFunctions.RandomColor;


public class ClassList extends AppCompatActivity implements View.OnClickListener {

    final static String BUNDLE_KEY = "BUNDLE_KEY";

    ListView listView;
    ImageButton ibAddNewClass;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        setTitle("Class List");

        listView = (ListView) findViewById(R.id.lvClassList);
        listView.setEmptyView(findViewById(R.id.empty));

        ibAddNewClass = (ImageButton) findViewById(R.id.ibAddNewClass);
        ibAddNewClass.setOnClickListener(this);

        db = new DatabaseHelper(this);

        MatrixCursor cursor = db.displaySavedClasses();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_for_class_list_activity, cursor, new String[]{"division", "class"}, new int[]{R.id.tvDivisionOfClass, R.id.tvClassDetails});

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() != R.id.tvDivisionOfClass)
                    return false;

                GradientDrawable background = (GradientDrawable) view.getBackground();

                background.setColor(new RandomColor(getApplicationContext()).getRandomColor(cursor.getString(columnIndex)));

                ((TextView) view).setText(cursor.getString(columnIndex));
                return true;


            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplication(), DetailClassList.class);
                intent.putExtra(BUNDLE_KEY, String.valueOf(id));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_list, menu);
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

        Intent intent = new Intent(this, AddClass.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
