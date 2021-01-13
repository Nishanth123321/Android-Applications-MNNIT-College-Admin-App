package com.nishanth.mnnit_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.nishanth.mnnit_admin.Faculty.UpdateFaculty;
import com.nishanth.mnnit_admin.Faculty.UpdateFacultyUpdateTeacher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadImage extends AppCompatActivity {
    private Spinner imageCategory;
    private ImageView galleryImageView;
    private String category;
    private final int REQ=2;
    private Bitmap bitmap;
    private ProgressDialog pd;
    private DatabaseReference dbReference;
    private StorageReference storageReference;
    private String downloadUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        imageCategory=findViewById(R.id.image_category);
        CardView selectImage = findViewById(R.id.addGalleryImage);
        Button uploadImageBtn = findViewById(R.id.uploadImageBtn);
        galleryImageView=findViewById(R.id.galleryImageView);
        dbReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        String[] items= new String[]{"Select Category", "Convocation", "Independence day", "Other Events"};
         imageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));
         imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              category=imageCategory.getSelectedItem().toString();
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    openGallery();

            }
        });
        pd=new ProgressDialog(this);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap==null)
                {
                    Toast.makeText(UploadImage.this, "Please upload image...!", Toast.LENGTH_SHORT).show();

                }else if(category.equals("Select Category"))
                {
                    Toast.makeText(UploadImage.this, "Please select category of image...!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pd.setMessage("Uploading...");
                    pd.show();
                    uploadImage();


                }

            }
        });
    }

    private void uploadImage() {

        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimage=baos.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("Gallery").child(finalimage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);

                                    uploadData();
                                }
                            });
                        }
                    });
                }
                else
                {
                    pd.dismiss();
                    Toast.makeText(UploadImage.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void uploadData() {

        dbReference=dbReference.child("Gallery").child(category);
        String key=dbReference.push().getKey();


        dbReference.child(key).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(UploadImage.this, "Image uploaded successfully...!", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(UploadImage.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadImage.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
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

            }
            galleryImageView.setImageBitmap(bitmap);
        }

    }

}