package cn.eoe.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import cn.eoe.app.fragment.PushContentFragment;

public class ProductCatagoryAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = ProductCatagoryAdapter.class.getSimpleName();

	public ProductCatagoryAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return PushContentFragment.newInstance(TAG);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TAG;
	}

	@Override
	public int getCount() {
		return 1;
	}
}
