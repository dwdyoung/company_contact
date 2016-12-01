package com.monday.companycontact.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

import com.monday.companycontact.db.Provider;

public class ContactUtils {

	/**
	 * 此方法会造成重复添加
	 * @param context
	 * @param provider
	 */
	public static void insert(Context context, Provider provider) {  
		ContentValues values = new ContentValues();  

		/* 
		 * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获得系统返回的rawContactId 
		 */  
		Uri rawContactUri = context.getContentResolver().insert(RawContacts.CONTENT_URI, values);  
		long rawContactId = ContentUris.parseId(rawContactUri);  

		//往data表里写入姓名数据  
		if(provider != null){
			values.clear();  
			values.put(Data.RAW_CONTACT_ID, rawContactId);  
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE); //内容类型  
			values.put(StructuredName.GIVEN_NAME, provider.getProviderName());  
			context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);  
			
			values.clear();  
			values.put(Data.RAW_CONTACT_ID, rawContactId);  
			values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);  
			values.put(Phone.NUMBER, provider.getProviderPhone());  
			values.put(Phone.TYPE, Phone.TYPE_MOBILE);  
			context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);  
		}
	}  
	
	
	/**
	 * 此方法不会造成重复添加
	 * 如果号码相同将会被覆盖
	 * @param context
	 * @param providerList
	 */
	public static void insertProviders(Context context, List<Provider> providerList){
		ArrayList<ContentProviderOperation> ops =new ArrayList<ContentProviderOperation>();  
		
		int count = providerList.size();
		for(int i = 0; i < count; i++){
			
			Provider item = providerList.get(i);
			
			int rawContactInsertIndex = ops.size();  
	        ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)  
	                  .withValue(RawContacts.ACCOUNT_TYPE, null)  
	                  .withValue(RawContacts.ACCOUNT_NAME, null)  
	                  .build());  
	  
	         ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)  
	                  .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)  
	                  .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)  
	                  .withValue(StructuredName.DISPLAY_NAME, item.getProviderName())  
	                  .build());  
	           
	         ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)  
	                  .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)  
	                  .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)  
	                  .withValue(Phone.NUMBER, item.getProviderPhone())  
	                  .withValue(Phone.TYPE, Phone.TYPE_MOBILE)  
	                  .withValue(Phone.LABEL, "手机号")  
	                  .build());  
	           
//	         ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)  
//	                  .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)  
//	                  .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)  
//	                  .withValue(Email.DATA, "liangchen@itcast.cn")  
//	                  .withValue(Email.TYPE, Email.TYPE_WORK)  
//	                  .build());  
		}
        
         ContentProviderResult[] results;  
        try {  
            results = context.getContentResolver()  
                        .applyBatch(ContactsContract.AUTHORITY, ops);  
             for(ContentProviderResult result : results) {  
                 Log.i("TEST", result.uri.toString());  
             }  
        } catch (RemoteException e) {  
            e.printStackTrace();  
        } catch (OperationApplicationException e) {  
            e.printStackTrace();  
        }
	}
	
	
	
	/**
	 * 对供应商进行排列
	 * @param providerList
	 */
	public static void sort(List<Provider> providerList){
		Collections.sort(providerList, new Comparator<Provider>() {

			@Override
			public int compare(Provider lhs, Provider rhs) {
				if(lhs.getPinyin() == null || rhs.getPinyin() == null){
					return 0;
				}
				return lhs.getPinyin().compareTo(rhs.getPinyin());
			}
		});
	}
	
	
	
}
