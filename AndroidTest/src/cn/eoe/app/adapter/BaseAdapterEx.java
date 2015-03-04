package cn.eoe.app.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import cn.eoe.app.widget.FSFinalBitmap;

public abstract class BaseAdapterEx<T> extends BaseAdapter {
	protected List<T> mList = null;
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected FinalBitmap mFinalBitmap = null;
	protected Bitmap mImageDefault = null;
	protected IGetViewCallback mIGetViewCallback = null;

	public BaseAdapterEx(Context context, List<T> list) {
		mList = list;
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFinalBitmap = FSFinalBitmap.getInstance();
	}

	public BaseAdapterEx(Context context, List<T> list, int drawableId) {
		mList = list;
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFinalBitmap = FSFinalBitmap.getInstance();
		// mImageDefault =
		// BitmapFactory.decodeResource(mContext.getResources(),drawableId);
		// mImageDefault = decodeResource(mContext, drawableId);
		// todo : load this image from raw resouce.
	}

	public static Bitmap decodeResource(Context context, int resId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(resId), null, opts);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mList != null && mList.size() >= position) {
			return mList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void releaseData() {
		if (null != mImageDefault) {
			mImageDefault.recycle();
			mImageDefault = null;
		}
		if (null != mContext) {
			mContext = null;
		}
	}

	public void reSetData() {

	}

	public interface IGetViewCallback {
		public void getPosition(int position);
	}

}
