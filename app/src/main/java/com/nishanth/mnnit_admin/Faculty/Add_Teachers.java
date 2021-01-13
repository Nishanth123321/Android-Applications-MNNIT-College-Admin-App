package com.nishanth.mnnit_admin.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nishanth.mnnit_admin.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Add_Teachers extends AppCompatActivity {


    private ImageView addTeacherImage;
    private final int REQ=4;
    private Spinner addTeacherCategory;
    private Bitmap bitmap=null;
    private String department;
    private String name, email, post, downloadUrl="";
    private EditText addTeacherEmail;
    private EditText addTeacherPost;
    private EditText addTeacherName;
    private DatabaseReference dbReference;
    private StorageReference storageReference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__teachers);
         addTeacherName = findViewById(R.id.addTeacherName);
        addTeacherImage=findViewById(R.id.addTeacherImage);
      addTeacherPost = findViewById(R.id.addTeacherPost);
        addTeacherCategory=findViewById(R.id.addTeacherCategory);
        Button addTeacherBtn = findViewById(R.id.addTeacherBtn);
          addTeacherEmail=findViewById(R.id.addTeacherEmail);
         dbReference= FirebaseDatabase.getInstance().getReference();
         storageReference= FirebaseStorage.getInstance().getReference();
         pd=new ProgressDialog(Add_Teachers.this);
         pd.setTitle("Please wait...!");
         pd.setMessage("Uploading...");
        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        String[] departments= new String[]{"Select a department", "Applied Mechanics", "Biotechnology", "Chemical Engineering", "Civil Engineering",
                      "Computer Science and Engineering", "Electrical Engineering", "Electronics and Comm Engineering", "Mechanical Engineering",
                       "Chemistry", "Mathematics", "Physics", "School of Management Studies", "Humanities and Social Sciences"};

        addTeacherCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, departments));
        addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                department=addTeacherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 checkValidation();

            }
        });
    }

    private void checkValidation() {
        name=addTeacherName.getText().toString();
        email=addTeacherEmail.getText().toString();
        post=addTeacherPost.getText().toString();
        if(name.isEmpty())
        {
            addTeacherName.setError("Can't be empty");
            addTeacherName.requestFocus();
        }
        else if(email.isEmpty())
        {
            addTeacherEmail.setError("Can't be empty");
            addTeacherEmail.requestFocus();
        }

        else if(post.isEmpty())
        {
            addTeacherPost.setError("Can't be empty");
            addTeacherPost.requestFocus();
        }
        else if(department.equals("Select a department"))
        {
            Toast.makeText(Add_Teachers.this, "Select the department of teacher...!", Toast.LENGTH_SHORT).show();

        }
        else if(bitmap==null)
        {
            pd.show();
           insertData();
        }
        else
        {
            pd.show();
          insertImage();
        }

    }

    private void insertData() {

        dbReference=dbReference.child("Faculty").child(department);
        String key=dbReference.push().getKey();
        TeacherData teacherdata;
        if(downloadUrl=="")
        {
            teacherdata=new TeacherData(name, email, post,  key);
        }
        else
        {
            teacherdata=new TeacherData(name, email, post,  key, downloadUrl );
        }

        dbReference.child(key).setValue(teacherdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(Add_Teachers.this, "Faculty data is uploaded successfully...!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Add_Teachers.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void insertImage() {

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
        byte[] finalimage=baos.toByteArray();
        final StorageReference filepath=storageReference.child("Faculty").child(finalimage+"jpg");
       final UploadTask uploadtask=filepath.putBytes(finalimage);
        uploadtask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    uploadtask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);

                                    insertData();
                                }
                            });
                        }
                    });
                }
                else
                {
                    pd.dismiss();
                    Toast.makeText(Add_Teachers.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                  pd.dismiss();
                Toast.makeText(Add_Teachers.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void openGallery()
    {
        Intent pickimage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage, REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addTeacherImage.setImageBitmap(bitmap);
        }
    }


}