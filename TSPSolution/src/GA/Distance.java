/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.util.Random;

/**
 * 城市距离类，保存城市间的距离，初始化后不再改变
 *
 * @author weangdan
 */
public class Distance {

    private static double distance[][];
    private int num;
    Random random = new Random();
    private int min = 2;
    private int max = 100;

    public Distance(int num, boolean ifDuichen) {
        this.num = num;
        initDistance(ifDuichen);
    }

    private void initDistance(boolean ifDuichen) {
        distance = new double[num][num];
        //distance是一个对称阵，且对角元素设为无穷大；对角线元素不会被用到，如果算法正确
        for (int i = 0; i < num; i++) {
            if (ifDuichen) {
                for (int j = i; j < num; j++) {
                    if (i == j) {
                        distance[i][j] = 0;
                    } else {
                        distance[i][j] = distance[j][i] = min + ((max - min) * random.nextDouble()); //产生2-100之间的随机浮点数
                    }
                }
            } else {
                for (int j = 0; j < num; j++) {
                    if (i == j) {
                        distance[i][j] = Double.MAX_VALUE;
                    } else {
                        distance[i][j] = min + ((max - min) * random.nextDouble()); //产生2-100之间的随机浮点数
                    }
                }

            }
        }
        printlndistance();
    }

    public static double getDistance(int i, int j) { //注意i,j和城市之间的对应关系
        return distance[i][j];
    }

    private void printlndistance() {
        System.out.printf("%8s","");
        for (int i = 0; i < num; i++) {
            System.out.printf("%5s",i);
        }
        System.out.println();
        for (int i = 0; i < num; i++) {
            System.out.printf("%5s",i);
            for (int j = 0; j < num; j++) {
                System.out.printf("%5s",(int)distance[i][j]);
            }
            System.out.println();
        }
    }

}
