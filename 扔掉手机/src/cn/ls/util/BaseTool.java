package cn.ls.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by ls on 2014/9/16.
 */
public class BaseTool {

    private DisplayMetrics dm= new DisplayMetrics();
    private Activity mActivity;

    public BaseTool(Activity  activity) {
        this.mActivity = activity;

    }

    public int getWidthPx(){
          mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;

    }
    public int  getHeightPx(){
        return dm.heightPixels;
    }




}
