package com.ahxiaodian.linquanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ahxiaodian.linquanapp.fragment.CommonFragment;
import com.ahxiaodian.linquanapp.fragment.HomeFragment;

public class CommonFragmentActivity extends FragmentActivity {
	String TAG = CommonFragmentActivity.class.getSimpleName();
	RadioGroup mRadioGroup;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.common_activity);
		mRadioGroup = (RadioGroup) findViewById(R.id.home_bottom_tab_body);
		if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Intent intent = getIntent();
			int index = intent.getIntExtra("index", 0);
			String url = intent.getStringExtra("url");
			ft.add(R.id.body, HomeFragment.newInstance(url, index), TAG);
			ft.commit();
			switch (index) {
			case 1:
				mRadioGroup.check(R.id.home_bottom_tab_index_1);
				break;
			case 2:
				mRadioGroup.check(R.id.home_bottom_tab_index_2);
				break;
			case 3:
				mRadioGroup.check(R.id.home_bottom_tab_index_3);
				break;
			case 4:
				mRadioGroup.check(R.id.home_bottom_tab_index_4);
				break;
			case 5:
				mRadioGroup.check(R.id.home_bottom_tab_index_5);
				break;
			default:
				break;
			}
		}
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int index = 0;
				switch (checkedId) {
				case R.id.home_bottom_tab_index_1:
					index = 1;
					break;
				case R.id.home_bottom_tab_index_2:
					index = 2;
					break;
				case R.id.home_bottom_tab_index_3:
					index = 3;
					break;
				case R.id.home_bottom_tab_index_4:
					index = 4;
					break;
				case R.id.home_bottom_tab_index_5:
					index = 5;
					break;
				default:
					break;
				}
				Intent intent = new Intent();
				intent.setClass(CommonFragmentActivity.this, HomeActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		});
	}
}
