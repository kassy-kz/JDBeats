package com.androidtsubu.jdbeats;

import com.androidtsubu.jdbeats.db.JDBeatsDBHelper;
import com.androidtsubu.jdbeats.util.JDBeatsHTMLFactory;
import com.androidtsubu.jdbeats.util.Validator;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class ChartFragment2 extends Fragment {

	private WebView mWebView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chart2, container, false);
		mWebView = (WebView) view.findViewById(R.id.webView1);
		mWebView.getSettings().setJavaScriptEnabled(true);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		LoaderManager manager = getLoaderManager();
		manager.initLoader(0, null, mCallback).forceLoad();
	}

	/**
	 * WebViewに描画されたグラフをキャプチャし、その画像を返す
	 * @return キャプチャ画像(グラフ)
	 */
	public Bitmap getGraph() {
		if (mWebView == null) {
			return null;
		}
		
		Bitmap bitmap = Bitmap.createBitmap(
				mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		mWebView.draw(canvas);
		
		return bitmap;
	}
	
	private LoaderManager.LoaderCallbacks<String> mCallback = new LoaderManager.LoaderCallbacks<String>() {
		
		@Override
		public Loader<String> onCreateLoader(int id, Bundle args) {
			return new ChartLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<String> loader, String data) {
			if (mWebView != null) {
				mWebView.loadData(data, "text/html", "utf-8");
			}
		}

		@Override
		public void onLoaderReset(Loader<String> loader) {
			mWebView.loadData("", "text/html", "utf-8");
		}
	};
	
	public static class ChartLoader extends AsyncTaskLoader<String> {

		/** データベース取得数 */
		private static final int DEF_LIMIT_VALUE = 15;

		/** アプリケーションコンテキスト */
		private Context mContext;

		/**
		 * コンストラクタ
		 * @param context アプリケーション・コンテキスト
		 */
		public ChartLoader(Context context) {
			super(context);
			mContext = context;
		}

		/**
		 * 測定データをDBから取得する
		 */
		@Override
		public String loadInBackground() {
			JDBeatsDBHelper helper = new JDBeatsDBHelper(mContext);

			String limit = mContext.getResources().getString(R.string.db_limit);
			limit = Validator.validate(limit, DEF_LIMIT_VALUE);

			Cursor cursor = helper.query(
					"SELECT DISTINCT jdbeats._id, jdbeats.datetime, jdbeats._value1 FROM jdbeats, jdbeats tmp "
					+ "WHERE jdbeats._id >= tmp._id GROUP BY jdbeats._id, jdbeats.datetime, jdbeats._value1 "
					+ "HAVING COUNT(*) <= " + limit + " ORDER BY COUNT(*);"
					, null);
			if (cursor == null) {
				return "";
			}

			return JDBeatsHTMLFactory.createHTML(mContext, cursor);
		}
	}
}
