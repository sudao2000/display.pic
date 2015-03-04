package cn.eoe.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import cn.eoe.app.R;
import cn.eoe.app.component.ProductItemCell;
import cn.eoe.app.entity.PushContentEntity;

public class PushContentAdapter extends BaseAdapterEx<PushContentEntity> {

	private final List<PushContentEntity> mList;
	private final Context mContext;

	public PushContentAdapter(Context context, List<PushContentEntity> list,
			int drawableId) {

		super(context, list, drawableId);
		mContext = context;
		mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = parent.inflate(mContext, R.layout.b1_product_cell,
					null);
		}

		PushContentEntity pce = mList.get(position);
		ProductItemCell cell = (ProductItemCell) convertView;
		cell.bindData(pce);
		return convertView;
	}

	@Override
	public int getCount() {
		if (mList != null)
			return mList.size();
		return 0;
	}
}
