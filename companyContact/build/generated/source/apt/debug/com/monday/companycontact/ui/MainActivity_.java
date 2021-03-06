//
// DO NOT EDIT THIS FILE.
// Generated using AndroidAnnotations 4.0.0.
// 
// You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.monday.companycontact.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.monday.companycontact.MyPrefs_;
import com.monday.companycontact.R;
import com.monday.companycontact.controller.ExtraInfoController_;
import com.monday.companycontact.controller.GroupController_;
import com.monday.companycontact.controller.ProviderController_;
import com.woozzu.android.widget.IndexableListView;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.builder.PostActivityStarter;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class MainActivity_
    extends MainActivity
    implements HasViews, OnViewChangedListener
{
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(R.layout.activity_main);
    }

    private void init_(Bundle savedInstanceState) {
        this.myPrefs = new MyPrefs_(this);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.providerController = ProviderController_.getInstance_(this);
        this.extraInfoController = ExtraInfoController_.getInstance_(this);
        this.groupController = GroupController_.getInstance_(this);
        start();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static MainActivity_.IntentBuilder_ intent(Context context) {
        return new MainActivity_.IntentBuilder_(context);
    }

    public static MainActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new MainActivity_.IntentBuilder_(fragment);
    }

    public static MainActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new MainActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        this.listView = ((IndexableListView) hasViews.findViewById(R.id.main_listview));
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId_ = item.getItemId();
        if (itemId_ == R.id.action_search) {
            onSearch();
            return true;
        }
        if (itemId_ == R.id.action_file) {
            onFile();
            return true;
        }
        if (itemId_ == R.id.action_http) {
            onHttp();
            return true;
        }
        if (itemId_ == R.id.action_introduce) {
            onIntroduce();
            return true;
        }
        if (itemId_ == R.id.action_import_contract) {
            onImportToContact();
            return true;
        }
        if (itemId_ == R.id.action_single) {
            onAddSingle();
            return true;
        }
        if (itemId_ == R.id.action_delete_all) {
            onDeletaAll();
            return true;
        }
        if (itemId_ == R.id.action_checkupdate) {
            onCheckUpdate();
            return true;
        }
        if (itemId_ == R.id.action_feedback) {
            onFeedback();
            return true;
        }
        if (itemId_ == R.id.action_share) {
            onShare();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<MainActivity_.IntentBuilder_>
    {
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, MainActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), MainActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), MainActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public PostActivityStarter startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                        fragment_.startActivityForResult(intent, requestCode, lastOptions);
                    } else {
                        fragment_.startActivityForResult(intent, requestCode);
                    }
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                    } else {
                        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                            context.startActivity(intent, lastOptions);
                        } else {
                            context.startActivity(intent);
                        }
                    }
                }
            }
            return new PostActivityStarter(context);
        }
    }
}
