/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HNN;

/**
 *
 * @author weangdan
 */
public class HNN {

    //网络参数定义，变化敏感，原则上不能随意更改
    private double para_A = 0.05;
    private double para_B = 0.05;
    private double para_D = 0.1;
    private double para_C = 0.02;
    private double u0 = 0.02;

    //城市数目为n，则输入和输出神经元数目分别为n×n,对应pm表中的每一个空格，pm表即是输出表
    private int citynum = 30;
    private String bestRoad;
    private double shortestLength;
    private PM pm;
    private int p = 20000;
    private double step = 0.01;
    private double energy = Double.MAX_VALUE;
    private double tempenergy = 0.0;
    private double delt_energy;
    private double k = 0;

    //初始化城市
    private void Init_Distance() {
        pm = new PM(citynum);
    }

    //初始化各神经元的初始状态
    private void InitPM_u() {
        PM.initPM_u(u0);
    }

    /**
     * 何时退出迭代？ p<最大迭代次数
     */
    //求各神经元的输出
    private void calPM_v() {
        PM.initPM_v(u0);
        PM.printlnPM_v();
    }

    //计算能量函数
    private void calEnergy() {
        int x = 0;
        int i = 0;
        int j = 0;
        int y = 0;
        double J1 = 0.0;
        double J2 = 0.0;
        double J3 = 0.0;
        double J4 = 0.0;
///计算J1,也是约束条件，即在换位矩阵中，每一城市行x 至多含有一个“1”，其余都是“0”，
        for (x = 0; x < citynum; x++) {
            for (i = 0; i < citynum - 1; i++) //j从i+1开始是为了避免j=i的情况
            {
                for (j = i + 1; j < citynum; j++) {
                    J1 += PM.getJ1(x, i, j);
                }
            }
        }
        //计算J2,也是约束条件，即在置换矩阵中，每一城市列y 至多含有一个“1”，其余都是“0”，
        for (i = 0; i < citynum; i++) {
            for (x = 0; x < citynum - 1; x++) //y从x+1开始是为了避免y=x的情况
            {
                for (y = x + 1; y < citynum; y++) {
                    J2 += PM.getJ2(i, x, y);
                }
            }
        }
        //计算J3,其中K 是计算置换矩阵的总和；J3也是约束条件，即在置换矩阵中,只能有N个1；最后一步平方，是为了防止出现负数
        for (x = 0; x < citynum; x++) {
            for (i = 0; i < citynum; i++) {
                J3 += PM.getJ3(x, i);
            }

        }
        k = J3;
        J3 = (J3 - citynum) * (J3 - citynum);
        /*
	计算J,可行旅行路线的路程
	J=min(sum(d[x][y]*v[x][i]*(v[y][i+1]+v[y][i-1]))),y!=x;
	v[x][i]的行下标x是城市编号，列下标i表示城市x在旅行顺序中的位置，下标对N取模运算
         */
        for (x = 0; x < citynum; x++) {
            for (y = 0; y < citynum; y++) {
                for (i = 0; i < citynum; i++) {
                    if (i == 0) //下标对N取模运算,由于i-1<0,而i从0开始取值,所以取模后i为Num-1
                    {
                        J4 += PM.getJ4(0, x, y, i);
                    } else if (i == citynum - 1) {
                        J4 += PM.getJ4(1, x, y, i);
                    } else {
                        J4 += PM.getJ4(2, x, y, i);
                    }
                }
            }
        }
        //得到能量函数
        System.out.println("J:" + J1 + ";" + J2 + ";" + J3 + ";" + J4);
        System.out.println("energy" + energy);
//        System.out.println("tempenergy" + tempenergy);
//        System.out.println("delt_energy"+energy);
        tempenergy = para_A * J1 / 2 + para_B * J2 / 2 + para_C * J3 / 2 + para_D * J4 / 2;
        System.out.println("tempenergy" + tempenergy);
        delt_energy = energy - tempenergy;
        System.out.println("delt_energy" + delt_energy);
        energy = tempenergy;
    }

    //计算delt    //求下一时刻的状态量
    private void updatePM_uv() {
        int x = 0;
        int i = 0;
        int j = 0;
        int y = 0;
        double delt = 0.0;
        /*
		取神经元的I/O函数为S型函数，可以求得TSP问题的网络方程
		delt=-u[x][i]-A*Sum(v[x][j])-B*Sum(v[y][j]-C*(Sum(v[x][i])-N)-D*Sum(d[x][y])(v[y][i+1]+v[y][i-1]));
		u[x*Num+i]=h*delt;
		v[x][i]通过G(u[u][i])求得
         */
        for (x = 0; x < citynum; x++) {
            for (i = 0; i < citynum; i++) {
                delt = 0 - PM.getPM_u(x, i);//u[x][i]

                for (j = 0; j < citynum; j++) {
                    if (i == j) {
                        continue;
                    }
                    delt -= para_A * PM.getPM_v(x, j); //v[x][j]
                }

                for (y = 0; y < citynum; y++) {
                    if (x == y) {
                        continue;
                    }
                    delt -= para_B * PM.getPM_v(y, i);   //v[y][i]
                }

                delt -= para_C * (k - citynum);//k=Sum(v[x][i])
                //i需对N取模
                for (y = 0; y < citynum; y++) {
                    if (i == 0) {
                        delt -= PM.getdeltJ4(0, para_D, x, y, i);
                    } else if (i == citynum - 1) {
                        delt -= PM.getdeltJ4(1, para_D, x, y, i);
                    } else {
                        delt -= PM.getdeltJ4(2, para_D, x, y, i);
                    }
                }
                PM.updatePM_uv(x, i, step, delt, u0);

            }
        }
    }

    /**
     * 迭代结束
     */
    //输出当前pm状态，即为找到的最优解
    private void printPM_v() {
        //最好转置一下
        System.out.println("当前最小路径为" + PM.printPM_v());
        System.out.println("能量消耗为" + energy);
        System.out.println("路径长度为" + PM.calRoadLength());
    }

    private void iterator() {
        for (double d = 0.05; d < 0.30; d += 0.01) {
            para_A = para_B = para_C = d;
            int i = 0;
            Init_Distance();
            InitPM_u();
            while (i < p) {
                System.out.println("第" + i + "次迭代：");
                calPM_v();
                calEnergy();
                if (delt_energy < 0.05) {
                    break;
                }
                updatePM_uv();
                printPM_v();
                i++;
            }
            printPM_v();
        }

    }

    public static void main(String[] args) {
        HNN hnn = new HNN();
        hnn.iterator();
    }

}
