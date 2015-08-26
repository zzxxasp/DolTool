package com.key.doltool.activity.dockyard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.activity.core.BaseFragmentActivity;
import com.key.doltool.adapter.PartListAdapter;
import com.key.doltool.adapter.SailBoatListAdapter;
import com.key.doltool.anime.MyAnimations;
import com.key.doltool.data.Part;
import com.key.doltool.data.SailBoat;
import com.key.doltool.event.MakeEvent;
import com.key.doltool.util.DBUtil;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.ViewUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.SlidingTabLayout;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;
import com.key.doltool.viewpage.MyPagerAdapter;
import com.the9tcat.hadi.DefaultDAO;
/**
 * 造船厂主界面(更大的意义,可以作为其他页面暂时的参考，提高20%编写效率)
 * @author key
 * @version 0.7
 * @time 2013-1-10
 * @日志
 * 0.1-加入基本Activity的相关方法和操作<br>
 * 0.2-加入ViewPager负责进行多个页面的切换，并加入显示效果<br>
 * 0.3-加入有限ListView的展示以及搜索功能<br>
 * 0.4-加入边缘菜单栏及其辅助功能<br>
 * 0.5-加入船只配件页面列表及相关操作<br>
 * 0.6-详细页面80%,造船界面启动<br>
 * 0.7-详细页面80%,造船界面70%(剩余:强化完毕流程,配方,技能,材质)<br>
 * 0.8-留给造船(G6奖励选择)<br>
 * 0.9-other(细部调节)<br>
 * 1.0完成三页所有功能以及菜单功能和跳转(第一版基本功能完成)<br> 
 * 注意：今后要避免过于复杂的界面
 */
public class DockYardFragment extends BaseFragment implements OnScrollListener{
	//定义部分
	private LinearLayout layout_alert;

	//查询条件
	private String select_if="id>?";
    private String[] select_if_x={"0"};
    
	private String select_if2="id>?";
    private String[] select_if_x2={"0"};
    
    private String order="name desc";
	//ViewPager定义部分
	private ViewPager main_ViewPage;
	private MyPagerAdapter main_adapter;
	private View layout1,layout2,layout3;
	private List<View> main_list;
	private LayoutInflater mInflater;
	//船只列表页面
	private ListView listview;
	//船件列表页面
	private ListView listview2;
	//造船模拟界面
	private TextView info_txt;
	private LinearLayout show_layout;
	private RelativeLayout ability;
	private LinearLayout choose_part;
	private SailBoat baseboat;
	private TextView sp_s,sp_c,sp_eq,sp_eq2;
	//控件显示(区域二:Ability-能力)
	private TextView vo_s,vo_f,vo_tu,vo_de,vo_p;
	private TextView bt_h,bt_p,bt_a,bt_c,bt_s;
	
	private TextView vo_s_a,vo_f_a,vo_tu_a,vo_de_a,vo_p_a;
	private TextView bt_h_a,bt_a_a,bt_c_a,bt_s_a;
	
	private FlatButton plus_btn;
	private FlatButton btn;
	//数据temp变量
	private DefaultDAO dao;
	private List<SailBoat> list=new ArrayList<>();
	private List<Part> list2=new ArrayList<>();
	private SailBoatListAdapter adapter;
	private PartListAdapter adapter2;
	private int add=-20,add2=-20;
	private int twice=0;
	private List<Part> s_list=new ArrayList<>();
	private List<Part> c_list=new ArrayList<>();
	private List<Part> eq_list1=new ArrayList<>();
	private List<Part> eq_list2=new ArrayList<>();
	private HashMap<String,Integer> s_map=new HashMap<>();
	private HashMap<String,Integer> c_map=new HashMap<>();
	private HashMap<String,Integer> eq_map=new HashMap<>();
	private HashMap<String,Integer> eq2_map=new HashMap<>();
	private Part part_s,part_c,part_eq,part_eq2;
	private int vo_s_add=0,vo_f_add=0,vo_de_add=0,vo_tu_add=0,vo_p_add=0;
	private int bt_h_add=0,bt_a_add=0,bt_s_add=0,bt_c_add=0;
	private Thread mThread;	// 线程
	private boolean end_flag=true; //是否为最末标记
	private boolean end_flag2=true; //是否为最末标记
    private View main;
	private SlidingTabLayout mSlidingTabLayout;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		 View view =  inflater.inflate(R.layout.dockyard_main_area, container,false);
		 init(view);
		 findView();
		 setListener();
		 if(dao!=null&&list.size()==0){
			 new Thread(mTasks).start();
		 }else{
			 layout_alert.setVisibility(View.GONE);
		 }
		 return view; 
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	private void init(View view){
		main=view;
		dao=SRPUtil.getDAO(getActivity());
		BaseFragmentActivity a=(BaseFragmentActivity)getActivity();
		a.toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}
	
