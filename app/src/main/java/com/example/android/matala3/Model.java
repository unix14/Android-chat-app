package com.example.android.matala3;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyal on 18-May-17.
 */

public class Model {

    private static final String TAG = "MODEL";

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Student> studentList;

    int image = R.drawable.c;

    void initialize(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("students");
//        myRef.setValue("Hello, World!");

//        myRef.setValue();
//        myRef.child("1").setValue(new Student("A11","B11",image,true));
//        addStudent(new Student("A423","B413",image,true));
//        DatabaseReference newRef = myRef.child("title");
//        String s = newRef.getKey();
//        newRef.setValue(new Student(s+"1",s+"2",image,false));


        studentList = new ArrayList<>();


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Student std  = dataSnapshot.getValue(Student.class);
                if(!studentList.contains(std)) {
                    studentList.add(std);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Student std  = dataSnapshot.getValue(Student.class);
                if(studentList.contains(std)) {
                    studentList.remove(std);
                    studentList.add(std);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Student std  = dataSnapshot.getValue(Student.class);
                if(studentList.contains(std)) {
                    studentList.remove(std);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        studentList = getStudents();


//        Student s = getStudents();


//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
    }



    public boolean addStudent(Student s){
        return myRef.child(s.title).setValue(s).isSuccessful();
    }
    public boolean removeStudent(String name){
        return myRef.child(name).removeValue().isSuccessful();
    }
    ArrayList<Student> getStudents(){
        return studentList;
    }
}
