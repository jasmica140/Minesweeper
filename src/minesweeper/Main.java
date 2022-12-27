package minesweeper;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Random gen = new Random();
    private static final Scanner scan = new Scanner(System.in);

    private static int[][] board = new int[16][16];
    static boolean[][] showcell = new boolean[16][16];     //true = uncovered

    private final static int bomb = -1;
    private static int stop;

    public static void main(String[] args) {

        stop = 0;
        createboard();
        mines();    //generate mines
        minecount();    //count adjacent mines

        while(stop == 0){
            displayboard(false);    //display board
            uncover(input());
            gameovercheck();       //check win/lose conditions
        }

        gameover(stop);
        displayboard(true);
    }

    private static void createboard() {
        board = new int[16][16];
        showcell = new boolean[16][16];
    }

    private static void mines() {

        int[][] grid = new int[40][2];
        for (int i = 0; i < 40; i++) {

            grid[i][0] = (gen.nextInt(16)); //x-axis
            grid[i][1] = (gen.nextInt(16)); //y-axis

            for (int j = 0; j < i; j++) { //check if cell already occupied
                if ((grid[j][0] == grid[i][0]) && (grid[j][1] == grid[i][1])) {
                    i--;
                    break;
                }
            }
        }

        //put mines in board
        for (int i = 0; i < 40; i++)
            board[grid[i][0]][grid[i][1]] = bomb;
    }

    private static void minecount() {
        for (int y=0; y<16; y++)
            for (int x=0; x<16; x++)
                if (!(board[x][y]==bomb))
                    for (int y1=-1; y1<=1; y1++)    //row below, current row, row above
                        for (int x1=-1; x1<=1; x1++)    //cols left, centre, right
                            if (x+x1>=0 && x+x1<=15 && y+y1>=0 && y+y1<=15 && board[x+x1][y+y1]==bomb)
                                board[x][y]++;
    }


    private static int[] input() {
        int x=-1;
        int y=-1;


        while(x<0 || x>15 ){
            System.out.print("\nX > ");
            try{
                x = scan.nextInt();
                if(x<0 || x>15){
                    System.err.print("X out of range!\n");
                }
            }catch (InputMismatchException e) {
                System.err.println("X must be an integer!");
                scan.nextLine();
            }

        }

        while(y<0 || y>15){
            System.out.print("Y > ");
            try{
                y = scan.nextInt();
                if(y<0 || y>15){
                    System.err.print("Y out of range!\n");
                    System.out.print("\n");
                }
            }catch (InputMismatchException e) {
                System.err.println("Y must be an integer!");
                scan.nextLine();
                System.out.print("\n");
            }
        }

        System.out.print("\n");

        return new int[]{x, y};
    }

    private static void uncover(int[] input) {
        int x = input[0];
        int y = input[1];

        if (!showcell[x][y]){
            showcell[x][y]=true;    //uncover cell

            if (board[x][y] == bomb){
                stop = 1;       //lose

            }else if(board[x][y]==0){
                for (int y1=-1; y1<=1; y1++) {
                    for (int x1 = -1; x1 <= 1; x1++) {
                        if(x+x1>=0 && y+y1>=0 && x+x1<=15 && y+y1<=15){
                            showcell[x+x1][y+y1]=true;    //uncover cell
                        }
                    }
                }
            }
        }
    }

    private static void displayboard(boolean showAll){

        System.out.print("__  ");
        for (int i=0; i<10; i++){
            System.out.print("0"+i+" ");
        }
        for (int i=10; i<16; i++){
            System.out.print(i+" ");
        }
        System.out.print("\n");
        for (int y=0; y<16; y++){
            if(y<10){
                System.out.print("0"+y+"\t");
            }else{
                System.out.print(y+"\t");
            }
            for (int x=0; x<16; x++){
                if(showAll || showcell[x][y]){
                    if(board[x][y]==-1){
                        System.out.print("XX ");
                    }else{
                        System.out.print("0"+board[x][y]+" ");
                    }
                }else{
                    System.out.print("__ ");
                }
            }
            System.out.print("\n");
        }
    }


    private static void gameovercheck() {
        for (int y=0; y<16; y++)
            for (int x=0; x<16; x++)
                if (board[x][y]!=bomb && !showcell[x][y]){ //if no bomb and cell is not uncovered
                    return;
                }
        stop=2;
    }


    private static void gameover(int a) {
        if (a == 2) {
            System.out.print("You win!\n");
        }else{
            System.out.print("You lose!\n");
        }
    }
}