	private Runnable mTasks =new Runnable(){
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            Message message1 = new Message();
            message1.what = 1;
            Message message2 = new Message();
            message2.what = 2;
            handler.sendMessage(message1);
            handler.sendMessage(message2);
		}
	};
	
	
	
	//通用findView
	private void findView() {
		initPage();
		layout_alert=(LinearLayout)main.findViewById(R.id.layout_alert);
	}
	//通用Listener
	private void setListener() {

	}
	//造船界面FindView
	private void findView_A(){
		vo_s=(TextView)layout2.findViewById(R.id.square_sail_point);
		vo_f=(TextView)layout2.findViewById(R.id.fore_sail_point);
		vo_tu=(TextView)layout2.findViewById(R.id.turn_point);
		vo_de=(TextView)layout2.findViewById(R.id.def_wave_point);
		vo_p=(TextView)layout2.findViewById(R.id.paddle_point);	
		bt_h=(TextView)layout2.findViewById(R.id.health_boat_point);
		bt_p=(TextView)layout2.findViewById(R.id.people_number_point);
		bt_a=(TextView)layout2.findViewById(R.id.armor_point);
		bt_c=(TextView)layout2.findViewById(R.id.crenelle_point);
		bt_s=(TextView)layout2.findViewById(R.id.shipping_space_point);
		
		vo_s_a=(TextView)layout2.findViewById(R.id.square_sail_add);
		vo_f_a=(TextView)layout2.findViewById(R.id.fore_sail_add);
		vo_tu_a=(TextView)layout2.findViewById(R.id.turn_add);
		vo_de_a=(TextView)layout2.findViewById(R.id.def_wave_add);
		vo_p_a=(TextView)layout2.findViewById(R.id.paddle_add);	
		
		bt_h_a=(TextView)layout2.findViewById(R.id.health_boat_add);
		bt_a_a=(TextView)layout2.findViewById(R.id.armor_add);
		bt_c_a=(TextView)layout2.findViewById(R.id.crenelle_add);
		bt_s_a=(TextView)layout2.findViewById(R.id.shipping_space_add);
		
		plus_btn=(FlatButton)layout2.findViewById(R.id.build_btn);
		btn=(FlatButton)layout2.findViewById(R.id.btn);
		//旋转切换页面
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(ability.getVisibility()==View.VISIBLE)
					MyAnimations.rotate3D(show_layout, ability, choose_part,1000);
				else
					MyAnimations.rotate3D(show_layout, choose_part, ability,1000);
			}
		});
		sp_s=(TextView)layout2.findViewById(R.id.sp_s);
		sp_c=(TextView)layout2.findViewById(R.id.sp_c);
		sp_eq=(TextView)layout2.findViewById(R.id.sp_eq);
		sp_eq2=(TextView)layout2.findViewById(R.id.sp_eq2);
	}
	private void initPage(){
		//初始化layout相关
		main_list = new ArrayList<>();
		mInflater = getActivity().getLayoutInflater();
		//添加布局文件
		layout1 = mInflater.inflate(R.layout.dockyard_main_item_layout1, null);
		layout2 = mInflater.inflate(R.layout.dockyard_main_item_layout2, null);
		layout3 =mInflater.inflate(R.layout.dockyard_main_item_layout3, null);
		main_list.add(layout1);
		main_list.add(layout2);
		main_list.add(layout3);
		//初始化ViewPager相关
		main_adapter = new MyPagerAdapter(main_list,new String[]{"船只列表","造船模拟","船只配件"});
		main_ViewPage = (ViewPager)main.findViewById(R.id.main_viewpagers);
		main_ViewPage.setAdapter(main_adapter);
		main_ViewPage.setCurrentItem(0);
		//初始化PageItem
		initPageItem();
		//初始化PageEvent相关
		mSlidingTabLayout=(SlidingTabLayout)main.findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.White));
		mSlidingTabLayout.setBackgroundResource(R.drawable.theme_dark_blue);
		mSlidingTabLayout.setViewPager(main_ViewPage);
	}
	private void initPageItem(){
		//初始化第一页(船只列表)
		listview=(ListView)layout1.findViewById(R.id.listview);
		adapter=new SailBoatListAdapter(list,getActivity());
		listview.setOnScrollListener(this);
		listview.setAdapter(adapter);
		//选择跳转
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent=new Intent(getActivity(),SailBoatActivity.class);
				intent.putExtra("id",list.get(position).getId());
				DockYardFragment.this.startActivity(intent);
			}
		});
		//长按获得基本船只数据
		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				baseboat=list.get(position);
				Toast.makeText(getActivity(),"挑选为造船白板", Toast.LENGTH_SHORT).show();
				//更新第二页内容
				resetBuild();
				refreshPage();
				return true;
			}
		});
		//初始化第二页(造船模拟)
		info_txt=(TextView)layout2.findViewById(R.id.info_txt);
		show_layout=(LinearLayout)layout2.findViewById(R.id.show_broad);
		choose_part=(LinearLayout)layout2.findViewById(R.id.choose_part);
		ability=(RelativeLayout)layout2.findViewById(R.id.ability);
		findView_A();
		//初始化第三页(船只配件)
		listview2=(ListView)layout3.findViewById(R.id.listview2);
		adapter2=new PartListAdapter(list2,getActivity());
		listview2.setOnScrollListener(this);
		listview2.setAdapter(adapter2);
		listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				Intent intent = new Intent(getActivity(), PartActivity.class);
				intent.putExtra("id", list2.get(position).getId());
				DockYardFragment.this.startActivity(intent);
			}
		});
	}
	//选择船只后更新界面
	private void refreshPage(){
		info_txt.setText(baseboat.getName());
		//部分界面元素显示
		setData();
		//船只体型限制
		if(baseboat.getSize()==0){
			setSpinner("小");
		}else if(baseboat.getSize()==1){
			setSpinner("中");
		}else{
			setSpinner("大");
		}
		final MakeEvent event=new MakeEvent(this);
		//填充布局
		sp_s.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				event.showPartDialog(s_list, sp_s,0);
			}
		});
		sp_c.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				event.showPartDialog(c_list, sp_c,1);
			}
		});
		sp_eq.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				event.showPartDialog(eq_list1, sp_eq,2);
			}
		});
		sp_eq2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				event.showPartDialog(eq_list2, sp_eq2,3);
			}
		});
	}
	public void setPartChose(Part part,int number){
		switch(number){
			case 0:part_s=part;break;
			case 1:part_c=part;break;
			case 2:part_eq=part;break;
			case 3:part_eq2=part;break;
		}
	}
	//数据填充
	private void setData(){
		btn.setVisibility(View.VISIBLE);
		show_layout.setVisibility(View.VISIBLE);
		//初始性能面板
		vo_s.setText(baseboat.getSquare_sail()+"");
		vo_f.setText(baseboat.getFore_sail()+"");
		vo_tu.setText(baseboat.getTurn()+"");
		vo_de.setText(baseboat.getDef_wave()+"");
		vo_p.setText(baseboat.getPaddle()+"");	
		bt_h.setText(baseboat.getHealth_boat()+"");
		bt_p.setText(baseboat.getPeople_number()+"");
		bt_a.setText(baseboat.getArmor()+"");
		bt_c.setText(baseboat.getCrenelle()+"");
		bt_s.setText(baseboat.getShipping_space()+"");
		//强化次数
		plus_btn.setText("强化("+baseboat.getPlus_point()+")");
		plus_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(part_s==null){
					Toast.makeText(getActivity(),"至少要选择一个强化素材", Toast.LENGTH_SHORT).show();
					return ;
				}
				//①强化次数变化
				twice++;
				if(baseboat.getPlus_point()-twice!=0){
					plus_btn.setText("强化("+(baseboat.getPlus_point()-twice)+")");
					//②性能变化
					getPart();
				}
				//③强化完毕展示
				else{
					plus_btn.setVisibility(View.GONE);
				}
			}
		});
	}
	public void onDestroy() {
		dao=null;
		super.onDestroy();
	}
	@SuppressWarnings("unchecked")
	//有限数据查询
	private void selectshow(String limit,int page){
		if(dao==null){
			return;
		}
		//数据前后记录
		int size_before,size_after;
		if(page==1){
			size_before=list.size();
			list.addAll(((List<SailBoat>) dao.select(SailBoat.class, false,select_if, select_if_x, 
				null, null,order,limit)));
			size_after=list.size();
		}else{
			size_before=list2.size();
			list2.addAll(((List<Part>) dao.select(Part.class, false,select_if2, select_if_x2, 
				null, null, order,limit)));
			size_after=list2.size();
		}
		//数据返回判断
    	if(size_after==size_before&&size_after!=0)
    	{
    		if(page==1)
    			end_flag=false;
    		else
    			end_flag2=false;
    		Toast.makeText(getActivity(),"已经返回所有查询结果了", Toast.LENGTH_LONG).show();
    	}else if(size_after==0){
    		Toast.makeText(getActivity(),"没有查到您想要的结果", Toast.LENGTH_LONG).show();
    	}
	}
	//数据添加
	private void change(int i){
		//1.为船只信息，2.为配件信息
		if(i==1){
			add+=SailBoatListAdapter.SIZE;
			selectshow(add+","+	SailBoatListAdapter.SIZE,i);
			adapter.notifyDataSetChanged();
		}else{
			add2+=PartListAdapter.SIZE;
			selectshow(add2+","+	PartListAdapter.SIZE,i);
			adapter2.notifyDataSetChanged();
		}
	}
	//条件查询船只
	private void findObject(){
		//弹出对话框
		View xc;
		if(main_ViewPage.getCurrentItem()==0){
			xc=mInflater.inflate(R.layout.select_boat, null);
		}else{
			xc=mInflater.inflate(R.layout.select_part, null);
		}
		ViewUtil.popDialog(DockYardFragment.this, xc,main_ViewPage.getCurrentItem()+1);
	}
	//修改查询条件
	public void change_if(String if_s,String if_args,int page){
		//初始化所有数据
		if(page==1){
			select_if=if_s;
			select_if_x=new String[1];
			select_if_x[0]=if_args;
			list.clear();
			add=0;
			selectshow("0,"+SailBoatListAdapter.SIZE,page);
			//重新setAdapter
			adapter=new SailBoatListAdapter(list,getActivity());
			listview.setAdapter(adapter);
		}
		else if(page==3){
			select_if2=if_s;
			select_if_x2=new String[1];
			select_if_x2[0]=if_args;
			list2.clear();
			add2=0;
			selectshow("0,"+PartListAdapter.SIZE,page);
			//重新setAdapter
			adapter2=new PartListAdapter(list2,getActivity());
			listview2.setAdapter(adapter2);
		}
		else{
			//重新选择造船模板
		}
	}
	//修改查询条件
	public void change_if(String if_s,List<String> if_args,int page){
		//初始化所有数据
		if(page==1){
			select_if=if_s;
			select_if_x=new String[if_args.size()];
			for(int i=0;i<select_if_x.length;i++){
				select_if_x[i]=if_args.get(i);
			}
			list.clear();
			add=0;
			selectshow("0,"+SailBoatListAdapter.SIZE,page);
			//重新setAdapter
			adapter=new SailBoatListAdapter(list,getActivity());
			listview.setAdapter(adapter);
		}
		else if(page==3){
			select_if2=if_s;
			select_if_x2=new String[if_args.size()];
			for(int i=0;i<select_if_x2.length;i++){
				select_if_x2[i]=if_args.get(i);
			}
			list2.clear();
			add2=0;
			selectshow("0,"+PartListAdapter.SIZE,page);
			//重新setAdapter
			adapter2=new PartListAdapter(list2,getActivity());
			listview2.setAdapter(adapter2);
		}
		else{
			//重新选择造船模板
		}
	}
	//重置最末尾标记
	public void begin(int page){
		if(page==1)
			end_flag=true;
		if(page==2)
			end_flag2=true;
	}
	 @SuppressWarnings("unchecked")
	//填充SP的值
	public void setSpinner(String str) {
		s_list=((List<Part>) dao.select(Part.class, false,"type=? and (name like ? or name like ?)",new String[]{"6","%"+str+"%","%德%"} 
				,null, null, null,null));
		c_list=((List<Part>)dao.select(Part.class, false,"type=? and name like ?",new String[]{"7","%"+str+"%"} 
		,null, null, null,null));
		if(baseboat.getType()==0){
			eq_list1=((List<Part>)dao.select(Part.class, false,"type=? and name not like ?",new String[]{"8","%"+"桨"+"%"} 
			,null, null, null,null));
			eq_list2=((List<Part>)dao.select(Part.class, false,"type=? and name not like ?",new String[]{"8","%"+"桨"+"%"} 
			,null, null, null,null));
		}else{
			eq_list1=((List<Part>)dao.select(Part.class, false,"type=?",new String[]{"8"} 
			,null, null, null,null));
			eq_list2=((List<Part>)dao.select(Part.class, false,"type=?",new String[]{"8"} 
			,null, null, null,null));
		}
	}

	//获取增量
	private void getPart(){
		if(part_s!=null)
			s_map=NumberUtil.excute_add_info(part_s.getAdd());
		else
			s_map=NumberUtil.excute_add_info("A:0~0");
		if(part_c!=null)
			c_map=NumberUtil.excute_add_info(part_c.getAdd());
		else
			c_map=NumberUtil.excute_add_info("A:0~0");
		if(part_eq!=null)
			eq_map=NumberUtil.excute_add_info(part_eq.getAdd());
		else
			eq_map=NumberUtil.excute_add_info("A:0~0");
		if(part_eq2!=null)
			eq2_map=NumberUtil.excute_add_info(part_eq2.getAdd());
		else
			eq2_map=NumberUtil.excute_add_info("A:0~0");
		plus_change();
	}
	//强化数据变化
	private void plus_change(){
		vo_s_add+=s_map.get("横帆性能")!=null?s_map.get("横帆性能"):0;
		vo_f_add+=s_map.get("纵帆性能")!=null?s_map.get("纵帆性能"):0;
		vo_de_add+=s_map.get("抗波")!=null?s_map.get("抗波"):0;
		vo_tu_add+=s_map.get("转向")!=null?s_map.get("转向"):0;
		bt_c_add+=c_map.get("炮门")!=null?c_map.get("炮门"):0;
		
		set_map(eq_map);
		set_map(eq2_map);
		
		set_add_txt(vo_s_a,vo_s_add);
		set_add_txt(vo_f_a,vo_f_add);
		set_add_txt(vo_de_a,vo_de_add);
		set_add_txt(vo_tu_a,vo_tu_add);
		set_add_txt(vo_p_a,vo_p_add);
		
		set_add_txt(bt_c_a,bt_c_add);
		set_add_txt(bt_a_a,bt_a_add);
		set_add_txt(bt_s_a,bt_s_add);
		set_add_txt(bt_h_a,bt_h_add);
		
		vo_s.setText((baseboat.getSquare_sail()+vo_s_add)+"");
		vo_f.setText((baseboat.getFore_sail()+vo_f_add)+"");
		vo_tu.setText((baseboat.getTurn()+vo_tu_add)+"");
		vo_de.setText((baseboat.getDef_wave()+vo_de_add)+"");
		vo_p.setText((baseboat.getPaddle()+vo_p_add)+"");	
		bt_h.setText((baseboat.getHealth_boat()+bt_h_add)+"");
		bt_p.setText(baseboat.getPeople_number()+"");
		bt_a.setText((baseboat.getArmor()+bt_a_add)+"");
		bt_c.setText((baseboat.getCrenelle()+bt_c_add)+"");
		bt_s.setText((baseboat.getShipping_space()+bt_s_add)+"");
	}
	//Map数据判断变化
	private void set_map(HashMap<String,Integer> map){
		vo_s_add+=map.get("横帆性能")!=null?map.get("横帆性能"):0;
		vo_f_add+=map.get("纵帆性能")!=null?map.get("纵帆性能"):0;
		vo_de_add+=map.get("抗波")!=null?map.get("抗波"):0;
		vo_tu_add+=map.get("转向")!=null?map.get("转向"):0;
		vo_p_add+=map.get("桨力")!=null?map.get("桨力"):0;
		
		bt_h_add+=map.get("耐久")!=null?map.get("耐久"):0;
		bt_a_add+=map.get("装甲")!=null?map.get("装甲"):0;
		bt_c_add+=map.get("炮门")!=null?map.get("炮门"):0;
		bt_s_add+=map.get("仓位")!=null?map.get("仓位"):0;
	}
	//正负值判断，字体颜色变化
	private void set_add_txt(TextView txt,int i){
		if(i>0){
			txt.setTextColor(ResourcesUtil.getColor(R.color.SpringGreen, getActivity()));
			txt.setText("+"+i);
		}
		else if(i<0){
			txt.setTextColor(ResourcesUtil.getColor(R.color.Crimson, getActivity()));
			txt.setText(""+i);
		}
	}
	//重置造船数据
	private void resetBuild(){
		//第一步：数据重置
		//①.强化数值重置
		vo_s_add=0;vo_f_add=0;vo_de_add=0;vo_tu_add=0;vo_p_add=0;
		bt_h_add=0;bt_a_add=0;bt_s_add=0;bt_c_add=0;
		//第二步：界面重置
		//①.重置附加值
		vo_s_a.setText("");vo_f_a.setText("");vo_de_a.setText("");vo_tu_a.setText("");
		vo_p_a.setText("");bt_h_a.setText("");bt_s_a.setText("");bt_c_a.setText("");
		bt_a_a.setText("");
		//②.重置选择
		sp_s.setText(R.string.sp_hint1);sp_c.setText(R.string.sp_hint2);
		sp_eq.setText(R.string.sp_hint3);sp_eq2.setText(R.string.sp_hint4);
		//③.重置按钮
		plus_btn.setVisibility(View.VISIBLE);
	}
