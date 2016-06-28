/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author weangdan
 */
/**
 * 遗传算法个体，采取基于路径的编码方式
 */
public class GAEntity {

    private static Integer[] initRoad = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
        20, 21, 22, 23, 24, 25, 26, 27, 28, 29};

    private int num;//城市个数
    private List<Integer> roadlist;
    private Integer[] road;
    private double adaptability = 0.0;//个体适应度
    private double p_lucky = 0.0;//幸存概率

    GAEntity(int n,String s) {
        num = n;
        roadlist = new ArrayList<Integer>();
        road = new Integer[num];
        InitRoad();
    }

    GAEntity(int n) {
        num = n;
        roadlist = new ArrayList<Integer>();
        road = new Integer[num];
    }

    private void InitRoad() {//随机解
        roadlist = Arrays.asList(initRoad);
        Collections.shuffle(roadlist);
        road = (Integer[]) roadlist.toArray();
    }

    public void setRoad(int i, int j) {
        roadlist.set(i, j);
        road[i] = j;
    }

    public int getRoad(int i) {
        return road[i];
    }

    public double getAdaptability() {
        return adaptability;
    }

    public void setAdaptability(double adaptability) {
        this.adaptability = adaptability;
    }

    public double getP_lucky() {
        return p_lucky;
    }

    public void setP_lucky(double p_lucky) {
        this.p_lucky = p_lucky;
    }

    public String printRoad() {
        String p = "";
        for (int i = 0; i < num; i++) {
            p += "  " + road[i] + ";";
        }
        p+="幸存概率："+p_lucky;
        return p;
    }

    public double cal_Adaptability() {
        adaptability = 0.0;
        for (int i = 0; i < num - 1; i++) {
            adaptability += Distance.getDistance(road[i], road[i + 1]);
        }
        adaptability +=Distance.getDistance(road[num-1], road[0]);
        return adaptability;
    }

    public double cal_preLucky(double all_ability) {
        p_lucky = 1 - adaptability / all_ability;
        return p_lucky;
    }

    public void cal_Lucky(double all_lucky) {
        p_lucky = p_lucky / all_lucky;
    }

    public void setRoad(GAEntity parent, int position1, int position2) {
        roadlist.clear();
        for (; position1 <= position2; position1++) {
            road[position1] = parent.getRoad(position1);
            roadlist.add(road[position1]);//当前已有路径统计
        }
    }

    public void modifyRoad(GAEntity parent, int position1, int position2, MatchTable matchTable, boolean ifChild1) {
        int roadnum;
        boolean ifModify = false;
        if (ifChild1) {//子代1的查询表应该从父代2开始,最终值落在父代1中
//            System.out.println("开始插入首尾值：子代1");
            for (int i = 0; i < num; i++) {
                if (i >= position1&&i<=position2) {
                    i = position2;
                    continue;
                }
                roadnum = parent.getRoad(i);
                ifModify = checkRoad(roadnum);

                while (ifModify) {
//                   System.out.println("开始查找匹配表:"+roadnum );
                    roadnum = matchTable.getRoadNum(false, roadnum);
//                     System.out.println(""+roadnum );
                    ifModify = checkRoad(roadnum);
                }
                road[i] = roadnum;
                roadlist.add(roadnum);
            }
//             System.out.println("子代1处理结束");
        } else {//子代2的查询表应该从父代1开始
//            System.out.println("开始插入首尾值：子代2");
            for (int i = 0; i < num; i++) {
                if (i >= position1&& i<=position2) {
                    i = position2;
                    continue;
                }
                roadnum = parent.getRoad(i);
                ifModify = checkRoad(roadnum);

                while (ifModify) {
//                    System.out.println("开始查找匹配表:"+roadnum );
                    roadnum = matchTable.getRoadNum(true, roadnum);
//                     System.out.println(""+roadnum );
                    ifModify = checkRoad(roadnum);
                }

                road[i] = roadnum;
                roadlist.add(roadnum);

            }
        }
//         System.out.println("子代2处理结束");
    }

    private boolean checkRoad(int roadnum) {
//        for(int i=0;i<roadlist.size();i++){
//             System.out.print("当前roadlist含有:"+roadlist.get(i) );
//        }
//        System.out.println("" );
        if (roadlist.contains(roadnum)) {
            return true;
        }
        return false;
    }

    public void exchange(int p1, int p2) {
        int t = road[p1];
        road[p1] = road[p2];
        road[p2] = t;
    }
    
    public boolean checkdifference(GAEntity g){
for(int i=0;i<num;i++){
    if(road[i]==g.getRoad(i)){
        continue;
    }else{
        return true;
    }
}
        return false;
    }
}
