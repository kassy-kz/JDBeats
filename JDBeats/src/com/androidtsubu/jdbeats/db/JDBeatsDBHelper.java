package com.androidtsubu.jdbeats.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

	/**
	 * 
	 * @param distinct
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return
	 */
	public Cursor query(boolean distinct, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return getWritableDatabase().query(
				distinct,
				JDBeatsDBManager.DATABASE_TABLE,
				columns,
				selection,
				selectionArgs,
				groupBy,
				having,
				orderBy,
				limit);
	}
	
	/**
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Cursor query(String sql, String[] selectionArgs) {
		return getWritableDatabase().rawQuery(sql, selectionArgs);
	}
	
	/**
	 * 
	 * @param nullColumnHack
	 * @param values
	 * @return
	 */
	public long insert(String nullColumnHack, ContentValues values) {
		return getWritableDatabase().insert(JDBeatsDBManager.DATABASE_TABLE, nullColumnHack, values);
	}
	
	/**
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int update(ContentValues values, String whereClause, String[] whereArgs) {
		return getWritableDatabase().update(JDBeatsDBManager.DATABASE_TABLE, values, whereClause, whereArgs);
	}
	
	/**
	 * 
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int delete(String whereClause, String[] whereArgs) {
		return getWritableDatabase().delete(JDBeatsDBManager.DATABASE_TABLE, whereClause, whereArgs);
	}
	
	public void begin() {
		getWritableDatabase().beginTransaction();
	}
	
	public void commit() {
		getWritableDatabase().setTransactionSuccessful();
		getWritableDatabase().endTransaction();
	}
	
	public void rollback() {
		getWritableDatabase().endTransaction();
	}
}
