package com.androidtsubu.jdbeats;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.androidtsubu.jdbeats.db.JDBeatsDBHelper;
import com.androidtsubu.jdbeats.db.JDBeatsDBManager;
import com.androidtsubu.jdbeats.db.JDBeatsEntity;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
	
	/** ImageViewのWidth/Height */
	private int mWidth;
	private int mHeight;
	
	/** Loaderコールバック定義 */
	private LoaderManager.LoaderCallbacks<Drawable> mCallbacks = new LoaderManager.LoaderCallbacks<Drawable>() {

		@Override
		public Loader<Drawable> onCreateLoader(int id, Bundle args) {
			return new ChartLoader(getActivity(), args.getInt("width"), args.getInt("height"));
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

		mWidth = mImageView.getWidth();
		mHeight = mImageView.getHeight();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// ローダを開始する
		LoaderManager manager = getLoaderManager();
		Bundle bundle = new Bundle();
		bundle.putInt("width", mWidth);
		bundle.putInt("height", mHeight);
		manager.initLoader(0, bundle, mCallbacks).forceLoad();
	}

	/**
	 * Google chart APIで作成したグラフをバックグラウンドで取得する
	 * @author TAN
	 *
	 */
	public static class ChartLoader extends AsyncTaskLoader<Drawable> {
		
		private Context context;
		private int width;
		private int height;

		/**
		 * コンストラクタ
		 * @param context
		 */
		public ChartLoader(Context context, int width, int height) {
			super(context);
			this.context = context;
			this.width = width;
			this.height = height;
		}

		private Drawable downloadGraph(List<JDBeatsEntity> lstEntity) {
			Drawable drawable = null;
			try {
				String uri = createURI(lstEntity);
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
		 * グラフ描画に必要なデータをDBから取得する
		 * @return
		 */
		private List<JDBeatsEntity> queryDB() {
			String[] columns = {JDBeatsDBManager.Columns.KEY_ID, JDBeatsDBManager.Columns.KEY_VALUE1};
			final String orderby = JDBeatsDBManager.Columns.KEY_ID + " DESC";
			final String limit = "30";

			JDBeatsDBHelper helper = new JDBeatsDBHelper(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			
			Cursor cursor = db.query(
					JDBeatsDBManager.DATABASE_TABLE,
					columns,
					null,
					null,
					null,
					null,
					orderby,
					limit);
			if (cursor == null || cursor.moveToFirst() == false) {
				return null;
			}

			List<JDBeatsEntity> lstEntity = new ArrayList<JDBeatsEntity>();
			do {
				JDBeatsEntity entity = new JDBeatsEntity();
				entity.setId(cursor.getInt(0));
				entity.setDateTime(cursor.getLong(1));
				entity.setValue1(cursor.getString(2));
				lstEntity.add(entity);
			}while(cursor.moveToNext());
			
			return lstEntity;
		}
		
		/**
		 * Google chart APIでグラフを作成し、バックグラウンドで読み込む
		 * @return 作成されたグラフ。取得に失敗した場合はnull
		 */
		@Override
		public Drawable loadInBackground() {
			List<JDBeatsEntity> lstEntity = queryDB();
			return downloadGraph(lstEntity);
		}

		/**
		 * グラフ組み立て
		 * @param lstEntity DBに登録されたデータ
		 * @return Google chart URL
		 * @throws UnsupportedEncodingException
		 */
		private String createURI(List<JDBeatsEntity> lstEntity) throws UnsupportedEncodingException {
			StringBuilder sb = new StringBuilder(
					"http://chart.apis.google.com/chart?cht=lc&chdlp=b"
					+ "&chtt=" + URLEncoder.encode("測定データ", "UTF-8")
//					+ "&chs=" + width + "x" + height
					+ "&chs=450x200"
					+ "&chd=t:");

			if (lstEntity != null) {
				int lstCount = 1;
				for(JDBeatsEntity entity : lstEntity) {
					try {
						Double value = Double.valueOf(entity.getValue1());
						sb.append(value);
					} catch(Exception e) {
						//数値ではないデータは無効として"_"を登録する
						sb.append("_");
					}
					if (lstCount != lstEntity.size()) {
						sb.append(",");
					}
					lstCount++;
				}
			} else {
				sb.append("_");
			}

			return sb.toString();
		}
	}
}
