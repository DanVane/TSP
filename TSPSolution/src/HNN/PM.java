/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HNN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author weangdan
 */
public class PM {

    private static int citynum;
    private static double[][] pm_u;
    private static int[][] pm_v;
    private static int[][] pm_vt;
    private static double[][] distance;
    private static Random random = new Random();
    private int min = 2;
    private int max = 100;
    private static List<Integer> roadList;
    private static int sum=0;

    PM(int citynum) {
        this.citynum = citynum;
        pm_u = new double[citynum][citynum];
        pm_v = new int[citynum][citynum];
        pm_vt = new int[citynum][citynum];
        roadList = new ArrayList<Integer>();
        initDistance();
    }

    private void initDistance() {
        distance = new double[citynum][citynum];
        //distance是一个对称阵，且对角元素设为无穷大；对角线元素不会被用到，如果算法正确
        for (int i = 0; i < citynum; i++) {

            for (int j = i; j < citynum; j++) {
                if (i == j) {
                    distance[i][j] = 0;
                } else {
                    distance[i][j] =distance[j][i]= min + ((max - min) * random.nextDouble()); //产生2-100之间的随机浮点数
                }
            }

        }

    }

    public static double getDistance(int i, int j) { //注意i,j和城市之间的对应关系
        return distance[i][j];
    }

    public static void initPM_u(double u0) {
        double randNum;
        int ii = 0;
        for (int i = 0; i < citynum; i++) {
            for (int j = i; j < citynum; j++) {
                randNum = Math.random();
                ii = random.nextInt(2);
                switch (ii) {
                    case 0:
                        break;
                    case 1:
                        randNum = 0 - randNum;
                }
//                System.out.println(randNum);
                pm_u[i][j] =pm_u[j][i]= (u0 * Math.log(citynum * citynum - 1)) / 2 + randNum;
//                 System.out.println( pm_u[i][j] );
//                pm_v[i][j] = (1+Math.tanh(pm_u[i][j]/u0))/2;
            }
        }

    }

    public static void initPM_v(double u0) {
        for (int i = 0; i < citynum; i++) {
            for (int j = 0; j < citynum; j++) {
                double u = (1 + Math.tanh(pm_u[i][j] / u0)) / 2;
//                System.out.println( u );
                if (u >= 0.2) {
                    pm_v[i][j] = 1;
                    pm_vt[j][i] = 1;
                } else {
                    pm_v[i][j] = 0;
                    pm_vt[j][i] = 0;
                }
            }

        }
//        for(int i=0;i<citynum;i++){
//            for(int j=0;j<citynum;j++){
//                System.out.print(pm_vt[i][j]+" ");
//            }
//            System.out.println("");
//        }
    }

    public static String printPM_v() {
//        for(int i=0;i<citynum;i++){
//            for(int j=0;j<citynum;j++){
//                System.out.print(pm_u[i][j]+" ");
//            }
//            System.out.println("");
//        }

  roadList.clear();
        String p = "";
        sum = 0;
        for (int i = 0; i < citynum; i++) {
            sum = 0;
            for (int j = 0; j < citynum; j++) {
                sum += pm_v[i][j];
            }
            if (sum > 1) {
                return "路径无效";
            }
        }
        for (int i = 0; i < citynum; i++) {
            sum = 0;
            for (int j = 0; j < citynum; j++) {
                sum += pm_vt[i][j];
                if (pm_vt[i][j] == 1) {
                    p += j + ";";
                    roadList.add(j);
                }
            }
            if (sum > 1) {
                return "路径无效";
            }
        }
        return p;
    }

    public static double calRoadLength() {
        if(sum<=1){
            double roadlength = 0;
        for (int i = 0; i < citynum- 1; i++) {
            roadlength += distance[roadList.get(i)][roadList.get(i + 1)];
        }
        roadlength += distance[roadList.get(citynum- 1)][roadList.get(0)];
        return roadlength;
        }
        return Double.MAX_VALUE;

    }

    public static double getJ1(int x, int i, int j) {
        return pm_v[x][i] * pm_v[x][j];//J1+=v[x][i]*v[x][j]
    }

    public static double getJ2(int i, int x, int y) {
        return pm_v[x][i] * pm_v[y][i];//j2+=v[x][i]*v[y][i]
    }

    public static double getJ3(int x, int i) {
        return pm_v[x][i];    //计算能量系数,k+=v[x][i]
    }

    public static double getJ4(int ca, int x, int y, int i) {
//        System.out.println("xyi:"+x+";"+y+";"+i);
        double K=0.0;
        switch (ca) {
            case 0: K=distance[x][y]*pm_v[x][i]*(pm_v[y][citynum-1]+pm_v[y][i+1]);//J+=dis[x][y]*v[x][i]*(v[y][N-1]+v[y][i+1]);
            break;

            case 1:
                K=distance[x][y]*pm_v[x][i]*(pm_v[y][i-1]+pm_v[y][0]);//J+=dis[x][y]*v[x][i]*(v[y][i-1]+v[y][0])
                break;
              
            case 2: 
                 K=distance[x][y]* pm_v[x][i]* (pm_v[y][i-1] + pm_v[y][i+1]);
                 break;
           
        }
//         System.out.println("K:"+K);
        return K;
    }
    
    public static double getPM_u(int i,int j){
        return pm_u[i][j];
    }
    
        public static double getPM_v(int i,int j){
        return pm_v[i][j];
    }
        
            public static double getdeltJ4(int ca, double D,int x, int y, int i) {
        switch (ca) {
            case 0: return D*(pm_v[y][citynum-1]+pm_v[y][i+1]);   

            case 1:return D*(pm_v[y][i-1]+pm_v[y][0]);    
              
            case 2: return D*(pm_v[y][i-1]+pm_v[y][i+1]);
           
        }
        return 1;
        
    
    }
            
            public static void updatePM_uv(int x,int i,double step,double delt,double u0){
                pm_u[x][i]+=step*delt;//缩小系数比例
//                   double u = (1 + Math.tanh(pm_u[x][i] / u0)) / 2;
//                if (u >= 0.2) {
//                    pm_v[x][i] = 1;
//                    pm_vt[i][x] = 1;
//                } else {
//                    pm_v[x][i] = 0;
//                    pm_vt[i][x] = 0;
//                }
             
            }
            
            public static void printlnPM_v(){
                        for(int i=0;i<citynum;i++){
            for(int j=0;j<citynum;j++){
                System.out.print(pm_u[i][j]+" ");
            }
            System.out.println("");
        }
            }
            

}
