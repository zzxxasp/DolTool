package com.key.doltool.activity.dockyard;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.key.doltool.R;
import com.key.doltool.activity.core.BaseFragment;
import com.key.doltool.anime.MyAnimations;
import com.key.doltool.data.MenuItem;
import com.key.doltool.data.SailBoat;
import com.key.doltool.data.sqlite.Part;
import com.key.doltool.event.MakeEvent;
import com.key.doltool.event.rx.RxBusEvent;
import com.key.doltool.util.NumberUtil;
import com.key.doltool.util.ResourcesUtil;
import com.key.doltool.util.db.SRPUtil;
import com.key.doltool.view.Toast;
import com.key.doltool.view.flat.FlatButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 造船模拟-0.1 基础功能模拟造船
 * Created by key on 2016/12/16.
 */
public class BuildBoatFragment extends BaseFragment {
    //造船模拟界面
    @BindView(R.id.info_txt) TextView info_txt;
    @BindView(R.id.show_broad) LinearLayout show_layout;
    @BindView(R.id.ability) LinearLayout ability;
    @BindView(R.id.choose_part) LinearLayout choose_part;

    @BindView(R.id.sp_s) TextView sp_s;
    @BindView(R.id.sp_c) TextView sp_c;
    @BindView(R.id.sp_eq) TextView sp_eq;
    @BindView(R.id.sp_eq2) TextView sp_eq2;

    @BindView(R.id.square_sail_point) TextView vo_s;
    @BindView(R.id.fore_sail_point) TextView vo_f;
    @BindView(R.id.turn_point) TextView vo_tu;
    @BindView(R.id.def_wave_point) TextView vo_de;
    @BindView(R.id.paddle_point) TextView vo_p;

    @BindView(R.id.health_boat_point) TextView bt_h;
    @BindView(R.id.people_number_point) TextView bt_p;
    @BindView(R.id.armor_point) TextView bt_a;
    @BindView(R.id.crenelle_point) TextView bt_c;
    @BindView(R.id.shipping_space_point) TextView bt_s;

    @BindView(R.id.square_sail_add) TextView vo_s_a;
    @BindView(R.id.fore_sail_add) TextView vo_f_a;
    @BindView(R.id.turn_add) TextView vo_tu_a;
    @BindView(R.id.def_wave_add) TextView vo_de_a;
    @BindView(R.id.paddle_add) TextView vo_p_a;

    @BindView(R.id.health_boat_add) TextView bt_h_a;
    @BindView(R.id.armor_add) TextView bt_a_a;
    @BindView(R.id.crenelle_add) TextView bt_c_a;
    @BindView(R.id.shipping_space_add) TextView bt_s_a;

    @BindView(R.id.build_btn) FlatButton plus_btn;
    @BindView(R.id.action)
    FloatingActionButton action;
    private SRPUtil dao;
    private SailBoat baseboat;
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
    private Disposable menuItemSubscription,sailSubscription;

    public int getContentViewId() {
        return R.layout.dockyard_main_item_layout2;
    }


