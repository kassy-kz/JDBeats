package com.androidtsubu.jdbeats.util;

import com.androidtsubu.jdbeats.R;

import android.content.Context;
import android.database.Cursor;

public class JDBeatsHTMLFactory {

	/** 残念女王の数値 */
	private static final double DEF_ZANNEN_VALUE = 291;
	/** ちゃんりなの数値 */
	private static final double DEF_JDBOSS_VALUE = 394;

	/** Google Chart Tools(HEADER前半) */
	private static final String HTML_HEAD_FIRST =
			"<html>" +
			"<head>" +
			"<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>" +
			"<script type=\"text/javascript\">" +
			"google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});" +
			"google.setOnLoadCallback(drawChart);" +
			"function drawChart() {" +
			"var data = google.visualization.arrayToDataTable([";

	/** Google Chart Tools(HEADER後半) */
	private static final String HTML_HEAD_LAST =
			"]);" +
			"var options = {" +
			"title: 'Udezumo Power Performance(UPP)'," +
			"vAxis: {" +
			"title: 'UPP'," +
			"minValue: '0'" +
			"}," +
			"legend: {" +
			"position: 'top'" +
			"}," +
			"hAxis: {" +
			"textPosition: 'none'" +
			"}" +
			"};" +
			"var chart = new google.visualization.LineChart(document.getElementById('chart_div'));" +
			"chart.draw(data, options);" +
			"}" +
			"</script>" +
			"</head>";

	/** Google Chart Tools(BODY) */
	private static final String HTML_BODY =
			"<body>" +
			"<div id=\"chart_div\" style=\"width: 100%; height: 50%;\">" +
			"</div>" +
			"</body>" +
			"</html>";

	/**
	 * Google Chart Toolsでグラフを描画するためのHTMLを生成する
	 * @param context アプリケーション・コンテキスト
	 * @param cursor DBカーソル(登録されている測定値)
	 * @return HTML
	 */
	public static String createHTML(Context context, Cursor cursor) {
		// 残念女王のUPP数値を設定する
		String zannenValue = context.getResources().getString(R.string.zannen_value);
		zannenValue = Validator.validate(zannenValue, DEF_ZANNEN_VALUE);
		// ちゃんりなのUPP数値を設定する
		String jdbossValue = context.getResources().getString(R.string.jdboss_value);
		jdbossValue = Validator.validate(jdbossValue, DEF_JDBOSS_VALUE);

		// HTMLヘッダ部(前半)
		StringBuilder sb = new StringBuilder(HTML_HEAD_FIRST);
		
		// グラフ数値作成
		sb.append("['', 'You', 'Zannen', 'Super JD'],");

		// 検索結果の先頭に移動できるか？
		int index = 1;
		if (cursor.moveToFirst() == false) {
			// 移動できない(0件)
			sb.append("['0', 0, " + zannenValue + ", " + jdbossValue + "],");
			sb.append("['1', 0, " + zannenValue + ", " + jdbossValue + "],");
		} else {
			if (cursor.getCount() == 1) {
				// 1件のみの場合は、最初の測定値を0にしておく
				sb.append("['0', 0, " + zannenValue + ", " + jdbossValue + "],");
			}
			do {
				String value = cursor.getString(2);	// 測定値(valule1)の値
				value = Validator.validate(value, 0.0);
				sb.append("['" + String.valueOf(index) + "', " + value + ", " + zannenValue + ", " + jdbossValue + "],");
				index++;
			} while (cursor.moveToNext());
		}
		
		// HTMLヘッダ部(後半)
		sb.append(HTML_HEAD_LAST);
		
		// HTMLボディ部
		sb.append(HTML_BODY);

		// HTMLを生成して、その文字列を返す
		return sb.toString();
	}
}
