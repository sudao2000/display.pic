package cn.eoe.app.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.eoe.app.R;
import cn.eoe.app.component.ProductItemCell;
import cn.eoe.app.entity.PushContentEntity;
import cn.eoe.app.utils.DisplayUtil;

import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class PushContentAdapter extends BaseAdapterEx<PushContentEntity> {

	private final List<PushContentEntity> mList;
	private final Context mContext;
	private AnimateFirstDisplayListener animateFirstListener;
	
	public PushContentAdapter(Context context, List<PushContentEntity> list,
			int drawableId) {

		super(context, list, drawableId);
		mContext = context;
		mList = list;		
		animateFirstListener = new AnimateFirstDisplayListener(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = parent.inflate(mContext, R.layout.image_containter_cell,
					null);
		}

		PushContentEntity pce = mList.get(position);
		ProductItemCell cell = (ProductItemCell) convertView;
		cell.bindData(pce, animateFirstListener);
		return convertView;
	}

	@Override
	public int getCount() {
		if (mList != null)
			return mList.size();
		return 0;
	}
	

	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		private Context context;
		private ImageView imgView;
		public AnimateFirstDisplayListener(Context c) {
			super();
			context = c;
		}
		
		public void setDisplayImageView(ImageView v) {
			if (v == null) {
				throw new NullPointerException();
			}
			imgView = v;
		}
		
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//
//				}
				
				int w = loadedImage.getWidth();
				int h = loadedImage.getHeight();
				
				int displayW = DisplayUtil.getScreenWidth(context);
				int displayH = h * displayW / w;
				LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(displayW, displayH);
				imgView.setLayoutParams(p);
				imgView.invalidate();
			}
		}
	}
		
}
