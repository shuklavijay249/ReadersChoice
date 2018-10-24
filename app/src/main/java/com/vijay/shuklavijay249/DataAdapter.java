package com.vijay.shuklavijay249;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vijay.shuklavijay249.UploadTest.Upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by shuklavijay249 on 2/23/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context context;
    private List<Upload> uploads;
    private OnItemClickListener mListener;
//    public ProgressBar progressBar;
    String downloadUrl = "";
    DownloadManager downloadManager;
    public long downloadReference;
    public MediaPlayer mMediaPlayer = null;


    public DataAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
        this.mMediaPlayer = null;
//        this.progressBar = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booklist_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Upload upload = uploads.get(position);

        holder.textViewName.setText(upload.getName());
        holder.btnStoryLoad.setTag(upload.getUrl());
        holder.btnDownload.setTag(upload.getUrl());
        holder.mMediaPlayer = null;
//        holder.progressBar = null;

        String path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/" + "Reader'Choice/").getPath();
        File file = new File(path + "/" + upload.getName());

//        File files_test = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS+ "/" + "Reader'Choice/");
//        File[] files_test = ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_DOWNLOADS + "/" + "/Reader'Choice/" + "/"+ upload.getName());
//        File path = new File(Environment.DIRECTORY_DOWNLOADS, "/Reader'Choice/" + "/" + upload.getName());
//
//   File path = new File(context.Environment.DIRECTORY_DOWNLOADS, "/Reader'Choice/" + "/" + upload.getName());


