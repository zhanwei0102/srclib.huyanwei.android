
package com.android.launcher3;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;

public class AppAdapter extends BaseAdapter {
	private List<ResolveInfo> mList;
	private Context mContext;
	public static final int APP_PAGE_SIZE = 16;
	private PackageManager pm;
	
	public AppAdapter(Context context, List<ResolveInfo> list, int page) {
		mContext = context;
		pm = context.getPackageManager();
		
		mList = new ArrayList<ResolveInfo>();
		int i = page * APP_PAGE_SIZE;
		int iEnd = i+APP_PAGE_SIZE;
		while ((i<list.size()) && (i<iEnd)) {
			mList.add(list.get(i));
			i++;
		}
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ResolveInfo appInfo = mList.get(position);		
		AppItem appItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.app_item, null);			
			appItem = new AppItem();
			appItem.mAppIcon = (ImageView)v.findViewById(R.id.ivAppIcon);
			appItem.mAppName = (TextView)v.findViewById(R.id.tvAppName);			
			v.setTag(appItem);
			convertView = v;
		} else {
			appItem = (AppItem)convertView.getTag();
		}
/*
		// set the icon
		appItem.mAppIcon.setImageDrawable(appInfo.loadIcon(pm));
		// set the app name
		appItem.mAppName.setText(appInfo.loadLabel(pm));		
*/

		Drawable bg_drawable = appInfo.loadIcon(pm);
		Drawable default_icon = mContext.getResources().getDrawable(R.drawable.default_icon);
			
		if(bg_drawable == null)
		{// set the icon
			appItem.mAppIcon.setImageDrawable(default_icon);
		}
		else
		{// set the icon
			appItem.mAppIcon.setImageDrawable(bg_drawable);
		}
		// set the app name
		appItem.mAppName.setText(appInfo.loadLabel(pm));

		return convertView;
	}

	class AppItem {
		ImageView mAppIcon;
		TextView mAppName;
	}
}
