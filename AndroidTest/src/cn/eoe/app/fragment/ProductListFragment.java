package cn.eoe.app.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.eoe.app.R;
import cn.eoe.app.adapter.B1_ProductListAdapter;
import cn.eoe.app.protocol.PHOTO;
import cn.eoe.app.protocol.SIMPLEGOODS;
import cn.eoe.app.xlistview.XListView;
import cn.eoe.app.xlistview.XListView.IXListViewListener;

/*
 import cn.eoe.app.ui.A0_SigninActivity;
 import cn.eoe.app.ui.AjaxStatus;
 import cn.eoe.app.ui.B1_ProductListActivity;
 import cn.eoe.app.ui.BeeBaseAdapter;
 import cn.eoe.app.ui.C0_ShoppingCartActivity;
 import cn.eoe.app.ui.D2_FilterActivity;
 import cn.eoe.app.ui.GoodListLargeListActivityAdapter;
 import cn.eoe.app.ui.GoodsListModel;
 import cn.eoe.app.ui.PAGINATED;
 import cn.eoe.app.ui.B1_ProductListActivity.TitleCellHolder;
 */

public class ProductListFragment extends Fragment implements
		IXListViewListener, OnClickListener {
	private ImageView backImageButton;

	private ImageView item_grid_button;
	private ImageView shopping_cart;
	private TextView good_list_shopping_cart_num;
	private LinearLayout good_list_shopping_cart_num_bg;

	private XListView goodlistView;
	// private GoodsListModel dataModel;
	private B1_ProductListAdapter listAdapter;

	private ImageView bg;

	private final int flag = IS_HOT;

	private boolean isSetAdapter = true;

	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	private View null_pager;

	public static final String KEYWORD = "keyword";
	public static final String CATEGORY_ID = "category_id";
	public static final String TITLE = "title";
	public static final String FILTER_KEY = "filter";

	public static final int IS_HOT = 0;
	public static final int PRICE_DESC_INT = 1;
	public static final int PRICE_ASC_INT = 2;

	public String predefine_category_id;

	protected class TitleCellHolder {
		public ImageView triangleImageView;
		public TextView titleTextView;
		public ImageView orderIconImageView;
		public RelativeLayout tabRelative;
	}

	TitleCellHolder tabOneCellHolder;
	TitleCellHolder tabTwoCellHolder;
	TitleCellHolder tabThreeCellHolder;

	// private BeeBaseAdapter currentAdapter;

	// FILTER filter = new FILTER();

	private ImageView search;
	private EditText input;
	private Button searchFilter;
	private View bottom_line;
	private View top_view;

	private View wholeView;
	private Activity attachedActivity;

	public static Fragment newInstance(String tag) {
		return new ProductListFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		attachedActivity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		wholeView = inflater.inflate(R.layout.b1_product_list, null);

		input = (EditText) wholeView.findViewById(R.id.search_input);
		search = (ImageView) wholeView.findViewById(R.id.search_search);
		searchFilter = (Button) wholeView.findViewById(R.id.search_filter);
		bottom_line = wholeView.findViewById(R.id.bottom_line);
		top_view = wholeView.findViewById(R.id.top_view);

		search.setOnClickListener(this);
		searchFilter.setOnClickListener(this);
		top_view.setOnClickListener(this);
		input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		input.setInputType(EditorInfo.TYPE_CLASS_TEXT);

		input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String keyword = input.getText().toString().toString();
					if (null != keyword && keyword.length() > 0) {
						// B1_ProductListActivity.this.filter.keywords =
						// keyword;
						// dataModel.fetchPreSearch(B1_ProductListActivity.this.filter);
					} else {
						// B1_ProductListActivity.this.filter.keywords = null;
						// dataModel.fetchPreSearch(B1_ProductListActivity.this.filter);
					}
					CloseKeyBoard();

				}
				return false;
			}
		});

		final LinearLayout mainView = (LinearLayout) wholeView
				.findViewById(R.id.keyboardLayout1);
		ViewTreeObserver mainViewObserver = mainView.getViewTreeObserver();
		if (null != mainViewObserver) {
			mainViewObserver
					.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {

							int heightDiff = mainView.getRootView().getHeight()
									- mainView.getHeight();
							if (heightDiff > 100) { // if more than 100 pixels,
													// its probably a
													// keyboard...
								input.setCursorVisible(true);
								top_view.setVisibility(View.VISIBLE);
								searchFilter.setVisibility(View.GONE);
							} else {
								input.setCursorVisible(false);
								top_view.setVisibility(View.INVISIBLE);
								searchFilter.setVisibility(View.VISIBLE);
							}
						}
					});
		}

		shared = attachedActivity.getSharedPreferences("userInfo", 0);
		editor = shared.edit();

		backImageButton = (ImageView) wholeView
				.findViewById(R.id.nav_back_button);
		backImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// finish();
				CloseKeyBoard();
				// overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			}
		});

		shopping_cart = (ImageView) wholeView
				.findViewById(R.id.good_list_shopping_cart);
		shopping_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * String uid = shared.getString("uid", ""); if(uid.equals(""))
				 * { Intent intent = new Intent(B1_ProductListActivity.this,
				 * A0_SigninActivity.class); startActivity(intent);
				 * overridePendingTransition
				 * (R.anim.push_buttom_in,R.anim.push_buttom_out); Resources
				 * resource = (Resources) getBaseContext().getResources();
				 * String nol=resource.getString(R.string.no_login); ToastView
				 * toast = new ToastView(B1_ProductListActivity.this, nol);
				 * toast.setGravity(Gravity.CENTER, 0, 0); toast.show(); } else
				 * { Intent it = new
				 * Intent(B1_ProductListActivity.this,C0_ShoppingCartActivity
				 * .class); startActivity(it); }
				 */
			}
		});

		/*
		 * good_list_shopping_cart_num = (TextView)
		 * findViewById(R.id.good_list_shopping_cart_num);
		 * good_list_shopping_cart_num_bg = (LinearLayout)
		 * findViewById(R.id.good_list_shopping_cart_num_bg); //设置购物车数量
		 * 
		 * if(ShoppingCartModel.getInstance().goods_num == 0) {
		 * good_list_shopping_cart_num_bg.setVisibility(View.GONE); } else {
		 * good_list_shopping_cart_num_bg.setVisibility(View.VISIBLE);
		 * if(ShoppingCartModel.getInstance()!=null){
		 * good_list_shopping_cart_num
		 * .setText(ShoppingCartModel.getInstance().goods_num+""); } }
		 */
		bg = (ImageView) wholeView.findViewById(R.id.goodslist_bg);
		null_pager = wholeView.findViewById(R.id.null_pager);

		goodlistView = (XListView) wholeView.findViewById(R.id.goods_listview);

		goodlistView.setPullLoadEnable(true);
		goodlistView.setRefreshTime();
		goodlistView.setXListViewListener(this, 1);

		// dataModel = new GoodsListModel(this);

		/*
		 * String filter_string = getIntent().getStringExtra(FILTER); if (null
		 * != filter_string) { try { JSONObject filterJSONObject = new
		 * JSONObject(filter_string); filter =new FILTER();
		 * filter.fromJson(filterJSONObject); filter.sort_by =
		 * dataModel.PRICE_DESC;
		 * 
		 * if(null != filter.category_id) { predefine_category_id =
		 * filter.category_id; }
		 * 
		 * if (null != filter.keywords) { input.setText(filter.keywords);
		 * input.setSelection(input.getText().length()); }
		 * 
		 * } catch (JSONException e) { e.printStackTrace(); } }
		 */

		// dataModel.addResponseListener(this);
		/*
		 * largeListActivityAdapter = new
		 * GoodListLargeListActivityAdapter(this,dataModel.simplegoodsList);
		 * 
		 * tabOneCellHolder = new TitleCellHolder(); tabTwoCellHolder = new
		 * TitleCellHolder(); tabThreeCellHolder = new TitleCellHolder();
		 * 
		 * tabOneCellHolder.titleTextView =
		 * (TextView)findViewById(R.id.filter_title_tabone);
		 * tabOneCellHolder.orderIconImageView =
		 * (ImageView)findViewById(R.id.filter_order_tabone);
		 * tabOneCellHolder.triangleImageView =
		 * (ImageView)findViewById(R.id.filter_triangle_tabone);
		 * tabOneCellHolder.tabRelative =
		 * (RelativeLayout)findViewById(R.id.tabOne);
		 * tabOneCellHolder.tabRelative.setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { selectedTab(IS_HOT); } });
		 * 
		 * tabTwoCellHolder.titleTextView =
		 * (TextView)findViewById(R.id.filter_title_tabtwo);
		 * tabTwoCellHolder.orderIconImageView =
		 * (ImageView)findViewById(R.id.filter_order_tabtwo);
		 * tabTwoCellHolder.triangleImageView =
		 * (ImageView)findViewById(R.id.filter_triangle_tabtwo);
		 * tabTwoCellHolder.tabRelative =
		 * (RelativeLayout)findViewById(R.id.tabTwo);
		 * tabTwoCellHolder.tabRelative.setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { flag = PRICE_DESC_INT;
		 * selectedTab(PRICE_DESC_INT);
		 * 
		 * } });
		 * 
		 * tabThreeCellHolder.titleTextView =
		 * (TextView)findViewById(R.id.filter_title_tabthree);
		 * tabThreeCellHolder.orderIconImageView =
		 * (ImageView)findViewById(R.id.filter_order_tabthree);
		 * tabThreeCellHolder.triangleImageView =
		 * (ImageView)findViewById(R.id.filter_triangle_tabthree);
		 * tabThreeCellHolder.tabRelative =
		 * (RelativeLayout)findViewById(R.id.tabThree);
		 * tabThreeCellHolder.tabRelative.setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { flag = PRICE_ASC_INT;
		 * selectedTab(PRICE_ASC_INT);
		 * 
		 * } }); selectedTab(PRICE_ASC_INT); flag = PRICE_ASC_INT;
		 */

		setContent();
		return wholeView;
	}

	void selectedTab(int index) {
		isSetAdapter = true;
		Resources resource = attachedActivity.getResources();
		ColorStateList selectedTextColor = resource
				.getColorStateList(R.color.filter_text_color);

		if (index == IS_HOT) {
			/*
			 * tabOneCellHolder.orderIconImageView.setImageResource(R.drawable.
			 * item_grid_filter_down_active_arrow);
			 * tabOneCellHolder.orderIconImageView.setWillNotCacheDrawing(true);
			 * tabOneCellHolder.triangleImageView.setVisibility(View.VISIBLE);
			 * tabOneCellHolder.titleTextView.setTextColor(Color.WHITE);
			 * 
			 * tabTwoCellHolder.orderIconImageView.setImageResource(R.drawable.
			 * item_grid_filter_down_arrow);
			 * tabTwoCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
			 * tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);
			 * 
			 * tabThreeCellHolder.triangleImageView.setVisibility(View.INVISIBLE)
			 * ;
			 * tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable
			 * .item_grid_filter_up_arrow);
			 * tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);
			 */

			// filter.sort_by = dataModel.IS_HOT;
			// dataModel.fetchPreSearch(filter);

		} else if (index == PRICE_DESC_INT) {
			/*
			 * tabTwoCellHolder.orderIconImageView.setImageResource(R.drawable.
			 * item_grid_filter_down_active_arrow);
			 * tabTwoCellHolder.triangleImageView.setVisibility(View.VISIBLE);
			 * tabTwoCellHolder.titleTextView.setTextColor(Color.WHITE);
			 * 
			 * tabOneCellHolder.orderIconImageView.setImageResource(R.drawable.
			 * item_grid_filter_down_arrow);
			 * tabOneCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
			 * tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);
			 * 
			 * tabThreeCellHolder.triangleImageView.setVisibility(View.INVISIBLE)
			 * ;
			 * tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable
			 * .item_grid_filter_up_arrow);
			 * tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);
			 */
			// filter.sort_by = dataModel.PRICE_DESC;
			// dataModel.fetchPreSearch(filter);
		} else {
			/*
			 * tabThreeCellHolder.triangleImageView.setVisibility(View.VISIBLE);
			 * tabThreeCellHolder
			 * .orderIconImageView.setImageResource(R.drawable.
			 * item_grid_filter_up_active_arrow);
			 * tabThreeCellHolder.titleTextView.setTextColor(Color.WHITE);
			 * 
			 * tabOneCellHolder.orderIconImageView.setImageResource(R.drawable.
			 * item_grid_filter_down_arrow);
			 * tabOneCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
			 * tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);
			 * 
			 * tabTwoCellHolder.orderIconImageView.setImageResource(R.drawable.
			 * item_grid_filter_down_arrow);
			 * tabTwoCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
			 * tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);
			 */
			// filter.sort_by = dataModel.PRICE_ASC;
			// dataModel.fetchPreSearch(filter);

		}
	}

	public void setContent() {

		if (listAdapter == null) {
			if (1 == 0) {
				// if(dataModel.simplegoodsList.size() == 0) {
				bg.setVisibility(View.GONE);
				null_pager.setVisibility(View.VISIBLE);
				bottom_line.setBackgroundColor(Color.parseColor("#FFFFFF"));

			} else {
				bg.setVisibility(View.GONE);
				null_pager.setVisibility(View.GONE);
				bottom_line.setBackgroundColor(Color.parseColor("#000000"));

				ArrayList<SIMPLEGOODS> itemList = new ArrayList<SIMPLEGOODS>();
				for (int i = 0; i < 10; i++) {
					SIMPLEGOODS d = new SIMPLEGOODS();
					d.id = "" + i;
					d.market_price = (10 + i) + "";
					d.promote_price = (20 + i) + "";
					d.brief = "货物" + i;
					d.img = new PHOTO();
					d.img.url = "http://passport.ixpub.net/data/avatar/026/61/13/83_avatar_middle.jpg";
					itemList.add(d);
				}

				// listAdapter = new B1_ProductListAdapter(this,
				// dataModel.simplegoodsList);
				listAdapter = new B1_ProductListAdapter(attachedActivity,
						itemList);
				goodlistView.setAdapter(listAdapter);
			}

		} else {

			if (isSetAdapter == true) {
				// listAdapter = new B1_ProductListAdapter(this,
				// dataModel.simplegoodsList);

				goodlistView.setAdapter(listAdapter);
			} else {
				// listAdapter.dataList = dataModel.simplegoodsList;
				listAdapter.notifyDataSetChanged();
			}
		}

		/*
		 * //设置购物车数量 if (ShoppingCartModel.getInstance().goods_num == 0) {
		 * good_list_shopping_cart_num_bg.setVisibility(View.GONE); } else {
		 * good_list_shopping_cart_num_bg.setVisibility(View.VISIBLE); if
		 * (ShoppingCartModel .getInstance()!= null) {
		 * good_list_shopping_cart_num
		 * .setText(ShoppingCartModel.getInstance().goods_num + ""); } }
		 */
	}

	/*
	 * public void OnMessageResponse(String url, JSONObject jo, AjaxStatus
	 * status) throws JSONException {
	 * 
	 * if(url.endsWith(ApiInterface.SEARCH)) { goodlistView.stopRefresh();
	 * goodlistView.stopLoadMore(); goodlistView.setRefreshTime();
	 * 
	 * setContent(); PAGINATED paginated = new PAGINATED();
	 * paginated.fromJson(jo.optJSONObject("paginated")); if (0 ==
	 * paginated.more) { goodlistView.setPullLoadEnable(false); } else {
	 * goodlistView.setPullLoadEnable(true); } }
	 * 
	 * }
	 */

	@Override
	public void onClick(View v) {
		String tag;
		Intent intent;
		switch (v.getId()) {
		case R.id.search_search:

			break;
		case R.id.top_view:
			CloseKeyBoard();
			input.setText("");
			break;
		/*
		 * case R.id.search_filter: {
		 * 
		 * Intent it = new Intent(this, D2_FilterActivity.class); try {
		 * it.putExtra("filter",filter.toJson().toString()); if (null !=
		 * predefine_category_id) {
		 * it.putExtra("predefine_category_id",predefine_category_id); }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * } startActivityForResult(it, 1); break; }
		 */
		}

	}

	/*
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * ((keyCode == KeyEvent.KEYCODE_BACK)) { finish();
	 * overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	 * return false; } ; }
	 */

	@Override
	public void onRefresh(int id) {
		isSetAdapter = true;
		// dataModel.fetchPreSearch(filter);

	}

	@Override
	public void onLoadMore(int id) {
		isSetAdapter = false;
		// dataModel.fetchPreSearchMore(filter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1: {
			/*
			 * if(null != data) { String filter_string =
			 * data.getStringExtra("filter"); if (null != filter_string) { try {
			 * JSONObject filterJSONObject = new JSONObject(filter_string);
			 * FILTER filter = new FILTER(); filter.fromJson(filterJSONObject);
			 * this.filter.category_id = filter.category_id;
			 * this.filter.price_range = filter.price_range;
			 * this.filter.brand_id = filter.brand_id;
			 * dataModel.fetchPreSearch(this.filter); input.clearFocus(); }
			 * catch (JSONException e) { e.printStackTrace(); } } }
			 */
		}

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		/*
		 * if(ShoppingCartModel.getInstance().goods_num == 0) {
		 * good_list_shopping_cart_num_bg.setVisibility(View.GONE); } else {
		 * good_list_shopping_cart_num_bg.setVisibility(View.VISIBLE);
		 * good_list_shopping_cart_num
		 * .setText(ShoppingCartModel.getInstance().goods_num+""); }
		 */
	}

	// 关闭键盘
	public void CloseKeyBoard() {
		input.clearFocus();
		InputMethodManager imm = (InputMethodManager) attachedActivity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
