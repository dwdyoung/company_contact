package com.monday.companycontact.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.monday.companycontact.R;
import com.monday.companycontact.db.Provider;
import com.woozzu.android.util.StringMatcher;

public class ProviderCheckableAdapter extends BaseAdapter implements SectionIndexer{

	private static String SECTIONS = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	List<Provider> providerList;
	
	Map<Integer, Boolean> checkMap;
	
	Context context;
	
	public ProviderCheckableAdapter(List<Provider> providerList, Context context) {
		super();
		this.providerList = providerList;
		this.context = context;
		
		checkMap = new HashMap<Integer, Boolean>();
	}
	
	public void setChecked(int position, boolean checked){
		checkMap.put(position, checked);
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
			view = LayoutInflater.from(context).inflate(R.layout.provider_checkable_item, null);
			holder = new Holder();
			holder.providerIndex = (TextView)view.findViewById(R.id.provider_item_index);
			holder.providerName = (TextView)view.findViewById(R.id.provider_item_name);
			holder.providerPhone = (TextView)view.findViewById(R.id.provider_item_phone);
			holder.providerCheckbox = (CheckBox)view.findViewById(R.id.provider_item_checkbox);
			view.setTag(holder);
		} else {
			holder = (Holder)view.getTag();
		}
		
		Provider item = providerList.get(position);
		
		boolean check = getCheck(position);
		
		holder.providerName.setText(item.getProviderName() + 
				(item.getProviderNum() == null ? "" : ("(" + item.getProviderNum() + ")")));
		holder.providerPhone.setText(item.getProviderPhone());
		holder.providerCheckbox.setChecked(check);
		
		if(position == 0){
			holder.providerIndex.setVisibility(View.VISIBLE);
			holder.providerIndex.setText(item.getPinyin().charAt(0) + "");
		} else {
			Provider lastItem = providerList.get(position - 1); 
			if(lastItem.getPinyin().charAt(0) == item.getPinyin().charAt(0)){
				holder.providerIndex.setVisibility(View.GONE);
			} else {
				holder.providerIndex.setVisibility(View.VISIBLE);
				holder.providerIndex.setText(item.getPinyin().charAt(0) + "");
			}
		}
		return view;
	}
	
	public boolean getCheck(int position){
		Boolean check = checkMap.get(position);
		if(check == null){
			return false;
		}
		return check;
	}
	
	static class Holder{
		TextView providerIndex;
		TextView providerName;
		TextView providerPhone;
		CheckBox providerCheckbox;
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
		// TODO Auto-generated method stub
		return 0;
	}
}
