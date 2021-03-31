package com.lei.algorithm;

public class EightQueen {

    private static int row;
    private static int[][] arrays;
    private static int count;
    private static String last;


    public static void main(String[] args) {
        row = 4;
        arrays = init(row);
        play(0);

    }

    private static void play(int m){
        for (int i = 0; i < arrays.length; i++) {
            if (display(m,i)){
                if (m == arrays.length - 1){
                    System.out.println("第" + (++count) + "种");
                    print();
                } else {
                    play(m + 1);
                }
                arrays[m][i] = 0;
            }
        }
    }


    private static int[][] init(final int row){
        int [][] arrays = new int[row][row];
        return arrays;
    }

    private static boolean display(int m,int n){
        for (int i = 0; i < m; i++) {
            if (arrays[i][n] == 1){
                return false;
            }
        }
        for (int i = Math.min(m, n); i > -1; i--) {
            if (arrays[m-i][n-i] == 1){
                return false;
            }
        }
        for (int i = Math.min(m, arrays.length - 1 -n) ; i > -1; i--) {
            if (arrays[m-i][n+i] == 1){
                return false;
            }
        }
        arrays[m][n] = 1;
        return true;
    }

    private static void print(){
        for (int[] rows : arrays) {
            for (int i : rows) {
                System.out.print(i + "  ");
            }
            System.out.println();
        }
    }


}
