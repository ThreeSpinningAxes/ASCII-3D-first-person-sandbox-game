public class Map {

    private static final char WALL_CHARACTER = '#';

    private static final char EMPTY_CHARACTER = ' ';

    private static final char PLAYER_CHARACTER = 'X';
    public int MAX_MAP_SIZE_X;
    public int MAX_MAP_SIZE_Y;

    public int playerX;

    public int playerY;

    public char[][] MAP;

    public void constructBasicMap() {
        /*
        for (int i = 0; i < MAX_MAP_SIZE_X; i++) {
            MAP[i][0] = WALL_CHARACTER;
            MAP[i][MAX_MAP_SIZE_Y - 1] = WALL_CHARACTER;
            MAP[0][i] = WALL_CHARACTER;
            MAP[MAX_MAP_SIZE_X - 1][i] = WALL_CHARACTER;
        }
        MAP[playerY][playerX] = PLAYER_CHARACTER;

        for (int i = 0; i < MAX_MAP_SIZE_X; i++) {
            for (int j = 0; j < MAX_MAP_SIZE_Y; j++) {
                if (MAP[i][j] == 0)
                    MAP[i][j] = EMPTY_CHARACTER;
            }
        }
*/
        MAP = new char[][]
                {       {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ','#','#','#','#','#',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ','#',' ',' ',' ','#',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ','#',' ','X',' ','#',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ','#',' ',' ',' ','#',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ','#',' ',' ',' ','#',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ','#','#',' ',' ','#',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ',' ','#',' ',' ','#',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ','#','#','#','#',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ','#',' ',' ','#',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ','#',' ',' ','#',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ','#',' ',' ','#',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ','#',' ',' ','#',' ','#'},
                        {'#',' ',' ',' ',' ',' ','#','#','#','#',' ',' ',' ',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#',' ','#'},
                        {'#',' ',' ',' ',' ',' ','#','#','#','#','#','#','#',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                        {'#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#'},
                        {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
                };


    }

    public String toString() {
        StringBuilder map = new StringBuilder();
        for (int i = MAX_MAP_SIZE_Y - 1; i >= 0; i--) {
            for (int j = 0; j < MAX_MAP_SIZE_X; j++) {

                map.append(MAP[j][i]);
                map.append(" ");
            }
            map.append('\n');
        }
        return map.toString();
    }

    public void setMap(char[][] map) {
        this.MAP = map;
    }

    public void setMapString(String mapString) {
        if (this.MAP != null) {
            this.MAP = null;
        }
        String[] splitMapString = mapString.split("\n");
        for (int i = 0; i < splitMapString.length; i++) {
            for (int j = 0; j < splitMapString[i].length(); j++) {

            }
        }
    }

    public char[][] getMap() {
        return this.MAP;
    }

    public Map(int max_map_size_x, int max_map_size_y, int playerX, int playerY) {
        this.MAX_MAP_SIZE_X = max_map_size_x;
        this.MAX_MAP_SIZE_Y = max_map_size_y;
        this.MAP = new char[MAX_MAP_SIZE_X][MAX_MAP_SIZE_Y];
        this.playerX = playerX;
        this.playerY = playerY;
    }

    public static void main(String[] args) throws InterruptedException {
        int x = 16;
        int y = 16;
        Map map = new Map(x, y, 9, 3);
        map.constructBasicMap();
        while (true) {
            System.out.println(map);
            Thread.sleep(1000);
        }
    }
}
