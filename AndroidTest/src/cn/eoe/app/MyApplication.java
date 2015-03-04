package cn.eoe.app;

import java.io.File;

import android.graphics.Bitmap;
import cn.eoe.app.config.Configs;

import com.avos.avoscloud.AVOSCloud;
import com.external.activeandroid.app.Application;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MyApplication extends Application {
	public static File cacheDir;

	public static DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	public static DisplayImageOptions options_head; // DisplayImageOptions是用于设置图片显示的类

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// if (!CommonUtil.sdCardIsAvailable()) { // sdcard not available
		// cacheDir = new File(Environment.getDataDirectory().getAbsolutePath()
		// + "/data/" + getPackageName()
		// + "/eoecn/cache/imgs");
		// } else {
		// cacheDir = new File(Constants.CachePath.IMAGE_CACHE_PATH);
		// }
		//

		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(this)
		// .memoryCacheExtraOptions(480, 800) // default = device screen
		// dimensions
		// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)
		// .threadPoolSize(3) // default
		// .threadPriority(Thread.NORM_PRIORITY - 1) // default
		// .denyCacheImageMultipleSizesInMemory()
		// .offOutOfMemoryHandling()
		// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) //
		// default
		// .discCache(new UnlimitedDiscCache(cacheDir)) // default
		// .discCacheSize(50 * 1024 * 1024)
		// .discCacheFileCount(100)
		// .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) //
		// default
		// .imageDownloader(new URLConnectionImageDownloader()) // default
		// .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		// .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) //
		// default
		// .enableLogging()
		// .build();
		// ImageLoader.getInstance().init(config);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_image) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_image) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_image) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		options_head = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile_no_avarta_icon) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.profile_no_avarta_icon) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.profile_no_avarta_icon) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(30)) // 设置成圆角图片
				.build();

		AVOSCloud.initialize(this, Configs.APP_ID, Configs.APP_KEY);

	}

}
