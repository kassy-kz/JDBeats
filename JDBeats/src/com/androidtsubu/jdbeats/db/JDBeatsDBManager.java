package com.androidtsubu.jdbeats.db;

import android.provider.BaseColumns;

public class JDBeatsDBManager {

	/** データベースファイル名 */
	public static final String DATABASE_NAME = "jdbeat.db";

	/** データベースバージョン */
	public static final int DATABASE_VERSION = 1;

	/** データベーステーブル名 */
	public static final String DATABASE_TABLE = "jdbeats";

	/** テーブル作成 */
	public static final String CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + DATABASE_NAME +
			" (" + Columns.KEY_ID + " INTERGER PRIMARY KEY AUTOINCREMENT," +
			Columns.KEY_DATETIME + " LONG," +
			Columns.KEY_VALUE1 + " TEXT," +
			Columns.KEY_VALUE2 + " TEXT);";

	/**
	 * DB項目定義
	 * @author TAN
	 *
	 */
	public static final class Columns implements BaseColumns {
		/** ID(PK) */
		public static final String KEY_ID = "_id";
		/** 測定日時 */
		public static final String KEY_DATETIME = "datetime";
		/** 測定値 */
		public static final String KEY_VALUE1 = "_value1";
		/** 体重？ */
		public static final String KEY_VALUE2 = "_value2";
	}
}
