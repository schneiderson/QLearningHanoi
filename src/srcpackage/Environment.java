package srcpackage;

import java.util.Random;

public class Environment {

    private int currentState;
    private int[][] rewardTable;
    private int[][] transitionTable;
    private int[][] availableActions;

    public Environment() {
        InitTables();
        //initialize the first state randomly
        this.currentState = getRandomState();
    }

    public void setRandomState() {
        this.currentState = getRandomState();
    }

    public int getRandomState() {
        //we do not use the terminal state for initialization
        int[] startingStates = new int[]{0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11};
        int rnd = new Random().nextInt(startingStates.length);
        return startingStates[rnd];
    }

    public class observation {
        public int reward;
        public int newState;
    }

    public observation execute(int action) {
        observation result = new observation();
        //0.9 probability for expected and 0.1 probability for the mistake state
        int takenAction = (new Random().nextInt(10) % 10 == 0) ? MistakeAction(this.currentState, action) : action;
        result.newState = this.transitionTable[this.currentState][takenAction] - 1;
        result.reward = this.rewardTable[this.currentState][takenAction];
        this.currentState = result.newState;
        return result;
    }

    private int MistakeAction(int state, int action) {
        int offset;
        offset = action < 3 ? 0 : 3;
        int result = 0;
        for (int i = offset; i < offset + 3; i++) {
            if (i != action && transitionTable[state][i] != 0) {
                result = i;
            }
        }
        return result;
    }

    private void InitTables() {
        this.rewardTable = new int[][]{
                {0, 0, 0, 0, -1, -1},
                {0, -1, -1, 0, 0, 0},
                {0, 0, 0, -1, 0, -1},
                {-1, 0, -1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {-1, -1, 0, 0, 0, 0},
                {0, -11, -1, -1, 0, -1},
                {0, -1, -11, -1, -1, 0},
                {-11, 0, -1, 0, -1, -1},
                {-1, 0, -11, -1, -1, 0},
                {-11, -1, 0, 0, -1, 100},
                {-1, -11, 0, -1, 0, 100}};

        this.transitionTable = new int[][]{
                {0, 0, 0, 0, 7, 8},
                {0, 9, 11, 0, 0, 0},
                {0, 0, 0, 9, 0, 10},
                {7, 0, 12, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {8, 10, 0, 0, 0, 0},
                {0, 4, 12, 1, 0, 8},
                {0, 10, 6, 1, 7, 0},
                {2, 0, 11, 0, 3, 10},
                {8, 0, 6, 9, 3, 0},
                {2, 9, 0, 0, 12, 5},
                {7, 4, 0, 11, 0, 5}};

        this.availableActions = new int[][]{
                {4, 5},
                {1, 2},
                {3, 5},
                {0, 2},
                {-1, -1},
                {0, 1},
                {1, 2, 3, 5},
                {1, 2, 3, 4},
                {0, 2, 4, 5},
                {0, 2, 3, 4},
                {0, 1, 4, 5},
                {0, 1, 3, 5}};
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public int[] getAvailableActions(int state) {
        return this.availableActions[state];
    }
}

