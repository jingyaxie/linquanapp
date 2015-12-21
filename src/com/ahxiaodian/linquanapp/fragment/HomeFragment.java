package com.ahxiaodian.linquanapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ahxiaodian.linquanapp.CommonFragmentActivity;
import com.ahxiaodian.linquanapp.R;
import com.ahxiaodian.util.NetUtil;
import com.ahxiaodian.widget.LoadFailView;
import com.ahxiaodian.widget.LoadFailView.ReloadListener;
import com.ahxiaodian.widget.ProgressWebView;
import com.ahxiaodian.widget.ProgressWebView.WebChromeClient;

public class HomeFragment extends Fragment implements ReloadListener {
	public ProgressWebView mWebView;
	View v;
	String url;
	LoadFailView mLoadFailView;
	String APP_CACAHE_DIRNAME = "/linquan/";
	int index;
	private SwipeRefreshLayout swipeLayout;
	private String current_url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.webviewfragment, null);
		mLoadFailView = (LoadFailView) v.findViewById(R.id.no_data_layout);
		return v;
	}

	public static HomeFragment newInstance(String s, int index) {
		HomeFragment newFragment = new HomeFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", s);
		bundle.putInt("index", index);
		newFragment.setArguments(bundle);
		// bundle还可以在每个标签里传送数据
		return newFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mWebView = (ProgressWebView) v.findViewById(R.id.webview);
		mWebView.getSettings().setUserAgentString("lqshapp");
		Bundle args = getArguments();
		url = args != null ? args.getString("url") : "";
		index = args != null ? args.getInt("index") : 0;
		initWebView();
		mWebView.loadUrl(url);
		mLoadFailView.setOnReloadListener(this);
		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 重新刷新页面
				mWebView.loadUrl(current_url);
			}
		});
		swipeLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
				R.color.holo_orange_light, R.color.holo_red_light);
	}

	public void loadData(boolean isLoad) {
		if (mWebView != null && isLoad)
			mWebView.loadUrl(url);
	}

	private void initWebView() {
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("mqqwpa://im/chat?chat_type=wpa&uin")) {
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), "无法启动QQ", Toast.LENGTH_SHORT).show();
					}
				} else if ("url".contains("tel")) {
					try {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
						startActivity(intent);
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), "无法启动电话", Toast.LENGTH_SHORT).show();
					}
				}
				// else {
				// HitTestResult hit = view.getHitTestResult();
				// if (hit != null) {
				// Intent intent = new Intent();
				// intent.setClass(getActivity(), CommonFragmentActivity.class);
				// intent.putExtra("url", url);
				// intent.putExtra("index", index);
				// startActivity(intent);
				// return true;
				// } else {
				// return false;
				// }
				// }
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mWebView.getSettings().setBlockNetworkImage(true);
				if (!NetUtil.isNetworkAvailable(getActivity())) {
					mLoadFailView.setVisibility(View.VISIBLE);
				}
				current_url = url;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if (!NetUtil.isNetworkAvailable(getActivity()) || url.contains("data:text/html")) {
					mLoadFailView.setVisibility(View.VISIBLE);
				} else {
					mLoadFailView.setVisibility(View.GONE);
				}
				mWebView.getSettings().setBlockNetworkImage(false);
				// 隐藏进度条
				swipeLayout.setRefreshing(false);
				current_url = url;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description,
					String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				mLoadFailView.setVisibility(View.VISIBLE);
			}
		});
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
	}

	@Override
	public void onReloadListener() {
		// TODO Auto-generated method stub
		if (mWebView != null)
			mWebView.loadUrl(url);
	}
}
