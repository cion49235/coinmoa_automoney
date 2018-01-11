package kr.co.inno.autocash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import coinmoa.app.automoney.R;


/**
 * Created by byapps on 2016. 3. 16..
 */
public class AutoLayoutGoogleResultActivity extends Activity {

    public static Context context;
    private Dialog dialog;
    WebView webview;

    String account_email2, googleType;
    String googlePasswd = "";

    @Override
    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_autolayout);
        context = this;

        dialog = new Dialog(context, R.style.full_screen_dialog);
        View v = LayoutInflater.from(context).inflate(R.layout.progress_circle,null);
        dialog.setContentView(v);

        Bundle bundle = getIntent().getExtras();
        googleType = bundle.getString("googleType");

        if ( googleType.equals("4") ) {
            account_email2 = bundle.getString("account_email2");
            if ( account_email2 != null && !account_email2.equals("") ) {
                googleLogin(account_email2, googlePasswd);
                AutoLayoutGoogleActivity.prevActivity.finish();
                finish();
            }
        } else if ( googleType.equals("5") ) {
            account_email2 = bundle.getString("account_email2");
            if ( account_email2 != null && !account_email2.equals("") ) {
                googleLogin(account_email2, googlePasswd);
                AutoLayoutGoogleActivity.prevActivity.finish();
                finish();
            }
        } else if ( googleType.equals("6") ) {
            account_email2 = bundle.getString("account_email2");
            if ( account_email2 != null && !account_email2.equals("") ) {
                googleLogin(account_email2, googlePasswd);
                AutoLayoutGoogleActivity.prevActivity.finish();
                finish();
            }
        }
    }

    public void googleLogin(String account_email, String account_passwd) {

        Log.d("dsu", "구글아이디 : " + account_email + "\n구글패스워드 : " + account_passwd);
    }

    private void close_autolayout(){
        finish();
        overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        close_autolayout();
    }
}
