import java.util.Scanner;

public class Board {
    private int[][] board = new int[8][8];
    Scanner scanner = new Scanner(System.in);

    Board() {
        board[3][3] = Stone.WHITE_STONE.ordinal();
        board[4][4] = Stone.WHITE_STONE.ordinal();
        board[3][4] = Stone.BLACK_STONE.ordinal();
        board[4][3] = Stone.BLACK_STONE.ordinal();
    }

    public void startGame() {
        GamePlay turn = GamePlay.WHITE_TURN;
        System.out.println("오델로 시작");
        while (true) {
            makeStar(turn);
            // 만약 생성된 star가 0개면 밑에 생략하고 다음 턴으로 변경
            // 변경했는데도 star가 0개면 게임 종료
            printBoard();
            System.out.print("어디 둘지 입력 x, y좌표 ex) 2, 4 : ");
            String[] str = scanner.nextLine().replaceAll(" ", "").split(",");
            int x, y;
            try {
                x = Integer.parseInt(str[0]);
                y = Integer.parseInt(str[1]);
                if (str.length != 2 || 0 > x || 8 < x
                        || 0 > y || 8 < y) {
                    System.out.println("잘못 입력하였습니다.");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("숫자를 입력해주세요.");
                continue;
            }
            if (reverseStone(x, y, turn))
                removeStar();
            turn = (turn == GamePlay.WHITE_TURN) ? GamePlay.BLACK_TURN : GamePlay.WHITE_TURN;
        }
        // 결산 함수 실행
    }

    private void printBoard() {
        for (int j = 0; j < board.length; j++) {
            for (int i = 0; i < board[j].length; i++) {
                if (board[i][j] == Stone.WHITE_STONE.ordinal()) {
                    System.out.print("○ ");
                } else if (board[i][j] == Stone.BLACK_STONE.ordinal()) {
                    System.out.print("● ");
                } else if (board[i][j] == Stone.BLACK_STAR.ordinal()) {
                    System.out.print("▲ ");
                } else if (board[i][j] == Stone.WHITE_STAR.ordinal()) {
                    System.out.print("▼ ");
                } else {
                    System.out.print("□ ");
                }
            }
            System.out.println();
        }
    }

    private Boolean makeStar(GamePlay turn) {
        Boolean result = false;
        String checkedStar = "";

        Stone targetStone = (turn == GamePlay.BLACK_TURN) ? Stone.BLACK_STONE : Stone.WHITE_STONE;

        for (int j = 0; j < board.length; j++) {
            for (int i = 0; i < board[j].length; i++) {
                if (board[i][j] == targetStone.ordinal()) {
                    checkedStar = starChecks(i, j, Stone.EMPTY);
                    if (checkedStar != "") {
                        String[] temp_str = checkedStar.split("/");
                        for (String str : temp_str) {
                            int x = Integer.parseInt(str.split(",")[0]);
                            int y = Integer.parseInt(str.split(",")[1]);
                            board[x][y] = (turn == GamePlay.BLACK_TURN) ? Stone.BLACK_STAR.ordinal()
                                    : Stone.WHITE_STAR.ordinal();
                        }
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    private void removeStar() {
        for (int j = 0; j < board.length; j++) {
            for (int i = 0; i < board[j].length; i++) {
                if (board[i][j] == Stone.BLACK_STAR.ordinal() || board[i][j] == Stone.WHITE_STAR.ordinal()) {
                    board[i][j] = Stone.EMPTY.ordinal();
                }
            }
        }
    }

    private Boolean reverseStone(int x, int y, GamePlay turn) {
        Stone currentStar = (turn == GamePlay.BLACK_TURN) ? Stone.BLACK_STAR : Stone.WHITE_STAR;
        Stone currentStone = (turn == GamePlay.BLACK_TURN) ? Stone.BLACK_STONE : Stone.WHITE_STONE;
        if (board[x][y] != currentStar.ordinal()) {
            return false;
        }
        board[x][y] = currentStone.ordinal();
        String[] string = starChecks(x, y, currentStone).split("/");
        for (String str : string) {
            int temp_x = Integer.parseInt(str.split(",")[0]);
            int temp_y = Integer.parseInt(str.split(",")[1]);
            while (temp_x != x || temp_y != y) {
                if (temp_x < x) {
                    temp_x++;
                } else if (temp_x > x) {
                    temp_x--;
                }
                if (temp_y < y) {
                    temp_y++;
                } else if (temp_y > y) {
                    temp_y--;
                }
                board[temp_x][temp_y] = currentStone.ordinal();
            }
        }

        return true;
    }

    // x, y값을 받는다
    // star위치 표시시에는 targetStone에 EMPTY, reverse위치 표시시에는 targetStone에 currentStone 입력
    // 그 위치의 돌에서 뒤집기가 가능한 돌들 위치를 String값으로 리턴 함
    // 출력예시) 3,4/4,5/6,7/3,7/
    private String starChecks(int x, int y, Stone targetStone) {
        String result = "";
        int currentStone = (board[x][y] == Stone.BLACK_STONE.ordinal()) ? Stone.WHITE_STONE.ordinal()
                : Stone.BLACK_STONE.ordinal();
        for (int i = 0; i < 8; i++) {
            int temp_x = x;
            int temp_y = y;
            int vector_x = 0;
            int vector_y = 0;
            if ((((i + 2) % 8) - 4) % 4 > 0) {
                vector_x = 1;
            } else if ((((i + 2) % 8) - 4) % 4 < 0) {
                vector_x = -1;
            }
            if ((4 - i) % 4 > 0) {
                vector_y = 1;
            } else if ((4 - i) % 4 < 0) {
                vector_y = -1;
            }

            Boolean flag = false;
            while (temp_x > 0 && temp_x < 7 && temp_y > 0 && temp_y < 7) {
                temp_x += vector_x;
                temp_y += vector_y;
                if (board[temp_x][temp_y] == currentStone) {
                    flag = true;
                } else if (flag && board[temp_x][temp_y] == targetStone.ordinal()) {
                    result += temp_x + "," + temp_y + "/";
                    break;
                } else {
                    break;
                }
            }
        }

        return result;
    }
}
