package com.vijay.shuklavijay249;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vijay.shuklavijay249.UploadTest.Constants;
import com.vijay.shuklavijay249.UploadTest.MyAdapter;
import com.vijay.shuklavijay249.UploadTest.ShowImagesActivity;
import com.vijay.shuklavijay249.UploadTest.Upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HindiFragment extends Fragment implements DataAdapter.OnItemClickListener{

    public static View view;
    private DataAdapter adapter;
//    public List<LafListPOJO> lafListPOJOList;

    public RecyclerView recyclerView;

    public ScrollView scrollView;

    private FirebaseStorage mstorage;
    private DatabaseReference mDatabase;
    private ValueEventListener mDBListener;

    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;


    public HindiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_english, container, false);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

//        adapter.setLoadMoreListener(new LafListAdapter.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//
//                recyclerView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //int index = lafListPOJOList.size() - 1;
//                        //loadMore(index);
//                    }
//                });
//                //Calling loadMore function in Runnable to fix the
//                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
//            }
//        });
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);
//        load(0);

//        btnPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(ActivityDashboard.this, EligibilityActivity.class);
//                ActivityDashboard.this.startActivity(intent);
//            }
//        });

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());

        uploads = new ArrayList<>();

        adapter = new DataAdapter(getActivity(), uploads);

        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(this);

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

        Toast.makeText(getActivity(), "Normal click at  " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(getActivity(), "Whatever click at  " + position, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDeleteClick(int position) {


        try {
            String filepath = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/" + "Reader'Choice/").getPath();
//            String filepath = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/" + "Reader'Choice/" + "/" +uploads.get(position).getName()).getPath();

//            File fdelete = new File(uri.getPath());
//            if (fdelete.exists()) {
//                if (fdelete.delete()) {
//                    System.out.println("file Deleted :" + uri.getPath());
//                } else {
//                    System.out.println("file not Deleted :" + uri.getPath());
//                }
//            }


//            File file = new File(filepath);
//            file.delete();
//            if(file.exists()){
//                file.getCanonicalFile().delete();
//                if(file.exists()){
//                    getActivity().deleteFile(file.getName());
//                }
//            }
            DeleteDir(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/" + "Reader'Choice/" + "/" +uploads.get(position).getName()));

        } catch (Exception e) {
            e.printStackTrace();
        }



//        Upload selectedItem = uploads.get(position);
//        StorageReference imageRef = mstorage.getReferenceFromUrl(selectedItem.getUrl());

//        final String selectedkey = selectedItem.getkey();
//        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                mDatabase.child(selectedkey).removeValue();
//                Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
//            }
//        });

//        Toast.makeText(getApplicationContext(), "Delete click at  " + position, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.removeEventListener(mDBListener);
    }

    public static void DeleteDir(File file)
            throws IOException {
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            } else {
                String files[] = file.list();

                for (String temp : files) {
                    File fileDelete = new File(file, temp);
                    DeleteDir(fileDelete);
                }
                if (file.list().length == 0) {
                    file.delete();
                }
            }
        } else {
            file.delete();
        }
    }

//    private void load(int index) {
//
//        //Disbursed
//        LafListPOJO listPOJO = new LafListPOJO();
//        listPOJO.id = "1";
//        listPOJO.fname = "com.vijay.shuklavijay249";
//        listPOJO.lname = "twinkle";
//        listPOJO.lafId = "Twinkle, twinkle, little star,\n" +
//                "How I wonder what you are.\n" +
//                "Up above the world so high,\n" +
//                "Like a diamond in the sky.\n" +
//                "\n" +
//                "When the blazing sun is gone,\n" +
//                "When he nothing shines upon,\n" +
//                "Then you show your little light,\n" +
//                "Twinkle, twinkle, all the night.\n" +
//                "\n" +
//                "Then the traveller in the dark,\n" +
//                "Thanks you for your tiny spark,\n" +
//                "He could not see which way to go,\n" +
//                "If you did not twinkle so.\n" +
//                "\n" +
//                "In the dark blue sky you keep,\n" +
//                "And often through my curtains peep,\n" +
//                "For you never shut your eye,\n" +
//                "‘Till the sun is in the sky.\n" +
//                "\n" +
//                "As your bright and tiny spark,\n" +
//                "Lights the traveller in the dark.\n" +
//                "Though I know not what you are,\n" +
//                "Twinkle, twinkle, little star.\n" +
//                "\n" +
//                "Twinkle, twinkle, little star.\n" +
//                "How I wonder what you are.\n" +
//                "Up above the world so high,\n" +
//                "Like a diamond in the sky.\n" +
//                "\n" +
//                "Twinkle, twinkle, little star.\n" +
//                "How I wonder what you are.\n" +
//                "How I wonder what you are.";
//        listPOJO.lafcreateddt = "28 july 2018";
//        listPOJO.address = "Mumbai";
//        listPOJO.loanamt = "54,123";
//        listPOJO.coursename = "ABCD Edge";
//        listPOJO.counsellorname = "Vivek Kumar";
//        listPOJO.lafstatus = "Disbursed";
//        listPOJO.type = "laf";
//        //add loaded data
//        lafListPOJOList.add(listPOJO);
//
//        //declined
//        LafListPOJO listPOJO1 = new LafListPOJO();
//        listPOJO1.id = "2";
//        listPOJO1.fname = "Vivek";
//        listPOJO1.lname = "johny_johny_yes_papa";
//        listPOJO1.lafId = "Johny Johny\n" +
//                "Yes Papa,\n" +
//                "Eating sugar?\n" +
//                "No Papa\n" +
//                "Telling lies?\n" +
//                "No Papa\n" +
//                "Open your mouth\n" +
//                "Ha! Ha! Ha!\n" +
//                "\n" +
//                "Johny Johny\n" +
//                "Yes Papa,\n" +
//                "Eating sugar?\n" +
//                "No Papa\n" +
//                "Telling lies?\n" +
//                "No Papa\n" +
//                "Open your mouth\n" +
//                "Ha! Ha! Ha!\n" +
//                "\n" +
//                "Johny Johny\n" +
//                "Yes Papa,\n" +
//                "Eating sugar?\n" +
//                "No Papa\n" +
//                "Telling lies?\n" +
//                "No Papa\n" +
//                "Open your mouth\n" +
//                "Ha! Ha! Ha!";
//        listPOJO1.lafcreateddt = "29 july 2018";
//        listPOJO1.address = "Dongri";
//        listPOJO1.loanamt = "59,123";
//        listPOJO1.coursename = "ABCD Edge";
//        listPOJO1.counsellorname = "Vivek Kumar";
//        listPOJO1.lafstatus = "Declined";
//        listPOJO.type = "laf";
//        //add loaded data
//        lafListPOJOList.add(listPOJO1);
//
//        //incomplete
//        LafListPOJO listPOJO2 = new LafListPOJO();
//        listPOJO2.id = "3";
//        listPOJO2.fname = "Bikash";
//        listPOJO2.lname = "alert_blop";
//        listPOJO2.lafId = "alert_blop";
//        listPOJO2.lafcreateddt = "29 july 2018";
//        listPOJO2.address = "Orissa";
//        listPOJO2.loanamt = "59,123";
//        listPOJO2.coursename = "WXYZ Edge";
//        listPOJO2.counsellorname = "Vivek Kumar";
//        listPOJO2.lafstatus = "Incomplete";
//        listPOJO.type = "laf";
//        //add loaded data
//        lafListPOJOList.add(listPOJO2);
//
//        //under evaluation
//        LafListPOJO listPOJO3 = new LafListPOJO();
//
//        listPOJO3.id = "4";
//        listPOJO3.fname = "Saurabh";
//        listPOJO3.lname = "alert_censored_beep";
//        listPOJO3.lafId = "alert_censored_beep";
//        listPOJO3.lafcreateddt = "29 july 2018";
//        listPOJO3.address = "Ghatkopar";
//        listPOJO3.loanamt = "59,123";
//        listPOJO3.coursename = "ABCD Edge";
//        listPOJO3.counsellorname = "Vivek Kumar";
//        listPOJO3.lafstatus = "Under Evaluation";
//        listPOJO.type = "laf";
//
//        //add loaded data
//        lafListPOJOList.add(listPOJO3);
//
//        LafListPOJO listPOJO4 = new LafListPOJO();
//
//        listPOJO4.id = "5";
//        listPOJO4.fname = "Santosh";
//        listPOJO4.lname = "alert_electrical_sweep";
//        listPOJO4.lafId = "alert_electrical_sweep";
//        listPOJO4.lafcreateddt = "29 july 2018";
//        listPOJO4.address = "Badlapur";
//        listPOJO4.loanamt = "59,123";
//        listPOJO4.coursename = "ABCD Edge";
//        listPOJO4.counsellorname = "Vivek Kumar";
//        listPOJO4.lafstatus = "Declined";
//        listPOJO.type = "laf";
//
//        //add loaded data
//        lafListPOJOList.add(listPOJO4);
//
//        LafListPOJO listPOJO5 = new LafListPOJO();
//
//        listPOJO5.id = "6";
//        listPOJO5.fname = "Santosh";
//        listPOJO5.lname = "alert_fire_pager";
//        listPOJO5.lafId = "alert_fire_pager";
//        listPOJO5.lafcreateddt = "29 july 2018";
//        listPOJO5.address = "Ulhas Nagar";
//        listPOJO5.loanamt = "59,123";
//        listPOJO5.coursename = "Diploma in Dotnet";
//        listPOJO5.counsellorname = "Vivek Kumar";
//        listPOJO5.lafstatus = "Approved";
//        listPOJO.type = "laf";
//
//        //add loaded data
//        lafListPOJOList.add(listPOJO5);
//
//        LafListPOJO listPOJO6 = new LafListPOJO();
//
//        listPOJO6.id = "7";
//        listPOJO6.fname = "Atul";
//        listPOJO6.lname = "alert_fog_horn";
//        listPOJO6.lafId = "alert_fog_horn";
//        listPOJO6.lafcreateddt = "29 july 2018";
//        listPOJO6.address = "Marol Naka";
//        listPOJO6.loanamt = "59,123";
//        listPOJO6.coursename = "Diploma in Android";
//        listPOJO6.counsellorname = "Vivek Kumar";
//        listPOJO6.lafstatus = "Approved";
//        listPOJO.type = "laf";
//
//        //add loaded data
//        lafListPOJOList.add(listPOJO6);
//
//        LafListPOJO listPOJO7 = new LafListPOJO();
//
//        listPOJO7.id = "8";
//        listPOJO7.fname = "Abdul";
//        listPOJO7.lname = "alert_metal_gong";
//        listPOJO7.lafId = "alert_metal_gong";
//        listPOJO7.lafcreateddt = "29 july 2018";
//        listPOJO7.address = "Andheri";
//        listPOJO7.loanamt = "59,123";
//        listPOJO7.coursename = "ABCD Edge";
//        listPOJO7.counsellorname = "Vivek Kumar";
//        listPOJO7.lafstatus = "Approved";
//        listPOJO.type = "laf";
//
//        //add loaded data
//        lafListPOJOList.add(listPOJO7);
//
//        adapter.notifyDataChanged();
//
//    }
//
//    private void loadMore(int index) {
//
//        //add loading progress view
//        lafListPOJOList.add(new LafListPOJO("load"));
//        adapter.notifyItemInserted(lafListPOJOList.size() - 1);
//
//        //remove loading view
//        lafListPOJOList.remove(lafListPOJOList.size() - 1);
//
//        LafListPOJO listPOJO = new LafListPOJO();
//        listPOJO.id = "1";
//        listPOJO.fname = "com.vijay.shuklavijay249";
//        listPOJO.lname = "Kumar";
//        listPOJO.lafId = "alert_metal_gong";
//        listPOJO.lafcreateddt = "28 july 2018";
//        listPOJO.address = "Mumbai";
//        listPOJO.loanamt = "54,123";
//        listPOJO.coursename = "Asp.net MVC";
//        listPOJO.counsellorname = "Vivek Kumar";
//        listPOJO.lafstatus = "Approved";
//        listPOJO.type = "laf";
//
//        //add loaded data
//        lafListPOJOList.add(listPOJO);
//
//        LafListPOJO listPOJO1 = new LafListPOJO();
//
//        listPOJO1.id = "2";
//        listPOJO1.fname = "com.vijay.shuklavijay249";
//        listPOJO1.lname = "Kumar";
//        listPOJO1.lafId = "alert_metal_gong";
//        listPOJO1.lafcreateddt = "29 july 2018";
//        listPOJO1.address = "Dongri";
//        listPOJO1.loanamt = "59,123";
//        listPOJO1.coursename = "Machine Learning";
//        listPOJO1.counsellorname = "Vivek Kumar";
//        listPOJO1.lafstatus = "Approved";
//        listPOJO.type = "laf";
//
//        //add loaded data
//        lafListPOJOList.add(listPOJO1);
//
////        if(result.size()>0){
////            //add loaded data
////            lafListPOJOList.addAll(result);
////        }else{//result size 0 means there is no more data available at server
////            adapter.setMoreDataAvailable(false);
////            //telling adapter to stop calling load more as no more server data available
////            Toast.makeText(ActivityDashboard.this,"No More Data Available",Toast.LENGTH_LONG).show();
////        }
//        adapter.notifyDataChanged();
//
//    }

}
