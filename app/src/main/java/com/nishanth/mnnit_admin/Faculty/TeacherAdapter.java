package com.nishanth.mnnit_admin.Faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nishanth.mnnit_admin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewAdapter>{
    private List<TeacherData> list;
    private Context context;
    private String department;

    public TeacherAdapter(List<TeacherData> teacherdata, Context context, String department) {
        this.list = teacherdata;
        this.context = context;
        this.department=department;
    }

    @NonNull
    @Override
    public TeacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.faculty_item_layout, parent, false);

        return new TeacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeacherViewAdapter holder, int position) {

        final TeacherData item=list.get(position);
        holder.name.setText(item.getName());
        holder.post.setText(item.getPost());
        holder.email.setText(item.getEmail());

        try {
            Picasso.get().load(item.getImageUrl()).into(holder.imageView);
        }catch (Exception e)
        {
         e.printStackTrace();
        }


        holder.updateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UpdateFacultyUpdateTeacher.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("email", item.getEmail());
                intent.putExtra("post", item.getPost());
               intent.putExtra("image", item.getImageUrl());
               intent.putExtra("key", item.getKey());
               intent.putExtra("department", department);
               context.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class TeacherViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, email, post;
        private Button updateinfo;
        private ImageView imageView;
        public TeacherViewAdapter(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.teacherName);
            email=itemView.findViewById(R.id.teacherEmail);
            post=itemView.findViewById(R.id.teacherPost);
            updateinfo=itemView.findViewById(R.id.teacherUpdate);
            imageView=itemView.findViewById(R.id.teacherImage);



        }
    }
}
