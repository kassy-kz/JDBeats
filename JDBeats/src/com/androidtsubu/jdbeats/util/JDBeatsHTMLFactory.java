package com.androidtsubu.jdbeats.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.androidtsubu.jdbeats.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;

/**
 * グラフ描画のHTMLを生成する
 * @author TAN
 *
 */
public class JDBeatsHTMLFactory {

	/** アプリケーション・コンテキスト */
	private Context mContext;
	/** リソースオブジェクト */
	private Resources mResources;

	/** 残念女王の数値 */
	private static final double DEF_ZANNEN_VALUE = 291;
	/** ちゃんりなの数値 */
	private static final double DEF_JDBOSS_VALUE = 394;
	/** 日付フォーマット */
	private static final String DATETIME_FMT = "MM/dd";


	/**
	 * コンストラクタ
	 * @param context アプリケーション・コンテキスト
	 */
	public JDBeatsHTMLFactory(Context context) {
		mContext = context;
		mResources = context.getResources();
	}
	
	/**
	 * Google Chart Toolsでグラフを描画するためのHTMLを生成する
	 * @param cursor DBカーソル(登録されている測定値)
	 * @return HTML
	 */
	public String createHTML(Cursor cursor) {
		
		// HTMLヘッダ部(前半)
		StringBuilder sb = new StringBuilder(readAssetsFile("index_head_first.txt"));

		// データ生成
		sb.append(createDataArray(cursor));
		
		// グラフタイトル生成
		sb.append(createChartTitle());
		
		// HTMLヘッダ部(後半)
		sb.append(readAssetsFile("index_head_last.txt"));
		
		// HTMLボディ部
		sb.append(readAssetsFile("index_body.txt"));

		// HTMLを生成して、その文字列を返す
		return sb.toString();
	}

	/**
	 * 折れ線グラフのオプションを設定する
	 * @return オプション文字列
	 */
	private String createChartTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append("var chart_options = {\n");
		sb.append("title: '" + mResources.getString(R.string.chart_title) + "',\n");
		sb.append("vAxis: {\n");
		sb.append("title: '" + mResources.getString(R.string.chart_v_axis_legend)+ "',\n");
		sb.append("minValue: '0'\n");
		sb.append("},\n");
		sb.append("legend: {\n");
		sb.append("position: 'top'\n");
		sb.append("}\n");
		sb.append("};\n");
		
		return sb.toString();
	}
	
	/**
	 * グラフ・表を表示するためのデータ配列を生成する
	 * @param cursor データベースカーソルy
	 * @return データ配列
	 */
	private String createDataArray(Cursor cursor) {
		// 残念女王のUPP数値を設定する
		String zannenValue = mContext.getResources().getString(R.string.zannen_value);
		zannenValue = Validator.validate(zannenValue, DEF_ZANNEN_VALUE);
		// ちゃんりなのUPP数値を設定する
		String jdbossValue = mContext.getResources().getString(R.string.jdboss_value);
		jdbossValue = Validator.validate(jdbossValue, DEF_JDBOSS_VALUE);

		// 日付フォーマット
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FMT, Locale.JAPAN);

		// データ生成
		StringBuilder sb = new StringBuilder("var data = new google.visualization.arrayToDataTable([");
		//タイトル生成
		sb.append("['" + mResources.getString(R.string.chart_h_axis_legend_1) + "', '" +
				mResources.getString(R.string.chart_h_axis_legend_2) + "', '" +
				mResources.getString(R.string.chart_h_axis_legend_3) + "', '" +
				mResources.getString(R.string.chart_h_axis_legend_4) + "'],");

		// 検索結果の先頭に移動できるか？
		if (cursor.moveToFirst() == false) {
			// 移動できない(0件)
			sb.append("['-', 0, " + zannenValue + ", " + jdbossValue + "],");
			sb.append("['-', 0, " + zannenValue + ", " + jdbossValue + "],");
		} else {
			if (cursor.getCount() == 1) {
				// 1件のみの場合は、最初の測定値を0にしておく
				sb.append("['-', 0, " + zannenValue + ", " + jdbossValue + "],");
			}
			do {
				long datetime = cursor.getLong(1);	// 測定日
				String value = cursor.getString(2);	// 測定値(valule1)の値
				value = Validator.validate(value, 0.0);
				sb.append("['" + sdf.format(datetime) + "', " + value + ", " + zannenValue + ", " + jdbossValue + "],");
			} while (cursor.moveToNext());
		}
		sb.append("]);");
		
		sb.append(createDataFormatter(zannenValue, jdbossValue));

		return sb.toString();
	}
	
	/**
	 * 表のフォーマッタを定義する
	 * @param zannenValue
	 * @param jdbossValue
	 * @return
	 */
	private String createDataFormatter(String zannenValue, String jdbossValue) {
		StringBuilder sb = new StringBuilder("var tableFormatter = new google.visualization.ColorFormat();\n");
		sb.append("tableFormatter.addRange(0, " + zannenValue + ", 'black', '#FFFFFF');\n");
		sb.append("tableFormatter.addRange(" + zannenValue + ", " + jdbossValue + ", 'black', '#99CC99');\n");
		sb.append("tableFormatter.addRange(" + jdbossValue + ", null, 'white', '#FF0000');\n");
		return sb.toString();
	}

	/**
	 * assets/ ディレクトリに格納されているテキストファイルを読み込み、その内容を返す
	 * @param fileName 読み込むテキストファイル
	 * @return テキストファイルの内容
	 */
	private String readAssetsFile(String fileName) {
		AssetManager as = mContext.getResources().getAssets();
		InputStream is = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			try {
				is = as.open(fileName);
				br = new BufferedReader(new InputStreamReader(is));
				String str;
				while((str = br.readLine()) != null) {
					sb.append(str + "\n");
				}
			} finally {
				if (br != null) {
					br.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
