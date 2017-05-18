package com.example.android.matala3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

import static android.R.color.transparent;

public class NewStudent extends AppCompatActivity {

    Button cancel;
    Button save;
    Button delete;

    EditText name;
    EditText line;


    Intent resultIntent = new Intent();

    CheckBox cb;

    ImageView image;

    MyTimePicker timePicker;
    MyTimePicker datePicker;

    int fileCount=0;
//    String fileName;

    Random rand = new Random();
    Bitmap selectedImage;

    public static final int RESULT_LOAD_IMG = 420;
    private static final int editExist=4201;
    private static final int DELETE_STUDENT=134201;

    private int result = Activity.RESULT_OK;

    int [] randImages = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);
        setTitle("Add Student");

        cancel = (Button) findViewById(R.id.button);
        save = (Button) findViewById(R.id.button2);
        delete = (Button)findViewById(R.id.button_delete);

        name = (EditText) findViewById(R.id.editText);
        line = (EditText) findViewById(R.id.editText2);

        cb = (CheckBox) findViewById(R.id.checkBox);
        image = (ImageView) findViewById(R.id.imageView);

        timePicker = (MyTimePicker) findViewById(R.id.main_input_time);
        timePicker.setClock(true);

        datePicker = (MyTimePicker) findViewById(R.id.main_input_time2);
        datePicker.setClock(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = Activity.RESULT_CANCELED;
                endActivity();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileCount++;
//                fileName= fileCount+".png";

                int i = rand.nextInt(3)+1;
                int img = randImages[i];

                Bundle conData = new Bundle();

                if(name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), R.string.STUDENT_NAME_REQUIRED, Toast.LENGTH_SHORT).show();

                }else if(datePicker.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), R.string.STUDENT_BDAY_REQUIRED, Toast.LENGTH_SHORT).show();

                }else{
                    conData.putString("name", name.getText().toString());
                    if(line.getText().toString().equals("")){
                        Random rand = new Random();
                        conData.putString("id", fileCount+rand.nextInt(419) +"");
                    }else{
                        conData.putString("id", line.getText().toString());
                    }
                    conData.putString("date",datePicker.getText().toString());
                    conData.putString("time",timePicker.getText().toString());
                    conData.putBoolean("bool", cb.isChecked());
                    conData.putInt("img",img);

                    resultIntent.putExtras(conData);
                    resultIntent.putExtra("bitmap",selectedImage);


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage(R.string.OPERATION_SUCCESS).setPositiveButton(R.string.OK, operationSuccessClickListener).show();


                }

            }
        });
        delete.setVisibility(View.INVISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(R.string.CONFIRM).setPositiveButton(R.string.YES, dialogClickListener)
                        .setNegativeButton(R.string.NO, dialogClickListener).show();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        if(getIntent().getBooleanExtra(getString(R.string.STUDENT_EXIST_IN_LIST),false)){
            editStudent();
        }
        else if(getIntent().getBooleanExtra(getString(R.string.SHOW_STUDENT_DETAILS),false)){
            studentDetails();
        }
    }
    public void editStudent(){
        setTitle(getString(R.string.EDIT_STUDENT));

        String a = getIntent().getStringExtra("title");
        String b = getIntent().getStringExtra("id");
        int c = getIntent().getIntExtra("image",R.drawable.d);
        Boolean d = getIntent().getBooleanExtra("checkBox",false);
        String e = getIntent().getStringExtra("date");
        String f = getIntent().getStringExtra("time");

        name.setText(a);
        line.setText(b);
        image.setImageResource(c);
        cb.setChecked(d);
        datePicker.setText(e);
        timePicker.setText(f);

        delete.setVisibility(View.VISIBLE);

        result = editExist;
        int pos = getIntent().getIntExtra("pos",0);
        resultIntent.putExtra("pos",pos);
    }
    public void studentDetails(){
        setTitle("Student Details");

        String a = getIntent().getStringExtra("title");
        String b = getIntent().getStringExtra("id");
        int c = getIntent().getIntExtra("image",R.drawable.d);
        Boolean d = getIntent().getBooleanExtra("checkBox",false);
        String e = getIntent().getStringExtra("date");
        String f = getIntent().getStringExtra("time");

        name.setText(a);
        line.setText(b);
        image.setImageResource(c);
        cb.setChecked(d);
        datePicker.setText(e);
        timePicker.setText(f);


        name.setBackgroundColor(getResources().getColor(transparent));
        name.setClickable(false);
        name.setKeyListener(null);

        line.setBackgroundColor(getResources().getColor(transparent));
        line.setClickable(false);
        line.setKeyListener(null);

        image.setBackgroundColor(getResources().getColor(transparent));
        image.setClickable(false);

        cb.setBackgroundColor(getResources().getColor(transparent));
        cb.setClickable(false);
        cb.setKeyListener(null);

        datePicker.setBackgroundColor(getResources().getColor(transparent));
        datePicker.setClickable(false);
        datePicker.setKeyListener(null);
        datePicker.setOnClickListener(null);

        timePicker.setBackgroundColor(getResources().getColor(transparent));

        delete.setVisibility(View.VISIBLE);
        save.setText("Edit");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editStudent();
//
//                name.setBackgroundColor(getResources().getColor(transparent));
//                name.setClickable(true);
////                name.setKeyListener(null);
//
//                line.setBackgroundColor(getResources().getColor(transparent));
//                line.setClickable(true);
////                line.setKeyListener(null);
//
//                image.setBackgroundColor(getResources().getColor(transparent));
//                image.setClickable(true);
////            image.lis(null);
//
//                cb.setBackgroundColor(getResources().getColor(transparent));
//                cb.setClickable(true);
////                cb.setKeyListener(null);
//
//                datePicker.setBackgroundColor(getResources().getColor(transparent));
//                datePicker.setClickable(true);
//
//                timePicker.setBackgroundColor(getResources().getColor(transparent));
//
//                delete.setVisibility(View.VISIBLE);
//                save.setText("Save");
            }
        });

    }

    public void endActivity(){
        if (getParent() == null) {
            setResult(result, resultIntent);
        } else {
            getParent().setResult(result, resultIntent);
        }
        finish();
    }
    DialogInterface.OnClickListener operationSuccessClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //OK button clicked
                    endActivity();
                    break;

            }
        }
    };

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked

                    Bundle conData = new Bundle();

                    int pos = getIntent().getIntExtra("pos",0);
                    conData.putInt("pos",pos);
                    conData.putString("title",name.getText().toString());
                    result = DELETE_STUDENT;

//                    resultIntent = new Intent();
//                    resultIntent.putExtras(conData);

                    endActivity();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.ERROR, Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, R.string.NO_IMAGE_SELECTED,Toast.LENGTH_LONG).show();
        }
    }
}
