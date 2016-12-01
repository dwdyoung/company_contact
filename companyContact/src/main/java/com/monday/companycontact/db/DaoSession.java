package com.monday.companycontact.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.monday.companycontact.db.Provider;
import com.monday.companycontact.db.ContractGroup;
import com.monday.companycontact.db.ExtraNum;

import com.monday.companycontact.db.ProviderDao;
import com.monday.companycontact.db.ContractGroupDao;
import com.monday.companycontact.db.ExtraNumDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig providerDaoConfig;
    private final DaoConfig contractGroupDaoConfig;
    private final DaoConfig extraNumDaoConfig;

    private final ProviderDao providerDao;
    private final ContractGroupDao contractGroupDao;
    private final ExtraNumDao extraNumDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        providerDaoConfig = daoConfigMap.get(ProviderDao.class).clone();
        providerDaoConfig.initIdentityScope(type);

        contractGroupDaoConfig = daoConfigMap.get(ContractGroupDao.class).clone();
        contractGroupDaoConfig.initIdentityScope(type);

        extraNumDaoConfig = daoConfigMap.get(ExtraNumDao.class).clone();
        extraNumDaoConfig.initIdentityScope(type);

        providerDao = new ProviderDao(providerDaoConfig, this);
        contractGroupDao = new ContractGroupDao(contractGroupDaoConfig, this);
        extraNumDao = new ExtraNumDao(extraNumDaoConfig, this);

        registerDao(Provider.class, providerDao);
        registerDao(ContractGroup.class, contractGroupDao);
        registerDao(ExtraNum.class, extraNumDao);
    }
    
    public void clear() {
        providerDaoConfig.getIdentityScope().clear();
        contractGroupDaoConfig.getIdentityScope().clear();
        extraNumDaoConfig.getIdentityScope().clear();
    }

    public ProviderDao getProviderDao() {
        return providerDao;
    }

    public ContractGroupDao getContractGroupDao() {
        return contractGroupDao;
    }

    public ExtraNumDao getExtraNumDao() {
        return extraNumDao;
    }

}