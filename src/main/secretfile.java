package main;

public class secretfile {

    public static final int TYPING_SPEED_MS = 50;

    public static final String YES = "\nBefore we start the game, I want you take a moment sir and read all the things we want to say for you sir!\n"; 
    public static final String jaryl = 
    "\nFrom Jaryl: Hi Sir Tupas, Thank you very muchieee for not only teaching us the concepts of Programming \n" + 
    "but for giving us the essential foundational knowledge and diverse ways of thinking needed for our future\n" +
    "success in programming! Lahams ka namin Sir Korean! \n\n\n";

    public static String getMessage1(){
        return YES;
    }

    public static String getMessage2(){
        return jaryl;
    }

    public static void typeMessage(String message) {
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try {
                // Pause for the defined duration
                Thread.sleep(TYPING_SPEED_MS);
            } catch (InterruptedException e) {
                // Restore the interrupted status 
                Thread.currentThread().interrupt();
                break; // Exit the loop if interrupted
            }
        }
    }
}