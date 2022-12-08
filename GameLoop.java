import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class GameLoop extends JFrame implements KeyListener, MouseMotionListener {

    TextArea visualMap;

    private Map map;

    private final Set<Integer> keysPressed = new HashSet<>();

    public Screen screen;

    private Point mousePosition = new Point(0, 0);

    double fps = 12000000.0;

    long framesPerMilli = (long) (1000 / fps);

    public GameLoop(String windowName, int WIDTH, int HEIGHT, Map map) {
        super(windowName);
        this.setPreferredSize(new Dimension(1200, 600));
        this.setSize(0, 0);
        //this.setLayout();
        this.setLocationRelativeTo(null);
        this.map = map;
        visualMap = new TextArea(40,121);

        visualMap.setEditable(false);
        visualMap.setFocusable(false);
        visualMap.addKeyListener(this);
        visualMap.addMouseMotionListener(this);
        visualMap.setBackground(Color.BLACK);
        visualMap.setForeground(Color.WHITE);

        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(visualMap);


        addKeyListener(this);
        addMouseMotionListener(this);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //keyEventHandler(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyEventHandler(e);
    }

    public synchronized void keyEventHandler(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        keysPressed.remove(e.getKeyCode());
    }

    private void performMovementsIfKeysArePressed() {
        if (keysPressed.contains(KeyEvent.VK_W)) {
            this.screen.updatePlayerPosition(KeyEvent.VK_W);
        }
        if (keysPressed.contains(KeyEvent.VK_A)) {
            this.screen.updatePlayerPosition(KeyEvent.VK_A);
        }
        if (keysPressed.contains(KeyEvent.VK_S)) {
            this.screen.updatePlayerPosition(KeyEvent.VK_S);
        }
        if (keysPressed.contains(KeyEvent.VK_D)) {
            this.screen.updatePlayerPosition(KeyEvent.VK_D);
        }
        if (keysPressed.contains(KeyEvent.VK_Q)) {
            this.screen.rotateCCW();
        }
        if (keysPressed.contains(KeyEvent.VK_E)) {
            this.screen.rotateCW();
        }
        if (keysPressed.contains(KeyEvent.VK_UP)) {
            this.screen.lookUp();
        }
        if (keysPressed.contains(KeyEvent.VK_DOWN)) {
            this.screen.lookDown();
        }
        if (keysPressed.contains(KeyEvent.VK_SPACE)) {
            this.screen.jump();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        int width = 120;
        int height = 40;
        Map map = new Map(24, 15, 5, 5);
        map.constructBasicMap();
        GameLoop m = new GameLoop("Key Listener Tester", 1000, 1000, map);
        Screen screen = new Screen(map, width, height);
        m.screen = screen;
        boolean x = true;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        while (x) {
            long startTimeMilli = System.currentTimeMillis();
            m.performMovementsIfKeysArePressed();
            screen.paintScreen();
            out.write( "x: " + (int) m.screen.playerXPos + ", y: " + (int) m.screen.playerYPos + '\n' + map);
            out.write(screen.toString());
            out.flush();
            long endTime = System.currentTimeMillis() - startTimeMilli;
            if (endTime < m.framesPerMilli) {
                Thread.sleep(m.framesPerMilli - endTime);
            }
            //x = false;
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //if mouse does not move
        try {
            //keep y the same and x is dragged past TL corner
            if (this.mousePosition.x == 0 && this.mousePosition.y == e.getYOnScreen()) this.screen.rotateCCW();
            if (this.mousePosition.x == e.getComponent().getWidth() && this.mousePosition.y == e.getYOnScreen()) this.screen.rotateCW();


            if (e.getXOnScreen()> this.mousePosition.x) this.screen.rotateCW();
            if (e.getXOnScreen() < this.mousePosition.x) this.screen.rotateCCW();
            if (e.getYOnScreen() > this.mousePosition.y) this.screen.lookDown();
            if (e.getYOnScreen() < this.mousePosition.y) this.screen.lookUp();
            this.mousePosition = e.getLocationOnScreen();
        }
        catch (Exception b) {
            visualMap.setText(b.getMessage());
            //System.exit(2);
        }

    }
}