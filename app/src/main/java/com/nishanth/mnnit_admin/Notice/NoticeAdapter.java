package com.nishanth.mnnit_admin.Notice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nishanth.mnnit_admin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewAdapter> {

    private Context context;
    private List<NoticeData> list;
    private String key;

    public NoticeAdapter(Context context, List<NoticeData> noticeData) {
        this.context = context;
        this.list = noticeData;
    }

    @NonNull
    @Override
    public NoticeViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.newsfeed_item_layout, parent, false);
        return new NoticeViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewAdapter holder, final int position) {
        NoticeData ndata=list.get(position);
        try {
            if(!ndata.getImage().isEmpty())
            Picasso.get().load(ndata.getImage()).into(holder.deletenoticeimagepreview);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        key=ndata.getKey();
        holder.deletenoticetitle.setText(ndata.getTitle());
        holder.noticeDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setMessage("Are you sure want to delete this Notice?");
                builder.setCancelable(true);
                builder.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        final DatabaseReference reference,dbRef ;
                        reference= FirebaseDatabase.getInstance().getReference();
                        dbRef=reference.child("Notice");
                        dbRef.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Notice deleted successfully...!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Something went wrong...!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                AlertDialog dialog=null;
                try {
                     dialog=builder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(dialog!=null)
                dialog.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoticeViewAdapter extends RecyclerView.ViewHolder
    {
            private ImageView noticeimage, deletenoticeimagepreview;
            private TextView noticetitle, deletenoticetitle;
            private Button noticeDeleteBtn;



        public NoticeViewAdapter(@NonNull View itemView) {
            super(itemView);
            noticeDeleteBtn=itemView.findViewById(R.id.noticeDeleteBtn);
            noticetitle=itemView.findViewById(R.id.noticetitle);
            deletenoticetitle=itemView.findViewById(R.id.deletenoticetitle);
            noticeimage=itemView.findViewById(R.id.noticeimage);
            deletenoticeimagepreview=itemView.findViewById(R.id.deletenoticeimagepreview);

        }
    }
}
