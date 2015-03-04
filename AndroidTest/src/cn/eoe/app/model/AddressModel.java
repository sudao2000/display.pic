package cn.eoe.app.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import cn.eoe.app.ui.F0_AddressListActivity;
import cn.eoe.app.ui.F1_NewAddressActivity;
import cn.eoe.app.ui.F3_RegionPickActivity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

public class AddressModel {

	public ArrayList<ADDRESS> addressList = new ArrayList<ADDRESS>();
	public ArrayList<REGIONS> regionsList0 = new ArrayList<REGIONS>();
	public ADDRESS address;
	private BusinessResponse listener;
	
	
	public static final String ADDRESS_LIST = "address_list";

	public static final String REGION_LIST = "region_list";
	
	private static final String TAG = "AddressModel";
	
	public AddressModel(Context context) {
	}

	// 获取地址列表
	public void getAddressList() {
		AVQuery<AVObject> query = new AVQuery<AVObject>(ADDRESS_LIST);

		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> obj, AVException e) {
				if (e == null) {

					if (obj != null) {
						for (AVObject o : obj) {
							ADDRESS addr = new ADDRESS();
							addr.country = o.getString("country_name");
							addr.email = o.getString("email");
							addr.province_name = o.getString("province_name");
							
							addr.default_address = 1;//todo
							addressList.add(addr);
							
						}

						listener.OnMessageResponse(F0_AddressListActivity.ADDRESS_LIST);
					}
				} else {
					Log.e(TAG, "查询错误: " + e.getMessage());
				}
			}
		});
	}

	public void addResponseListener(
			BusinessResponse l) {
		listener = l;
	}

	// 添加地址
	public void addAddress(String consignee, String tel, String email,
			String mobile1, String zipcode, String address, String country,
			String province, String city, String district) {

		AVObject addr = new AVObject("address_list");
		addr.put("user_id", AVUser.getCurrentUser());
		addr.put("consignee", consignee);
		addr.put("tel", tel);
		addr.put("email", email);
		addr.put("mobile1", mobile1);
		addr.put("zipcode", zipcode);
		addr.put("address", address);
		addr.put("country", country);
		addr.put("province", province);
		addr.put("city", city);
		addr.put("district", district);	
		
		
		addr.saveInBackground(new SaveCallback() {
			   @Override
			   public void done(AVException e) {
			        if (e == null) {
			            Log.i("LeanCloud", "Save address successfully.");
			            listener.OnMessageResponse(F1_NewAddressActivity.ADDRESS_ADD_OK);
			        } else {
			            Log.e("LeanCloud", "Save address failed.");
			            listener.OnMessageResponse(F1_NewAddressActivity.ADDRESS_ADD_FAILED);
			        }
			    }
			});
		
		/*
		MyProgressDialog pd = new MyProgressDialog(mContext, mContext
				.getResources().getString(R.string.hold_on));
		aq.progress(pd.mDialog).ajax(cb);
		*/

	}	

	// 获取地区城市
	public void region(int parent_id) {
		AVQuery<AVObject> query = new AVQuery<AVObject>(REGION_LIST);

		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> obj, AVException e) {
				if (e == null) {

					if (obj != null) {
						for (AVObject o : obj) {
							REGIONS addr = new REGIONS();
							addr.id = o.getString("id");
							addr.name = o.getString("name");
							addr.parent_id = o.getString("parent_id");
							
							regionsList0.add(addr);							
						}
						listener.OnMessageResponse(F3_RegionPickActivity.REGION_LIST_OK);
						
					}
				} else {
					listener.OnMessageResponse(F3_RegionPickActivity.REGION_LIST_FAILED);
					Log.e(TAG, "查询错误: " + e.getMessage());
				}
			}
		});
	}

	// 获取地址详细信息
	public void getAddressInfo(String address_id) {
		AVQuery<AVObject> query = new AVQuery<AVObject>(ADDRESS_LIST);
		query.whereEqualTo("id", address_id);
		
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> obj, AVException e) {
				if (e == null) {

					if (obj != null) {
						for (AVObject o : obj) {
							address = new ADDRESS();
							address.country = o.getString("country_name");
							address.email = o.getString("email");
							address.province_name = o.getString("province_name");
							
							address.default_address = 1;//todo
						}

						listener.OnMessageResponse(F0_AddressListActivity.ADDRESS_DETAIL);
					}
				} else {
					Log.e(TAG, "查询错误: " + e.getMessage());
				}
			}
		});

	}

	// 设置默认地址
	public void addressDefault(final String address_id) {
		final AVObject address_list_table = new AVObject(ADDRESS_LIST);
		AVQuery<AVObject> query = new AVQuery<AVObject>(ADDRESS_LIST);
		query.whereEqualTo("id", address_id);

		query.getFirstInBackground(new GetCallback<AVObject>() {
			@Override
			public void done(AVObject queryResult, AVException e) {
				if (e == null) {
					if (queryResult != null) {
						queryResult.put("default_address_id", address_id);
						queryResult.saveInBackground(new SaveCallback() {
							@Override
							public void done(AVException e) {
								
								if (e == null)
									Log.e(TAG, "save address default ok.");
								else
									Log.e(TAG, "save address default error : " + e.getMessage());
							}							
						});
					}
				} else {
					Log.e(TAG, "save address default error : " + e.getMessage());
				}
			}
			
		});
//
//		cb.url(ApiInterface.ADDRESS_SETDEFAULT).type(JSONObject.class)
//				.params(params);
//		MyProgressDialog pd = new MyProgressDialog(mContext, mContext
//				.getResources().getString(R.string.hold_on));
//		aq.progress(pd.mDialog).ajax(cb);

	}

	// 删除地址
	public void addressDelete(String address_id) {
		AVObject address_list_table = new AVObject(ADDRESS_LIST);
		AVQuery<AVObject> query = new AVQuery<AVObject>(ADDRESS_LIST);
		query.whereEqualTo("id", address_id);
		query.deleteAllInBackground(new DeleteCallback() {

			@Override
			public void done(AVException e) {
				if (e == null) {
					Log.e(TAG, "delete address ok ");
				} else {
					Log.e(TAG, "delete address error " + e.getMessage());
				}				
			}
			
		});
		
//		cb.url(ApiInterface.ADDRESS_DELETE).type(JSONObject.class)
//				.params(params);
//		MyProgressDialog pd = new MyProgressDialog(mContext, mContext
//				.getResources().getString(R.string.hold_on));
//		aq.progress(pd.mDialog).ajax(cb);

	}

	// 修改地址
	public void addressUpdate(String address_id, final String consignee, final String tel,
			final String email, final String mobile1, final String zipcode, final String address,
			final String country, final String province, final String city, final String district) {
		
		AVObject address_list_table = new AVObject(ADDRESS_LIST);
		
		AVQuery<AVObject> query = new AVQuery<AVObject>(ADDRESS_LIST);
		query.whereEqualTo("id", address_id);		
		query.getInBackground(address_id, new GetCallback<AVObject>() {			
				@Override
				public void done(AVObject obj, AVException e) {
					obj.put("consignee", consignee);
					obj.put("tel", tel);
					obj.put("email", email);
					obj.put("mobile1", mobile1);
					obj.put("zipcode", zipcode);
					obj.put("address", address);
					obj.put("country", country);
					obj.put("province", province);
					obj.put("city", city);
					obj.put("district", district);
					
					obj.saveInBackground(new SaveCallback() {
							@Override
							public void done(AVException e) {
								
								if (e == null)
									Log.e(TAG, "update address ok.");
								else
									Log.e(TAG, "update address error : " + e.getMessage());
							}							
						});
				}
			});
		
//		cb.url(ApiInterface.ADDRESS_UPDATE).type(JSONObject.class)
//				.params(params);
//		MyProgressDialog pd = new MyProgressDialog(mContext, mContext
//				.getResources().getString(R.string.hold_on));
//		aq.progress(pd.mDialog).ajax(cb);

	}

}
