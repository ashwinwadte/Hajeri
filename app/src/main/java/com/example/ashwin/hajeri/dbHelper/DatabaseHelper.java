package com.example.ashwin.hajeri.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ashwin.hajeri.model.ClassListModel;
import com.example.ashwin.hajeri.model.StudentModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashwin on 11/7/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Common columns
    public static final String COMMON_COLUMN_ID = "_id";
    public static final String COMMON_COLUMN_GRNO = "GRNo";
    public static final String COMMON_COLUMN_ROLLNO = "RollNo";
    public static final String COMMON_COLUMN_DIVISION = "Division";
    public static final String COMMON_COLUMN_CLASS_NAME = "Class";
    public static final String COMMON_COLUMN_YEAR = "Year";
    public static final String COMMON_COLUMN_CLASS_TYPE = "ClassType";
    public static final String COMMON_COLUMN_BATCH = "Batch";
    public static final String COMMON_COLUMN_STRENGTH = "Strength";

    //Table_credentials columns
    public static final String CREDENTIALS_COLUMN_USERNAME = "Username";
    public static final String CREDENTIALS_COLUMN_PASSWORD = "Password";

    //Table_student columns
    public static final String STUDENT_COLUMN_LASTNAME = "Lastname";
    public static final String STUDENT_COLUMN_FIRSTNAME = "Firstname";
    public static final String STUDENT_COLUMN_MIDDLENAME = "Middlename";

    //Table_class_student columns
    public static final String COLUMN_CLASS_ID = "Class_id";
    public static final String COLUMN_STUDENT_ID = "Student_id";
    public static final String TABLE_STUDENT = "Student";
    //Table_total_attendance columns
    private static final String COLUMN_DATE_TIME_STAMP = "DateTimeStamp";
    private static final String COLUMN_ATTENDANCE = "Attendance";
    // Logcat tag
    private static final String LOG_DATABASE_HELPER = "DatabaseHelper";
    //Database details
    private static final String DATABASE_NAME = "Hajeri.db";
    private static final int DATABASE_VERSION = 1;
    //Tables in Database
    private static final String TABLE_CREDENTIALS = "Credentials";
    private static final String TABLE_CLASS_LIST = "Class_List";
    private static final String TABLE_SAVED_CLASSES = "Saved_Classes";
    private static final String TABLE_CLASS_STUDENT = "Class_Student";
    private static final String TABLE_ATTENDANCE = "Total_Attendance";

    //Table_student_year columns

    //Table_class_list columns


    //Table_saved_classes columns
    //Create table queries
    private static final String CREATE_TABLE_CREDENTIALS = "CREATE TABLE " + TABLE_CREDENTIALS + " (" + COMMON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CREDENTIALS_COLUMN_USERNAME + " TEXT NOT NULL, " + CREDENTIALS_COLUMN_PASSWORD + " TEXT NOT NULL)";
    private static final String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_STUDENT + " (" + COMMON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COMMON_COLUMN_GRNO + " TEXT, " + STUDENT_COLUMN_LASTNAME + " TEXT, " + STUDENT_COLUMN_FIRSTNAME + " TEXT, " + STUDENT_COLUMN_MIDDLENAME + " TEXT, " + COMMON_COLUMN_ROLLNO + " TEXT, " + COMMON_COLUMN_DIVISION + " TEXT, " + COMMON_COLUMN_YEAR + " TEXT, " + COMMON_COLUMN_CLASS_NAME + " TEXT)";
    private static final String CREATE_TABLE_CLASS_LIST = "CREATE TABLE " + TABLE_CLASS_LIST + " (" + COMMON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COMMON_COLUMN_YEAR + " TEXT, " + COMMON_COLUMN_CLASS_NAME + " TEXT, " + COMMON_COLUMN_CLASS_TYPE + " TEXT, " + COMMON_COLUMN_DIVISION + " TEXT, " + COMMON_COLUMN_BATCH + " TEXT)";
    private static final String CREATE_TABLE_SAVED_CLASSES = "CREATE TABLE " + TABLE_SAVED_CLASSES + " (" + COMMON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COMMON_COLUMN_STRENGTH + " TEXT, " + COMMON_COLUMN_YEAR + " TEXT, " + COMMON_COLUMN_CLASS_NAME + " TEXT, " + COMMON_COLUMN_CLASS_TYPE + " TEXT, " + COMMON_COLUMN_DIVISION + " TEXT, " + COMMON_COLUMN_BATCH + " INTEGER)";
    private static final String CREATE_TABLE_CLASS_STUDENT = "CREATE TABLE " + TABLE_CLASS_STUDENT + " (" + COMMON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CLASS_ID + " TEXT, " + COLUMN_STUDENT_ID + " TEXT)";
    private static final String CREATE_TABLE_ATTENDANCE = "CREATE TABLE " + TABLE_ATTENDANCE + " (" + COMMON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CLASS_ID + " TEXT, " + COLUMN_STUDENT_ID + " TEXT, " + COLUMN_DATE_TIME_STAMP + " TEXT, " + COLUMN_ATTENDANCE + " TEXT)";

    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CREDENTIALS);
        db.execSQL(CREATE_TABLE_STUDENT);
        db.execSQL(CREATE_TABLE_CLASS_LIST);
        db.execSQL(CREATE_TABLE_SAVED_CLASSES);
        db.execSQL(CREATE_TABLE_CLASS_STUDENT);
        db.execSQL(CREATE_TABLE_ATTENDANCE);

        populateTableClassList(db);
        Log.d(LOG_DATABASE_HELPER, "Databases created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_CLASSES);

        onCreate(db);
    }

    public DatabaseHelper openDB() {
        database = this.getWritableDatabase();
        return this;
    }

    public void closeDB() {
        this.close();
    }

    public void insertCredentials(String Username, String Password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CREDENTIALS_COLUMN_USERNAME, Username);
        contentValues.put(CREDENTIALS_COLUMN_PASSWORD, Password);

        db.insert(TABLE_CREDENTIALS, null, contentValues);
        db.close();
    }

    public long addStudent(StudentModel studentModel) {
        long id;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        if (!studentModel.getGRNo().equals("") && !studentModel.getLastName().equals("") && !studentModel.getFirstName().equals("") && !studentModel.getRollNo().equals("") && !studentModel.getDivision().equals("") && !studentModel.getYear().equals("") && !studentModel.getClassName().equals("")) {
            contentValues.put(COMMON_COLUMN_GRNO, studentModel.getGRNo());
            contentValues.put(STUDENT_COLUMN_LASTNAME, studentModel.getLastName());
            contentValues.put(STUDENT_COLUMN_FIRSTNAME, studentModel.getFirstName());
            contentValues.put(STUDENT_COLUMN_MIDDLENAME, studentModel.getMiddleName());
            contentValues.put(COMMON_COLUMN_ROLLNO, studentModel.getRollNo());
            contentValues.put(COMMON_COLUMN_DIVISION, studentModel.getDivision());
            contentValues.put(COMMON_COLUMN_YEAR, studentModel.getYear());
            contentValues.put(COMMON_COLUMN_CLASS_NAME, studentModel.getClassName());

            id = db.insert(TABLE_STUDENT, null, contentValues);
        } else {
            throw new NullPointerException();
        }
        db.close();

        return id;
    }

    public void updateStudent(StudentModel studentModel, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        if (!studentModel.getGRNo().equals("") && !studentModel.getLastName().equals("") && !studentModel.getFirstName().equals("") && !studentModel.getRollNo().equals("") && !studentModel.getDivision().equals("") && !studentModel.getYear().equals("") && !studentModel.getClassName().equals("")) {
            contentValues.put(COMMON_COLUMN_GRNO, studentModel.getGRNo());
            contentValues.put(STUDENT_COLUMN_LASTNAME, studentModel.getLastName());
            contentValues.put(STUDENT_COLUMN_FIRSTNAME, studentModel.getFirstName());
            contentValues.put(STUDENT_COLUMN_MIDDLENAME, studentModel.getMiddleName());
            contentValues.put(COMMON_COLUMN_ROLLNO, studentModel.getRollNo());
            contentValues.put(COMMON_COLUMN_DIVISION, studentModel.getDivision());
            contentValues.put(COMMON_COLUMN_YEAR, studentModel.getYear());
            contentValues.put(COMMON_COLUMN_CLASS_NAME, studentModel.getClassName());
            db.update(TABLE_STUDENT, contentValues, COMMON_COLUMN_ID + " = " + id, null);
        } else {
            throw new NullPointerException();
        }
        db.close();
    }

    public void deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_STUDENT, COMMON_COLUMN_ID + " = " + id, null);
        db.close();

    }

    public long addClass(ClassListModel classListModel) {
        long id;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        if (!classListModel.getStrength().equals("") && !classListModel.getYear().equals("") && !classListModel.getClassName().equals("") && !classListModel.getClassType().equals("") && !classListModel.getDivision().equals("")) {
            contentValues.put(COMMON_COLUMN_STRENGTH, classListModel.getStrength());
            contentValues.put(COMMON_COLUMN_YEAR, classListModel.getYear());
            contentValues.put(COMMON_COLUMN_CLASS_NAME, classListModel.getClassName());
            contentValues.put(COMMON_COLUMN_CLASS_TYPE, classListModel.getClassType());
            contentValues.put(COMMON_COLUMN_DIVISION, classListModel.getDivision());

            contentValues.put(COMMON_COLUMN_BATCH, classListModel.getBatch());

            id = db.insert(TABLE_SAVED_CLASSES, null, contentValues);
        } else {
            throw new NullPointerException();
        }
        db.close();
        return id;
    }

    public void updateClass(ClassListModel classListModel, String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        if (!classListModel.getStrength().equals("") && !classListModel.getYear().equals("") && !classListModel.getClassName().equals("") && !classListModel.getClassType().equals("") && !classListModel.getDivision().equals("")) {
            contentValues.put(COMMON_COLUMN_STRENGTH, classListModel.getStrength());
            contentValues.put(COMMON_COLUMN_YEAR, classListModel.getYear());
            contentValues.put(COMMON_COLUMN_CLASS_NAME, classListModel.getClassName());
            contentValues.put(COMMON_COLUMN_CLASS_TYPE, classListModel.getClassType());
            contentValues.put(COMMON_COLUMN_DIVISION, classListModel.getDivision());

            contentValues.put(COMMON_COLUMN_BATCH, classListModel.getBatch());

            db.update(TABLE_SAVED_CLASSES, contentValues, COMMON_COLUMN_ID + " = " + id, null);
        } else {
            throw new NullPointerException();
        }

        db.close();
    }

    public void deleteClass(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SAVED_CLASSES, COMMON_COLUMN_ID + " = " + id, null);
        db.close();

    }

    public long addClassStudent(String Class_id, String Student_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CLASS_ID, Class_id);
        contentValues.put(COLUMN_STUDENT_ID, Student_id);

        Long id = db.insert(TABLE_CLASS_STUDENT, null, contentValues);

        db.close();
        return id;

    }

    public void insertInTableSavedClasses(String Year, String Class, String ClassType, String Batch, String Division) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COMMON_COLUMN_YEAR, Year);
        contentValues.put(COMMON_COLUMN_CLASS_NAME, Class);
        contentValues.put(COMMON_COLUMN_CLASS_TYPE, ClassType);
        contentValues.put(COMMON_COLUMN_BATCH, Batch);
        contentValues.put(COMMON_COLUMN_DIVISION, Division);

        database.insert(TABLE_SAVED_CLASSES, null, contentValues);
    }

    public void populateTableClassList(SQLiteDatabase db) {
        ContentValues contentValues;
        char c = 'A';
        String[] year = new String[]{"FY", "SY", "TY", "LY"};
        String[] className = new String[]{"B. Tech", "M. Tech", "MCA"};
        String[] classType = new String[]{"Theory", "Lab", "Tutorial"};

        for (int i = 0; i < 26; i++) {
            contentValues = new ContentValues();

            if (i < 4) {
                contentValues.put(COMMON_COLUMN_YEAR, year[i]);
                contentValues.put(COMMON_COLUMN_BATCH, String.valueOf(i + 1));
            } else {
                contentValues.putNull(COMMON_COLUMN_YEAR);
                contentValues.putNull(COMMON_COLUMN_BATCH);
            }

            if (i < 3) {
                contentValues.put(COMMON_COLUMN_CLASS_NAME, className[i]);
                contentValues.put(COMMON_COLUMN_CLASS_TYPE, classType[i]);
            } else {
                contentValues.putNull(COMMON_COLUMN_CLASS_NAME);
                contentValues.putNull(COMMON_COLUMN_CLASS_TYPE);
            }

            if (c <= 'Z') {
                contentValues.put(COMMON_COLUMN_DIVISION, String.valueOf(c));
                c++;
            }

            db.insert(TABLE_CLASS_LIST, null, contentValues);
        }
    }

    public Cursor displayAllRecords(String TableName) {
        this.openDB();
        switch (TableName) {
            case TABLE_STUDENT:
                return database.query(TABLE_STUDENT, null, null, null, null, null, null, null);
            case TABLE_CLASS_LIST:
                return database.query(TABLE_CLASS_LIST, null, null, null, null, null, null, null);
            case TABLE_SAVED_CLASSES:
                return database.query(TABLE_SAVED_CLASSES, null, null, null, null, null, null, null);
        }
        this.closeDB();
        return null;
    }

    public Cursor displayStudentDetail(String id) {
        this.openDB();
        return database.query(TABLE_STUDENT, null, COMMON_COLUMN_ID + " = ?", new String[]{id}, null, null, null);
    }

    public Cursor displayClassDetail(String id) {
        this.openDB();
        return database.query(TABLE_SAVED_CLASSES, null, COMMON_COLUMN_ID + " = " + id, null, null, null, null);
    }

    public List<String> displayClassColumn(String ColumnName) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = null;
        this.openDB();

        /*
        cursor = database.query(TABLE_CLASS_LIST, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(ColumnName)));
        }
        */

        switch (ColumnName) {
            case COMMON_COLUMN_DIVISION:
                cursor = database.query(TABLE_CLASS_LIST, new String[]{COMMON_COLUMN_ID, COMMON_COLUMN_DIVISION}, null, null, null, null, null, "26");
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(COMMON_COLUMN_DIVISION)));
                }
                break;
            case COMMON_COLUMN_YEAR:
                cursor = database.query(TABLE_CLASS_LIST, new String[]{COMMON_COLUMN_ID, COMMON_COLUMN_YEAR}, null, null, null, null, null, "4");
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(COMMON_COLUMN_YEAR)));
                }
                break;
            case COMMON_COLUMN_CLASS_NAME:
                cursor = database.query(TABLE_CLASS_LIST, new String[]{COMMON_COLUMN_ID, COMMON_COLUMN_CLASS_NAME}, null, null, null, null, null, "3");
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(COMMON_COLUMN_CLASS_NAME)));
                }
                break;
            case COMMON_COLUMN_CLASS_TYPE:
                cursor = database.query(TABLE_CLASS_LIST, new String[]{COMMON_COLUMN_ID, COMMON_COLUMN_CLASS_TYPE}, null, null, null, null, null, "3");
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(COMMON_COLUMN_CLASS_TYPE)));
                }
                break;
            case COMMON_COLUMN_BATCH:
                cursor = database.query(TABLE_CLASS_LIST, new String[]{COMMON_COLUMN_ID, COMMON_COLUMN_BATCH}, null, null, null, null, null, "4");
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(COMMON_COLUMN_BATCH)));
                }
                break;
        }
        assert cursor != null;
        cursor.close();

        this.closeDB();
        return list;
    }

    public MatrixCursor displayAllStudents() {
        MatrixCursor list = new MatrixCursor(new String[]{"_id", "rollno", "name"});
        String id, rollno, name;
        String select = "SELECT * FROM " + TABLE_STUDENT;

        this.openDB();
        Cursor c = database.rawQuery(select, null);
        while (c.moveToNext()) {
            id = c.getString(c.getColumnIndex(COMMON_COLUMN_ID));

            rollno = c.getString(c.getColumnIndex(COMMON_COLUMN_ROLLNO));

            name = c.getString(c.getColumnIndex(STUDENT_COLUMN_LASTNAME));
            name = name + " " + c.getString(c.getColumnIndex(STUDENT_COLUMN_FIRSTNAME));
            name = name + " " + c.getString(c.getColumnIndex(STUDENT_COLUMN_MIDDLENAME));

            list.addRow(new Object[]{id, rollno, name});
        }

        c.close();

        this.closeDB();
        return list;
    }

    public MatrixCursor displayAllStudentsForClassToSelect(String class_id) {
        MatrixCursor list = new MatrixCursor(new String[]{"_id", "division", "name"});
        String id, rollno, division, name;

        String select = "SELECT " + TABLE_STUDENT + ".* FROM " + TABLE_STUDENT + " , " + TABLE_SAVED_CLASSES + " WHERE " + TABLE_SAVED_CLASSES + "." + COMMON_COLUMN_ID + " = " + class_id + " AND " + TABLE_STUDENT + "." + COMMON_COLUMN_DIVISION + " = " + TABLE_SAVED_CLASSES + "." + COMMON_COLUMN_DIVISION + " AND " + TABLE_STUDENT + "." + COMMON_COLUMN_YEAR + " = " + TABLE_SAVED_CLASSES + "." + COMMON_COLUMN_YEAR + " AND " + TABLE_STUDENT + "." + COMMON_COLUMN_CLASS_NAME + " = " + TABLE_SAVED_CLASSES + "." + COMMON_COLUMN_CLASS_NAME
                + " AND " + TABLE_STUDENT + "." + COMMON_COLUMN_ID + " NOT IN " + "(SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_CLASS_STUDENT + " WHERE " + TABLE_CLASS_STUDENT + "." + COLUMN_CLASS_ID + " = " + class_id + ") ORDER BY " + COMMON_COLUMN_ROLLNO + " , " + COMMON_COLUMN_DIVISION + " ASC ";


        this.openDB();
        Cursor c = database.rawQuery(select, null);
        while (c.moveToNext()) {
            id = c.getString(c.getColumnIndex(COMMON_COLUMN_ID));

            rollno = c.getString(c.getColumnIndex(COMMON_COLUMN_ROLLNO));
            division = c.getString(c.getColumnIndex(COMMON_COLUMN_DIVISION));
            division = division + "-" + rollno;

            name = c.getString(c.getColumnIndex(STUDENT_COLUMN_LASTNAME));
            name = name + " " + c.getString(c.getColumnIndex(STUDENT_COLUMN_FIRSTNAME));
            name = name + " " + c.getString(c.getColumnIndex(STUDENT_COLUMN_MIDDLENAME));

            list.addRow(new Object[]{id, division, name});
        }
        c.close();

        this.closeDB();
        return list;
    }

    public MatrixCursor displayStudentsOfClass(String class_id) {
        MatrixCursor list = new MatrixCursor(new String[]{"_id", "rollno", "name"});
        String id, rollno, name;

        String select = "SELECT " + TABLE_STUDENT + ".* FROM " + TABLE_STUDENT + " , " + TABLE_CLASS_STUDENT + " WHERE " + TABLE_CLASS_STUDENT + "." + COLUMN_CLASS_ID + " = " + class_id + " AND " + TABLE_CLASS_STUDENT + "." + COLUMN_STUDENT_ID + " = " + TABLE_STUDENT + "." + COMMON_COLUMN_ID + " ORDER BY " + COMMON_COLUMN_ROLLNO + " ASC ";

        this.openDB();
        Cursor c = database.rawQuery(select, null);
        while (c.moveToNext()) {
            id = c.getString(c.getColumnIndex(COMMON_COLUMN_ID));

            rollno = c.getString(c.getColumnIndex(COMMON_COLUMN_ROLLNO));

            name = c.getString(c.getColumnIndex(STUDENT_COLUMN_LASTNAME));
            name = name + " " + c.getString(c.getColumnIndex(STUDENT_COLUMN_FIRSTNAME));
            name = name + " " + c.getString(c.getColumnIndex(STUDENT_COLUMN_MIDDLENAME));

            list.addRow(new Object[]{id, rollno, name});
        }
        c.close();

        this.closeDB();
        return list;
    }

    public String setTitleClassName(String class_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_SAVED_CLASSES, null, COMMON_COLUMN_ID + " = " + class_id, null, null, null, null);

        //setTitle("D - TY B. Tech");
        c.moveToFirst();

        String className = c.getString(c.getColumnIndex(COMMON_COLUMN_DIVISION)) + " - " + c.getString(c.getColumnIndex(COMMON_COLUMN_YEAR)) + " " +
                c.getString(c.getColumnIndex(COMMON_COLUMN_CLASS_NAME));

        return className;

    }

    public void deleteStudentOfClass(String class_id, String student_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CLASS_STUDENT, COLUMN_CLASS_ID + " = " + class_id + " AND " + COLUMN_STUDENT_ID + " = " + student_id, null);
        db.close();

    }

    public Long markAttendance(String Class_id, String Student_id, String datetimestamp, int attendance) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CLASS_ID, Class_id);
        contentValues.put(COLUMN_STUDENT_ID, Student_id);
        contentValues.put(COLUMN_DATE_TIME_STAMP, datetimestamp);
        contentValues.put(COLUMN_ATTENDANCE, attendance);

        Long id = db.insert(TABLE_ATTENDANCE, null, contentValues);

        db.close();
        return id;

    }

    public void unmarkedAttendance(String Class_id, String datetimestamp, int attendance) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        String select = "SELECT DISTINCT " + TABLE_CLASS_STUDENT + "." + COMMON_COLUMN_ID + " , " + TABLE_CLASS_STUDENT + "." + COLUMN_STUDENT_ID + " FROM " + TABLE_CLASS_STUDENT + " , " + TABLE_ATTENDANCE + " WHERE " + TABLE_CLASS_STUDENT + "." + COLUMN_CLASS_ID + " = " + Class_id
                + " AND " + TABLE_CLASS_STUDENT + "." + COLUMN_STUDENT_ID + " NOT IN " + "(SELECT " + TABLE_ATTENDANCE + "." + COLUMN_STUDENT_ID + " FROM " + TABLE_ATTENDANCE + " WHERE " + TABLE_ATTENDANCE + "." + COLUMN_CLASS_ID + " = " + Class_id + " AND " + TABLE_ATTENDANCE + "." + COLUMN_DATE_TIME_STAMP + " = '" + datetimestamp + "' ) ORDER BY " + TABLE_CLASS_STUDENT + "." + COLUMN_STUDENT_ID + " ASC ";


        Cursor c = db.rawQuery(select, null);

        while (c.moveToNext()) {
            contentValues.put(COLUMN_CLASS_ID, Class_id);
            contentValues.put(COLUMN_STUDENT_ID, c.getString(c.getColumnIndex(COLUMN_STUDENT_ID)));
            contentValues.put(COLUMN_DATE_TIME_STAMP, datetimestamp);
            contentValues.put(COLUMN_ATTENDANCE, attendance);

            db.insert(TABLE_ATTENDANCE, null, contentValues);
        }
        c.close();

        db.close();
    }

    public int getTotalAttendance(String Class_id, String Student_id) {
        int total;
        SQLiteDatabase db = this.getWritableDatabase();

        String select = "SELECT SUM(" + COLUMN_ATTENDANCE + ") FROM " + TABLE_ATTENDANCE + " WHERE " + COLUMN_CLASS_ID + " = " + Class_id
                + " AND " + COLUMN_STUDENT_ID + " = " + Student_id + " AND " + COLUMN_ATTENDANCE + " = 1";

        Cursor c = db.rawQuery(select, null);

        if (c.moveToFirst())
            total = c.getInt(0);
        else
            total = -1;


        db.close();
        return total;

    }


    public void parseAndSaveCSV(String fileName) {
        SQLiteDatabase db = this.getWritableDatabase();
        FileReader file = null;

        try {
            file = new FileReader(fileName);

            BufferedReader buffer = new BufferedReader(file);
            String line = "";

            String columns = COMMON_COLUMN_GRNO + ", " + STUDENT_COLUMN_LASTNAME + ", " + STUDENT_COLUMN_FIRSTNAME + ", " + STUDENT_COLUMN_MIDDLENAME + ", "
                    + COMMON_COLUMN_ROLLNO + ", " + COMMON_COLUMN_DIVISION + ", " + COMMON_COLUMN_YEAR + ", " + COMMON_COLUMN_CLASS_NAME;

            String str1 = "INSERT INTO " + DatabaseHelper.TABLE_STUDENT + " (" + columns + ") values(";
            String str2 = ");";

            db.beginTransaction();
            line = buffer.readLine();//read 1st line that is column
            while ((line = buffer.readLine()) != null) {
                String[] str = line.split(",");

                StringBuilder sb = new StringBuilder(str1);

                sb.append("'" + str[1] + "', ");
                sb.append("'" + str[2] + "', ");
                sb.append("'" + str[3] + "', ");
                sb.append("'" + str[4] + "', ");
                sb.append("'" + str[5] + "', ");
                sb.append("'" + str[6] + "', ");
                sb.append("'" + str[7] + "', ");
                sb.append("'" + str[8] + "'");
                sb.append(str2);

                db.execSQL(sb.toString());
            }
            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public MatrixCursor displaySavedClasses() {
        MatrixCursor list = new MatrixCursor(new String[]{"_id", "division", "class"});

        String id, division, classDetails, batch = "", tempBatch;

        String select = "SELECT * FROM " + TABLE_SAVED_CLASSES;

        this.openDB();

        Cursor c = database.rawQuery(select, null);
        while (c.moveToNext()) {
            id = c.getString(c.getColumnIndex(COMMON_COLUMN_ID));

            classDetails = "(" + c.getString(c.getColumnIndex(COMMON_COLUMN_CLASS_TYPE)) + ") ";
            classDetails = classDetails + " " + c.getString(c.getColumnIndex(COMMON_COLUMN_YEAR));
            classDetails = classDetails + " " + c.getString(c.getColumnIndex(COMMON_COLUMN_CLASS_NAME));

            tempBatch = c.getString(c.getColumnIndex(COMMON_COLUMN_BATCH));
            if (tempBatch != null && tempBatch.matches("\\d")) {
                batch = "-" + tempBatch;
            }

            division = c.getString(c.getColumnIndex(COMMON_COLUMN_DIVISION)) + batch;

            batch = "";

            list.addRow(new Object[]{id, division, classDetails});
        }

        c.close();

        this.closeDB();
        return list;
    }


    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }
}
