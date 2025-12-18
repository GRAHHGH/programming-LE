package main;

public class secretfile {

    public static final int TYPING_SPEED_MS = 50;

    public static final String YES = "\nBefore we start the game, I want you take a moment sir and read all the things we want to say for you sir!\n"; 
    public static final String jaryl = 
    "\nFrom Jaryl: Hi Sir Tupas, Thank you very muchieee for not only teaching us the concepts of Programming \n" + 
    "but for giving us the essential foundational knowledge and diverse ways of thinking needed for our future\n" +
    "success in programming! Lahams ka namin Sir Korean! \n";
    public static final String gerose = 
    "\nFrom Gerose: hi sir tupas! not to be cringe, but i'm vv thankful that youre our prof in programming paradigm. \n" +
    "(hoping youll be our prof in PP2 too!) i came from CT, and even though we had programming there, i honestly\n" +
    "finished that subject with high grades but without actually knowing how to code. thats why i'm grateful now,\n" +
    "because for the first time, i can actually code, even if its just in SCRATCH! we luv u sir \n" ;
    public static final String francis = 
    "\nFrom francis: Hi, hello, halo Sir Tupas, thank you for the teaching and for making a engaging atmosphere for us that encourages us.\n" +
    "to learn more and letting us grow with challenges you've made for us, thank very much again sir.\n\n";
    public static final String jd = 
    "From JD: Hi sir, i just want you to say na thank you sa lahat lahat talaga sir!! because of you\n" +
    "I started this project 3 months ago sooo i hope you enjoy this game sir!!!!\n\n";


    public static String getMessage1(){
        return YES;
    }

    public static String getMessage2(){
        return jaryl;
    }

    public static String getMessage3(){
        return gerose;
    }

    public static String getMessage4(){
        return francis;
    }

    public static String getMessage5(){
        return jd;
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