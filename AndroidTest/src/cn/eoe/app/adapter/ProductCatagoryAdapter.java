package cn.eoe.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import cn.eoe.app.fragment.PushContentFragment;

public class ProductCatagoryAdapter extends FragmentStatePagerAdapter {
	private static final String[] CONTENT1 = new String[] { "按价格低", "按价格高" };

	public ProductCatagoryAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return PushContentFragment.newInstance(CONTENT1[position
				% CONTENT1.length]);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CONTENT1[position % CONTENT1.length].toUpperCase();
	}

	@Override
	public int getCount() {
		return CONTENT1.length;
	}
}
