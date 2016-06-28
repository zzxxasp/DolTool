package com.key.doltool.activity.ability;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.BaseActivity;
import com.key.doltool.adapter.SailBoatListAdapter;
import com.key.doltool.data.SailBoat;
import com.key.doltool.data.sqlite.Skill;
import com.key.doltool.util.BitMapUtil;
import com.key.doltool.util.FileManager;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.LinearLayoutForListView;
import com.the9tcat.hadi.DefaultDAO;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

/**
 * 船只技能详情显示界面
 * 展示特点：显示哪些船拥有此项船技能
 * **/
public class AbilityForBoatDetailActivity extends BaseActivity{
	@BindView(R.id.boat_array) LinearLayoutForListView boat_array;
	@BindView(R.id.pic) ImageView img;
	@BindView(R.id.name) TextView name;
	@BindView(R.id.details)	TextView details;
	@BindView(R.id.skill_need) TextView skill_need;

	@Override
	public int getContentViewId() {
		return R.layout.skill_details_boat;
	}

	@Override
	protected void initAllMembersView(Bundle savedInstanceState) {
		DefaultDAO dao = SRPUtil.getDAO(this);
		String id = getIntent().getStringExtra("id");
		Skill item = (Skill) dao.select(Skill.class, false, "id=?", new String[]{"" + id}, null, null, null, null).get(0);
		name.setText(item.getName());
		details.setText(item.getDetail());
		skill_need.setText(item.getNeed());
		try {
			img.setImageBitmap(BitMapUtil.getBitmapByInputStream(getAssets().open(FileManager.SKILL+ item.getPic_id()+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<SailBoat> sail= SRPUtil.getInstance(this).select(SailBoat.class,false,"ability like ?"
				,new String[]{"%"+ item.getName()+"%"},null,null,null,null);
		boat_array.setAdapter(new SailBoatListAdapter(sail, this));
	}
}
