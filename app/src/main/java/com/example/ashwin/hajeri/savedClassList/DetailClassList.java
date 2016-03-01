package com.example.ashwin.hajeri.savedClassList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailClassList extends AppCompatActivity implements Dialog.OnClickListener {
    private static final String BUNDLE_KEY = "BUNDLE_KEY";
    TextView tvClassType, tvClassName, tvDivision, tvStrength;
    TextView tvBatch, tvBatchLable;

    DatabaseHelper db;
    Cursor cursor;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setTheme(R.style.AppThemeNoBar); //set theme without actionbar
        setContentView(R.layout.activity_detail_class_list);
        setTitle("");

        initWidgets();

        showDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_class_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_editClass:
                Intent intent = new Intent(getApplication(), EditClass.class);
                intent.putExtra(BUNDLE_KEY, String.valueOf(this.id));
                startActivity(intent);
                break;
            case R.id.action_deleteClass:
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailClassList.this);

                builder.setMessage("Do you want to delete?").setPositiveButton("Delete", this).setNegativeButton("Cancel", null);

                AlertDialog deleteDialog = builder.create();
                deleteDialog.show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void initWidgets() {
        tvClassType = (TextView) findViewById(R.id.tvClassType);
        tvClassName = (TextView) findViewById(R.id.tvClassName);
        tvDivision = (TextView) findViewById(R.id.tvDivision);
        tvStrength = (TextView) findViewById(R.id.tvStrength);

        tvBatch = (TextView) findViewById(R.id.tvBatch);
        tvBatchLable = (TextView) findViewById(R.id.tvBatchLable);
    }

    private void showDetails() {
        id = getIntent().getStringExtra(BUNDLE_KEY);

        db = new DatabaseHelper(this);
        cursor = db.displayClassDetail(id);
        cursor.moveToFirst();

        String ClassType = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_CLASS_TYPE));

        String ClassName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_YEAR)) + " " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_CLASS_NAME)) + currentYear();
        String Division = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_DIVISION));

        String Strength = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_STRENGTH));

        String Batch = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMMON_COLUMN_BATCH));

        if (Batch != null) {
            tvBatch.setVisibility(View.VISIBLE);
            tvBatchLable.setVisibility(View.VISIBLE);
            tvBatch.setText(Batch);
        } else {
            tvBatch.setVisibility(View.INVISIBLE);
            tvBatchLable.setVisibility(View.INVISIBLE);
        }

        tvClassType.setText(ClassType);
        tvClassName.setText(ClassName);
        tvDivision.setText(Division);
        tvStrength.setText(Strength);
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
        db.deleteClass(this.id);
        Toast.makeText(getApplicationContext(), "Deleted.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ClassList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, ClassList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
