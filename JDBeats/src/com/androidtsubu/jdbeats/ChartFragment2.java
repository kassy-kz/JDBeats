package com.androidtsubu.jdbeats;

import com.androidtsubu.jdbeats.db.JDBeatsDBHelper;
import com.androidtsubu.jdbeats.event.OnDrawFailureListener;
import com.androidtsubu.jdbeats.event.OnDrawSuccessListener;
import com.androidtsubu.jdbeats.util.JDBeatsHTMLFactory;
import com.androidtsubu.jdbeats.util.Validator;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ChartFragment2 extends Fragment {

	/** グラフ表示用WebView　*/
	private WebView mWebView;
	/** 表示待ちアニメーション */
	private ProgressBar mProgressBar;
	/** チャート描画フラグ */
	private boolean mIsChartDraw;
	/** 表描画フラグ */
	private boolean mIsTableDraw;
	
	private Bitmap mBitmap;

	/** 描画成功通知 */
	private OnDrawSuccessListener mSuccessListener;
	
	/** 描画失敗通知 */
	private OnDrawFailureListener mFailureListener;

	/** 表示待ちアニメーションを停止するためのハンドラ */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 1:
				mIsChartDraw = true;
				break;
			case 2:
				mIsTableDraw = true;
				break;

			case -1:	// 失敗
				mIsChartDraw = false;
				mIsTableDraw = false;
				if (mFailureListener != null) {
					mFailureListener.onDrawFailure();
				}
			}

			// フラグと表の描画が完了したら、プログレス表示を停止し、描画成功を通知する
			if (mIsChartDraw && mIsTableDraw) {
				mProgressBar.setVisibility(View.GONE);
				mProgressBar.setEnabled(false);
				createBitmapImage();
			} else {
				mProgressBar.setVisibility(View.VISIBLE);
			}
		}
	};

	
	/**
	 * グラフ描画終了を通知するイベントハンドラ
	 * @param listener リスナ
	 */
	public void setOnDrawSuccessListener(OnDrawSuccessListener listener) {
		mSuccessListener = listener;
	}
	
	/**
	 * グラフ描画失敗を通知するイベントハンドラ
	 * @param listener リスナ
	 */
	public void setOnDrawFailureListener(OnDrawFailureListener listener) {
		mFailureListener = listener;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chart2, container, false);
		mWebView = (WebView) view.findViewById(R.id.webView1);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new JavaScriptInterface(), "AndroidEvent");
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		draw();
	}

	/**
	 * グラフを描画する
	 */
	public void draw() {
		LoaderManager manager = getLoaderManager();
		manager.initLoader(0, null, mCallback).forceLoad();
	}

	/**
	 * WebViewに描画されたグラフをキャプチャし、その画像を返す
	 * @return キャプチャ画像(グラフ)
	 */
	public Bitmap getGraph() {
		return mBitmap;
	}
	
	private void createBitmapImage() {
		// WebViewが生成されていない場合は画像取得不可
		if (mWebView == null) {
			return;
		}

		// 読み込み中の場合は画像取得をしない
		if (mIsChartDraw == false || mIsTableDraw == false) {
			return;
		}

		// WebViewの画像を取得する
		mWebView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mWebView.setDrawingCacheEnabled(true);
				mWebView.buildDrawingCache();
				mBitmap = Bitmap.createBitmap(
						mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(mBitmap);
				mWebView.draw(canvas);
				mWebView.setDrawingCacheEnabled(false);
				if (mSuccessListener != null) {
					mSuccessListener.onDrawSuccess();
				}
			}
		}, 1000);
	}
	
	private LoaderManager.LoaderCallbacks<String> mCallback = new LoaderManager.LoaderCallbacks<String>() {
		
		@Override
		public Loader<String> onCreateLoader(int id, Bundle args) {
			mIsChartDraw = false;
			mIsTableDraw = false;

			mProgressBar.setVisibility(View.VISIBLE);
			mProgressBar.setEnabled(true);

			return new ChartLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<String> loader, String data) {
			if (mWebView != null) {
				mWebView.loadData(data, "text/html; charset=utf-8", "utf-8");
			}
		}

		@Override
		public void onLoaderReset(Loader<String> loader) {
			mIsChartDraw = false;
			mIsChartDraw = false;

			mProgressBar.setVisibility(View.VISIBLE);
			mProgressBar.setEnabled(true);
			mWebView.loadData("", "text/html", "utf-8");
		}
	};
	
	public static class ChartLoader extends AsyncTaskLoader<String> {

		/** データベース取得数 */
		private static final int DEF_LIMIT_VALUE = 10;

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
					+ "WHERE jdbeats._id <= tmp._id GROUP BY jdbeats._id, jdbeats.datetime, jdbeats._value1 "
					+ "HAVING COUNT(*) <= " + limit + " ORDER BY COUNT(*) DESC;"
					, null);
			if (cursor == null) {
				return "";
			}

			JDBeatsHTMLFactory factory = new JDBeatsHTMLFactory(mContext);
			return factory.createHTML(cursor);
		}
	}
	
	public class JavaScriptInterface {
		@JavascriptInterface
		public synchronized void readyEvent(int index) {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(index);
			}
		}
		
		@JavascriptInterface
		public synchronized void errorEvent() {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(-1);
			}
		}
	}
}
