package coinmoa.app.automoney;

import java.util.ArrayList;

import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import coinmoa.app.automoney.data.Favorite_DBopenHelper;
import coinmoa.app.automoney.data.Favorite_Data;
import coinmoa.app.automoney.utils.PreferenceUtil;

public class FavoriteActivity extends Activity implements OnClickListener,OnItemClickListener, OnScrollListener, AdViewListener {
	public static Context context;
	private ArrayList<Favorite_Data> list;
	private LinearLayout layout_listview_main, layout_nodata;
	private ListView listview;
	private ActivityAdapter adapter;
	private ImageButton btnLeft;
	private TextView main_title;
	private RelativeLayout ad_layout;
	private Favorite_DBopenHelper favorite_mydb;
	private Button bt_favorite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_favorite);
		context = this;
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "kh19o528");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4092414235173954/3785554198");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4092414235173954/9668436264");
    	addBannerView();
		init_ui();
		set_titlebar();
		list = new ArrayList<Favorite_Data>();
		list.clear();
		displaylist();
	}
	
	public void addBannerView() {
    	AdInfo adInfo = new AdInfo("kh19o528");
    	adInfo.setTestMode(false);
        AdView adView = new AdView(this);
        adView.setAdInfo(adInfo, this);
        adView.setAdViewListener(this);
        ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
    }
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void init_ui(){
		favorite_mydb = new Favorite_DBopenHelper(this);
		layout_listview_main = (LinearLayout)findViewById(R.id.layout_listview_main);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		bt_favorite = (Button)findViewById(R.id.bt_favorite);
		bt_favorite.setVisibility(View.INVISIBLE);
		listview = (ListView)findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setItemsCanFocus(false);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setSingleLine();
		main_title.setText(context.getString(R.string.bt_favorite));
		main_title.setSingleLine();
	}
	
	private void displaylist(){
		bind_favorite_db(list);
		adapter = new ActivityAdapter();
		listview.setAdapter(adapter);
		
		if(adapter.getCount() > 0){
			layout_nodata.setVisibility(View.GONE);
		}else{
			layout_nodata.setVisibility(View.VISIBLE);
		}
	}

	public void bind_favorite_db(ArrayList<Favorite_Data> list){
		try{
			Cursor cursor = favorite_mydb.getReadableDatabase().rawQuery("select * from favorite_list order by _id desc", null);
			while(cursor.moveToNext()){
				list.add(new Favorite_Data(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
			}
		}catch (Exception e) {
		}finally{
			close_favorite_db();
		}
	}

	private void close_favorite_db(){
		if(favorite_mydb != null)
			favorite_mydb.close();
	}
	
	@Override
	public void onClick(View view) {
		if(view == btnLeft){
			onBackPressed();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Favorite_Data favorite_data = (Favorite_Data)adapter.getItem(position);
		PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_URL, favorite_data.getPortal());
		Intent intent = new Intent(context, MainActivity.class);
		startActivity(intent);

	}
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
			listview.setFastScrollEnabled(true);
		}else{
			listview.setFastScrollEnabled(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	
	public class ActivityAdapter extends BaseAdapter{
		public ActivityAdapter() {
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
			try{
				if(view == null){	
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					view = layoutInflater.inflate(R.layout.favorite_activity_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.txt_title = (TextView)view.findViewById(R.id.txt_title);
					holder.bt_favorite_del = (ImageButton)view.findViewById(R.id.bt_favorite_del);
					holder.bt_favorite_del.setFocusable(false);
					holder.bt_favorite_del.setSelected(false);
					view.setTag(holder);
				}
				
				final ViewHolder holder = (ViewHolder)view.getTag();
				
				holder.txt_title.setText(list.get(position).getTitle());
				holder.bt_favorite_del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						AlertShow_CCM_Activity_Favorite(context.getString(R.string.bt_favorite_del21), list.get(position).get_id());
					}
				});
			}catch (Exception e) {
			}
			return view;
		}
	}

	public void delete_ccm_favorite_db(int _id){
		try{
			favorite_mydb.getWritableDatabase().delete("favorite_list", "_id" + "=" +_id, null);
		}catch (Exception e) {
		}finally{
			close_favorite_db();
		}
	}
	
	public void AlertShow_CCM_Activity_Favorite(String msg, final int id) {
        AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
        alert_internet_status.setCancelable(true);
        alert_internet_status.setMessage(msg);
        alert_internet_status.setPositiveButton(context.getString(R.string.frg_ccm_19),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	Toast.makeText(context, context.getString(R.string.frg_ccm_22), Toast.LENGTH_SHORT).show();
						delete_ccm_favorite_db(id);
						list = new ArrayList<Favorite_Data>();
						list.clear();
						displaylist();
                    }
                });
        alert_internet_status.setNegativeButton(context.getString(R.string.frg_ccm_20), 
       		 new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        alert_internet_status.show();
	}
	
	private class ViewHolder {
		public TextView txt_title;
		public ImageButton bt_favorite_del;
	}
	
	private void setTextViewColorPartial(TextView view, String fulltext, String subtext, int color) {
		try{
			view.setText(fulltext, TextView.BufferType.SPANNABLE);
			Spannable str = (Spannable) view.getText();
			int i = fulltext.indexOf(subtext);
			str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}catch (IndexOutOfBoundsException e) {
		}
	}
	
	private void datasetchanged(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	//** BannerAd 이벤트들 *************
	@Override
	public void onClickedAd(String arg0, AdView arg1) {
	}
	@Override
	public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
		
	}
	@Override
	public void onReceivedAd(String arg0, AdView arg1) {
	}			
}
