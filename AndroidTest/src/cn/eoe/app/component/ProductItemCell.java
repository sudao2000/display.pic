package cn.eoe.app.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.eoe.app.MyApplication;
import cn.eoe.app.R;
import cn.eoe.app.entity.PushContentEntity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProductItemCell extends LinearLayout {
	Context mContext;
	private ImageView good_cell_photo_one;

	private TextView good_cell_price_one;

	private TextView market_price_one;

	private TextView good_cell_desc_one;

	private LinearLayout good_cell_one;

	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public ProductItemCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	void init() {
		if (null == good_cell_one) {
			good_cell_one = (LinearLayout) findViewById(R.id.good_item_one);
		}

		if (null == good_cell_photo_one) {
			good_cell_photo_one = (ImageView) good_cell_one
					.findViewById(R.id.gooditem_photo);
		}

		if (null == good_cell_price_one) {
			good_cell_price_one = (TextView) good_cell_one
					.findViewById(R.id.shop_price);
		}

		if (null == good_cell_desc_one) {
			good_cell_desc_one = (TextView) good_cell_one
					.findViewById(R.id.good_desc);// description info
		}

		if (null == market_price_one) {
			market_price_one = (TextView) good_cell_one
					.findViewById(R.id.promote_price);
			market_price_one.getPaint().setAntiAlias(true);
			market_price_one.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}

		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}

	public void bindData(PushContentEntity pce) {
		init();

		if (null != pce && null != pce.getImg_url()) {
			imageLoader.displayImage(pce.getImg_url(), good_cell_photo_one,
					MyApplication.options);
		}

	}

}
