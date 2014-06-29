
package com.androidtsubu.jdbeats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidtsubu.jdbeats.db.JDBeatsDBHelper;
import com.androidtsubu.jdbeats.db.JDBeatsEntity;
import com.androidtsubu.jdbeats.event.OnDrawFailureListener;
import com.androidtsubu.jdbeats.event.OnDrawSuccessListener;

/**
 * Twitter投稿用Activity
 * 
 * @author miguse
 */
public class MainActivity extends Activity {
    private Twitter mTwitter;

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
            final ChartFragment2 fragment2 = new ChartFragment2();
            // グラフ描画が正常終了した時にonDrawSuccess()が呼び出される
            fragment2.setOnDrawSuccessListener(new OnDrawSuccessListener() {
                @Override
                public void onDrawSuccess() {
                    // 描画が正常終了したので、ビットマップが取得できる
                    // 描画が正常終了していない間(描画中や失敗時)はビットマップは取得できない(nullが返る)
                    Bitmap bitmap = fragment2.getGraph();

                    if (bitmap != null) {
                        // Tweet設定
                        String sTweet = getTweetMessage();
                        showToast(sTweet);
                        tweet(bitmap,sTweet);
                    }
                }
            });

            // グラフ描画が失敗した時にonDrawFailure()が呼び出される
            fragment2.setOnDrawFailureListener(new OnDrawFailureListener() {
                @Override
                public void onDrawFailure() {
                    // グラフ描画が失敗した場合の処理
                }
            });
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment2, "ChartFragment2").commit();
        }
    }

    protected void onResume() {
        super.onResume();

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

    private void tweet(final Bitmap bmp, final String sTweet) {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    StatusUpdate status = new StatusUpdate(sTweet);

                    if (bmp == null) {
                        return false;
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    InputStream inputStream = new ByteArrayInputStream(
                            bos.toByteArray());

                    status.media(sTweet, inputStream);
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
        task.execute(sTweet);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private CharSequence getResourcesText(int r_id) {
        return getResources().getText(r_id);
    }

    private String getTweetMessage() {
        String sTweet;
        
        /* DBから最新の計測値を取得 */
        JDBeatsDBHelper helper = new JDBeatsDBHelper(
                MainActivity.this);
        Cursor cursor = helper
                .query("SELECT jdbeats._id, jdbeats._value1 FROM jdbeats;",
                        null);

        if (cursor.moveToFirst() == true) {
            List<JDBeatsEntity> lstEntity = new ArrayList<JDBeatsEntity>();
            do {
                JDBeatsEntity entity = new JDBeatsEntity();
                entity.setId(cursor.getInt(0));
                entity.setValue1(cursor.getString(1));
                lstEntity.add(entity);
            } while (cursor.moveToNext());

            Calendar clen = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm");
            String strGetTime = sdf.format(clen.getTime());

            sTweet = strGetTime
                    + getResourcesText(R.string.header_miguse)
                    + getResourcesText(R.string.name_miguse)
                    + lstEntity.get(lstEntity.size() - 1)
                            .getValue1()
                    + getResourcesText(R.string.unit_awp)
                    + getResourcesText(R.string.header_jdboss)
                    + getResourcesText(R.string.name_jdboss)
                    + getResourcesText(R.string.jdboss_value)
                    + getResourcesText(R.string.unit_awp)
                    + getResourcesText(R.string.header_zannen)
                    + getResourcesText(R.string.name_zannen)
                    + getResourcesText(R.string.zannen_value)
                    + getResourcesText(R.string.unit_awp)
                    + getResourcesText(R.string.hash_tag);
        } else {
            sTweet = "";
        }
        return sTweet;
    }
}
