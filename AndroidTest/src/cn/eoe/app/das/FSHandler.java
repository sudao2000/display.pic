package cn.eoe.app.das;

import java.util.List;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVObject;

public abstract class FSHandler implements Handler.Callback {
	private final static String TAG = "FSHandler";

	/* message id defined for send message */
	private final static int SUCCESS_MSG = 1;
	private final static int FAILED_MSG = 2;

	/* handler for activity thread to process */
	private Handler handler = null;

	/* for store some object need */
	protected Object obj = null;

	public FSHandler() {
		if (Looper.myLooper() != null) {
			this.handler = new Handler(this);
		}
	}

	public FSHandler(Object obj) {
		if (Looper.myLooper() != null) {
			this.handler = new Handler(this);
		}

		this.obj = obj;
	}

	public FSHandler(Object obj, Handler handler) {
		this.handler = handler;
		this.obj = obj;
	}

	/**
	 * overwrite this method for handle succeed response
	 * 
	 * @param sresp
	 *            : success response message
	 */
	public abstract void onSuccess(SuccessResp sresp);

	public abstract void onSuccess(List<AVObject> list);

	/**
	 * overwrite this method for handle failed response, which response is not
	 * 200 OK or failed to access the server
	 * 
	 * @param eresp
	 *            : error response message
	 */
	public abstract void onFailed(ErrorResp eresp);

	public abstract void onFailed(List<AVObject> list);

	public void processSuccess(SuccessResp sresp) {
		if (this.handler != null) {
			/* send message to the caller through handler */
			Message msg = this.handler.obtainMessage(SUCCESS_MSG, sresp);
			this.handler.sendMessage(msg);
		} else {
			this.onSuccess(sresp);
		}
	}

	public void processError(ErrorResp eresp) {
		if (this.handler != null) {
			/* send failed message when exception */
			Message msg = this.handler.obtainMessage(FAILED_MSG, eresp);
			this.handler.sendMessage(msg);
		} else {
			this.onFailed(eresp);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		try {
			switch (msg.what) {
			case SUCCESS_MSG:
				this.onSuccess((SuccessResp) msg.obj);
				break;
			case FAILED_MSG:
				this.onFailed((ErrorResp) msg.obj);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return true;
	}

	/**
	 * success response content
	 */
	public static class SuccessResp {
		public enum Type {
			CACHE, SERVER
		};

		private final Type type;
		private final boolean expired; // only used when type is CACHE
		private String url = null;
		private long timeUsed = -1;
		private Object entity = null;

		public SuccessResp(Type type, boolean expired, String url,
				long timeUsed, Object entity) {
			this.type = type;
			this.expired = expired;
			this.url = url;
			this.timeUsed = timeUsed;
			this.entity = entity;
		}

		public Type getType() {
			return this.type;
		}

		public boolean isExpired() {
			return this.expired;
		}

		public String getUrl() {
			return this.url;
		}

		public long getTimeUsed() {
			return this.timeUsed;
		}

		public Object getEntity() {
			return this.entity;
		}

		public String toDebugString() {
			return "" + type + " " + url + " " + timeUsed + " " + entity;
		}
	}

	/**
	 * error response entity
	 */
	public static class ErrorResp {
		private final String url;
		private final int errCode;
		private final int httpCode;
		private final String errMsg;

		public ErrorResp(String url, int errCode, int httpCode, String errMsg) {
			this.url = url;
			this.errCode = errCode;
			this.httpCode = httpCode;
			this.errMsg = errMsg;
		}

		public String getUrl() {
			return this.url;
		}

		public int getErrCode() {
			return errCode;
		}

		public String getErrMsg() {
			return errMsg;
		}

		public int getHttpCode() {
			return httpCode;
		}
	}

}
