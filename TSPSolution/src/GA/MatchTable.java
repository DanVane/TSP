/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

/**
 *该表用于部分匹配法解决路径重复问题
 * @author weangdan
 */
public class MatchTable {
    
    private int[]  parent1;//这两个数组是一一对应的关系
    private int[] parent2;
    GAEntity gaentity1,gaentity2;
    int position1 ,position2;
    int num;
    
    MatchTable(int citynum){
        num = citynum;
        parent1 = new int[citynum];
        parent2 = new int[citynum];
    }
    
    private void ClearPP(){
        parent1 = null;
        parent2 = null;
       parent1 = new int[num];
        parent2 = new int[num];
    }
    
    public void setTable(GAEntity ga1,GAEntity ga2,int p1,int p2){
        ClearPP();
        gaentity1 = ga1;
        gaentity2 = ga2;
        position1 = p1;
        position2 = p2;
        for(;position1<=position2;position1++){
            parent1[gaentity1.getRoad(position1)] = gaentity2.getRoad(position1);
            parent2[gaentity2.getRoad(position1)] = gaentity1.getRoad(position1);
        }
        
    }
    
    public int getRoadNum(boolean ifParent1,int roadIndex){
        if(ifParent1){
            return parent1[roadIndex];
        }else{
            return parent2[roadIndex];
        }
    }
    
}
