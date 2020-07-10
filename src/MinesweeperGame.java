import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score;



    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
                setCellValue(x, y, "");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
        //isGameStopped = false;

    }


    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void countMineNeighbors(){
        List<GameObject> result;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x].isMine == false) {
                    //System.out.println("HI pidarok");
                    result = getNeighbors(gameField[y][x]);
                    for (GameObject z : result) {
                        if (z.isMine == true) {
                            gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
                /*
                if (gameField[y][x].isMine == true) {
                    System.out.println("количество мин для ячеек с минами " + gameField[y][x].countMineNeighbors);
                }
                */
            }
        }

    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        
        if (isGameStopped == true) {
            restart();
        } else {
            openTile(x, y);
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    private void openTile(int x, int y) {
        if (isGameStopped == true || gameField[y][x].isFlag || gameField[y][x].isOpen == true) {

        } else {
            gameField[y][x].isOpen = true;
            countClosedTiles--;
            setCellColor(x, y, Color.GREEN);
                if (gameField[y][x].isOpen && !gameField[y][x].isMine) {
                    score +=5;
                }
            setScore(score);
                
            if (gameField[y][x].isMine == true) {
                setCellValue(x, y, MINE);
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            } else {
                if (gameField[y][x].countMineNeighbors == 0) {
                    setCellValue(x, y, "");

                    if (gameField[y][x].countMineNeighbors == 0 && !(gameField[y][x].isMine == true)) {
                        List<GameObject> result = getNeighbors(gameField[y][x]);
                        for (int ind = 0; ind < result.size(); ind++) {
                            if (!result.get(ind).isOpen) {
                                int y1 = result.get(ind).y;
                                int x1 = result.get(ind).x;
                                openTile(x1, y1);
                            }
                        }

                    }

                } else {
                    setCellNumber(x, y, gameField[y][x].countMineNeighbors);
                }
            }
            if (countClosedTiles == countMinesOnField && !(gameField[y][x].isMine == true)){
                win();
            }
        }

    }

    private void markTile(int x, int y) {
        if (isGameStopped == true) {
        } else {
            if (gameField[y][x].isOpen) {
            } else if (countFlags == 0 && !gameField[y][x].isFlag) {
            } else {
                if (!gameField[y][x].isFlag) {
                    gameField[y][x].isFlag = true;
                    countFlags = countFlags - 1;
                    setCellValue(x, y, FLAG);
                    setCellColor(x, y, Color.YELLOW);
                } else {
                    gameField[y][x].isFlag = false;
                    countFlags = countFlags + 1;
                    setCellValue(x, y, "");
                    setCellColor(x, y, Color.ORANGE);
                }
            }
        }
    }

    private void  gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.SILVER, "You lose", Color.BLACK, 23);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.SILVER, "You WIN chmo pozornoe!", Color.BLACK, 23);
    }

    private void restart() {
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        countMinesOnField = 0;
        setScore(score);
        createGame();




    }

}