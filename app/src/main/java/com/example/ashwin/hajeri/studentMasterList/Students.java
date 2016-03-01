package com.example.ashwin.hajeri.studentMasterList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ashwin.hajeri.R;


public class Students extends AppCompatActivity implements StudentListFragment.OnStudentListFragmentInteractionListener, AddStudentFragment.OnAddStudentFragmentInteractionListener, DetailStudentFragment.OnDetailStudentFragmentInteractionListener, EditStudentFragment.OnEditStudentFragmentInteractionListener {

    public static String BUNDLE_KEY = "BUNDLE_KEY";
    Bundle args;
    private String STUDENT_LIST_FRAGMENT_TAG = "studentListFragment";
    private String DETAIL_LIST_FRAGMENT_TAG = "detailListFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_container);
        setTitle("Students");

        if (savedInstanceState == null) {
            StudentListFragment studentListFragment = new StudentListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, studentListFragment, STUDENT_LIST_FRAGMENT_TAG).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_students, menu);

        menu.findItem(R.id.action_edit).setVisible(false);

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
    public void onStudentListFragmentInteraction(String id) {
        setTitle("");

        DetailStudentFragment detailStudentFragment = new DetailStudentFragment();

        args = new Bundle();
        args.putString(BUNDLE_KEY, id);
        detailStudentFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailStudentFragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public void onStudentListFragmentAddStudentClick() {
        AddStudentFragment addStudents = new AddStudentFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, addStudents).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public void onAddStudentFragmentInteraction() {
        StudentListFragment studentListFragment = new StudentListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, studentListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public void onDetailStudentFragmentInteraction(String id) {
        EditStudentFragment editStudentFragment = new EditStudentFragment();
        args = new Bundle();
        args.putString(BUNDLE_KEY, id);
        editStudentFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editStudentFragment).addToBackStack(DETAIL_LIST_FRAGMENT_TAG).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public void onEditStudentFragmentInteraction(String id) {

        setTitle("");
        onBackPressed();

        /*
        //pop backStack
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2).getName();
        if (tag.equals(DETAIL_LIST_FRAGMENT_TAG)) {
            getSupportFragmentManager().popBackStackImmediate(DETAIL_LIST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
           // getSupportFragmentManager().executePendingTransactions();
        }

        DetailStudentFragment detailStudentFragment = new DetailStudentFragment();

        args = new Bundle();
        args.putString(BUNDLE_KEY, id);
        detailStudentFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailStudentFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        */
    }
}
