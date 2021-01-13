package com.nishanth.mnnit_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nishanth.mnnit_admin.Faculty.UpdateFaculty;

import com.nishanth.mnnit_admin.Notice.DeleteNoticeActivity;
import com.nishanth.mnnit_admin.Notice.UploadNotice;

public class MainActivity extends AppCompatActivity {


    CardView uploadnotice, uploadimage, uploadpdf, faculty, deletenotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadnotice=findViewById(R.id.addNotice);
        uploadimage=findViewById(R.id.addGalleryImage);
        uploadpdf=findViewById(R.id.addEbook);
        faculty=findViewById(R.id.faculty);
        deletenotice=findViewById(R.id.deleteNotice);
        uploadnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent= new Intent(MainActivity.this, UploadNotice.class);
             startActivity(intent);
            }
        });
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, UploadImage.class);
                startActivity(intent);
            }
        });
        uploadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, UploadPdf.class);
                startActivity(intent);
            }
        });
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent);
            }
        });
        deletenotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, DeleteNoticeActivity.class);
                startActivity(intent);
            }
        });

    }



}