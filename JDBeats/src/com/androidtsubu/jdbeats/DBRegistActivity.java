package com.androidtsubu.jdbeats;

import java.util.Date;

import com.androidtsubu.jdbeats.db.JDBeatsDBHelper;
import com.androidtsubu.jdbeats.db.JDBeatsDBManager;
import com.androidtsubu.jdbeats.db.JDBeatsEntity;
import com.androidtsubu.jdbeats.event.OnFinishListener;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class DBRegistActivity extends Activity {

	/** 測定データ1(Key) */
	public static final String VALUE1 = JDBeatsDBManager.Columns.KEY_VALUE1;
	/** 測定データ2(Key) */
	public static final String VALUE2 = JDBeatsDBManager.Columns.KEY_VALUE2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		JDBeatsEntity entity = new JDBeatsEntity();
		boolean result = true;
		do {

			// 呼び出し元からパラメータを取得する
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				result = false;
				break;
			}

			// パラメータ1の値を取得する
			// 取得に失敗した場合は結果をfalseに設定
			String value = extras.getString(VALUE1);
			if (value == null) {
				result = false;
				break;
			}
			entity.setValue1(value);
			
			// パラメータ2の値を取得する
			// 任意のため、取得結果は反映させない
			value = extras.getString(VALUE2);
			entity.setValue2(value);

		} while( false );

		// データの取得に失敗した場合は、CANCELを呼び出し元に通知する
		if (result == false) {
			setResult(RESULT_CANCELED);
			finish();
		}
		
		// 取得した測定値データをDBに登録する
		entity.setDateTime(new Date().getTime());
		DBRegisterTask task = new DBRegisterTask();
		task.setContext(getApplicationContext());
		task.setOnFinishListener(new OnFinishListener() {
			//DBへの登録処理が完了したらここに飛ぶ */
			public void onFinish(int result) {
				setResult(result);	// RESULT_OK: 登録成功、RESULT_CANCELED: 登録失敗
				// ツイート送信画面に遷移
				Intent intent = new Intent(DBRegistActivity.this, MainActivity.class);
                startActivity(intent);
				finish();
			}
		});
		task.execute(entity, null, null);
	}

	/**
	 * DBにデータを登録する
	 * @author TAN
	 *
	 */
	private class DBRegisterTask extends AsyncTask<JDBeatsEntity, Void, Void> {

		private Context context;
		private JDBeatsDBHelper helper;
		private OnFinishListener onFinishListener;
		private int result;
		
		public void setContext(Context context) {
			this.context = context;
		}
		
		@Override
		protected Void doInBackground(JDBeatsEntity... params) {
			result = RESULT_OK;
			if (context == null || params.length < 1 || params[0] == null) {
				result = RESULT_CANCELED;
				return null;
			}
			JDBeatsEntity entity = params[0];
			helper = new JDBeatsDBHelper(context);
			ContentValues values = new ContentValues();
			values.put(JDBeatsDBManager.Columns.KEY_DATETIME, entity.getDateTime());
			values.put(JDBeatsDBManager.Columns.KEY_VALUE1, entity.getValue1());
			if (entity.getValue2() != null) {
				values.put(JDBeatsDBManager.Columns.KEY_VALUE2, entity.getValue2());
			}
			helper.begin();
			try {
				helper.insert(null, values);
				helper.commit();
			} catch(Exception e) {
				result = RESULT_CANCELED;
				helper.rollback();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void value) {
			super.onPostExecute(value);
			if (onFinishListener != null) {
				onFinishListener.onFinish(result);
			}
		}

		/**
		 * データ登録が終了したことをActivityに通知するリスナ
		 * @param onFinishListener
		 */
		public void setOnFinishListener(OnFinishListener onFinishListener) {
			this.onFinishListener = onFinishListener;
		}
	}
}
