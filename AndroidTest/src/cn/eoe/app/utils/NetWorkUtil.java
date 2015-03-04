package cn.eoe.app.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

public class NetWorkUtil {

    public final static int TYPE_WIFI = 1;

    public final static int TYPE_MOBILE = 2;

    public final static int TYPE_ETHERNET = 3;

    public final static int TYPE_ERROR = -1;

    private static final int TIME_OUT = 10000;

	public static boolean isNetworkAvailable(Context context) {
		boolean isAvailable = false;
		if(null != context){
			NetworkInfo info = getNetworkInfo(context);
			if (info != null && info.isAvailable()) {
				isAvailable = true;
			}
		}
		return isAvailable;
	}

    public static NetworkInfo getNetworkInfo(Context context){
    	try{
    		final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    		if (null != connectivityManager){
    			return connectivityManager.getActiveNetworkInfo();
    		}
    	}catch(Exception e){

    	}
    	return null;
    }

	public static int reportNetType(Context context) {
		int netMode = TYPE_ERROR;
		try {
			NetworkInfo info = getNetworkInfo(context);
			if (info != null && info.isAvailable()) {
				int netType = info.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					netMode = TYPE_WIFI;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					netMode = TYPE_MOBILE;
				} else if (netType == ConnectivityManager.TYPE_ETHERNET){
					netMode = TYPE_ETHERNET;
				}
			}
		} catch (Exception e) {
			android.util.Log.e("system", "message: " + e.getMessage() + ", cause: " + e.getCause());
		}

		return netMode;
	}

	public static String NetTypeName(Context context) {
		String netTypeName = "";
		try {
			NetworkInfo info = getNetworkInfo(context);
			if (info != null && info.isAvailable()) {
				netTypeName = info.getTypeName();
			}
		} catch (Exception e) {
			android.util.Log.e("system", "message: " + e.getMessage() + ", cause: " + e.getCause());
		}
		return netTypeName;
	}


	 //获取当前的平均网速
	public static long getAverageRateSpeed(long startBytes, long startTime){
		long averageRateSpeed = 0;;
		try {
			long currentRateBytes = TrafficStats.getTotalRxBytes() - startBytes;
			double time = (double)System.currentTimeMillis() - (double)startTime;
			averageRateSpeed = (int)((double)currentRateBytes * 1000 / time);
		}catch(NoClassDefFoundError e){
			android.util.Log.e("system", "message: " + e.getMessage() + ", cause: " + e.getCause());
			return -1;
		}catch (Exception e) {
			android.util.Log.e("system", "message: " + e.getMessage() + ", cause: " + e.getCause());
			return -1;
		}
		return averageRateSpeed;
	}

	public static String HttpGet(String url) {
		String ret = "";

		HttpClient httpclient = new DefaultHttpClient();
        // 创建一个GET请求
        HttpGet request = new HttpGet(url);
        // 发送GET请求，并将响应内容转换成字符串

        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
        try {
        	ret = httpclient.execute(request, new BasicResponseHandler());

		} catch (Exception e) {
			android.util.Log.e("system", "message: " + e.getMessage() + ", cause: " + e.getCause());
		}
		return ret;
	}
}
