package com.monday.companycontact.db;

import java.util.Iterator;
import java.util.List;

public class MyIterable<T> implements Iterable<T>{

	List<T> providerList;
	
	public MyIterable(List<T> providerList) {
		super();
		this.providerList = providerList;
	}

	@Override
	public Iterator<T> iterator() {
		return providerList.iterator();
	}

}
