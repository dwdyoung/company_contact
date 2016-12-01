package com.monday.companycontact.ui.adapter;

import java.io.File;
import java.util.List;

import com.monday.companycontact.R;
import com.monday.companycontact.utils.FileUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FileAdapter extends BaseAdapter{

	Context context;
	
	List<File> fileList;
	
	public FileAdapter(Context context, List<File> fileList) {
		super();
		this.context = context;
		this.fileList = fileList;
	}
	
	@Override
	public int getCount() {
		return fileList.size();
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
			view = LayoutInflater.from(context).inflate(R.layout.file_item, null);
			holder = new Holder();
			holder.actionBarView = view.findViewById(R.id.provider_item_actionbar);
			holder.fileIcon = (ImageView)view.findViewById(R.id.file_item_icon);
			holder.fileName = (TextView)view.findViewById(R.id.file_item_name);
			view.setTag(holder);
		} else {
			holder = (Holder)view.getTag();
		}
		
		if(position == 0){
			holder.actionBarView.setVisibility(View.VISIBLE);
		}else {
			holder.actionBarView.setVisibility(View.GONE);
		}
		
		File file = fileList.get(position);
		holder.fileName.setText(file.getName());
		if(file.isDirectory()){
			holder.fileIcon.setImageResource(R.drawable.folder);
		} else {
			String suffix = FileUtils.getFileSuffix(file);
			if(suffix.equals("xls")  || suffix.equals("xlsx")){
				holder.fileIcon.setImageResource(R.drawable.excel);
			} else {
				holder.fileIcon.setImageResource(R.drawable.other_file);
			}
		}
		
		return view;
	}
	
	static class Holder{
		View actionBarView;
		TextView fileName;
		ImageView fileIcon;
	}

}
