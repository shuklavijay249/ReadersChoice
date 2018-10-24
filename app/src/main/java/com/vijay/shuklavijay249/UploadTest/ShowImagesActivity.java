package com.vijay.shuklavijay249.UploadTest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vijay.shuklavijay249.R;

import java.util.ArrayList;
import java.util.List;

public class ShowImagesActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener{
    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private MyAdapter adapter;

    private FirebaseStorage mstorage;
    private DatabaseReference mDatabase;
    private ValueEventListener mDBListener;

    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        //creating adapter
        adapter = new MyAdapter(getApplicationContext(), uploads);

        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(ShowImagesActivity.this);

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mstorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        //adding an event listener to fetch values
       mDBListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                uploads.clear();
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setkey(postSnapshot.getKey());
                    uploads.add(upload);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(getApplicationContext(), "Normal click at  " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(getApplicationContext(), "Whatever click at  " + position, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = uploads.get(position);
        final String selectedkey = selectedItem.getkey();

        StorageReference imageRef = mstorage.getReferenceFromUrl(selectedItem.getUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabase.child(selectedkey).removeValue();
                Toast.makeText(ShowImagesActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

//        Toast.makeText(getApplicationContext(), "Delete click at  " + position, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.removeEventListener(mDBListener);
    }
}
