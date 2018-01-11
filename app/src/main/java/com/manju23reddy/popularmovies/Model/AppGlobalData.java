package com.manju23reddy.popularmovies.Model;

import com.manju23reddy.popularmovies.BuildConfig;

/**
 * Created by MReddy3 on 1/6/2018.
 */

public class AppGlobalData {

    private String API_KEY;

    private final static class INSTANCE_CREATOR{
        private final static AppGlobalData INSTANCE = new AppGlobalData();
    }


    private AppGlobalData(){
        API_KEY = BuildConfig.API_KEY;
    }

    public static AppGlobalData getAppGlobalInstance(){
        return INSTANCE_CREATOR.INSTANCE;
    }

    public String getAPIKEY(){
        return API_KEY;
    }
}
