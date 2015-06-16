package com.key.doltool.activity.battle;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;

public class BattleMainActivity extends BaseActivity{
	private TextView level;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle_main);
		findView();
	}
	private void findView(){
		level=(TextView)findViewById(R.id.level);
		level.setTypeface(Typeface.createFromAsset(getAssets(),
                "wiki_html/fonts/shift_bold.ttf"));
	}
}
