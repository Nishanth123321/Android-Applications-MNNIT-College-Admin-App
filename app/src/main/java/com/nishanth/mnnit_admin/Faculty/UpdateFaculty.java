package com.nishanth.mnnit_admin.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nishanth.mnnit_admin.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {


     FloatingActionButton fab;

    private RecyclerView appliedmechanicsDepartment,biotechDepartment, chemicalDepartment,civilDepartment,csDepartment,electricalDepartment, eceDepartment,mechDepartment,chemistryDepartment,mathsDepartment,physicsDepartment,managementstudiesDepartment,socialsciencesDepartment;

    private LinearLayout appliedmechanicsNoData,biotechNoData, chemicalNoData,civilNoData,csNoData,electricalNoData, eceNoData,mechNoData,chemistryNoData,mathsNoData,physicsNoData,managementstudiesNoData,socialsciencesNoData;
    private List<TeacherData> list1, list2, list3, list4, list5, list6, list7, list8, list9, list10, list11, list12, list13;
    private DatabaseReference reference, dbRef;
    private TeacherAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        fab=findViewById(R.id.fab);
        reference= FirebaseDatabase.getInstance().getReference().child("Faculty");
        appliedmechanicsDepartment=findViewById(R.id.appliedmechanicsDepartment);
        biotechDepartment=findViewById(R.id.biotechDepartment);
        chemicalDepartment=findViewById(R.id.chemicalDepartment);
        civilDepartment=findViewById(R.id.civilDepartment);
        csDepartment=findViewById(R.id.csDepartment);
        electricalDepartment=findViewById(R.id.electricalDepartment);
        eceDepartment=findViewById(R.id.eceDepartment);
        mechDepartment=findViewById(R.id.mechDepartment);
        chemistryDepartment=findViewById(R.id.chemistryDepartment);
        mathsDepartment=findViewById(R.id.mathsDepartment);
        physicsDepartment=findViewById(R.id.physicsDepartment);
        managementstudiesDepartment=findViewById(R.id.managementstudiesDepartment);
        socialsciencesDepartment=findViewById(R.id.socialsciencesDepartment);

        appliedmechanicsNoData=findViewById(R.id.appliedmechanicsNoData);
        biotechNoData=findViewById(R.id.biotechNoData);
        chemicalNoData=findViewById(R.id.chemicalNoData);
        civilNoData=findViewById(R.id.civilNoData);
        csNoData=findViewById(R.id.csNoData);
        electricalNoData=findViewById(R.id.electricalNoData);
        eceNoData=findViewById(R.id.eceNoData);
        mechNoData=findViewById(R.id.mechNoData);
        chemistryNoData=findViewById(R.id.chemistryNoData);
        mathsNoData=findViewById(R.id.mathsNoData);
        physicsNoData=findViewById(R.id.physicsNoData);
        managementstudiesNoData=findViewById(R.id.managementstudiesNoData);
        socialsciencesNoData=findViewById(R.id.socialsciencesNoData);

        aappliedmechanicsDepartment();
        bbiotechDepartment();
        cchemicalDepartment();
        ccivilDepartment();
        ccsDepartment();
        eelectricalDepartment();
        eeceDepartment();
        mmechDepartment();
        cchemistryDepartment();
        mmathsDepartment();
        pphysicsDepartment();
        mmanagementstudiesDepartment();
        ssocialsciencesDepartment();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UpdateFaculty.this, com.nishanth.mnnit_admin.Faculty.Add_Teachers.class);
               startActivity(intent);

            }



        });


    }

    private void aappliedmechanicsDepartment() {
        dbRef=reference.child("Applied Mechanics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                       list1=new ArrayList<>();
                       if(!snapshot.exists())
                       {
                           appliedmechanicsNoData.setVisibility(View.VISIBLE);
                           appliedmechanicsDepartment.setVisibility(View.GONE);
                       }
                       else
                       {
                           appliedmechanicsNoData.setVisibility(View.GONE);
                           appliedmechanicsDepartment.setVisibility(View.VISIBLE);
                           for(DataSnapshot datasnapshot :snapshot.getChildren() )
                           {
                               TeacherData data=datasnapshot.getValue(TeacherData.class);
                               list1.add(data);
                           }
                       }
                       appliedmechanicsDepartment.setHasFixedSize(true);
                       appliedmechanicsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                       adapter=new TeacherAdapter(list1, UpdateFaculty.this, "Applied Mechanics");
                appliedmechanicsDepartment.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
   private void bbiotechDepartment(){
       dbRef=reference.child("Biotechnology");
       dbRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               list2=new ArrayList<>();
               if(!snapshot.exists())
               {
                   biotechDepartment.setVisibility(View.GONE);
                   biotechNoData.setVisibility(View.VISIBLE);
               }
               else
               {
                   biotechDepartment.setVisibility(View.VISIBLE);
                   biotechNoData.setVisibility(View.GONE);
                   for(DataSnapshot datasnapshot:snapshot.getChildren())
                   {
                       TeacherData data=datasnapshot.getValue(TeacherData.class);
                       list2.add(data);
                   }
                   biotechDepartment.setHasFixedSize(true);
                   biotechDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                   adapter=new TeacherAdapter(list2, UpdateFaculty.this, "Biotechnology");
                   biotechDepartment.setAdapter(adapter);
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

           }
       });
   }
   private void cchemicalDepartment(){
       dbRef=reference.child("Chemical Engineering");
       dbRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               list3=new ArrayList<>();
               if(!snapshot.exists())
               {
                   chemicalDepartment.setVisibility(View.GONE);
                   chemicalNoData.setVisibility(View.VISIBLE);
               }
               else
               {
                   chemicalDepartment.setVisibility(View.VISIBLE);
                   chemicalNoData.setVisibility(View.GONE);
                   for(DataSnapshot datasnapshot:snapshot.getChildren())
                   {
                       TeacherData data=datasnapshot.getValue(TeacherData.class);
                       list3.add(data);
                   }
                   chemicalDepartment.setHasFixedSize(true);
                   chemicalDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                   adapter=new TeacherAdapter(list3, UpdateFaculty.this,"Chemical Engineering");
                   chemicalDepartment.setAdapter(adapter);
                   chemicalNoData.setVisibility(View.VISIBLE);
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

           }
       });


   }
    private void ccivilDepartment(){
        dbRef=reference.child("Civil Engineering");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4=new ArrayList<>();
                if(!snapshot.exists())
                {
                    civilDepartment.setVisibility(View.GONE);
                    civilNoData.setVisibility(View.VISIBLE);

                }
                else
                {
                    civilDepartment.setVisibility(View.VISIBLE);
                    civilNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    civilDepartment.setHasFixedSize(true);
                    civilDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list4, UpdateFaculty.this, "Civil Engineering");
                    civilDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void ccsDepartment(){
        dbRef=reference.child("Computer Science and Engineering");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list5=new ArrayList<>();
                if(!snapshot.exists())
                {
                    csDepartment.setVisibility(View.GONE);
                    csNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    csDepartment.setVisibility(View.VISIBLE);
                    csNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list5.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list5, UpdateFaculty.this, "Computer Science and Engineering");
                    csDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void eelectricalDepartment(){
        dbRef=reference.child("Electrical Engineering");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list6=new ArrayList<>();
                if(!snapshot.exists())
                {
                    electricalDepartment.setVisibility(View.GONE);
                    electricalNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    electricalDepartment.setVisibility(View.VISIBLE);
                    electricalNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list6.add(data);
                    }
                    electricalDepartment.setHasFixedSize(true);
                    electricalDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list6, UpdateFaculty.this, "Electrical Engineering");
                    electricalDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void eeceDepartment(){
        dbRef=reference.child("Electronics and Comm Engineering");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list7=new ArrayList<>();
                if(!snapshot.exists())
                {
                    eceDepartment.setVisibility(View.GONE);
                    eceNoData.setVisibility(View.VISIBLE);
                }
                else
                {

                    eceDepartment.setVisibility(View.VISIBLE);
                    eceNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list7.add(data);
                    }
                    eceDepartment.setHasFixedSize(true);
                    eceDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list7, UpdateFaculty.this, "Electronics and Comm Engineering");
                    eceDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void mmechDepartment(){
        dbRef=reference.child("Mechanical Engineering");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list8=new ArrayList<>();
                if(!snapshot.exists())
                {
                    mechDepartment.setVisibility(View.GONE);
                    mechNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    mechDepartment.setVisibility(View.VISIBLE);
                    mechNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list8.add(data);
                    }
                    mechDepartment.setHasFixedSize(true);
                    mechDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list8, UpdateFaculty.this, "Mechanical Engineering");
                    mechDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void cchemistryDepartment(){
        dbRef=reference.child("Chemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list9=new ArrayList<>();
                if(!snapshot.exists())
                {
                    chemistryDepartment.setVisibility(View.GONE);
                    chemistryNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    chemistryDepartment.setVisibility(View.VISIBLE);
                    chemistryNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list9.add(data);
                    }
                    chemistryDepartment.setHasFixedSize(true);
                    chemistryDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list9, UpdateFaculty.this, "Chemistry");
                    chemistryDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void mmathsDepartment(){
        dbRef=reference.child("Mathematics");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list10=new ArrayList<>();
                if(!snapshot.exists())
                {
                    mathsDepartment.setVisibility(View.GONE);
                    mathsNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    mathsDepartment.setVisibility(View.VISIBLE);
                    mathsNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list10.add(data);
                    }
                    mathsDepartment.setHasFixedSize(true);
                    mathsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list10, UpdateFaculty.this, "Mathematics");
                    mathsDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void pphysicsDepartment(){
        dbRef=reference.child("Physics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list11=new ArrayList<>();
                if(!snapshot.exists())
                {
                    physicsDepartment.setVisibility(View.GONE);
                    physicsNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    physicsDepartment.setVisibility(View.VISIBLE);
                    physicsNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list11.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list11, UpdateFaculty.this, "Physics");
                    physicsDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void mmanagementstudiesDepartment(){
        dbRef=reference.child("School of Management Studies");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list12=new ArrayList<>();
                if(!snapshot.exists())
                {
                    managementstudiesDepartment.setVisibility(View.GONE);
                    managementstudiesNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    managementstudiesDepartment.setVisibility(View.VISIBLE);
                    managementstudiesNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list12.add(data);
                    }
                    managementstudiesDepartment.setHasFixedSize(true);
                    managementstudiesDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list12, UpdateFaculty.this, "School of Management Studies");
                    managementstudiesDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void ssocialsciencesDepartment(){
        dbRef=reference.child("Humanities and Social Sciences");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list13=new ArrayList<>();
                if(!snapshot.exists())
                {
                    socialsciencesDepartment.setVisibility(View.GONE);
                    socialsciencesNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    socialsciencesDepartment.setVisibility(View.VISIBLE);
                    socialsciencesNoData.setVisibility(View.GONE);
                    for(DataSnapshot datasnapshot:snapshot.getChildren())
                    {
                        TeacherData data=datasnapshot.getValue(TeacherData.class);
                        list13.add(data);
                    }
                    socialsciencesDepartment.setHasFixedSize(true);
                    socialsciencesDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list13, UpdateFaculty.this, "Humanities and Social Sciences");
                    socialsciencesDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}