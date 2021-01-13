package com.nishanth.mnnit_admin.Notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nishanth.mnnit_admin.Faculty.UpdateFaculty;
import com.nishanth.mnnit_admin.Faculty.UpdateFacultyUpdateTeacher;
import com.nishanth.mnnit_admin.MainActivity;
import com.nishanth.mnnit_admin.R;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoticeActivity extends AppCompatActivity {

    private RecyclerView deletenoticerecyclerview;
    private ProgressBar progressBar;
    private List<NoticeData> list;
    private NoticeAdapter adapter;
    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);
        deletenoticerecyclerview=findViewById(R.id.deletenoticerecyclerview);
        progressBar=findViewById(R.id.progressBar);
        deletenoticerecyclerview.setLayoutManager(new LinearLayoutManager(this));
        deletenoticerecyclerview.setHasFixedSize(true);
        reference= FirebaseDatabase.getInstance().getReference().child("Notice");
        getNotice();


    }
    private void getNotice()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   list=new ArrayList<>();
                   for(DataSnapshot datasnapshot: snapshot.getChildren())
                   {
                       NoticeData data=datasnapshot.getValue(NoticeData.class);
                       list.add(data);
                   }
                   if(list.size()==0)
                   {
                    Toast.makeText(DeleteNoticeActivity.this, "You have no notices to delete. Upload atleast a notice and try.For now, please go back...!", Toast.LENGTH_LONG).show();



                   }else {
                       adapter = new NoticeAdapter(DeleteNoticeActivity.this, list);
                       adapter.notifyDataSetChanged();
                       progressBar.setVisibility(View.GONE);
                       deletenoticerecyclerview.setAdapter(adapter);
                   }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}