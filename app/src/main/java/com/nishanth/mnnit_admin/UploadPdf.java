package com.nishanth.mnnit_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {


    private CardView addPdf;
    private final int REQ=3;
    private Uri pdfdata=null;

    private EditText pdfTitle;
    private Button uploadPdfBtn;
    private DatabaseReference dbReference;
    private StorageReference storageReference;
    private String downloadUrl="";
    private ProgressDialog pd;
    private String pdfName;
    private TextView pdftext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        addPdf=findViewById(R.id.addPdf);
          pdftext=findViewById(R.id.pdftext);
        pd=new ProgressDialog(this);
          pd.setMessage("Uploading...");
          pd.setTitle("Please wait..!");
        pdfTitle =findViewById(R.id.pdfTitle);
        uploadPdfBtn=findViewById(R.id.uploadPdfBtn);
        dbReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
        pdfTitle=findViewById(R.id.pdfTitle);

        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(UploadPdf.this, "Hi Nishanth...!", Toast.LENGTH_SHORT).show();

                 if(pdfTitle.getText().toString().isEmpty())
                 {
                     pdfTitle.setError("Empty");
                     pdfTitle.requestFocus();
                 }else if(pdfdata==null)
                 {
                     Toast.makeText(UploadPdf.this, "Please upload a pdf...!", Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                    uploadPdf();
                     //Toast.makeText(UploadPdf.this, "All Right...!", Toast.LENGTH_SHORT).show();
                 }
                 
            }
        });
    }

    private void uploadPdf() {

        pd.show();
        storageReference=storageReference.child("pdf/"+ pdfName+"-"+System.currentTimeMillis()+".pdf");
        storageReference.putFile(pdfdata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri>  uriTask=taskSnapshot.getStorage().getDownloadUrl();
                   while(!uriTask.isComplete());
                   Uri uri=uriTask.getResult();
                   String dwnldurl=String.valueOf(uri);

                //Toast.makeText(UploadPdf.this, "Just going into uploadData...!"+dwnldurl, Toast.LENGTH_LONG).show();
                   uploadData(dwnldurl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

              pd.dismiss();
               Toast.makeText(UploadPdf.this, "Something went wrong...!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadData(String downloadUrl) {



        String key=dbReference.child("pdf").push().getKey();
        HashMap Data=new HashMap();
        Data.put("pdfTitle", pdfTitle.getText().toString());
        Data.put("pdfUrl", downloadUrl);
       dbReference.child("pdf").child(key).setValue(Data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                    pd.dismiss();
                    Toast.makeText(UploadPdf.this, "Pdf uploaded successfully", Toast.LENGTH_SHORT).show();
                    pdfTitle.setText("");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(UploadPdf.this, "Failed to upload pdf...!", Toast.LENGTH_SHORT).show();
                pdfTitle.setText("");

            }
        });


    }

    public void openGallery()
    {

        Intent pickpdf=new Intent();
        pickpdf.setType("application/pdf");
        pickpdf.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(pickpdf,"Select Pdf file"), REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK)
        {

            pdfdata=data.getData();

            if(pdfdata.toString().startsWith("content://") )
            {
                Cursor  cursor=null;
                try {
                    cursor=UploadPdf.this.getContentResolver().query(pdfdata,null, null, null, null);
                    if(cursor!=null && cursor.moveToFirst())
                    {
                      pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(pdfdata.toString().startsWith("file://"))
            {
                pdfName=new File(pdfdata.toString()).getName();
            }
            pdftext.setText(pdfName);

        }

    }
}