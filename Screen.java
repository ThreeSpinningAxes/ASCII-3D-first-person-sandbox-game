import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class Screen {

    //the pixels accumulated for the perspective image

    private int widthColumns;

    private int heightRows;

    //the screen that projects the perspective image
    private char[][] screen;

    private char[][] visualMap;

    private Map map;

    public double playerXPos = 0.0;

    public double playerYPos = 0.0;

    public double movementSpeed = 0.005;

    public double jumpHeight = 2.0;

    public AtomicBoolean isJumping = new AtomicBoolean(false);

    public AtomicBoolean isFalling = new AtomicBoolean(false);

    public double povHorizontalAngle = 0.0;

    public double povVerticalAngle = 0.0;


    private double fov = Math.PI / 4; //90 degrees

    private double maxRayDistance = 8.0;

    public double playerHeight = 1.0;

    public double wallHeight = 1;

    private final char WALL_CHARACTER = '#';

    private final char EMPTY_CHARACTER = ' ';

    private final char FLOOR_CHARACTER = '.';

    private static final char PLAYER_CHARACTER = 'X';

    public double horizontalSens = 0.005;

    public double verticalSens = 0.005;

    public Screen(Map map, int widthColumns, int heightRows) {
        this.map = map;
        this.visualMap = map.getMap();
        this.widthColumns = widthColumns;
        this.heightRows = heightRows;
        this.screen = new char[widthColumns][heightRows];
        for (int i = 0; i < widthColumns; i++) {
            for (int j = 0; j < heightRows; j++) {
                screen[i][j] = 'b';
            }
        }
        setPlayerPosition();
    }

    public void setPlayerPosition() {
        this.playerXPos = map.playerX;
        this.playerYPos = map.playerY;
    }


    public void rotateCW() {
        this.povHorizontalAngle += this.horizontalSens;
    }

    public void rotateCCW() {
        this.povHorizontalAngle -= this.horizontalSens;
    }

    public void lookUp() {
        //minus since 0,0 starts from top left
        if (povVerticalAngle > -Math.PI / 2)
            this.povVerticalAngle -= this.verticalSens;
    }

    public void lookDown() {
        if (povVerticalAngle < Math.PI / 2)
            this.povVerticalAngle += this.verticalSens;
    }

    public void jump() {
        if (isJumping.get()) return;
        isJumping.set(true);
    }

    public void jumpPhysics() {
        double increment = 0.005;
        if (isJumping.get()) {
            if (!isFalling.get() && playerHeight < jumpHeight) {
                playerHeight += increment;
                increment *= 0.05;
            } else {
                isFalling.set(true);
                playerHeight -= increment;
                increment *= 1.05;
                if (playerHeight <= 1.0) {
                    playerHeight = 1.0;
                    isJumping.set(false);
                    isFalling.set(false);
                }
            }
        }
    }

    public void updatePlayerPosition(int keyCode) {

        double tempX = playerXPos, tempY = playerYPos;

        //left
        if (keyCode == KeyEvent.VK_A) {
            tempX = playerXPos - movementSpeed * Math.cos(povHorizontalAngle);
            tempY = playerYPos + movementSpeed * Math.sin(povHorizontalAngle);
            //right
        } if (keyCode == KeyEvent.VK_D) {
            tempX = playerXPos + movementSpeed * Math.cos(povHorizontalAngle);
            tempY = playerYPos - movementSpeed * Math.sin(povHorizontalAngle);
            //down
        } if (keyCode == KeyEvent.VK_S) {
            tempX = playerXPos - movementSpeed * Math.sin(povHorizontalAngle);
            tempY = playerYPos - movementSpeed * Math.cos(povHorizontalAngle);
            //up
        } if (keyCode == KeyEvent.VK_W) {
            tempX = playerXPos + movementSpeed * Math.sin(povHorizontalAngle);
            tempY = playerYPos + movementSpeed * Math.cos(povHorizontalAngle);
        }

        if (tempX < 0 || tempX >= this.map.MAX_MAP_SIZE_X || tempY < 0 || tempY > this.map.MAX_MAP_SIZE_Y || this.map.MAP[(int) tempX][(int) tempY] == WALL_CHARACTER) {
            return;
        }
        this.map.MAP[(int) playerXPos][(int) playerYPos] = EMPTY_CHARACTER;
        this.map.MAP[(int) tempX][(int) tempY] = PLAYER_CHARACTER;
        playerXPos = tempX;
        playerYPos = tempY;
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < heightRows; i++) {
            for (int j = 0; j < widthColumns; j++) {
                builder.append(screen[j][i]);
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public void paintScreen() {

        jumpPhysics();
        for (int colNumber = 0; colNumber < widthColumns; colNumber++) {
            // if ray goes over wall
            boolean collidesWithWall = false;
            //calculate angle of ray vector
            double angleOfRayForCurrentColumn = ((povHorizontalAngle - (fov / 2.0)) + ((double) colNumber / (double) widthColumns) * fov);

            //unit vectors of current ray
            double rayUnitVectorX = Math.sin(angleOfRayForCurrentColumn);
            double rayUnitVectorY = Math.cos(angleOfRayForCurrentColumn);

            //distance ray has travelled so far
            double rayDistance = 0.0;

            while (!collidesWithWall && rayDistance < maxRayDistance) {

                rayDistance += 0.1;

                int rayVectorX = (int) (playerXPos + (rayUnitVectorX * rayDistance));
                int rayVectorY = (int) (playerYPos + (rayUnitVectorY * rayDistance));

                if (visualMap[rayVectorX][rayVectorY] == WALL_CHARACTER) {
                    collidesWithWall = true;
                }
                else if (rayVectorX < 0 || rayVectorX >= map.MAX_MAP_SIZE_X || rayVectorY < 0 || rayVectorY >= map.MAX_MAP_SIZE_Y) {
                    //rayDistance = maxRayDistance;
                    break;
                }

            }
            char shade = FLOOR_CHARACTER;

            for (int rowNumber = 0; rowNumber < heightRows; rowNumber++) {
                double angleOfRayForCurrentRow = (povVerticalAngle - fov / 2.0) + (fov / heightRows) * (double) rowNumber;
                //then ray can hit floor or wall
                double rayHeightFromPlayerHeight = playerHeight - (rayDistance * Math.sin(angleOfRayForCurrentRow));
                if (angleOfRayForCurrentColumn < 0) {
                    angleOfRayForCurrentColumn *= -1;
                }
                double rayLengthDistanceFromPlayer = playerHeight / Math.sin(angleOfRayForCurrentRow);

                if (rayHeightFromPlayerHeight > wallHeight) {
                    screen[colNumber][rowNumber] = EMPTY_CHARACTER;
                } else if (rayHeightFromPlayerHeight < 0) {
                    if (rayLengthDistanceFromPlayer < 0.1 * maxRayDistance) shade = '0';
                    else if (rayLengthDistanceFromPlayer < 0.25 * maxRayDistance) shade = '-';
                    else if (rayLengthDistanceFromPlayer < 0.5 * maxRayDistance) shade = ',';
                    else if (rayLengthDistanceFromPlayer < 0.75 * maxRayDistance) shade = '.';
                    else if (rayLengthDistanceFromPlayer < 0.90 * maxRayDistance) shade = ' ';
                    screen[colNumber][rowNumber] = shade;
                } else {
                            /*
                            if (rayDistance < 0.25 * maxRayDistance) shade = 0x2591;
                            else if (rayDistance < 0.5 * maxRayDistance) shade = 0x2591;
                            else if (rayDistance < 0.75 * maxRayDistance) shade = 0x2593;
                            else if (rayDistance < 0.90 * maxRayDistance) shade = ' ';
                            */
                    if (collidesWithWall)
                        screen[colNumber][rowNumber] = WALL_CHARACTER;
                    else screen[colNumber][rowNumber] = EMPTY_CHARACTER;
                }
            }


        }


    }

    class JumpThread extends Thread {
        public void run() {
            boolean isFalling = false;

            while (isJumping.get()) {
                if (!isFalling && playerHeight < jumpHeight) {
                    playerHeight += 0.00000001;
                } else {
                    isFalling = true;
                    playerHeight -= 0.00000001;
                    if (playerHeight <= 1.0) {
                        playerHeight = 1.0;
                        isJumping.set(false);
                        return;
                    }
                }
            }
        }
    }}

                /*
                int ceilingHeight = (int) ((heightRows / 2.0) - heightRows / rayDistance);
                int floorHeight = heightRows - ceilingHeight;
                for (int rowNumber = 0; rowNumber < heightRows; rowNumber++) {
                    //since (0,0) starts at top left, the ceiling starts at 0
                    if (rowNumber <= ceilingHeight ) {
                        screen[colNumber][rowNumber] = EMPTY_CHARACTER;
                    }
                    else if (rowNumber > floorHeight) {
                        screen[colNumber][rowNumber] = FLOOR_CHARACTER;
                    }
                    else {
                        screen[colNumber][rowNumber] = WALL_CHARACTER;
                    }
                }
                */