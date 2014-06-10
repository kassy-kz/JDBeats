package com.androidtsubu.jdbeats;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Google chart表示Fragment
 * @author TAN
 *
 */
public class ChartFragment extends Fragment {

	/** Google chart表示用ImageView */
	private ImageView mImageView;
	
	/** Loaderコールバック定義 */
	private LoaderManager.LoaderCallbacks<Drawable> mCallbacks = new LoaderManager.LoaderCallbacks<Drawable>() {

		@Override
		public Loader<Drawable> onCreateLoader(int id, Bundle args) {
			return new ChartLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<Drawable> loader, Drawable data) {
			if (data != null) {
				mImageView.setImageDrawable(data);
			}
		}

		@Override
		public void onLoaderReset(Loader<Drawable> loader) {
			mImageView.setImageDrawable(null);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chart, null, false);
		mImageView = (ImageView) view.findViewById(R.id.chart);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// ローダを開始する
		LoaderManager manager = getLoaderManager();
		manager.initLoader(0, null, mCallbacks).forceLoad();
	}

	/**
	 * Google chart APIで作成したグラフをバックグラウンドで取得する
	 * @author TAN
	 *
	 */
	public static class ChartLoader extends AsyncTaskLoader<Drawable> {
		
		/**
		 * コンストラクタ
		 * @param context
		 */
		public ChartLoader(Context context) {
			super(context);
		}

		/**
		 * Google chart APIでグラフを作成し、バックグラウンドで読み込む
		 * @return 作成されたグラフ。取得に失敗した場合はnull
		 */
		@Override
		public Drawable loadInBackground() {
			Drawable drawable = null;
			try {
				String uri = createURI();
				InputStream is = (InputStream) new URL(uri).getContent();
				drawable = Drawable.createFromStream(is, "");
				is.close();
				return drawable;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * グラフ組み立て
		 * @return Google chart URL
		 * @throws UnsupportedEncodingException
		 */
		private String createURI() throws UnsupportedEncodingException {
			// TODO:デモ用URL。折れ線グラフ等に修正する
			return "http://chart.apis.google.com/chart?cht=p3&chdlp=b"
					+ "&chtt=" + URLEncoder.encode("サンプル", "UTF-8")
					+ "&chd=t:40,5,10,25,20,50"
					+ "&chdl=Cupcake|Donut|Eclair|Froyo|Gingerbread|Honeycomb"
					+ "&chs=450x200";
		}
	}
}