//        if (files_test.exists()) {
//            holder.btnDownload.setVisibility(View.GONE);
//            holder.btnPlay.setVisibility(View.VISIBLE);
//        }

        if (file.exists()) {
            holder.btnDownload.setVisibility(View.GONE);
            holder.btnPlay.setVisibility(View.VISIBLE);
            holder.btnPause.setVisibility(View.GONE);
            holder.btnPlay.setTag(file.getPath());
        } else {
            holder.btnDownload.setVisibility(View.VISIBLE);
            holder.btnPlay.setVisibility(View.GONE);
            holder.btnPause.setVisibility(View.GONE);
            holder.btnPlay.setTag(file.getPath());

        }

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public Button btnStoryLoad;
        public ImageButton btnDownload, btnPause, btnPlay;
        public ProgressBar progressBar;
        public MediaPlayer mMediaPlayer;

        public ViewHolder(View itemView) {
            super(itemView);

            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            context.registerReceiver(downloadReceiver, filter);


            textViewName = (TextView) itemView.findViewById(R.id.textViewName);

            btnStoryLoad = (Button) itemView.findViewById(R.id.btnStoryLoad);
            btnDownload = (ImageButton) itemView.findViewById(R.id.btnDownload);
            btnPause = (ImageButton) itemView.findViewById(R.id.btnPause);
            btnPlay = (ImageButton) itemView.findViewById(R.id.btnPlay);
            progressBar = (ProgressBar) itemView.findViewById(R.id.downloadProgress);
            mMediaPlayer = null;
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            btnStoryLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
//                        Log.e(MainApplication.TAG, "downloadUrl+++++: " + downloadUrl);
                        Uri Download_Uri = Uri.parse(String.valueOf(btnStoryLoad.getTag()));
                        String urls[] = Download_Uri.toString().split("\\?");
                        String filename = urls[0].substring(urls[0].lastIndexOf('%') + 1, urls[0].length());
                        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setAllowedOverRoaming(false);
                        request.setTitle("Downloading");
//                request.setDescription("Downloading " + "Sample" + ".png");
                        request.setVisibleInDownloadsUi(true);
                        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, "/Reader'Choice/" + "/" + "filename");
                        progressBar.setVisibility(View.VISIBLE);
                        downLoad(String.valueOf(Download_Uri), 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
//                        Log.e(MainApplication.TAG, "downloadUrl+++++: " + downloadUrl);
                        Uri Download_Uri = Uri.parse(String.valueOf(btnDownload.getTag()));
                        String urls[] = Download_Uri.toString().split("\\?");
                        String filename = urls[0].substring(urls[0].lastIndexOf('%') + 1, urls[0].length());

                        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setAllowedOverRoaming(false);
                        request.setTitle("Downloading");
//                request.setDescription("Downloading " + "Sample" + ".png");
                        request.setVisibleInDownloadsUi(true);
                        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, "/Reader'Choice/" + "/" + textViewName.getText().toString());
                        progressBar.setVisibility(View.VISIBLE);
                        downLoad(String.valueOf(Download_Uri), 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnPause.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                    playSound3(String.valueOf(btnPlay.getTag()));

                }
            });

            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnPause.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.VISIBLE);
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                }
            });

        }

        private void playSound3(String assetName) {
            try {
                String path = String.valueOf(btnPlay.getTag());
//                AssetFileDescriptor afd = (AssetFileDescriptor) btnPlay.getTag();
//                AssetFileDescriptor afd =  context.getAssets().openFd("sounds/" + assetName + ".mp3");
//                mMediaPlayer = new MediaPlayer();
//                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
                mMediaPlayer = new MediaPlayer();

                mMediaPlayer.setDataSource(path);

                mMediaPlayer.prepare();

                mMediaPlayer.start();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


        public void downLoad(String uri, int status) {
            try {
                String fname = "";
                if (status == 1) {
                    String urls[] = uri.toString().split("\\?");
                    fname = urls[0].substring(urls[0].lastIndexOf('%') + 1, urls[0].length());
                }
                downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
                Uri Download_Uri = Uri.parse(uri);
                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

                //Restrict the types of networks over which this download may proceed.
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                //Set whether this download may proceed over a roaming connection.
                request.setAllowedOverRoaming(false);
                //Set the title of this download, to be displayed in notifications (if enabled).
                request.setTitle("Your File is Downloading");
                //Set a description of this download, to be displayed in notifications (if enabled)
                request.setDescription("Android Data download using DownloadManager.");
                //Set the local destination for the downloaded file to a path within the application's external files directory
//        if(isImage) {

                request.setDestinationInExternalFilesDir(context, DIRECTORY_DOWNLOADS, "/Reader'Choice/" + "/" + textViewName.getText().toString());//
//        }else {
//            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, "DATA"+System.currentTimeMillis()+".pdf");
//        }
                //Enqueue a new download and same the referenceId
                downloadReference = downloadManager.enqueue(request);
//        TextView showCountries = (TextView) findViewById(R.id.countryData);
//        showCountries.setText("Getting data from Server, Please WAIT...");

//        Button checkStatus = (Button) findViewById(R.id.checkStatus);
//        checkStatus.setEnabled(true);
//        Button cancelDownload = (Button) findViewById(R.id.cancelDownload);
//        cancelDownload.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                //check if the broadcast message is for our Enqueued download
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadReference == referenceId) {

//                Button cancelDownload = (Button) findViewById(R.id.cancelDownload);
//                cancelDownload.setEnabled(false);
//file:///storage/emulated/0/Android/data/com.eduvanzapplication/files/Download/SIGNED APPLICATIONEdutesterEduvanz1530095441962.pdf
                    int ch;
                    ParcelFileDescriptor file;
                    StringBuffer strContent = new StringBuffer("");

                    //parse the JSON data and display on the screen
                    try {
                        file = downloadManager.openDownloadedFile(downloadReference);
                        FileInputStream fileInputStream
                                = new ParcelFileDescriptor.AutoCloseInputStream(file);

                        while ((ch = fileInputStream.read()) != -1)
                            strContent.append((char) ch);

//                    JSONObject responseObj = new JSONObject(strContent.toString());
//                    JSONArray countriesObj = responseObj.getJSONArray("countries");
//
//                    for (int i = 0; i < countriesObj.length(); i++) {
//                        Gson gson = new Gson();
//                        String countryInfo = countriesObj.getJSONObject(i).toString();
//                        Country country = gson.fromJson(countryInfo, Country.class);
//                        countryData.append(country.getCode() + ": " + country.getName() + "\n");
//                    }
//
//                    TextView showCountries = (TextView) findViewById(R.id.countryData);
//                    showCountries.setText(countryData.toString());

                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.GONE);
                        btnDownload.setVisibility(View.GONE);
                        btnPause.setVisibility(View.GONE);
                        btnPlay.setVisibility(View.VISIBLE);

                        Toast toast = Toast.makeText(context,
                                "Downloading of File just finished", Toast.LENGTH_LONG);
                        //toast.setGravity(Gravity.TOP, 25, 400);
                        //toast.show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select Action");
            MenuItem doWhatEver = menu.add(Menu.NONE, 1, 1, "Do WhatEver");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            doWhatEver.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (mListener != null) {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;

                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {

        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);

    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
