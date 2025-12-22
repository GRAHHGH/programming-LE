package main;
import java.util.Scanner;
import static main.secretfile.typeMessage;

public class MainGame { // MainGame is the starting class that contains the main method to run the application.

    public static void main(String[] args){ 
        Scanner key = new Scanner(System.in);

    char yn = 0;
    System.out.print("Do you want to play this game? [y/n]: ");
    yn = key.next().charAt(0);

    if(yn == 'y' || yn == 'Y'){
        // typeMessage(secretfile.YES); // after you done reading u can comment this all
        // typeMessage(secretfile.jaryl);
        // typeMessage(secretfile.gerose);
        // typeMessage(secretfile.francis);
        // typeMessage(secretfile.jd);
        new Game(); // to access contructor which is the game.java
    }
    else{
        System.out.println("Okay Bye!");
    }

    key.close();
    }


}
