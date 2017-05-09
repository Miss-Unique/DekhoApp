package com.dekhoapp.android.app.base.ListOfSongsPackage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dekhoapp.android.app.base.AccessCameraActivity;
import com.dekhoapp.android.app.base.CategoryPackage.ClickListener;
import com.dekhoapp.android.app.base.CategoryPackage.RecyclerTouchListener;
import com.dekhoapp.android.app.base.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class List_of_Songs extends AppCompatActivity {

    private List<LOSClass> LOSList = new ArrayList<>();
    private List<LOSClass> m = new ArrayList<>();
    public RecyclerView recyclerView;
    private LOSAdapter mAdapter;
    GridLayoutManager mLayoutManager;
    DatabaseReference mDatabase,db;
    String name ;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of__songs);

        recyclerView = (RecyclerView) findViewById(R.id.rv_songs);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name =  getIntent().getStringExtra("Name");
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        //set adapter
        addingrows(LOSList);
        m=LOSList;

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                final LOSClass LOS1 = m.get(position);

                storageRef = FirebaseStorage.getInstance().getReference().child("Music/"+name+"/"+LOS1.getname());

                File storagePath = new File(Environment.getExternalStorageDirectory(), "directory_name");

                if(!storagePath.exists()) {
                    storagePath.mkdirs();
                }

                final File myFile = new File(storagePath,"file_name");

                storageRef.getFile(myFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Uri.fromFile(myFile));
//                        mp.start();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                Intent intent=new Intent(List_of_Songs.this,AccessCameraActivity.class);
                intent.putExtra("Category",name);
                intent.putExtra("Song", LOS1.getname());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position)
            {
            }
        }));
        prepareLOSData();
    }

    private void addingrows(final List<LOSClass> k)
    {
        mAdapter = new LOSAdapter(getApplicationContext(),k);
        mLayoutManager=new GridLayoutManager(List_of_Songs.this,1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void prepareLOSData()
    {
        db = mDatabase.child("Soumya").child("Music").child(name).getRef();

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                LOSClass LOS = dataSnapshot.getValue(LOSClass.class);

                String title = LOS.getname();

                LOSClass ct = new LOSClass(title,R.drawable.category);
                LOSList.add(ct);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}