/**
 * 华丽的分割线——以下是Handler,线程,系统按键等处理 
 */
	//Handler——线程结束后更新界面
	 private Handler handler = new Handler() {
		 public void handleMessage(Message msg) {
			if(msg.what!=0){	
				change(msg.what);
				layout_alert.setVisibility(View.GONE);
			}else if(msg.what==100){
				change(1);
				change(2);
				layout_alert.setVisibility(View.GONE);
			}
		 }
	 };

	//系统按键监听覆写
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 //菜单键覆写，调用边缘栏菜单
		 if(keyCode==KeyEvent.KEYCODE_MENU){
			 return true;
		 }
		 //条件:当菜单未关闭且搜索条件为初始态，允许退出
		if(select_if.equals("id>?")&&main_ViewPage.getCurrentItem()==0){
			return false;
		}else if(select_if2.equals("id>?")&&main_ViewPage.getCurrentItem()==2){
			return false;
		}else if(main_ViewPage.getCurrentItem()==1){
			return false;
		}else{
			//按键返回
			if(keyCode==KeyEvent.KEYCODE_BACK)
			{
				//条件不是初始状态就重置
				if(!select_if.equals("id>?")&&main_ViewPage.getCurrentItem()==0){
					end_flag=true;
					change_if("id>?","0",main_ViewPage.getCurrentItem()+1);
					Toast.makeText(getActivity(),"重置搜索条件", Toast.LENGTH_SHORT).show();
				}
				else if(!select_if2.equals("id>?")&&main_ViewPage.getCurrentItem()==2){
					end_flag2=true;
					change_if("id>?","0",main_ViewPage.getCurrentItem()+1);
					Toast.makeText(getActivity(),"重置搜索条件", Toast.LENGTH_SHORT).show();
				}
			}
		}
		 return true;
	 }
	//滚动监听① - useless
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	//滚动监听②
	public void onScrollStateChanged(final AbsListView view, int scrollState) {
        //当不滚动时
		int page=main_ViewPage.getCurrentItem();
		boolean flag=true;
		if(page==0){
			flag=end_flag;
		}else if(page==2){
			flag=end_flag2;
		}
        if(scrollState == SCROLL_STATE_IDLE){  
                System.out.println(view.getFirstVisiblePosition()+"===" + view.getLastVisiblePosition()+"==="+view.getCount());
                //判断滚动到底部   
                if(view.getLastVisiblePosition()==(view.getCount()-1)){
                	//没有线程且不为最末时
                    if (mThread == null || !mThread.isAlive()&&flag) {
                    	//显示进度条，区域操作控制
                    	layout_alert.setVisibility(View.VISIBLE);
                        mThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //2.5秒后进行界面更新
                                if(view.equals(listview)){
                                	Message message = new Message();
                                	message.what = 1;
                                	handler.sendMessage(message);
                                }else{
                                	Message message = new Message();
                                	message.what = 2;
                                	handler.sendMessage(message);
                                }
                            }  
                        };
                        mThread.start();                
                    }
                }
        	}
	}
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.city_search:findObject();break;
				case R.id.type_search:DBUtil.copyDB_SD(getActivity());break;

			}
			return true;
		}
	};
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.adc_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}