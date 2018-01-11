package coinmoa.app.automoney.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.webkit.WebView;


public class PreferenceUtil {
	
	private static final String SHARED_FILE_TITLE = "pref_sunshinelon";
    public final static String PREF_URL = "pref_url";
    public final static String PREF_AD_VIEW = "ad_view";
    public final static String PREF_AD_TIME = "ad_time";
	//==============================================================================================//
	//================================ 프리퍼런스 저장하고 불러오기 ================================//
	public static boolean getBooleanSharedData(Context context, String key, boolean defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultData);
    }
    public static void setBooleanSharedData(Context context, String key, boolean flag) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        Editor e = p.edit();
        e.putBoolean(key, flag);
        e.commit();
    }
    public static int getIntSharedData(Context context, String key, int defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultData);
    }
    public static void setIntSharedData(Context context, String _key, int _data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        Editor e = p.edit();
        e.putInt(_key, _data);
        e.commit();
    }
    public static long getLongSharedData(Context context, String key, long defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getLong(key, defaultData);
    }
    public static void setLongSharedData(Context context, String _key, long _data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        Editor e = p.edit();
        e.putLong(_key, _data);
        e.commit();
    }
    public static String getStringSharedData(Context context, String key, String defaultData) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        return pref.getString(key, defaultData);
    }
    public static void setStringSharedData(Context context, String key, String data) {
        SharedPreferences p = context.getSharedPreferences(SHARED_FILE_TITLE, Context.MODE_PRIVATE);
        Editor e = p.edit();
        e.putString(key, data);
        e.commit();
    }
    
    /**
     * 테블릿인지
     */
    private static final String PREFERENCE_NAME         = "ATTEND_PREFERENCE";
    private static final String KEY_IS_TABLE			= "KEY_IS_TABLE";
    
    public static boolean getIsTable(Context cxt) {
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        if(null == sp) {
            return false;
        }

        return sp.getBoolean(KEY_IS_TABLE, false);
    }

    public static void setIsTable(Context cxt, boolean isTable){
        SharedPreferences sp = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(null == sp) {
            return;
        }
        Editor et = sp.edit();
        et.putBoolean(KEY_IS_TABLE, isTable);
        et.commit();
    }
    
    public static boolean checkTabletDeviceWithUserAgent(Context context) {
        WebView webView = new WebView(context);
        String ua=webView.getSettings().getUserAgentString();
        webView = null;
        if(ua.contains("Mobile Safari")){
            return false;
        }else{
           return true;
        }
    }
}
