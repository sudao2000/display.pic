/*************************************************************************************
 * Module Name:com.funshion.fwidget.widget
 * File Name:FSErrorView.java
 * Description:
 * Author: 苏文盛
 * Copyright 2007-, Funshion Online Technologies Ltd.
 * All Rights Reserved
 * 版权 2007-，北京风行在线技术有限公司
 * 所有版权保护
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of Funshion Online Technologies Ltd.;
 * the contents of this file may not be disclosed to third parties, copied or
 * duplicated in any form, in whole or in part, without the prior written
 * permission of Funshion Online Technologies Ltd.
 * 这是北京风行在线技术有限公司未公开的私有源代码。本文件及相关内容未经风行在线技术有
 * 限公司事先书面同意，不允许向任何第三方透露，泄密部分或全部; 也不允许任何形式的私自备份。
 ***************************************************************************************/
package cn.eoe.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author suws
 * @Description:错误提示控件 控件的自定义属性如下： errortext_size:the text size of the
 *                     error_text retrytext_size:the text size of the retry
 *                     description retrybutton_size:The text size of the retry
 *                     no_net_src:no net image resource no_data_src: no data
 *                     image resource
 */
public class FSErrorView extends LinearLayout {

	// 没有网络时显示的图片
	private ImageView mNoNetImage;

	// 数据请求失败时显示的图片
	private ImageView mNoDataImage;

	// 错误提示中显示的错误信息
	private TextView mErrorText;

	// 重试按钮的监听回调
	private OnRetryClick mOnRetryClick;

	// 错误类型：没有网络
	public static final int NO_NET = 0;

	// 错误类型：数据请求失败
	public static final int NO_DATA = 1;

	public FSErrorView(Context context) {
		super(context);
		initView();
	}

	public FSErrorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		setAttrs(context, attrs);
	}

	/**
	 * 
	 * @Title: setAttrs
	 * @Description: 设置控件的自定义属性
	 * @param:@param context
	 * @param:@param attrs
	 * @return: void
	 * @throws
	 */
	private void setAttrs(Context context, AttributeSet attrs) {
	}

	private void initView() {
	}

	/**
	 * 
	 * @Title: setOnRetryClick
	 * @Description: 设置重试按钮的回调，使用该控件需回调该函数
	 * @param:@param l
	 * @return: void
	 * @throws
	 */
	public void setOnRetryClick(OnRetryClick l) {
		mOnRetryClick = l;
	}

	/**
	 * 
	 * @Title: reloadErrorView
	 * @param:@param errortype
	 * @return: void
	 * @throws
	 */
	private void loadErrorView(int errortype) {
	}

	/**
	 * 
	 * @Title: show
	 * @Description: 显示控件
	 * @param:@param errortype
	 * @return: void
	 * @throws
	 */
	public void show(int errortype) {
		this.setVisibility(View.VISIBLE);
		loadErrorView(errortype);
	}

	/**
	 * 
	 * @Title: hide
	 * @Description: 隐藏控件
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void hide() {
		this.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @author suws
	 * @Description: 重试按钮回调接口
	 */
	public interface OnRetryClick {
		public void retry(FSErrorView view);
	}
}
