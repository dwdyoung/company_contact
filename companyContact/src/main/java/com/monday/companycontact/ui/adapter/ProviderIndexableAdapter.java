package com.monday.companycontact.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.monday.companycontact.R;
import com.monday.companycontact.db.Provider;
import com.woozzu.android.util.StringMatcher;

public class ProviderIndexableAdapter extends BaseAdapter implements SectionIndexer{

	private static String SECTIONS = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	List<Provider> providerList;
	
	Context context;
	
	OnClickListener onClickListener;
	
	OnItemBtClickListener btClickListener;
	
	boolean isEditting;
	
	public ProviderIndexableAdapter(List<Provider> providerList, Context context) {
		super();
		this.providerList = providerList;
		this.context = context;
		
		onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(btClickListener != null){
					btClickListener.onClick((Integer)v.getTag(), v);
				}
			}
		};
		
		isEditting = false;
		
	}
	
	@Override
	public int getCount() {
		return providerList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		Holder holder = null;
		if(view == null){
			view = LayoutInflater.from(context).inflate(R.layout.provider_item, null);
			holder = new Holder();
			holder.actionBarView = view.findViewById(R.id.provider_item_actionbar);
			holder.providerIndex = (TextView)view.findViewById(R.id.provider_item_index);
			holder.providerName = (TextView)view.findViewById(R.id.provider_item_name);
			holder.providerPhone = (TextView)view.findViewById(R.id.provider_item_phone);
			holder.delete = view.findViewById(R.id.provider_item_delete);
			holder.delete.setOnClickListener(onClickListener);
			view.setTag(holder);
		} else {
			holder = (Holder)view.getTag();
		}
		
		if(position == 0){
			holder.actionBarView.setVisibility(View.VISIBLE);
		} else {
			holder.actionBarView.setVisibility(View.GONE);
		}
		
		Provider item = providerList.get(position);
		
		holder.providerName.setText(item.getProviderName() + 
				(item.getProviderNum() == null ? "" : ("(" + item.getProviderNum() + ")")));
		holder.providerPhone.setText(item.getProviderPhone());
		
		if(position == 0){
			holder.providerIndex.setVisibility(View.VISIBLE);
			String pinyin = item.getPinyin();
			if(!pinyin.isEmpty()){
				holder.providerIndex.setText(item.getPinyin().charAt(0) + "");
			}
			
		} else {
			Provider lastItem = providerList.get(position - 1); 
			String lastpinyin = lastItem.getPinyin();
			String pinyin = item.getPinyin();
			if(!pinyin.isEmpty() && !lastpinyin.isEmpty()){
				if(lastItem.getPinyin().charAt(0) == item.getPinyin().charAt(0)){
					holder.providerIndex.setVisibility(View.GONE);
				} else {
					holder.providerIndex.setVisibility(View.VISIBLE);
					holder.providerIndex.setText(item.getPinyin().charAt(0) + "");
				}
			}
		}
		
		if(isEditting){
			holder.delete.setVisibility(View.VISIBLE);
		} else {
			holder.delete.setVisibility(View.GONE);
		}
		
		return view;
	}
	
	
	public void setOnItemBtClickListener(OnItemBtClickListener listener){
		btClickListener = listener;
	}

	public static interface OnItemBtClickListener{
		public void onClick(int position, View view);
	}
	
	static class Holder{
		View actionBarView;
		TextView providerIndex;
		TextView providerName;
		TextView providerPhone;
		View delete;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[SECTIONS.length()];
		for (int i = 0; i < SECTIONS.length(); i++)
			sections[i] = String.valueOf(SECTIONS.charAt(i));
		return sections;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = sectionIndex; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(providerList.get(j).getPinyin().toUpperCase(), String.valueOf(k)))
							return j;
					}
				} else {
					String pingyin = providerList.get(j).getPinyin().toUpperCase();
					if(pingyin.isEmpty()){
						continue;
					}
					
					
					
					if (StringMatcher.match(String.valueOf(pingyin.charAt(0)), 
							String.valueOf(SECTIONS.charAt(i)))){
						return j;
					}
				}
			}
		}
		
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}
	
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}
