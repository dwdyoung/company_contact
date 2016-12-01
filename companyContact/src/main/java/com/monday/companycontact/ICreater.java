package com.monday.companycontact;

import java.io.File;
import java.util.List;

public interface ICreater {

	public IProviderLoader createLoader(List<List<Object>> excelObjs, IInfoProvider infoProvider);
	
}
