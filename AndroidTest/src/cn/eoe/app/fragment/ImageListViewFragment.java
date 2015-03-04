package cn.eoe.app.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.eoe.app.R;
import cn.eoe.app.adapter.BaseAdapterEx;
import cn.eoe.app.adapter.BaseAdapterEx.IGetViewCallback;
import cn.eoe.app.adapter.PushContentAdapter;
import cn.eoe.app.das.FSHandler.SuccessResp;
import cn.eoe.app.entity.PushContentEntity;
import cn.eoe.app.widget.FSErrorView;
import cn.eoe.app.widget.FSFinalBitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ImageListViewFragment extends PushContentBaseFragment {

	private static final String TAG = "PushContentFragment";

	private volatile List<PushContentEntity> mPushContentList;
	private PullToRefreshListView mListView;

	private BaseAdapterEx<PushContentEntity> mNormalMediaAdapter;

	protected FinalBitmap mFinalBitmap = null;
	protected Bitmap mImageDefault = null;
	public int mScreenWidth;
	public int mScreenHeight;
	private static final int pageSize = 6;
	private int lastLoadPos = 0;
	private int headWidth;
	private int headHight;

	public static Fragment newInstance(String tag) {
		return new ImageListViewFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.recommend_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		reLoadData();
		Log.d(TAG, "onActivityCreated");
		mPushContentList = new ArrayList<PushContentEntity>(100);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			reLoadData();
		}
	}

	private void reLoadData() {
		Log.d(TAG, "reLoadData");
		if (mPushContentList == null || mPushContentList.size() == 0) {
			mCurrentPage = 1;
			onRequestData();
		}
	}

	private void initScreenParam(Activity context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;

		int spacing = (int) context.getResources().getDimension(
				R.dimen.fragment_channel_recommend_padding);
		headWidth = mScreenWidth - spacing * 2;
		headHight = (int) (headWidth * 0.56);
	}

	protected OnLastItemVisibleListener mVisibleListener = new OnLastItemVisibleListener() {

		@Override
		public void onLastItemVisible() {
			Log.d(TAG, "onLastItemVisible mCurrentPage=" + mCurrentPage
					+ "  mLastRequestPage=" + mLastRequestPage);

			if (mDown) {
				isQuietRequest = false;
				loadMore();
				Log.d(TAG, "onLastItemVisible draging up, loadMore data");
			} else {
				Log.d(TAG, "onLastItemVisible draging down");
			}
		}
	};

	private final OnRefreshListener<ListView> mOnRefreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			isQuietRequest = false;
			lastLoadPos = 0;
			reFresh();
		}

	};

	private final IGetViewCallback mIGetViewCallback = new IGetViewCallback() {

		@Override
		public void getPosition(int position) {
			int firsPos = mPushContentList.size() - pageSize / 2;
			int secondPos = mPushContentList.size() - pageSize / 3;
			if (position == firsPos && lastLoadPos < firsPos) {
				lastLoadPos = firsPos;
				isQuietRequest = true;
				// loadMore();
				loadData();
				Log.d(TAG, "mIGetViewCallback -------------");
			} else if (position == secondPos) {// 濡傛灉绗竴娆″姞杞藉け璐ワ紝鍒扮浜屼釜浣嶇疆鍐嶈瘯涓�娆★紝濡傛灉绗竴娆℃垚鍔燂紝涓嶄細婊¤冻绗簩涓潯浠�
				// loadMore();
			}
		}

	};

	private void updateGridViewData() {
		Log.d(TAG, "updateGridViewData");
		if (mNormalMediaAdapter == null) {
			mNormalMediaAdapter = new PushContentAdapter(mActivity,
					mPushContentList, R.drawable.translucent_background);
			mListView.setAdapter(mNormalMediaAdapter);
		} else {
			mNormalMediaAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDetach() {
		Log.d(TAG, "kurt onDetach");
		super.onDetach();
	}

	@Override
	public void onDestroy() {
		if (mNormalMediaAdapter != null) {
			mNormalMediaAdapter.releaseData();
			mNormalMediaAdapter = null;
		}

		if (mActivity != null) {
			Log.d(TAG, "kurt onDestory");
			mActivity = null;
		}
		super.onDestroy();
	}

	@Override
	protected boolean loadData() {
		try {
			if (mCurrentPage != 1) {
				if (mCurrentPage != mLastRequestPage) {
					mLastRequestPage = mCurrentPage;
					sendRequest();
					return true;
				} else {
					Log.d(TAG, "mCurrentPage=" + mCurrentPage
							+ "  mLastRequestPage=" + mLastRequestPage);
					return false;
				}
			} else {
				sendRequest();
				return true;
			}
		} catch (Exception e) {
			Log.e(TAG, "loadData", e);
		}
		return false;
	}

	private void sendRequest() {
		ProductListQueryAsync();
	}

	@Override
	protected void initView() {
		initScreenParam(mActivity);
		LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFinalBitmap = FSFinalBitmap.getInstance();
		// mImageDefault =
		// BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.channel_media_default);
		mImageDefault = decodeResource(mActivity, R.drawable.ic_launcher);
		View root = getView();
		mListView = (PullToRefreshListView) root
				.findViewById(R.id.recommend_listview);
		mLoadingContainer = (LinearLayout) root
				.findViewById(R.id.loading_container_recommend);
		mLoadMoreContainer = (LinearLayout) root
				.findViewById(R.id.recommend_load_more_container);
		mFSErrorView = (FSErrorView) root
				.findViewById(R.id.fs_error_view_recommend);
		// initHeadView(inflater);
	}

	private float startY = 0;
	private boolean mDown = true;
	private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				if (event.getY() > startY) {
					// 鍚戜笅婊戯紱
					mDown = true;
				} else if (event.getY() < startY) {
					// 鍚戜笂婊戯紱
					mDown = false;
				}
				break;
			case MotionEvent.ACTION_MOVE:

				break;
			}
			return false;
		}

	};

	@Override
	protected void setListener() {
		mListView.setOnRefreshListener(mOnRefreshListener);
		mListView.setOnLastItemVisibleListener(mVisibleListener);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mActivity == null) {
					return;
				}
				if (position < mPushContentList.size()) {

				}
			}
		});
	}

	@Override
	protected void onRequestSuccess(List<AVObject> list) {
		try {
			if (list == null || list.size() <= 0) {
				mLastRequestPage--;
				Toast.makeText(getActivity(), R.string.no_more_data,
						Toast.LENGTH_SHORT).show();
				return;
			}
			mCurrentPage++;
			updateGridViewData();
		} catch (Exception e) {
			setRefreshOrLoadMoreFlag();
		}
	}

	@Override
	protected void onRequestSuccess(SuccessResp sresp) {

	}

	@Override
	protected void setRefreshOrLoadMoreFlag() {
		mIsRefreshing = false;
		mListView.onRefreshComplete();
		mIsLoadingMore = false;
	}

	@Override
	public void onNetAvailable() {
		if (mActivity != null) {
			reLoadData();
		}
	}

	private void ProductListQueryAsync() {
		AVQuery<AVObject> query = new AVQuery<AVObject>("product");

		query.setSkip((mCurrentPage - 1) * pageSize);
		query.setLimit(pageSize);

		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> obj, AVException e) {
				if (e == null) {

					if (obj != null) {
						for (AVObject o : obj) {
							PushContentEntity pce = new PushContentEntity();
							pce.setTitle(o.getString("title"));
							pce.setImg_url(o.getString("img_url"));
							mPushContentList.add(pce);
						}

						mDasHandler.onSuccess(obj);
						setRefreshOrLoadMoreFlag();// 下拉或下拉没有返回时，不重复发送相同请求
					}
				} else {
					Log.e(TAG, "查询错误: " + e.getMessage());
					mDasHandler.onFailed(obj);
				}
			}
		});
	}

	private ArrayList<PushContentEntity> objectQueryImpl() throws Exception {
		AVQuery q2 = AVQuery.getQuery("product");
		List<AVObject> objects = q2.find();
		return null;
	}

	private class QueryTask extends
			AsyncTask<String, Void, ArrayList<PushContentEntity>> {

		volatile private String message = null;
		volatile private Exception exception = null;

		@Override
		protected ArrayList<PushContentEntity> doInBackground(String... params) {
			message = params[0];
			String type = params[1];
			try {
				return ImageListViewFragment.this.objectQueryImpl();
			} catch (Exception e) {
				exception = e;
				exception.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<PushContentEntity> products) {

		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private void ProductListQuery(final String string) throws AVException {
		QueryTask task = new QueryTask();
		task.execute("", string);
	}
}
