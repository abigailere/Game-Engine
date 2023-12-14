import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class GatorEngine {
    //UI Components (things that are "more" related to the UI)
    static JFrame WINDOW;
    static JPanel DISPLAY_CONTAINER;
    static JLabel DISPLAY_LABEL;
    static BufferedImage DISPLAY;
    static int WIDTH=500, HEIGHT=500;

    //Engine Components (things that are "more" related to the engine structures)
    static Graphics2D RENDERER;
    static ArrayList<GameObject> OBJECTLIST = new ArrayList<>(); //list of GameObjects in the scene
    static ArrayList<GameObject> CREATELIST = new ArrayList<>(); //list of GameObjects to add to OBJECTLIST at the end of the frame
    static ArrayList<GameObject> DELETELIST = new ArrayList<>(); //list of GameObjects to remove from OBJECTLIST at the end fo the frame
    static float FRAMERATE = 60; //target frames per second;
    static float FRAMEDELAY = 1000/FRAMERATE; //target delay between frames
    static Timer FRAMETIMER; //Timer controlling the update loop
    static Thread FRAMETHREAD; //the Thread implementing the update loop
    static Thread ACTIVE_FRAMETHREAD; //a copy of FRAMETHREAD that actually runs.


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CreateEngineWindow();
            }
        });
    }

    static void CreateEngineWindow(){
        //Sets up the GUI
        WINDOW = new JFrame("Gator Engine");
        WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WINDOW.setVisible(true);

        DISPLAY = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
        RENDERER = (Graphics2D) DISPLAY.getGraphics();
        DISPLAY_CONTAINER = new JPanel();
        DISPLAY_CONTAINER.setFocusable(true);
        DISPLAY_LABEL = new JLabel(new ImageIcon(DISPLAY));
        DISPLAY_CONTAINER.add(DISPLAY_LABEL);
        WINDOW.add(DISPLAY_CONTAINER);
        WINDOW.pack();

        FRAMETHREAD = new Thread(new Runnable() {
            @Override
            public void run() {
                Update();
                Input.UpdateInputs();
                UpdateObjectList();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        WINDOW.repaint();
                    }
                });
            }
        });


        //This copies the template thread made above
        ACTIVE_FRAMETHREAD = new Thread(FRAMETHREAD);

        FRAMETIMER = new Timer((int)FRAMEDELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!ACTIVE_FRAMETHREAD.isAlive()){
                    ACTIVE_FRAMETHREAD = FRAMETHREAD;
                    ACTIVE_FRAMETHREAD.run();

                }
                FRAMETIMER.restart();
            }
        });
        FRAMETIMER.start();

        Start();

        //===================INPUT=========================
        //Set up some action listeners for input on the PANEL
        //These should update the Input classes ArrayLists and other members
        DISPLAY_CONTAINER.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                Input.pressed.add(e.getKeyChar());
                Input.held.add(e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {

                Input.released.add(e.getKeyChar());
            }
        });
        DISPLAY_CONTAINER.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Input.MouseClicked = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {

                Input.MousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Input.MousePressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        DISPLAY_CONTAINER.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Input.MouseX = e.getX() - WINDOW.getX();
                Input.MouseY = e.getY() - WINDOW.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Input.MouseX = e.getX() - WINDOW.getX();
                Input.MouseY = e.getY() - WINDOW.getY();
            }
        });
    }

    static void Create(GameObject g){
        OBJECTLIST.add(g);
        g.Start();
    }

    static void Delete(GameObject g){

        DELETELIST.add(g);
    }

    static void UpdateObjectList(){
        for(int i = 0; i< CREATELIST.size(); i++){
            OBJECTLIST.add(CREATELIST.get(i));
        }
        for(int i = 0; i< DELETELIST.size(); i++){
            OBJECTLIST.remove(DELETELIST.get(i));
        }
        CREATELIST.clear();
        DELETELIST.clear();
    }



    //This begins the "user-side" of the software; above should set up the engine loop, data, etc.
    //Here you can create GameObjects, assign scripts, set parameters, etc.
    //NOTE: This is where we should be able to insert out own code and scripts
    static void Start(){
        for(int i = 0; i< OBJECTLIST.size(); i++){
            OBJECTLIST.get(i).Start();
        }

    }

    //Redraw the Background(), then Draw() and Update() all GameObjects in OBJECTLIST
    static void Update(){
        Background();
        // Project5.Start();
        for(int i = 0; i< OBJECTLIST.size(); i++){
            OBJECTLIST.get(i).Draw(RENDERER);
            OBJECTLIST.get(i).Update();
        }
    }

    //draws a background on the Renderer. right now it is solid, but we could load an image
    static void Background(){
        RENDERER.setColor(Color.WHITE);
        RENDERER.fillRect(0,0,WIDTH,HEIGHT);
    }

}
