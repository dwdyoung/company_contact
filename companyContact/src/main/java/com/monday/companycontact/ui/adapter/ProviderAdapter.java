package com.monday.companycontact.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monday.companycontact.R;
import com.monday.companycontact.db.Provider;
import com.monday.companycontact.ui.adapter.ProviderIndexableAdapter.OnItemBtClickListener;

public class ProviderAdapter extends BaseAdapter {

	List<Provider> providerList;
	
	Context context;
	
	OnClickListener onClickListener;
	
	OnItemBtClickListener btClickListener;
	
	public ProviderAdapter(List<Provider> providerList, Context context) {
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
			holder.providerIndex = (TextView)view.findViewById(R.id.provider_item_index);
			holder.providerName = (TextView)view.findViewById(R.id.provider_item_name);
			holder.providerPhone = (TextView)view.findViewById(R.id.provider_item_phone);
			
			holder.providerIndex.setVisibility(View.GONE);
			view.setTag(holder);
		} else {
			holder = (Holder)view.getTag();
		}
		
		Provider item = providerList.get(position);
		
		holder.providerName.setText(item.getProviderName() + 
				(item.getProviderNum() == null ? "" : ("(" + item.getProviderNum() + ")")));
		holder.providerPhone.setText(item.getProviderPhone());
		
		return view;
	}
	
	
	public void setOnItemBtClickListener(OnItemBtClickListener listener){
		btClickListener = listener;
	}

	static class Holder{
		TextView providerIndex;
		TextView providerName;
		TextView providerPhone;
	}

}
