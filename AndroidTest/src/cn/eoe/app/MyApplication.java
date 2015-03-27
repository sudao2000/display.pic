package cn.eoe.app;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.util.Log;
import cn.eoe.app.config.Configs;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.external.activeandroid.app.Application;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MyApplication extends Application {
	
	private static MyApplication instance;
	public static File cacheDir;

	public static DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	public static DisplayImageOptions options_head; // DisplayImageOptions是用于设置图片显示的类

	@Override
	public void onCreate() {
		super.onCreate();

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_image) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_image) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_image) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		AVOSCloud.initialize(this, Configs.APP_ID, Configs.APP_KEY);
		
		getUserLike();
		instance = this;
	}
	
	public static MyApplication getInstance() {
		return instance;
	}
	
	public static Set<String> likeList = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>(100));
	public static void getUserLike() {
		
		likeList.clear();
		AVUser currentUser = AVUser.getCurrentUser();
		if (currentUser == null) {
			return;
		}

		AVQuery<AVObject> query = new AVQuery<AVObject>("LIKE");
		query.whereEqualTo("user", AVUser.getCurrentUser());
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
		    public void done(List<AVObject> avObjects, AVException e) {
		        if (e == null) {
		            Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
		            
		            if (avObjects != null) {
						for (AVObject o : avObjects) {
							likeList.add(o.getString("postId"));
						}
					}
		        } else {
		            Log.d("失败", "查询错误: " + e.getMessage());
		        }
		    }
		});
	}
	
	public static void removeLikePost(String postId) {
		if (likeList.contains(postId)) {
			likeList.remove(postId);
		}
	}
	
	public static void clearLocalUserLike() {
		likeList.clear();
	}

	
	@Override
	public void onTerminate() {
		super.onTerminate();
		instance = null;
	}
}
