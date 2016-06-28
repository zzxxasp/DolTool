package com.key.doltool.activity.battle;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;

public class BattleMainActivity extends BaseActivity{
	private TextView level;

	@Override
	public int getContentViewId() {
		return R.layout.battle_main;
	}


	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		findView();
	}

	private void findView(){
		level=(TextView)findViewById(R.id.level);
		level.setTypeface(Typeface.createFromAsset(getAssets(),
                "wiki_html/fonts/shift_bold.ttf"));
	}
}
