package cn.eoe.app.component;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.eoe.app.MyApplication;
import cn.eoe.app.R;
import cn.eoe.app.protocol.SIMPLEGOODS;
import cn.eoe.app.ui.AboutActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GoodItemCell extends LinearLayout
{
    Context mContext;
    private ImageView good_cell_photo_one;
    
    private TextView    good_cell_price_one;

    private TextView market_price_one;

    private TextView    good_cell_desc_one;

    private LinearLayout good_cell_one;
    
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public GoodItemCell(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
	}

    void init()
    {
        if (null == good_cell_one)
        {
            good_cell_one = (LinearLayout)findViewById(R.id.good_item_one);
        }

        if (null == good_cell_photo_one)
        {
            good_cell_photo_one = (ImageView)good_cell_one.findViewById(R.id.gooditem_photo);
        }

        if (null == good_cell_price_one)
        {
            good_cell_price_one = (TextView)good_cell_one.findViewById(R.id.shop_price);
        }

        if (null == good_cell_desc_one)
        {
            good_cell_desc_one = (TextView)good_cell_one.findViewById(R.id.good_desc);
        }
        
        if (null == market_price_one)
        {
        	market_price_one = (TextView)good_cell_one.findViewById(R.id.promote_price);
        	market_price_one.getPaint().setAntiAlias(true);
        	market_price_one.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
    }

    public void bindData(List<SIMPLEGOODS> listData)
    {
        init();
        
        if (listData.size() > 0) {
        	for (SIMPLEGOODS goodOne : listData) {
        		if (null != goodOne && null != goodOne.img && null != goodOne.img.url) {
        			imageLoader.displayImage(goodOne.img.url ,good_cell_photo_one, MyApplication.options);
        		}
        	}
        	return;
        }
        
        shared = mContext.getSharedPreferences("userInfo", 0); 
		editor = shared.edit();
		
		String imageType = shared.getString("imageType", "mind");		
		

        if (listData.size() > 0)
        {
            final SIMPLEGOODS goodOne = listData.get(0);
            if (null != goodOne && null != goodOne.img && null != goodOne.img.thumb && null != goodOne.img.small)
            {
            	
            	if(imageType.equals("high")) {
                    imageLoader.displayImage(goodOne.img.thumb,good_cell_photo_one, MyApplication.options);

        		} else if(imageType.equals("low")) {
                    imageLoader.displayImage(goodOne.img.small,good_cell_photo_one, MyApplication.options);
        		} else {
        			String netType = shared.getString("netType", "wifi");
        			if(netType.equals("wifi")) {
                        imageLoader.displayImage(goodOne.img.thumb,good_cell_photo_one, MyApplication.options);
        			} else {
                        imageLoader.displayImage(goodOne.img.small,good_cell_photo_one, MyApplication.options);
        			}
        		}
                
            }

            if (null!= goodOne.promote_price&& goodOne.promote_price.length() > 0)
            {
                good_cell_price_one.setText(goodOne.promote_price);
            }
            else
            {
                good_cell_price_one.setText(goodOne.shop_price);
            }


            market_price_one.setText(goodOne.market_price);
            good_cell_desc_one.setText(goodOne.name);
            
            good_cell_photo_one.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
					//Intent it = new Intent(mContext,B2_ProductDetailActivity.class);
					Intent it = new Intent(mContext,AboutActivity.class);					
		        	it.putExtra("good_id", goodOne.goods_id);
		            mContext.startActivity(it);
				}
			});
        }
    }
	

}
