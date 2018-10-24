package com.vijay.shuklavijay249.UploadTest;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shuklavijay249 on 2/23/2017.
 */
@IgnoreExtraProperties
public class Upload{

    private String name;
    private String url;
    private String mkey;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url) {
        if(name.trim().equals(""))
        {
            name = "No Name";
        }
        this.name = name;
        this.url= url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Exclude
    public String getkey()
    {
        return mkey;
    }

    @Exclude
    public void setkey(String key)
    {
        mkey = key;
    }
}