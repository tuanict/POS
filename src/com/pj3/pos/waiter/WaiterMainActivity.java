package com.pj3.pos.waiter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.pj3.pos.R;
import com.pj3.pos.res_public.FoodStatistic;

public class WaiterMainActivity extends Activity {
	// receive data from server
	public ArrayList<String> arrTable = new ArrayList<String>();
	public ArrayList<String> arrTurn = new ArrayList<String>();
	public ArrayList<String> listOrder = new ArrayList<String>();

	// du lieu mon an cua 1 order
	public ArrayList<FoodStatistic> listOrderDetail;

	public ArrayAdapter<String> aaTable, aaTurn;
	public OrderAdapter aaOrder;
	private FoodAdapter aaFood;

	public Spinner spTable, tab3spTable, tab3spTurn;
	public ListView lvOrder, lv_order_detail, tab3_list;
	private LinearLayout tab2_order, tab2_order_detail;
	private ArrayList<HashMap<String, Object>> groupList = new ArrayList<HashMap<String, Object>>();
	private ArrayList<ArrayList<HashMap<String, Object>>> childList = new ArrayList<ArrayList<HashMap<String, Object>>>();
	public Tab1Adapter tab1adpt;

	private ArrayList<HashMap<String, Object>> tab3ListItem = new ArrayList<HashMap<String, Object>>();
	public Tab3Adapter tab3Adapter;

	boolean makeblur = false;
	boolean taomoi_scroll = false;

	// bien chon ban cho order
	private int idBanOrder;
	Button btThemMon, btHoanThanh;

