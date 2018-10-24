package com.vijay.shuklavijay249;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class LafListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<LafListPOJO> lafListPOJOList;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    String mSound = "blzwrn";

    /*
     * isLoading - to set the remote loading and complete status to fix back to back load more call
     * isMoreDataAvailable - to set whether more data from server available or not.
     * It will prevent useless load more request even after all the server data loaded
     * */

    public LafListAdapter(Context context, List<LafListPOJO> movies) {
        this.context = context;
        this.lafListPOJOList = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MOVIE) {
            return new MovieHolder(inflater.inflate(R.layout.laflist_card_view, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_MOVIE) {
            ((MovieHolder) holder).bindData(lafListPOJOList.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (lafListPOJOList.get(position).type.equals("laf")) {
                return TYPE_MOVIE;
            } else {
                return TYPE_LOAD;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return lafListPOJOList.size();
    }

    /* VIEW HOLDERS */

    static class MovieHolder extends RecyclerView.ViewHolder {
        TextView txtFName,txtStory,txtStoryLoad;
        CardView card_view;
        ImageButton btnPause, btnPlay;
        Button btnStoryLoad;
        ProgressBar downloadProgress;
        MediaPlayer mMediaPlayer = null;
        String StoryText;

        public MovieHolder(View itemView) {
            super(itemView);
            txtFName = (TextView) itemView.findViewById(R.id.txtFName);
            txtStory = (TextView) itemView.findViewById(R.id.txtStory);
            txtStoryLoad = (TextView) itemView.findViewById(R.id.txtStoryLoad);

            btnStoryLoad = (Button) itemView.findViewById(R.id.btnStoryLoad);
            btnPause = (ImageButton) itemView.findViewById(R.id.btnPause);
            btnPlay = (ImageButton) itemView.findViewById(R.id.btnPlay);

            downloadProgress = (ProgressBar) itemView.findViewById(R.id.downloadProgress);

            card_view = (CardView) itemView.findViewById(R.id.card_view);

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnPause.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                    playSound3(txtFName.getText().toString().trim());

                }
            });

            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnPause.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.VISIBLE);
                    if(mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                }
            });

//            txtStoryLoad.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(txtStory.getText().equals("Read More...")) {
//                        txtStory.setVisibility(View.VISIBLE);
//                        txtStoryLoad.setText("Close");
//                    }
//                    else {
//                        txtStory.setVisibility(View.GONE);
//                        txtStoryLoad.setText("Read More...");
//                    }
//                }
//            });

            btnStoryLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btnStoryLoad.getText().equals("Read More...")) {
                        txtStory.setVisibility(View.VISIBLE);
                        btnStoryLoad.setText("Close");
                    }
                    else {
                        txtStory.setVisibility(View.GONE);
                        btnStoryLoad.setText("Read More...");
                    }
                }
            });
        }

        void bindData(LafListPOJO lafListPOJOListMmodel) {

            txtFName.setText(lafListPOJOListMmodel.lname);
            txtStory.setText(lafListPOJOListMmodel.lafId);

        }

        private void playSound3(String assetName) {
            try {
                AssetFileDescriptor afd =  context.getAssets().openFd("sounds/" + assetName + ".mp3");
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


    }

//    private void playSound2(String assetName) {
//        try {
//            // Syntax  :  android.resource://[package]/[res type]/[res name]
//            // Example : Uri.parse("android.resource://com.my.package/raw/sound1");
//            //
//            // Syntax  : android.resource://[package]/[resource_id]
//            // Example : Uri.parse("android.resource://com.my.package/" + R.raw.sound1);
//
//            String RESOURCE_PATH = ContentResolver.SCHEME_ANDROID_RESOURCE + "://";
//
//            String path;
//            if (false) {
//                path = RESOURCE_PATH + context.getPackageName() + "/raw/" + assetName;
//            } else {
//                int resID = context.getResources().getIdentifier(assetName, "raw", context.getPackageName());
//                path = RESOURCE_PATH + context.getPackageName() + File.separator + resID;
//            }
//            Uri soundUri = Uri.parse(path);
//            mSoundName.setText(path);
//
//            mMediaPlayer = new MediaPlayer();
//            if (true) {
//                mMediaPlayer.setDataSource(context.getApplicationContext(), soundUri);
//                mMediaPlayer.prepare();
//            } else if (false) {
//                // How to load external audio files.
//                // "/sdcard/sample.mp3";
//                //  "http://www.bogotobogo.com/Audio/sample.mp3";
//                mMediaPlayer.setDataSource(path);
//                mMediaPlayer.prepare();
//            } else {
//                ContentResolver resolver = getContentResolver();
//                AssetFileDescriptor afd = resolver.openAssetFileDescriptor(soundUri, "r");
//                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
//            }
//
//            mMediaPlayer.start();
//        } catch (Exception ex) {
//            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }


    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
