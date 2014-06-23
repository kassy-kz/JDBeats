package com.androidtsubu.jdbeats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidtsubu.jdbeats.db.JDBeatsDBHelper;

/**
 * Twitter投稿用Activity
 * 
 * @author miguse
 * 
 */
public class MainActivity extends Activity {
	private Twitter mTwitter;
	private String mTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}

		mTwitter = TwitterUtils.getTwitterInstance(this);

		if (savedInstanceState == null) {
			ChartFragment fragment = new ChartFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment, "ChartFragment").commit();
		}

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						/* DBから最新の計測値を取得 */
						JDBeatsDBHelper helper = new JDBeatsDBHelper(
								MainActivity.this);
						Cursor cursor = helper
								.query("SELECT MAX(jdbeats._id), jdbeats._value1, jdbeats._value2 FROM jdbeats;",
										null);

						cursor.moveToFirst();

						Calendar clen = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
				        String strGetTime = sdf.format(clen.getTime());
						
						mTweet = strGetTime + "時点のmiguse："
								+ cursor.getString(1) + "JDP #jdbeats";

						tweet();
					}
				});

	}

	public boolean onCreatOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	private void tweet() {
		AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				try {
					StatusUpdate status = new StatusUpdate(mTweet);

					ImageView imageView = (ImageView) findViewById(R.id.chart);
					if (imageView == null) {
						return false;
					}
					Bitmap bmp = ((BitmapDrawable) imageView.getDrawable())
							.getBitmap();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
					InputStream inputStream = new ByteArrayInputStream(
							bos.toByteArray());

					status.media(mTweet, inputStream);
					mTwitter.updateStatus(status);
					return true;
				} catch (TwitterException e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					showToast("ついーとＯＫ");
				} else {
					showToast("ついーとＮＧ");
				}
			}
		};
		task.execute(mTweet);
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
