package com.key.doltool.event;

import java.util.ArrayList;
import java.util.List;

import com.key.doltool.data.MenuItem;
public class MenuEvent {
	private String[] content={
			"发现物概览","回报地点","论战组合",
			"交易品","生产配方","船只概览",
			"百科","快速查询",
			"新闻讯息","广场中的人",
			"设置","帮助","开发者"
		};
		public String[] head={
			"发现","商业","博物","杂项","系统"	
		};
		private int[] parent_id={
			0,0,0,
			1,1,1,
			2,2,
			3,3,
			4,4,4,
		};
		public List<MenuItem> initMenuList(){
			List<MenuItem> list=new ArrayList<>();
			for(int i=0;i<content.length;i++){
				MenuItem item=new MenuItem();
				item.text=content[i];
				item.index=i;
				item.parent_id=parent_id[i];
				list.add(item);
			}
			return list;
		}
}
