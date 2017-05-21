package com.example.android.matala3;

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

//    private static final String TAG = "MODEL";

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Student> studentList;

//    int image = R.drawable.c;

    void initialize(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("students");

        studentList = new ArrayList<>();


//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Student> std  = (ArrayList<Student>) dataSnapshot.getValue();
//                studentList = (ArrayList<Student>) std;
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

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
