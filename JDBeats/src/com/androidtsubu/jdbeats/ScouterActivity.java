
package com.androidtsubu.jdbeats;

import com.androidtsubu.jdbeats.db.JDBeatsDBManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

public class ScouterActivity extends IOIOActivity implements OnClickListener {

    private static final String TAG = "ScouterActivity";
    TextView mTxtInput;
    TextView mTxtInputMin;
    TextView mTxtInputMax;
    TextView mTxtValue;
    TextView mTxtValueMin, mTxtValueMax;
    TextView mTxtInput2;
    TextView mTxtInput2Min;
    TextView mTxtInput2Max;
    TextView mTxtValue2;
    TextView mTxtValue2Min, mTxtValue2Max;
    TextView mTxtMax;

    static int sInitialValue;
    static int sMaxValue;

    static float sInputMax = -100.f, sInputMin = 100.f;
    static float sInput2Max = -100.f, sInput2Min = 100.f;
    static float sValueMax = -100.f, sValueMin = 100.f;
    static float sValue2Max = -100.f, sValue2Min = 100.f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scouter);
        mTxtInput = (TextView) findViewById(R.id.txtScouterInput);
        mTxtInputMax = (TextView) findViewById(R.id.txtScouterInputMax);
        mTxtInputMin = (TextView) findViewById(R.id.txtScouterInputMin);
        mTxtValue = (TextView) findViewById(R.id.txtScouterValue);
        mTxtInput2 = (TextView) findViewById(R.id.txtScouterInput2);
        mTxtInput2Max = (TextView) findViewById(R.id.txtScouterInput2Max);
        mTxtInput2Min = (TextView) findViewById(R.id.txtScouterInput2Min);
        mTxtValue2 = (TextView) findViewById(R.id.txtScouterValue2);
        mTxtValue2Max = (TextView) findViewById(R.id.txtScouterValue2Max);
        mTxtValue2Min = (TextView) findViewById(R.id.txtScouterValue2Min);
        mTxtValueMax = (TextView) findViewById(R.id.txtScouterValueMax);
        mTxtValueMin = (TextView) findViewById(R.id.txtScouterValueMin);
        mTxtMax = (TextView) findViewById(R.id.txtScouterMax);
        Button btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

    /**
     * IOIOの処理を行うLooperクラス
     * 
     * @author kashimoto
     */
    class Looper extends BaseIOIOLooper {
        private AnalogInput mInput;
        private AnalogInput mInput2;

        @Override
        public void setup() throws ConnectionLostException,
                InterruptedException {
            mInput = ioio_.openAnalogInput(40);
            mInput2 = ioio_.openAnalogInput(41);
            sMaxValue = 0;
            sInitialValue = getScouterValueFromVoltage(mInput.getVoltage());
            enableUi(true);
        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
            // setNumber(input_.read());
            displayScouterValue(mInput.getVoltage(), mInput2.getVoltage());
            Thread.sleep(10);
        }

        @Override
        public void disconnected() {
            enableUi(false);
        }
    }

    /**
     * （ハードが接続外された際に）UIの操作可否を変える
     * 
     * @param enable true 操作可能, 　false 操作不可
     */
    private void enableUi(final boolean enable) {
        // TODO UI enable処理
    }

    /**
     * スカウターの値を画面に表示する
     * 
     * @param voltage
     */
    private void displayScouterValue(final float voltage, final float voltage2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 素の値をまず表示
                String str = String.format("%.2f", voltage);
                mTxtInput.setText(str);
                String str2 = String.format("%.2f", voltage2);
                mTxtInput2.setText(str2);

                // 素の値の最大値最小値を表示
                if (sInputMax < voltage) {
                    sInputMax = voltage;
                    mTxtInputMax.setText(Float.toString(sInputMax));
                }
                if (sInput2Max < voltage2) {
                    sInput2Max = voltage2;
                    mTxtInput2Max.setText(Float.toString(sInput2Max));
                }
                if (sInputMin > voltage) {
                    sInputMin = voltage;
                    mTxtInputMin.setText(Float.toString(sInputMin));
                }
                if (sInput2Min > voltage2) {
                    sInput2Min = voltage2;
                    mTxtInput2Min.setText(Float.toString(sInput2Min));
                }

                // 戦闘力に変換した値を表示
                int scouterValue = getScouterValueFromVoltage(voltage);
                mTxtValue.setText(Integer.toString(scouterValue));
                int scouterValue2 = getScouterValueFromVoltage2(voltage2);
                mTxtValue2.setText(Integer.toString(scouterValue2));

                // 戦闘力の最大値最小値を表示
                if (sValueMax < scouterValue) {
                    sValueMax = scouterValue;
                    mTxtValueMax.setText(Float.toString(sValueMax));
                }
                if (sValueMin > scouterValue) {
                    sValueMin = scouterValue;
                    mTxtValueMin.setText(Float.toString(sValueMin));
                }
                if (sValue2Max < scouterValue2) {
                    sValue2Max = scouterValue2;
                    mTxtValue2Max.setText(Float.toString(sValue2Max));
                }
                if (sValue2Min > scouterValue2) {
                    sValue2Min = scouterValue2;
                    mTxtValue2Min.setText(Float.toString(sValue2Min));
                }

                int totalValue = scouterValue + scouterValue2;
                // 最大値を表示
                if (sMaxValue < totalValue) {
                    sMaxValue = totalValue;
                    mTxtMax.setText(Integer.toString(sMaxValue));
                }

                // 初期値に戻ったら終了処理走らせる
                // if(scouterValue == sInitialValue) {
                // finishMeasurement();
                // }
            }
        });
    }

    /**
     * 計測処理を終了する
     */
    protected void finishMeasurement() {
        // TODO 計測の最大値をIntentで投げる
        // 終了する。
        Toast.makeText(this, "finish", Toast.LENGTH_LONG).show();
    }

    /**
     * Voltageの値からScouterの値をとりだすメソッド
     * 
     * @param voltage
     */
    private int getScouterValueFromVoltage(float voltage) {
        // TODO ここはとりあえず仮実装な
        int value = (int) ((voltage - 1.3f) * 600 - 300);
        Log.i(TAG, voltage + " scouter value = " + value);
        return value;
    }

    private int getScouterValueFromVoltage2(float voltage) {
        // TODO ここはとりあえず仮実装な
        int value = (int) ((1.4f - voltage) * 600 - 300);
        Log.i(TAG, voltage + " scouter value = " + value);
        return value;
    }

    /**
     * TextViewに数値の値をセットする（小数点以下２桁）
     * 
     * @param textView
     * @param value
     */
    private void setNumber2f(final TextView textView, float value) {
        final String str = String.format("%.2f", value);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(str);
            }
        });
    }

    /**
     * TextViewに数値の値をセットする（小数点以下２桁）
     * 
     * @param textView
     * @param value
     */
    private void setNumberInt(final TextView textView, float value) {
        final int value2 = (int) (value * 100);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(Integer.toString(value2));
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnFinish:
                // 計測結果をDBに登録
                Intent intent1 = new Intent(this, DBRegistActivity.class);
                intent1.putExtra(JDBeatsDBManager.Columns.KEY_VALUE1,
                        String.valueOf(sMaxValue));
                startActivity(intent1);
                finish();
                break;

            default:
                break;
        }

    }

}
