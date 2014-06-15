package com.androidtsubu.jdbeats;

import android.os.Bundle;
import android.widget.TextView;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

public class ScouterActivity extends IOIOActivity {

    TextView mTxtInput;
    TextView mTxtValue;
    TextView mTxtMax;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scouter);
        mTxtInput = (TextView) findViewById(R.id.txtScouterInput);
        mTxtValue = (TextView) findViewById(R.id.txtScouterValue);
        mTxtMax = (TextView) findViewById(R.id.txtScouterMax);
    }
    
    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }
    
    /**
     * IOIOの処理を行うLooperクラス
     * @author kashimoto
     */
    class Looper extends BaseIOIOLooper {
        private AnalogInput mInput;

        @Override
        public void setup() throws ConnectionLostException {
            mInput = ioio_.openAnalogInput(40);
            enableUi(true);
        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
            //setNumber(input_.read());
            setNumber2f(mTxtInput, mInput.getVoltage());
            Thread.sleep(10);
        }

        @Override
        public void disconnected() {
            enableUi(false);
        }
    }

    /**
     * （ハードが接続外された際に）UIの操作可否を変える
     * @param enable true 操作可能, 　false 操作不可
     */
    private void enableUi(final boolean enable) {
        //TODO UI enable処理
    }
    
    /**
     * TextViewに数値の値をセットする（小数点以下２桁）
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

}
