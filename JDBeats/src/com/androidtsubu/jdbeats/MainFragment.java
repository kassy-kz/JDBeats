package com.androidtsubu.jdbeats;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        Button tweetBtn = new Button(inflater.getContext());  
        tweetBtn.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                try {  
                    MainActivity activity = (MainActivity) getActivity();  
                    activity.onTweetClicked();
                } catch (ClassCastException e) {  
                    throw new ClassCastException("activityがonTweetBtnClickListenerを実装していません。");  
                }  
            }  
        });  
  
        return tweetBtn;  
    }  
}