	// tab3 cua Thang
	// public Tab3Adapter tab3Adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waiter_main);
		loadTab();
		tab2();
		tab1();
		// tab3();
	}

	public void tab1() {
		if (isConnected()) {
			new HttpAsyncTask().execute("order.json");

		} else {

		}
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	public void tab2() {

		btThemMon = (Button) findViewById(R.id.btThemMon);
		btThemMon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent itGridFood = new Intent(WaiterMainActivity.this,
						WaiterOrderGridfood.class);
				startActivityForResult(itGridFood, 111);
			}
		});
		btHoanThanh = (Button) findViewById(R.id.btHoanThanh);
		btHoanThanh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tab2_order.setVisibility(View.VISIBLE);
				tab2_order_detail.setVisibility(View.GONE);
			}
		});

		listOrderDetail = new ArrayList<FoodStatistic>();
		spTable = (Spinner) findViewById(R.id.spTable1);
		spTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				idBanOrder = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		// 20 ban
		for (int i = 1; i < 21; i++) {
			arrTable.add(i + "");
		}
		aaTable = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arrTable);
		spTable.setAdapter(aaTable);
		lvOrder = (ListView) findViewById(R.id.lvOrder);
		registerForContextMenu(lvOrder);
		aaOrder = new OrderAdapter(this, R.layout.waiter_tab2_item_order,
				listOrder);
		lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// Intent itGridFood = new Intent(
				// "android.intent.action.WAITERORDERGRIDFOOD");
				Intent itGridFood = new Intent(WaiterMainActivity.this,
						WaiterOrderGridfood.class);
				startActivityForResult(itGridFood, 111);
			}
		});
		lvOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				return false;
			}
		});
		lvOrder.setAdapter(aaOrder);

		tab2_order = (LinearLayout) findViewById(R.id.waiter_tab2_main);
		tab2_order_detail = (LinearLayout) findViewById(R.id.tab2_orderdetail);
		tab2_order_detail.setVisibility(View.GONE);

		lv_order_detail = (ListView) findViewById(R.id.lv_order_detail);
		// FoodStatistic fs1 = new FoodStatistic();
		// fs1.setF_b_id(1);
		// fs1.setM_name("Táo");
		// hehe.add(fs1);
		// listOrderDetail.add(fs1);
		aaFood = new FoodAdapter(WaiterMainActivity.this,
				R.layout.waiter_tab2_item_food, listOrderDetail);
		lv_order_detail.setAdapter(aaFood);
	}

	// nhan du lieu tu viec chon mon
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 111 && resultCode == 1111) {
			Bundle b = data.getBundleExtra("b");
			FoodStatistic fs = null;
			fs = (FoodStatistic) b.getSerializable("fs");
			// Log.d("hehe",fs.getM_option()+"");
			listOrderDetail.add(fs);
			aaFood.notifyDataSetChanged();
			tab2_order.setVisibility(View.GONE);
			tab2_order_detail.setVisibility(View.VISIBLE);
		}
	}

	public void tab3() {
		tab3spTable = (Spinner) findViewById(R.id.spTable2);
		aaTable = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arrTable);
		tab3spTable.setAdapter(aaTable);

		arrTurn.add("1");
		arrTurn.add("2");
		arrTurn.add("3");
		arrTurn.add("4");
		arrTurn.add("5");

		tab3spTurn = (Spinner) findViewById(R.id.spTurn);
		aaTurn = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arrTurn);
		tab3spTurn.setAdapter(aaTurn);

		HashMap<String, Object> a1 = new HashMap<String, Object>();
		a1.put("imgF", R.drawable.coffeeicon);
		a1.put("nameF", "Cafe");
		a1.put("qttyF", "x1");
		a1.put("priceF", "20k/coc");

		tab3ListItem.add(a1);
		tab3ListItem.add(a1);
		tab3ListItem.add(a1);
		tab3ListItem.add(a1);

		tab3_list = (ListView) findViewById(R.id.listDish);
		// tab3 cua Thang
		// tab3Adapter = new Tab3Adapter(this, R.layout.waiter_tab3_item_noti,
		// tab3ListItem);
		// tab3_list.setAdapter(tab3Adapter);
		// tab3Adapter.notifyDataSetChanged();

	}

	public void loadTab() {
		final TabHost tabhost = (TabHost) findViewById(android.R.id.tabhost);

		tabhost.setup();
		TabHost.TabSpec spec;
		spec = tabhost.newTabSpec("spec1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("", this.getResources().getDrawable(R.drawable.tab1));
		tabhost.addTab(spec);

		spec = tabhost.newTabSpec("spec2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("", this.getResources().getDrawable(R.drawable.tab2));

		tabhost.addTab(spec);

		spec = tabhost.newTabSpec("spec3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("", this.getResources().getDrawable(R.drawable.tab3));
		tabhost.addTab(spec);
		tabhost.setCurrentTab(1);
	}

	// event click button ThemMoi
	public void click_AddOrder(View v) {
		listOrder
				.add("Bàn " + idBanOrder + " Lượt " + getTableTurn(idBanOrder));
		aaOrder.notifyDataSetChanged();
	}

	static class OrderHolder {
		TextView tvOrderName;

		public OrderHolder(View row) {
			tvOrderName = (TextView) row.findViewById(R.id.tv_order_name);
		}

		public void populateFrom(String s) {
			tvOrderName.setText(s);
		}
	}

	// for listview Order, "String" is temporary input
	class OrderAdapter extends ArrayAdapter<String> {
		Activity context;
		int idLayout;
		ArrayList<String> listOrder;

		public OrderAdapter(Activity context, int resource,
				ArrayList<String> listOrder) {
			super(context, resource, listOrder);
			this.context = context;
			this.idLayout = resource;
			this.listOrder = listOrder;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			OrderHolder holder;
			if (row == null) {
				row = context.getLayoutInflater().inflate(idLayout, parent,
						false);
				holder = new OrderHolder(row);
				row.setTag(holder);
			} else {
				holder = (OrderHolder) row.getTag();
			}
			holder.populateFrom(listOrder.get(position));
			return row;
		}
	}

	static class FoodHolder {
		ImageView ivFoodIcon;
		TextView tvFoodName;
		TextView tvFoodNumber;
		TextView tvFoodOptionChecked;

		public FoodHolder(View row) {
			ivFoodIcon = (ImageView) row.findViewById(R.id.iv_food_icon);
			tvFoodName = (TextView) row.findViewById(R.id.tv_food_name);
			tvFoodNumber = (TextView) row.findViewById(R.id.tv_food_number);
			tvFoodOptionChecked = (TextView) row
					.findViewById(R.id.tv_food_option_checked);
		}

		public void populateFrom(FoodStatistic fs) {
			// Chua xu ly anh
			tvFoodName.setText(fs.getM_name());
			tvFoodNumber.setText(fs.getF_count() + "");
			// options nay chi gom nhung cai da check
			tvFoodOptionChecked.setText(fs.getM_option() + "");
		}
	}

	// for listview order_detail, "String" is temporary input
	class FoodAdapter extends ArrayAdapter<FoodStatistic> {
		Activity context;
		int idLayout;
		// La FoodStatistic vi can su dung ten mon an
		ArrayList<FoodStatistic> data;

		public FoodAdapter(Activity context, int idLayout,
				ArrayList<FoodStatistic> data) {
			super(context, idLayout, data);
			this.context = context;
			this.idLayout = idLayout;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			FoodHolder holder;
			if (row == null) {
				row = context.getLayoutInflater().inflate(idLayout, parent,
						false);
				holder = new FoodHolder(row);
				row.setTag(holder);
			} else {
				holder = (FoodHolder) row.getTag();
			}
			holder.populateFrom(data.get(position));
			return row;
		}
	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			String json = "{\"o_array\":[{\"o_id\":\"1\",\"t_id\":\"1\",\"t_count\":\"1\",\"f_array\":[{\"f_id\":\"1\",\"f_count\":\"3\",\"f_name\":\"cafe\"},{\"f_id\":\"2\",\"f_count\":\"3\",\"f_name\":\"yohurt\"}]},{\"o_id\":\"2\",\"t_id\":\"2\",\"t_count\":\"1\",\"f_array\":[{\"f_id\":\"2\",\"f_count\":\"3\",\"f_name\":\"cafe\"},{\"f_id\":\"004\",\"f_count\":\"3\",\"f_name\":\"yohurt\"}]}]}";
			parsingJson(json);
			return GET(urls[0]);
		}

	}

	public void parsingJson(String result) {
		try {
			JSONObject json = new JSONObject(result);
			JSONArray orderArr = json.getJSONArray("o_array");
			for (int i = 0; i < orderArr.length(); i++) {
				HashMap<String, Object> table = new HashMap<String, Object>();
				table.put("txtTable", "Bàn"
						+ orderArr.getJSONObject(i).getString("t_id"));
				table.put("txtTurn", "Lượt"
						+ orderArr.getJSONObject(i).getString("t_count"));
				groupList.add(table);

				JSONArray foodArr = orderArr.getJSONObject(i).getJSONArray(
						"f_array");
				ArrayList<HashMap<String, Object>> foods_i = new ArrayList<HashMap<String, Object>>();
				for (int j = 0; j < foodArr.length(); j++) {
					HashMap<String, Object> food = new HashMap<String, Object>();
					food.put("imgFood", R.drawable.coffeeicon);
					food.put("txtFood",
							foodArr.getJSONObject(j).getString("f_name"));
					food.put("txtQuantity",
							foodArr.getJSONObject(j).getString("f_count"));
					food.put("idFood", j++);
					foods_i.add(food);
				}
				childList.add(foods_i);

			}

			ExpandableListView subList = (ExpandableListView) findViewById(R.id.expandableListView1);
			tab1adpt = new Tab1Adapter(getApplicationContext(), groupList,
					childList);

			SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
					subList,
					new SwipeDismissListViewTouchListener.DismissCallbacks() {

						@Override
						public void onDismiss(ListView subList,
								int[] reverseSortedPositions) {
							// TODO Auto-generated method stub
							for (final int position : reverseSortedPositions) {

								AlphaAnimation animation = new AlphaAnimation(
										0.3f, 0.2f);
								animation.setDuration(9000);
								animation.setFillAfter(false);

								subList.getChildAt(position).startAnimation(
										animation);
								makeblur = true;
								Handler mHandler = new Handler();
								Runnable _run = new Runnable() {
									@Override
									public void run() {
										// if(position !=
										// AbsListView.INVALID_POSITION){
										// childList.remove(position);
										// }
										// else groupList.remove(position);
										taomoi_scroll = true;
										makeblur = false;
									}
								};
								mHandler.postDelayed(_run, 9000);
							}

						}

						@Override
						public boolean canDismiss(int position) {
							// TODO Auto-generated method stub
							return true;
						}
					});
			subList.setOnTouchListener(touchListener);
			subList.setOnScrollListener(touchListener.makeScrollListener());
			subList.setAdapter(tab1adpt);
			tab1adpt.notifyDataSetChanged();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
public void onCreateContextMenu(ContextMenu menu, View v,
		ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
	getMenuInflater().inflate(R.menu.waiter_tab2_contextmenu, menu);
}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.thanhtoan)
		{
			Toast.makeText(getBaseContext(),"Thanh toan",Toast.LENGTH_SHORT).show();
		}
		return super.onContextItemSelected(item);	
	}

	// chi dung de tang luot cho ban
	private class tableChoose {
		int tableIndex;
		int tableTurn;

		public tableChoose(int tableIndex, int tableTurn) {
			this.tableIndex = tableIndex;
			this.tableTurn = tableTurn;
		}
	}

	private ArrayList<tableChoose> arrTableTurn = new ArrayList<WaiterMainActivity.tableChoose>();

	private int getTableTurn(int tableIndex) {
		int result = 1;
		boolean isExist = false;
		for (int i = 0; i < arrTableTurn.size(); i++) {
			tableChoose tc = arrTableTurn.get(i);
			if (tc.tableIndex == tableIndex) {
				result = tc.tableTurn + 1;
				tc.tableTurn++;
				isExist = true;
				break;
			}

		}
		if (!isExist) {
			tableChoose tc = new tableChoose(tableIndex, 1);
			arrTableTurn.add(tc);
		}
		return result;
	}
}
