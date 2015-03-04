package cn.eoe.app.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.eoe.app.R;
import cn.eoe.app.adapter.F0_AddressListAdapter;
import cn.eoe.app.fragment.ToastView;
import cn.eoe.app.model.AddressModel;
import cn.eoe.app.model.BusinessResponse;
import cn.eoe.app.protocol.ADDRESS;
import cn.eoe.app.protocol.ApiInterface;
import cn.eoe.app.ui.base.BaseActivity;

import com.external.activeandroid.query.Select;
import com.umeng.analytics.MobclickAgent;

/**
 * 收货地址管理
 */
public class F0_AddressListActivity extends BaseActivity implements BusinessResponse {

	public static final int ADDRESS_LIST = 0;
	public static final int ADDRESS_SETDEFAULT = 1;
	public static final int ADDRESS_DETAIL = 2;
	
	private ImageView back;
	private ImageView add;
	private ListView listView;
	private ImageView bg;
	private F0_AddressListAdapter addressManageAdapter;
	private AddressModel addressModel;
	public Handler messageHandler;
	public int flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.f0_address_list);

		Intent intent = getIntent();
		flag = intent.getIntExtra("flag", 0);

		back = (ImageView) findViewById(R.id.address_manage_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		add = (ImageView) findViewById(R.id.address_manage_add);
		listView = (ListView) findViewById(R.id.address_manage_list);
		bg = (ImageView) findViewById(R.id.address_list_bg);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(F0_AddressListActivity.this,
						F1_NewAddressActivity.class);
				startActivity(intent);
			}
		});

		addressModel = new AddressModel(this);
		addressModel.addResponseListener(this);

		messageHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg.what == 1) {
					Integer address_id = msg.arg1;
					//addressModel.addressDefault(address_id + "");
				}

			}
		};
	}

	public void setAddress() {

		if (addressModel.addressList.size() == 0) {
			listView.setVisibility(View.GONE);
			Resources resource = getBaseContext().getResources();
			String non = resource.getString(R.string.non_address);
			ToastView toast = new ToastView(this, non);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			bg.setVisibility(View.VISIBLE);
		} else {
			bg.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			addressManageAdapter = new F0_AddressListAdapter(this,
					addressModel.addressList, flag);
			listView.setAdapter(addressManageAdapter);

			addressManageAdapter.parentHandler = messageHandler;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		addressModel.getAddressList();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

	@Override
	public void OnMessageResponse(int resultCode) {
		if (resultCode == ADDRESS_LIST) {
			setAddress();
		} else if (resultCode == ADDRESS_SETDEFAULT) {
			Intent intent = new Intent();
			intent.putExtra("address", "address");
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

}
