package com.example.ashwin.hajeri.studentMasterList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.ashwin.hajeri.R;
import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;
import com.example.ashwin.hajeri.helperFunctions.RandomColor;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStudentListFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StudentListFragment extends Fragment implements View.OnClickListener {
    ListView listview;
    DatabaseHelper db;
    SimpleCursorAdapter adaptRollNo;
    MatrixCursor rollNo;

    ProgressDialog pd;

    ImageButton bAddNewStudent;
    View rootView;
    private OnStudentListFragmentInteractionListener listener;

    public StudentListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_student_list, container, false);

        getActivity().setTitle("Students");

        listview = (ListView) rootView.findViewById(R.id.lvStudentList);
        listview.setEmptyView(rootView.findViewById(R.id.empty));

        bAddNewStudent = (ImageButton) rootView.findViewById(R.id.ibAddNewStudent);
        bAddNewStudent.setOnClickListener(this);

        db = new DatabaseHelper(getActivity());

        new populateListAsync().execute();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnStudentListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAddStudentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        listener.onStudentListFragmentAddStudentClick();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStudentListFragmentInteractionListener {
        void onStudentListFragmentInteraction(String id);

        void onStudentListFragmentAddStudentClick();
    }

    private class populateListAsync extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getContext());
            pd.setMessage("Loading...");
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            rollNo = db.displayAllStudents();

            if (Looper.myLooper() == null)
                Looper.prepare();


            adaptRollNo = new SimpleCursorAdapter(getActivity(), R.layout.row_for_roll_no_name, rollNo, new String[]{"rollno", "name"}, new int[]{R.id.tvRoll, R.id.ctvName});

            adaptRollNo.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() != R.id.tvRoll)
                        return false;

                    GradientDrawable background = (GradientDrawable) view.getBackground();

                    background.setColor(new RandomColor(getActivity()).getRandomColor(cursor.getString(columnIndex)));

                    ((TextView) view).setText(cursor.getString(columnIndex));
                    return true;
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            listview.setAdapter(adaptRollNo);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onStudentListFragmentInteraction(String.valueOf(id));
                }
            });

            pd.dismiss();
        }
    }

}