    protected void initAllMembersView(Bundle savedInstanceState) {
        initData();
        setListener();
    }
    private void initData(){
        dao=SRPUtil.getInstance(context);
        Observable<MenuItem> menuItemObservable = RxBusEvent.get().register(RxBusEvent.SAILMENU);
        menuItemSubscription=menuItemObservable.subscribe(new Consumer<MenuItem>() {
            @Override
            public void accept(MenuItem item) {

            }
        });
        Observable<SailBoat> sailBoatObservable = RxBusEvent.get().register(RxBusEvent.SAILBOAT);
        sailSubscription=sailBoatObservable.subscribe(new Consumer<SailBoat>() {
            @Override
            public void accept(SailBoat item) {
                if(item!=null) {
                    baseboat = item;
                    resetBuild();
                    refreshPage();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBusEvent.get().unregister(RxBusEvent.SAILMENU);
        RxBusEvent.get().unregister(RxBusEvent.SAILBOAT);
        menuItemSubscription.dispose();
        sailSubscription.dispose();
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

    private void setData(){
        action.setVisibility(View.VISIBLE);
        show_layout.setVisibility(View.VISIBLE);
        //初始性能面板
        vo_s.setText(String.valueOf(baseboat.getSquare_sail()));
        vo_f.setText(String.valueOf(baseboat.getFore_sail()));
        vo_tu.setText(String.valueOf(baseboat.getTurn()));
        vo_de.setText(String.valueOf(baseboat.getDef_wave()));
        vo_p.setText(String.valueOf(baseboat.getPaddle()));
        bt_h.setText(String.valueOf(baseboat.getHealth_boat()));
        bt_p.setText(String.valueOf(baseboat.getPeople_number()));
        bt_a.setText(String.valueOf(baseboat.getArmor()));
        bt_c.setText(String.valueOf(baseboat.getCrenelle()));
        bt_s.setText(String.valueOf(baseboat.getShipping_space()));
        //强化次数
        plus_btn.setText("强化("+baseboat.getPlus_point()+")");
        plus_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(part_s==null){
                    Toast.makeText(context.getApplicationContext(),"至少要选择一个强化素材", Toast.LENGTH_SHORT).show();
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

    //获取增量
    private void getPart(){
        if(part_s!=null)
            s_map= NumberUtil.excute_add_info(part_s.getAdd());
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

        vo_s.setText(String.valueOf(baseboat.getSquare_sail()+vo_s_add));
        vo_f.setText(String.valueOf(baseboat.getFore_sail()+vo_f_add));
        vo_tu.setText(String.valueOf(baseboat.getTurn()+vo_tu_add));
        vo_de.setText(String.valueOf(baseboat.getDef_wave()+vo_de_add));
        vo_p.setText(String.valueOf(baseboat.getPaddle()+vo_p_add));
        bt_h.setText(String.valueOf(baseboat.getHealth_boat()+bt_h_add));
        bt_p.setText(String.valueOf(baseboat.getPeople_number()));
        bt_a.setText(String.valueOf(baseboat.getArmor()+bt_a_add));
        bt_c.setText(String.valueOf(baseboat.getCrenelle()+bt_c_add));
        bt_s.setText(String.valueOf(baseboat.getShipping_space()+bt_s_add));
    }

    public void setSpinner(String str) {
        s_list= dao.select(Part.class, false,"type=? and (name like ? or name like ?)",new String[]{"6","%"+str+"%","%德%"}
                ,null, null, null,null);
        c_list= dao.select(Part.class, false,"type=? and name like ?",new String[]{"7","%"+str+"%"}
                ,null, null, null,null);
        if(baseboat.getType()==0){
            eq_list1= dao.select(Part.class, false,"type=? and name not like ?",new String[]{"8","%"+"桨"+"%"}
                    ,null, null, null,null);
            eq_list2= dao.select(Part.class, false,"type=? and name not like ?",new String[]{"8","%"+"桨"+"%"}
                    ,null, null, null,null);
        }else{
            eq_list1= dao.select(Part.class, false,"type=?",new String[]{"8"}
                    ,null, null, null,null);
            eq_list2= dao.select(Part.class, false,"type=?",new String[]{"8"}
                    ,null, null, null,null);
        }
    }

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
            txt.setText(String.valueOf(i));
        }
    }

    //重置造船数据
    private void resetBuild(){
        //第一步：数据重置
        twice=0;
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

    private void setListener(){
        action.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(baseboat!=null){
                    if(ability.getVisibility()==View.VISIBLE){
                        MyAnimations.rotate3D(show_layout, ability, choose_part,320);
                    }else {
                        MyAnimations.rotate3D(show_layout, choose_part, ability,320);
                    }
                }else{
                    Toast.makeText(context.getApplicationContext(),"请先挑选船只",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
