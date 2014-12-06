package com.pj3.pos.waiter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.Options;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pj3.pos.R;
import com.pj3.pos.res_public.Food;
import com.pj3.pos.res_public.FoodStatistic;

public class WaiterOrderGridfood extends Activity {
	private GridView gvFood;
	private ArrayList<Food> data;
	private GridfoodAdapter aa;
	private ArrayList<String> arrFoodOption = new ArrayList<String>();
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.waiter_tab2_gridfood);
		data = new ArrayList<Food>();
		getListFoodFS();
		intent = getIntent();
		gvFood = (GridView) findViewById(R.id.gvfood);

		// Food a = new Food(1, "Coffee", 20, null, true);
		// data.add(a);

		aa = new GridfoodAdapter(this, R.layout.waiter_tab2_gridfood_item, data);
		gvFood.setAdapter(aa);
		gvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				String nameFood = data.get(position).getM_name();
				arrFoodOption.clear();
				// nho gan du lieu cho arrFoodOption lay tu doi tuong Food
				// String option1 = "Không đường";
				// String option2 = "Không đá";
				// arrFoodOption.add(option1);
				// arrFoodOption.add(option2);
//				if (!data.get(position).getM_option().equals(""))
//					arrFoodOption = splitFoodOption(data.get(position)
//							.getM_option());
				arrFoodOption = splitFoodOption(data.get(position)
						.getM_option());
				AlertDialog.Builder ad = new AlertDialog.Builder(
						WaiterOrderGridfood.this);
				ad.setTitle(nameFood);
				// Tao custom dialog
				final View v = getLayoutInflater().inflate(
						R.layout.waiter_tab2_gridfood_alertdialog, null);
				ad.setView(v);
				LinearLayout rgOption = (LinearLayout) v
						.findViewById(R.id.rgOption);
				// arraylist de luu tru cac radiobutton thong tin option cua mon
				// an
				final ArrayList<RadioButton> arrOptions = new ArrayList<RadioButton>();
				// tao cac radiobutton ung voi cac option cua moi Food
				for (int i = 0; i < arrFoodOption.size(); i++) {
					String radioButtonName = i + "";
					RadioButton rb = new RadioButton(WaiterOrderGridfood.this);
					rb.setText(arrFoodOption.get(i));
					rgOption.addView(rb);
					arrOptions.add(rb);
				}

				ad.setPositiveButton("Đã xong",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								EditText etSoluong = (EditText) v
										.findViewById(R.id.etSoLuong);
								String optionsChecked = "";
								for (int i = 0; i < arrOptions.size(); i++) {
									RadioButton rb = arrOptions.get(i);
									if (rb.isChecked())
										optionsChecked += rb.getText() + " ";
								}
								FoodStatistic fs = new FoodStatistic();
								// if
								// (!etSoluong.getText().toString().equals(""))
								// {
								try {
									int soLuong = Integer.parseInt(etSoluong
											.getText() + "");
									fs.setF_count(soLuong);

									fs.setF_id(data.get(position)
											.getM_food_id());
									fs.setM_option(optionsChecked);
									fs.setM_name(data.get(position).getM_name());
									// gui du lieu ve listview order_detail
									// Intent it = getIntent();
									Bundle b = new Bundle();
									b.putSerializable("fs", fs);
									intent.putExtra("b", b);
									setResult(1111, intent);
									finish();
								} catch (NumberFormatException e) {
									Toast.makeText(getBaseContext(),
											"Cần nhập số lượng!",
											Toast.LENGTH_LONG).show();
								}

								// } else {

								// Toast.makeText(getBaseContext(),fs.toString(),
								// Toast.LENGTH_LONG).show();
							}
						});
				ad.setNegativeButton("Hủy bỏ",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				ad.create().show();
			}
		});
		super.onCreate(savedInstanceState);
	}

	private class GridfoodAdapter extends ArrayAdapter<Food> {
		ArrayList<Food> data = new ArrayList<Food>();

		public GridfoodAdapter(Context context, int idItemLayout,
				ArrayList<Food> data) {
			super(context, idItemLayout, data);
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(
					R.layout.waiter_tab2_gridfood_item, null);
			final TextView tvFoodName = (TextView) convertView
					.findViewById(R.id.gridfood_tv);
			tvFoodName.setText(data.get(position).getM_name());
			return convertView;
		}
	}

	// get data: listfood from server
	public void getListFoodFS() {
		new GetMenuAsyncTask()
				.execute("hehehttp://hmkcode.appspot.com/rest/controller/get.json");
	}

	private class GetMenuAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			return GET(urls[0]);
		}

		// nap data cho ArrayList data o day
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			JSONObject json = null;
			try {
				// ep kieu ve JSONObject
				json = new JSONObject(result);
				JSONArray menu = json.getJSONArray("m_array");
				for (int i = 0; i < menu.length(); i++) {
					JSONObject jObject = menu.getJSONObject(i);
					String f_id = jObject.getString("f_id");
					String f_name = jObject.getString("f_name");
					String f_price = jObject.getString("f_price");
					String f_image = jObject.getString("f_image");
					String f_status = jObject.getString("f_status");
					String f_option = jObject.getString("f_option");
					Food food = new Food();
					int f_id_int = Integer.parseInt(f_id);
					int f_price_int = Integer.parseInt(f_price);

					food.setM_name(f_name);
					if (f_option != null)
						food.setM_option(f_option);
					else
						food.setM_option("");
					food.setM_food_id(f_id_int);
					food.setM_price(f_price_int);
					data.add(food);
				}
				// menu.getJSONObject(0);
				// get first article keys [title,url,categories,tags]
				// menu.getJSONObject(0).names();
				// return an article title
				// String titledemo = menu.getJSONObject(0).getString("title");
				//
				// JSONArray arrayTags = articles
				// .getJSONObject(0).getJSONArray("tags");
				// String android = arrayTags.get(0);
				// lay phan tu dau tien trong mang 'tags'

			} catch (JSONException e) {
				// Toast.makeText(getBaseContext(),"Cann't convert json to String!"+e.toString(),Toast.LENGTH_LONG).show();
				Log.i("hehe", e.toString());
				e.printStackTrace();
			}
		}
	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {
			// HttpClient httpclient = new DefaultHttpClient();
			// HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			// inputStream = httpResponse.getEntity().getContent();
			// dung de test
			String pathOfFile = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			pathOfFile += "/Download/menu.json";
			inputStream = new FileInputStream(pathOfFile);

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		} catch (Exception e) {
			Log.d("hehe", "Loi trong ham Get: " + e.toString());
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

	// tach option theo dau ","
	public ArrayList<String> splitFoodOption(String roughOption) {
		ArrayList<String> result = new ArrayList<String>();
		// test
//		roughOption = "dog,cat,fish";

		StringTokenizer line = new StringTokenizer(roughOption, ",");
		int count = line.countTokens();
		for (int i = 0; i < count; i++) {
			String items = line.nextToken();
			result.add(items);
		}
		return result;
	}
}
