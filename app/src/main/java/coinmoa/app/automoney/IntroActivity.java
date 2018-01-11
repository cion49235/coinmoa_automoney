package coinmoa.app.automoney;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.admixer.CustomPopup;
import com.admixer.CustomPopupListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import coinmoa.app.automoney.data.Favorite_DBopenHelper;
import coinmoa.app.automoney.data.Main_Data;
import coinmoa.app.automoney.utils.Crypto;
import coinmoa.app.automoney.utils.Utils;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import gun0912.tedadhelper.TedAdHelper;
import gun0912.tedadhelper.backpress.OnBackPressListener;
import gun0912.tedadhelper.backpress.TedBackPressDialog;
import kr.co.inno.autocash.AutoLayoutGoogleActivity;
import coinmoa.app.automoney.utils.PreferenceUtil;

import static android.os.Build.VERSION.SDK_INT;


public class IntroActivity extends Activity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, AdViewListener, CustomPopupListener, View.OnClickListener {
	public Handler handler;
	public Context context;
	private String num;
	private ArrayList<Main_Data> list;
	private LinearLayout layout_progress;
	private Favorite_DBopenHelper favorite_mydb;
	private ListView listview_main;
	private MainAdapter main_adapter;
	private int current_position = 0;
	private LinearLayout layout_nodata;
	private boolean retry_alert = false;
	private Main_ParseAsync main_parseAsync = null;
	private Button bt_favorite;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		context = this;
		retry_alert = true;
		init_ui();
		if (SDK_INT >= Build.VERSION_CODES.M) {
			checkPermission();
		} else {
			num = "1508";
			list = new ArrayList<Main_Data>();
			list.clear();
			start_index = 1;
			displaylist();
		}

		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "kh19o528");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4092414235173954/3785554198");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4092414235173954/9668436264");

		CustomPopup.setCustomPopupListener(this);
		CustomPopup.startCustomPopup(this, "kh19o528");
		addBannerView();
	}

	private RelativeLayout ad_layout;

	public void addBannerView() {
		AdInfo adInfo = new AdInfo("kh19o528");
		adInfo.setTestMode(false);
		com.admixer.AdView adView = new com.admixer.AdView(this);
		adView.setAdInfo(adInfo, this);
		adView.setAdViewListener(this);
		adView.setAdAnimation(AdView.AdAnimation.TopSlide);
		ad_layout = (RelativeLayout) findViewById(R.id.ad_layout);
		if (ad_layout != null) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			ad_layout.addView(adView, params);
		}
	}

	private final int MY_PERMISSION_REQUEST_STORAGE = 100;

	private void checkPermission() {
		if (SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
					|| checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_NETWORK_STATE)) {
					// Explain to the user why we need to write the permission.
					Toast.makeText(this, context.getString(R.string.introAcitivty_12), Toast.LENGTH_SHORT).show();
				}
				requestPermissions(new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
			} else {
				list = new ArrayList<Main_Data>();
				list.clear();
				num = "1508";
				displaylist();
			}
		} else {
			Intent intent = new Intent(context, AutoLayoutGoogleActivity.class);
			intent.putExtra("googleType", "2");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSION_REQUEST_STORAGE:
				try {
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
						list = new ArrayList<Main_Data>();
						list.clear();
						num = "1508";
						displaylist();
					} else {
						Return_AlertShow(context.getString(R.string.permission_cancel));
					}
					break;
				} catch (ArrayIndexOutOfBoundsException e) {
				} catch (Exception e) {
				}
		}
	}

	public void Return_AlertShow(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setNeutralButton(context.getString(R.string.txt_finish_yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
				finish();
				dialog.dismiss();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		myAlertDialog.show();
	}

	private void init_ui() {
		bt_favorite = (Button)findViewById(R.id.bt_favorite);
		bt_favorite.setOnClickListener(this);
		layout_nodata = (LinearLayout) findViewById(R.id.layout_nodata);
		layout_progress = (LinearLayout) findViewById(R.id.layout_progress);
		listview_main = (ListView) findViewById(R.id.listview_main);
		favorite_mydb = new Favorite_DBopenHelper(this);
	}

	public void displaylist() {
		main_parseAsync = new Main_ParseAsync();
		main_parseAsync.execute();
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			listview_main.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		listview_main.setOnItemClickListener(this);
		listview_main.setOnScrollListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
		Main_Data main_data = (Main_Data) main_adapter.getItem(position);
		PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_URL, main_data.portal);
		Intent intent = new Intent(context, MainActivity.class);
		startActivity(intent);


	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		/*if(view == listview_main){
			if(totalItemCount != 0 && firstVisibleItem  > 1 ){
				listview_main.setFastScrollEnabled(true);
			}else{
				listview_main.setFastScrollEnabled(false);
			}
		}*/
	}

	@Override
	public void onClick(View view) {
		if(view == bt_favorite){
			Intent intent = new Intent(this, FavoriteActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
	}

	public class Main_ParseAsync extends AsyncTask<String, Integer, String> {
		String Response;
		Main_Data main_data;
		ArrayList<Main_Data> menuItems = new ArrayList<Main_Data>();
		String i;
		int _id;
		String id;
		String title;
		String portal;
		String thumbnail_hq;
		String sprit_title[];

		public Main_ParseAsync() {
		}

		@Override
		protected String doInBackground(String... params) {
			String sTag;
			try {
				String str = context.getString(R.string.cion_url) + i + ".php?view=" + num;
				HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(str).openConnection();
				HttpURLConnection.setFollowRedirects(false);
				localHttpURLConnection.setConnectTimeout(15000);
				localHttpURLConnection.setReadTimeout(15000);
				localHttpURLConnection.setRequestMethod("GET");
				localHttpURLConnection.connect();
				InputStream inputStream = new URL(str).openStream();
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(inputStream, "EUC-KR");
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
					} else if (eventType == XmlPullParser.END_DOCUMENT) {
					} else if (eventType == XmlPullParser.START_TAG) {
						sTag = xpp.getName();
						if (sTag.equals("Content")) {
							main_data = new Main_Data();
							_id = Integer.parseInt(xpp.getAttributeValue(null, "id") + "");
						} else if (sTag.equals("videoid")) {
							Response = xpp.nextText() + "";
						} else if (sTag.equals("subject")) {
							title = xpp.nextText() + "";
							sprit_title = title.split("-");
						} else if (sTag.equals("portal")) {
							portal = xpp.nextText() + "";
						} else if (sTag.equals("thumb")) {
							thumbnail_hq = xpp.nextText() + "";
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						sTag = xpp.getName();
						if (sTag.equals("Content")) {
							main_data._id = _id;
							main_data.id = Response;
							main_data.title = title;
							main_data.portal = portal;
							main_data.category = context.getString(R.string.app_name);
							main_data.thumbnail_hq = thumbnail_hq;
							list.add(main_data);
						}
					} else if (eventType == XmlPullParser.TEXT) {
					}
					eventType = xpp.next();
				}
			} catch (SocketTimeoutException localSocketTimeoutException) {
			} catch (ClientProtocolException localClientProtocolException) {
			} catch (IOException localIOException) {
			} catch (Resources.NotFoundException localNotFoundException) {
			} catch (NullPointerException NullPointerException) {
			} catch (Exception e) {
			}
			return Response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			i = "6";
			layout_progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(String Response) {
			super.onPostExecute(Response);
//			Log.i("dsu", "Response : " + Response);
			layout_progress.setVisibility(View.GONE);
			try {
				if (Response != null) {
					for (int i = 0; ; i++) {
						if (i >= list.size()) {
//							while (i > list.size()-1){
							main_adapter = new MainAdapter(context, menuItems, listview_main);
							listview_main.setAdapter(main_adapter);
							listview_main.setFocusable(true);
							listview_main.setSelected(true);
							listview_main.setSelection(current_position);
							if (listview_main.getCount() == 0) {
								layout_nodata.setVisibility(View.VISIBLE);
							} else {
								layout_nodata.setVisibility(View.GONE);
							}
							return;
						}
						menuItems.add(list.get(i));
					}
				} else {
					layout_nodata.setVisibility(View.VISIBLE);
					Retry_AlertShow(context.getString(R.string.sub6_txt8));
				}
			} catch (NullPointerException e) {
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}

	public static boolean loadingMore = true;
	public static boolean exeFlag;
	public static int start_index;

	public void Retry_AlertShow(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		builder.setNeutralButton(context.getString(R.string.txt_main_activity14), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				current_position = 0;
				loadingMore = true;
				exeFlag = false;
				main_parseAsync = new Main_ParseAsync();
				main_parseAsync.execute();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(context.getString(R.string.txt_main_activity13), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
				dialog.dismiss();
				finish();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		if (retry_alert) myAlertDialog.show();
	}

	public class MainAdapter extends BaseAdapter {
		public Context context;
		public int _id = -1;
		public String id = "empty";
		public Cursor cursor;
		public ImageButton bt_favorite;
		public ArrayList<Main_Data> list;
		public ListView listview_main;

		public MainAdapter(Context context, ArrayList<Main_Data> list, ListView listview_main) {
			this.context = context;
			this.list = list;
			this.listview_main = listview_main;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			try {
				if (view == null) {
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					view = layoutInflater.inflate(R.layout.main_activity_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_title = (TextView) view.findViewById(R.id.txt_title);
					holder.bt_favorite = (ImageView) view.findViewById(R.id.bt_favorite);
					holder.bt_favorite.setFocusable(false);
					holder.bt_favorite.setSelected(false);
					view.setTag(holder);
				}
				final ViewHolder holder = (ViewHolder) view.getTag();

				holder.txt_title.setText(list.get(position).title);

				favorite_db(context, list.get(position).id, holder.bt_favorite);
				holder.bt_favorite.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						insert_delete_ccm_favorite_db(context, list.get(position).id, list.get(position).title, list.get(position).portal,holder.bt_favorite);
						datasetchanged();
					}
				});


			} catch (Exception e) {
			}
			return view;
		}
	}

	private void datasetchanged() {
		if (main_adapter != null) {
			main_adapter.notifyDataSetChanged();
		}
	}

	public void insert_delete_ccm_favorite_db(Context context, String get_id, String title, String portal, ImageView bt_favorite) {
		try {
			Cursor cursor = favorite_mydb.getReadableDatabase().rawQuery(
					"select * from favorite_list where id = '" + get_id + "'", null);
			if (null != cursor && cursor.moveToFirst()) {
				id = cursor.getString(cursor.getColumnIndex("id"));
				_id = cursor.getInt(cursor.getColumnIndex("_id"));
			} else {
				id = "empty";
				_id = -1;
			}
			if (id.equals("empty")) {
				bt_favorite.setBackgroundResource(R.drawable.bt_favorite_pressed);
				ContentValues cv = new ContentValues();
				cv.put("id", get_id);
				cv.put("title", title);
				cv.put("portal", portal);
				favorite_mydb.getWritableDatabase().insert("favorite_list", null, cv);
				Toast.makeText(context, context.getString(R.string.txt_favorite_03), Toast.LENGTH_SHORT).show();
			} else {
				bt_favorite.setImageResource(R.drawable.bt_favorite_normal);
				favorite_mydb.getWritableDatabase().delete("favorite_list", "_id" + "=" + _id, null);
				Toast.makeText(context, context.getString(R.string.txt_favorite_04), Toast.LENGTH_SHORT).show();
			}

		} finally {
			close_favorite_db();
		}
	}

	private void close_favorite_db() {
		if (favorite_mydb != null)
			favorite_mydb.close();
	}

	public int _id = -1;
	String id = "empty";

	public void favorite_db(Context context, String get_id, ImageView bt_favorite) {
		try {
			Cursor cursor = favorite_mydb.getReadableDatabase().rawQuery(
					"select * from favorite_list where id = '" + get_id + "'", null);
			if (null != cursor && cursor.moveToFirst()) {
				id = cursor.getString(cursor.getColumnIndex("id"));
				_id = cursor.getInt(cursor.getColumnIndex("_id"));
			} else {
				id = "empty";
				_id = -1;
			}
			if (id.equals("empty")) {
				bt_favorite.setImageResource(R.drawable.bt_favorite_normal);
			} else {
				bt_favorite.setImageResource(R.drawable.bt_favorite_pressed);
			}
		} catch (Exception e) {
		} finally {
			close_favorite_db();
		}
	}

	private class ViewHolder {
		public TextView txt_title;
		public ImageView bt_favorite;
	}



	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		retry_alert = false;
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}

		current_position = 0;
		start_index = 1;
		loadingMore = true;
		exeFlag = false;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
		PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, false);
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
		}
	};

	@Override
	public void onCloseCustomPopup(String arg0) {
	}

	@Override
	public void onHasNoCustomPopup() {
	}

	@Override
	public void onShowCustomPopup(String arg0) {
	}

	@Override
	public void onStartedCustomPopup() {
	}

	@Override
	public void onWillCloseCustomPopup(String arg0) {
	}

	@Override
	public void onWillShowCustomPopup(String arg0) {
	}

	@Override
	public void onReceivedAd(String s, AdView adView) {

	}

	@Override
	public void onFailedToReceiveAd(int i, String s, AdView adView) {

	}

	@Override
	public void onClickedAd(String s, AdView adView) {

	}

	@Override
	public void onBackPressed() {
		TedBackPressDialog.startAdmobDialog(this, getString(R.string.app_name), getString(R.string.ADMOB_KEY_BACKPRESS), new OnBackPressListener() {
			@Override
			public void onReviewClick() {
				String packageName = "";
				try {
					PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
					packageName = getPackageName();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
				} catch (PackageManager.NameNotFoundException e){
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
				}
			}
			@Override
			public void onFinish() {
				PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
				finish();
			}

			@Override
			public void onError(String errorMessage) {
			}

			@Override
			public void onLoaded(int adType) {
			}

			@Override
			public void onAdClicked(int adType) {

			}
		});
	}
}
