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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateFacultyUpdateTeacher extends AppCompatActivity {

    private ImageView updateTeacherImage;
    private EditText updateTeacherName, updateTeacherEmail, updateTeacherPost;
    private int REQ=5;
    private Bitmap bitmap;
    private DatabaseReference dbReference;
    private StorageReference storageReference;
    private String downloadUrl="";
    private String department="";
    private  String name, email, post, imageurl, key;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty_update_teacher);
        updateTeacherImage=findViewById(R.id.updateTeacherImage);
        updateTeacherName=findViewById(R.id.updateTeacherName);
        updateTeacherEmail=findViewById(R.id.updateTeacherEmail);
        updateTeacherPost=findViewById(R.id.updateTeacherPost);
        Button updateTeacherBtn = findViewById(R.id.updateTeacherBtn);
        Button deleteTeacherBtn = findViewById(R.id.deleteTeacherBtn);
        pd=new ProgressDialog(UpdateFacultyUpdateTeacher.this);
        pd.setTitle("Please wait...!");
        pd.setMessage("Updating...");
        dbReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        Intent intent=getIntent();

        name=intent.getStringExtra("name");
        email=intent.getStringExtra("email");
        post=intent.getStringExtra("post");
        imageurl=intent.getStringExtra("image");
        key=intent.getStringExtra("key");
        department=intent.getStringExtra("department");
        try {
            Picasso.get().load(imageurl).into(updateTeacherImage);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        updateTeacherName.setText(name);
        updateTeacherEmail.setText(email);
        updateTeacherPost.setText(post);
         updateTeacherImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openGallery();
             }
         });
         updateTeacherBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                      checkValidation();
             }
         });

         deleteTeacherBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 deleteData();
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
            updateTeacherImage.setImageBitmap(bitmap);
        }
    }
    private void insertData()
    {

        pd.show();
        dbReference=dbReference.child("Faculty").child(department);
        HashMap hp=new HashMap();
          hp.put("name", name);
          hp.put("email", email);
          hp.put("post", post);
          hp.put("key", key);
          hp.put("imageUrl", downloadUrl);

        dbReference.child(key).updateChildren(hp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                pd.dismiss();
                Toast.makeText(UpdateFacultyUpdateTeacher.this, "Faculty data is updated successfully...!", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(UpdateFacultyUpdateTeacher.this, UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(UpdateFacultyUpdateTeacher.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void insertImage()
    {

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

                    Toast.makeText(UpdateFacultyUpdateTeacher.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UpdateFacultyUpdateTeacher.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
            }
        });




    }
    private void deleteData()
    {
            dbReference.child("Faculty").child(department).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(UpdateFacultyUpdateTeacher.this, "Faculty data is deleted successfully...!", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(UpdateFacultyUpdateTeacher.this, UpdateFaculty.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateFacultyUpdateTeacher.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();

                }
            });
    }
    private void checkValidation() {
        String name=updateTeacherName.getText().toString();
       String  email=updateTeacherEmail.getText().toString();
        String post=updateTeacherPost.getText().toString();
        if(name.isEmpty())
        {
            updateTeacherName.setError("Can't be empty");
            updateTeacherName.requestFocus();
        }
        else if(email.isEmpty())
        {
            updateTeacherEmail.setError("Can't be empty");
            updateTeacherEmail.requestFocus();
        }

        else if(post.isEmpty())
        {
            updateTeacherPost.setError("Can't be empty");
            updateTeacherPost.requestFocus();
        }

        else if(bitmap==null)
        {
             downloadUrl=imageurl;
            insertData();
        }
        else
        {

            insertImage();
        }

    }
}