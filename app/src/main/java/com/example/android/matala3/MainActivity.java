package com.example.android.matala3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView list;

    ArrayList<String> title;
    ArrayList<String> descriptions;
    ArrayList<String> dates;
    ArrayList<String> times;
    ArrayList<Boolean> checkBoxes;
    ArrayList<Integer> imgs;
//    ArrayList<Bitmap> bitmaps;

    MyAdapter adapter;
    int studentListCount = 4;

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;
    private static final int editExist=4201;
    private static final int DELETE_STUDENT=134201;

    BottomNavigationView navigation;
    private int selectedItemFromList;

    //firebase
    Model model;
    ArrayList<Student> students;

//private void openFragment(){
//    StudentDetailsFragment nextFrag= new StudentDetailsFragment();
//    this.getFragmentManager().beginTransaction()
////            .add(nextFrag,"")
//            .addToBackStack(null)
//            .commit();
//}

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showList();
                    return true;
//                case R.id.navigation_dashboard:
////                    hideList();
//                    students = model.getStudents();//refresh list
//                    if(!students.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "aa"+students.size(), Toast.LENGTH_SHORT).show();
//                    }
//                    return true;
                case R.id.navigation_notifications:
                    hideList();
                    showAddStudent();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(getApplicationContext(), R.string.ON_START_MESSAGE,Toast.LENGTH_LONG).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        getActionBar().setDisplayHomeAsUpEnabled(false);


        list = (ListView) findViewById(R.id.list);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                editStudent(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(R.string.DELETE_STUDENT).setPositiveButton(R.string.YES, dialogClickListener)
                        .setNegativeButton(R.string.NO, dialogClickListener).show();
                selectedItemFromList = i;
                return false;

            }
        });
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    removeStudent(selectedItemFromList);
                    Toast.makeText(getApplicationContext(), R.string.STUDENT_DELETED,Toast.LENGTH_LONG).show();

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
    public void showList(){
        students = model.getStudents();
        adapter = new MyAdapter(this,students);

        list.setAdapter(adapter);
        list.setVisibility(View.VISIBLE);
    }

    public void hideList(){
        list.setVisibility(View.INVISIBLE);
    }

    public void addStudent(Bundle res){
        studentListCount++;
        String a = res.getString("name");
        String b = res.getString("id");
        int c = res.getInt("img");
        Boolean d = res.getBoolean("bool");
        String e = res.getString("date");
        String f = res.getString("time");

        Student newStudent = new Student(a,b,c,d,e,f);
        if(model.addStudent(newStudent)){
            adapter.notifyDataSetChanged();
        }
    }

    public void removeStudent(int pos){
        if(model.removeStudent(students.get(pos).title)){
            adapter.notifyDataSetChanged();
            studentListCount--;
            students.remove(pos);
            showList();
        }

    }

    public void showAddStudent(){
        //move to another activity
        Intent intent = new Intent(this, NewStudent.class);
        intent.putExtra(getString(R.string.STUDENT_EXIST_IN_LIST),false);
        startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);


    }

    public void showStudentDetails(int position){
        //move to another activity
        Intent intent = new Intent(this, NewStudent.class);
        intent.putExtra(getString(R.string.SHOW_STUDENT_DETAILS),true);

        Bundle ret = adapter.getIntentData(position);

        intent.putExtras(ret);
        intent.putExtra("pos",position);

        startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);
    }

    public void editStudent(int position){
        //move to another activity
        Intent intent = new Intent(this, NewStudent.class);
        intent.putExtra(getString(R.string.STUDENT_EXIST_IN_LIST),true);

        Bundle ret = adapter.getIntentData(position);

        intent.putExtras(ret);
        intent.putExtra("pos",position);

        startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SECOND_ACTIVITY_RESULT_CODE) {
            navigation.setSelectedItemId(R.id.navigation_home);
            if (resultCode == RESULT_OK) {
                Bundle res = data.getExtras();
//                int pos = res.getInt("pos");
//                Bitmap bitmap = data.getParcelableExtra("bitmap");
//                bitmaps.add(bitmap);
                addStudent(res);
                showList();
            }
            else if (resultCode == editExist){
                Bundle res = data.getExtras();
                int pos = res.getInt("pos");
                removeStudent(pos);
                addStudent(res);
            }
            else if(resultCode == DELETE_STUDENT){
                Bundle res = data.getExtras();
                int pos = res.getInt("pos");
                removeStudent(pos);
            }
        }
    }
    class MyAdapter extends ArrayAdapter<String>{
        Context context;


        MyAdapter(Context c,ArrayList<Student> studentList){
            super(c,R.layout.row,R.id.text1,(List)studentList);
            this.context = c;
//            this.students = studentList;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row,parent,false);
            row.setClickable(true);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getApplicationContext(),"getDetails("+position+");",Toast.LENGTH_LONG).show();
                    editStudent(position);
//                    showStudentDetails(position);

                }
            });
            ImageView myImage = (ImageView) row.findViewById(R.id.icon);
            TextView myTitle = (TextView) row.findViewById(R.id.text1);
            TextView myDesc = (TextView) row.findViewById(R.id.text2);
            CheckBox myCheckBox = (CheckBox) row.findViewById(R.id.checkBox);


            StringBuilder sb = new StringBuilder(students.get(position).date);

            int day =  Integer.parseInt(sb.substring(0,2).replace("/",""));
            int month =  Integer.parseInt(sb.substring(3,4).replace("/",""));
            int year = Integer.parseInt(sb.substring(5).replace("/",""));

            int studentAge = Integer.parseInt(getAge(year,month,day));

            if(studentAge>0){
                myTitle.setText(students.get(position).title+", "+studentAge);
            }else{
                myTitle.setText(students.get(position).title);
            }

            myImage.setImageResource(students.get(position).image);
//                myImage.setImageResource(imgs.get(position));
////                myImage.setImageBitmap(bitmaps.get(position-4));
            myDesc.setText(students.get(position).description);
            myCheckBox.setChecked(students.get(position).checkBox);
            return row;

        }

    /**
     * Method to extract the user's age from the entered Date of Birth.
     *
     * @param DoB String The user's date of birth.
     *
     * @return ageS String The user's age in years based on the supplied DoB.
     */
        private String getAge(int year, int month, int day){
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.set(year, month, day);

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                age--;
            }

            Integer ageInt = new Integer(age);
            String ageS = ageInt.toString();

            return ageS;
        }
        public Bundle getIntentData(int position){
             Bundle student = new Bundle();
             student.putInt("image",students.get(position).image);
             student.putString("title",students.get(position).title);
             student.putString("id",students.get(position).description);
             student.putString("date",students.get(position).date);
             student.putString("time",students.get(position).time);
             student.putBoolean("checkBox",students.get(position).checkBox);

             return student;
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        model = new Model();
        model.initialize();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
