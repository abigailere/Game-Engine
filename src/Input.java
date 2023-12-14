import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Input {
    static public ArrayList<Character> pressed = new ArrayList<>(); //contains characters that were just pressed this frame
    static public ArrayList<Character> held = new ArrayList<>(); //contains characters that were either just pressed this frame, or have been down for any number of previous frames
    static public ArrayList<Character> released = new ArrayList<>(); //contains characters that were just released this frame

    public static int MouseX; //the current mouse X position on the DISPLAY BufferedImage
    public static int MouseY; //the current mouse Y position on the DISPLAY BufferedImage
    public static boolean MousePressed; //true if the mouse is currently down
    public static boolean MouseClicked; //true if the mouse was clicked this frame

    static void UpdateInputs(){
        held.removeAll(released);
        released.clear();
        pressed.clear();
    }

    //return true if c is in the released list
    static boolean GetKeyPressed(char c){
        return pressed.contains(c);
    }

    //return true if c is in the held list
    static boolean GetKeyDown(char c){
        return held.contains(c);
    }

    //return true if c is in the released list
    static boolean GetKeyUp(char c){
        return released.contains(c);
    }
}
