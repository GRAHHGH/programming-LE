package main;
import java.util.Scanner;
public class MainGame {
    public static void main(String[] args){ 
        Scanner key = new Scanner(System.in);

    char yn = 0;
    System.out.print("Do you want to play this game? [y/n]: ");
    yn = key.next().charAt(0);

    if(yn == 'y' || yn == 'Y'){
        new Game(); // to access contructor which is the game.java
    }
    else{
        System.out.println("Okay Bye!");
    }

    key.close();
    }
}
