package cn.eoe.app.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.eoe.app.MyApplication;
import cn.eoe.app.R;
import cn.eoe.app.entity.PushContentEntity;
import cn.eoe.app.ui.LoginActivity;
import cn.eoe.app.ui.UserCenterActivity;
import cn.eoe.app.utils.IntentUtil;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProductItemCell extends LinearLayout {
	
	
	public static final int LIKE_IT = 1;
	public static final int UNLIKE_IT = 0;
	
	Context mContext;
	private ImageView mainImage;

	private TextView i_reblog;

	private ImageButton i_like;

	private TextView description;

	private LinearLayout good_cell_one;
	private String postId = "";

	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public ProductItemCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	private OnClickListener likeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.i_like:					
					updateILike();
					break;
			}
		}
	};
	
	private void updateILike() {
		AVUser currentUser = AVUser.getCurrentUser();
		if (currentUser != null) {
			if (MyApplication.getInstance().likeList.contains(postId)) {

				saveUnLikeOnServer();
			} else {

				saveLikeOnServer();
			}
		} else {
			IntentUtil.start_activity(mContext, LoginActivity.class);
		}
	}

	private void saveUnLikeOnServer() {
		
		String tableName = "LIKE";
		AVObject gameScore = new AVObject(tableName);
		AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
		query.whereEqualTo("user", AVUser.getCurrentUser());
		query.whereEqualTo("postId", postId);
		query.deleteAllInBackground(new DeleteCallback() {
			   @Override
			   public void done(AVException e) {
			        if (e == null) {
			            Log.i("LeanCloud", "delete like favor successfully.");
						i_like.setBackgroundResource(R.drawable.i_like);
						MyApplication.getInstance().removeLikePost(postId);
			        } else {
			            Log.e("LeanCloud", "delete like favor failed.");
			        }
			    }
			});
	}

	private void saveLikeOnServer() {
		AVObject favor = new AVObject("LIKE");
		favor.put("like", LIKE_IT);
		favor.put("user", AVUser.getCurrentUser());
		favor.put("postId", postId);
		favor.saveInBackground(new SaveCallback() {
		    public void done(AVException e) {
		        if (e == null) {
		            Log.i("LeanCloud", "Save like favor successfully.");
					i_like.setBackgroundResource(R.drawable.i_like_selected);
					MyApplication.getInstance().likeList.add(postId);
		        } else {		        	
		            Log.e("LeanCloud", "Save like favor failed. reason : " + e.getMessage());
		        }
		    }
		});
	}

	void init() {
		if (null == good_cell_one) {
			good_cell_one = (LinearLayout) findViewById(R.id.good_item_one);
		}

		if (null == mainImage) {
			mainImage = (ImageView) good_cell_one.findViewById(R.id.gooditem_photo);
		}

		if (null == i_reblog) {
			i_reblog = (TextView) good_cell_one.findViewById(R.id.i_reblog);
		}

		if (null == description) {
			description = (TextView) good_cell_one.findViewById(R.id.good_desc);
		}

		if (null == i_like) {
			i_like = (ImageButton) good_cell_one.findViewById(R.id.i_like);
			i_like.setOnClickListener(likeListener);
		}

		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}

	public void bindData(PushContentEntity pce) {
		init();

		if (null != pce && null != pce.getImg_url()) {
			imageLoader.displayImage(pce.getImg_url(), mainImage,
					MyApplication.options);
			postId = pce.getRecord_id();
			updateLikeUi();
		}

	}

	private void updateLikeUi() {
		if (MyApplication.getInstance().likeList.contains(postId)) {
			i_like.setBackgroundResource(R.drawable.i_like_selected);
		} else {			
			i_like.setBackgroundResource(R.drawable.i_like);
		}
	}
}
