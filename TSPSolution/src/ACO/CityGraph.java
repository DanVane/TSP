/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ACO;

import java.util.Random;

/**
 * distance[][]一旦确定不再改变，迭代过程中更新pheromone[][]
 *
 * @author weangdan
 */
public class CityGraph {

    private static double distance[][];
    private static double pheromone[][];
    private static int num;
    Random random = new Random();
    private int min = 2;
    private int max = 100;

    public CityGraph(int num, boolean ifDuichen) {
        this.num = num;

        initDistance(ifDuichen);
        initPheronome();
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
                        distance[i][j] = 0;
                    } else {
                        distance[i][j] = min + ((max - min) * random.nextDouble()); //产生2-100之间的随机浮点数
                    }
                }

            }
        }
printlndistance();
    }

    private void initPheronome() {
        pheromone = new double[num][num];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                pheromone[i][j] = 0.1;
            }
        }
    }

    public static void normalizePheromone() {
        double all_phero = 0.0;
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                all_phero += pheromone[i][j];
            }
        }
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                 pheromone[i][j]=pheromone[i][j]/all_phero;
            }
        }
         
    }

    public static double getDistance(int i, int j) { //注意i,j和城市之间的对应关系
        return distance[i][j];
    }

    public static double getPheronmone(int i, int j) {
        return pheromone[i][j];
    }
    
    public static double[] getCities(int i){
        return pheromone[i];
    }
    
    public static void setPhero(int i,int j ,double Q,long t){
        pheromone[i][j]+=Q-t/100* pheromone[i][j];
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
