package com.androidtsubu.jdbeats.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JDBeatsDBHelper extends SQLiteOpenHelper {

	public JDBeatsDBHelper(Context context) {
		super(context, JDBeatsDBManager.DATABASE_NAME, null, JDBeatsDBManager.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(JDBeatsDBManager.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO: 次DBアップグレード時に実装
	}

}
