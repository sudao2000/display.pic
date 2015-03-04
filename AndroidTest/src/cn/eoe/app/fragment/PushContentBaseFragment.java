package cn.eoe.app.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.eoe.app.R;
import cn.eoe.app.das.FSHandler;
import cn.eoe.app.das.FSHandler.SuccessResp;
import cn.eoe.app.widget.FSErrorView;
import cn.eoe.app.widget.FSErrorView.OnRetryClick;

import com.avos.avoscloud.AVObject;

public abstract class PushContentBaseFragment extends Fragment {

	private static final String TAG = "PushContentBaseFragment";
	// protected static final int PAGE_SIZE = 20;
	protected int mCurrentPage = 1;
	protected int mLastRequestPage = 1;
	protected String mChannelId = null;
	protected String mChannelCode = null;

	protected Activity mActivity;

	private boolean isSuccess = false;

	/** 加载更多提示框 */
	// protected View mLoadingView = null;
	protected LinearLayout mLoadMoreContainer;
	protected LinearLayout mLoadingContainer;
	protected boolean mIsRefreshing = false;// 是否处在刷新状态中，默认flase
	protected boolean mIsLoadingMore = false;// 是否处在分页加载状态中，默认flase
	protected boolean isQuietRequest = false;

	protected enum STATE {
		LOADING, REFRESH, LOADMORE
	};// 加载的三种状态，正在加载、刷新、加载更多

	protected STATE mState = STATE.LOADING;
	protected FSErrorView mFSErrorView;

	protected abstract boolean loadData();

	protected abstract void initView();

	protected abstract void setListener();

	protected abstract void onRequestSuccess(SuccessResp sresp);

	protected abstract void onRequestSuccess(List<AVObject> list);

	protected abstract void setRefreshOrLoadMoreFlag();// 如果没有加载更多和下拉刷新，则不用做任何处理

	// protected Template mTemplate = Template.MEDIA;

	public abstract void onNetAvailable();// 网络恢复时自动刷数据

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getmArguments();
		initView();
		initCommontView();
		setListener();
		// onRequestData();
	}

	protected void onRequestData() {
		Log.d(TAG, "base onRequestData:");
		boolean send = loadData();
		if (send) {
			showLoading(true);
		}
	}

	private void initCommontView() {
		mFSErrorView.hide();
		mFSErrorView.setOnRetryClick(new OnRetryClick() {

			@Override
			public void retry(FSErrorView view) {
				view.hide();
				mState = STATE.LOADING;
				onRequestData();
			}
		});
	}

	/**
	 * 获取activity传递过来的参数
	 */
	public void getmArguments() {
		/*
		 * Bundle bundle = getArguments(); if (bundle != null) { mChannelId =
		 * bundle.getString(PushMessageActivity.CHANNEL_ID); mChannelCode =
		 * bundle.getString(PushMessageActivity.CHANNEL_CODE); String template =
		 * bundle.getString(PushMessageActivity.TEMPLATE); Log.d(TAG,
		 * "template=" + template); if (template != null) { mTemplate =
		 * Template.getTemplate(template); } }
		 */
	}

	private void showLoading(boolean show) {
		if (mState == STATE.LOADMORE) {
			showLoading(mLoadMoreContainer, show);
		} else if (mState == STATE.REFRESH) {
		} else {
			showLoading(mLoadingContainer, show);
		}
	}

	private void showLoading(ViewGroup container, boolean show) {
		if (container == null) {
			return;
		}
		if (container.getChildCount() == 0) {
			View loadingView = getMoreLayout();
			loadingView.setVisibility(View.VISIBLE);
			container.addView(loadingView);
		}
		container.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	private View getMoreLayout() {
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(R.layout.view_common_loadmore, null);
	}

	// 重新加载数据---刷新
	protected void reFresh() {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			mState = STATE.REFRESH;
			mCurrentPage = 1;// 下拉刷新，把页数恢复到第一页
			onRequestData();
		}
	}

	// 加载更多
	protected void loadMore() {
		if (!mIsLoadingMore) {// 正在请求，但是还没完成时，不允许再次请求
			mIsLoadingMore = true;
			mState = STATE.LOADMORE;
			onRequestData();
			Log.d(TAG, "base loadMore:");
		}
	}

	protected FSHandler mDasHandler = new FSHandler() {

		@Override
		public void onSuccess(List<AVObject> list) {
			try {
				isSuccess = true;
				mFSErrorView.hide();
				showLoading(false);
				setRefreshOrLoadMoreFlag();// 下拉或下拉没有返回时，不重复发送相同请求
				onRequestSuccess(list);
			} catch (Exception e) {
				setRefreshOrLoadMoreFlag();// 下拉或下拉没有返回时，不重复发送相同请求
			}

		}

		@Override
		public void onFailed(List<AVObject> list) {
			mLastRequestPage--;
			showLoading(false);
			setRefreshOrLoadMoreFlag();
			if (!isSuccess) {
				mFSErrorView.show(FSErrorView.NO_DATA);
			}
		}

		@Override
		public void onFailed(ErrorResp eresp) {

		}

		@Override
		public void onSuccess(SuccessResp sresp) {
		}

	};

	public static Bitmap decodeResource(Context context, int resId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(resId), null, opts);
	}

	@Override
	public void onDestroy() {
		if (mActivity != null) {
			mActivity = null;
		}
		super.onDestroy();

	}
}
