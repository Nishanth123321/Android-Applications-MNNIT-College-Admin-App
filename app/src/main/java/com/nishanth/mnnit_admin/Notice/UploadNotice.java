package com.nishanth.mnnit_admin.Notice;

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
import com.nishanth.mnnit_admin.MainActivity;
import com.nishanth.mnnit_admin.R;
import com.nishanth.mnnit_admin.UploadImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadNotice extends AppCompatActivity {

    private CardView addImage;
    private final int REQ=1;
    private Bitmap bitmap;
    private ImageView noticeimageview;
    private EditText noticeTitle;
    private Button uploadNoticeBtn;
    private DatabaseReference dbReference;
    private StorageReference storageReference;
    private String downloadUrl="";
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);
        addImage=findViewById(R.id.addImage);
        noticeimageview=findViewById(R.id.noticeImageView);
        noticeTitle =findViewById(R.id.noticeTitle);
        uploadNoticeBtn=findViewById(R.id.uploadNoticeBtn);
        dbReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
        pd=new ProgressDialog(this);
        uploadNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(noticeTitle.getText().toString().isEmpty())
              {
                  noticeTitle.setError("Can't be empty");
                  noticeTitle.requestFocus();
              }else if(bitmap==null)
              {
                  uploadData();
              }else
              {
                 uploadImage();
              }


            }
        });

    }

    private void uploadData() {
        dbReference=dbReference.child("Notice");
        String key=dbReference.push().getKey();
        Calendar cal= Calendar.getInstance();
        String title=noticeTitle.getText().toString();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MM-yyyy");
        String date=currentDate.format(cal.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("hh-mm a");
        cal=Calendar.getInstance();
        String time=currentTime.format(cal.getTime());
     NoticeData noticeData=new NoticeData(title, downloadUrl, date, time, key);
     dbReference.child(key).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
         @Override
         public void onSuccess(Void aVoid) {
             pd.dismiss();
             Toast.makeText(UploadNotice.this, "Notice uploaded successfully...!", Toast.LENGTH_SHORT).show();

             Intent intent=new Intent(UploadNotice.this, MainActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(intent);
         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             pd.dismiss();
             Toast.makeText(UploadNotice.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
         }
     });

    }

    private void uploadImage() {
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimage=baos.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("com/nishanth/mnnit_admin/Notice").child(finalimage+"jpg");
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
                    Toast.makeText(UploadNotice.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
                }
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
             noticeimageview.setImageBitmap(bitmap);

        }

    }
}