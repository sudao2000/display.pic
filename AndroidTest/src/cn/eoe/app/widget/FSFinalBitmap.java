package cn.eoe.app.widget;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;

public class FSFinalBitmap {
	private static FinalBitmap instance = null;

	private static final int WIDGET_AFINAL_LOAD_THREAD_SIZE = 16;

	public static void init(Context context, String cacheDir) {
		instance = FinalBitmap.create(context);

		instance.configDiskCachePath(cacheDir);
		instance.configMemoryCacheSize(4 * 1024 * 1024);
		instance.configDiskCacheSize(16 * 1024 * 1024);
		instance.configBitmapLoadThreadSize(WIDGET_AFINAL_LOAD_THREAD_SIZE);
	}

	public static FinalBitmap getInstance() {
		return instance;
	}

	public static void clearCache() {
		if (instance == null)
			return;

		instance.clearCache();
	}

	public static void destroy() {
		if (instance == null)
			return;
		instance.closeCache();
	}
}
