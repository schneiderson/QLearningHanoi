import java.util.*;

public class Qlearning {
    private int[] optimalPolicy;
    private double[][] QValues;
    private int[][] occurrence;
    private Environment towersOfHanoi;
    private double maxEpisode;

    public Qlearning(int maxEpisode, Environment environment) {
        towersOfHanoi = new Environment();
        optimalPolicy = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        if (maxEpisode <= 1) {
            this.maxEpisode = 100;
            System.out.println("Invalid parameter. The number of iterations have been set to the default value.");
        } else
            this.maxEpisode = maxEpisode;
		/*
		We consider every state-action combination to update the table by vector indexing.
		The invalid actions are set to 0 along with the valid ones. 
		We can use the function getAvailableActions
		to get the valid actions for each state.
		*/
        QValues = new double[12][6];
        occurrence = new int[12][6];
        fillRowsWithOnes(occurrence, occurrence.length - 1, occurrence[0].length);
    }

    private static void fillRowsWithOnes(int[][] a, int rows, int cols) {
        if (rows >= 0) {
            int[] row = new int[cols];
            Arrays.fill(row, 1);
            a[rows] = row;
            fillRowsWithOnes(a, rows - 1, cols);
        }
    }

    public void learn(double omega) {
        int episodeNum = 1;
        boolean explore = false;
        int action;

        ArrayList<ArrayList<Integer>> performedActions = getInitialPerformed();


        while (maxEpisode > episodeNum) {
            int state = towersOfHanoi.getCurrentState();
            int[] availableActions = towersOfHanoi.getAvailableActions(state);

            // We ensure exploration by choosing random action in the random state
            if (explore) {
                //choose a random action
                action = chooseAction(availableActions);
                explore = false;
            }
            //After the random state we use exploitation
            else {
                //choose the best action according to the current Q values
                action = bestAction(state, performedActions.get(state));
            }
            Environment.observation obs = towersOfHanoi.execute(action);
            // save performed action
            if(!performedActions.get(state).contains(action)) performedActions.get(state).add(action);

            //terminal state
            double NextQMax = 0;
            if (obs.newState == 4) {
                episodeNum++;
                towersOfHanoi.setRandomState();
                explore = true;

                // reset performed actions
                performedActions = getInitialPerformed();
            } else
                NextQMax = maxQ(obs.newState);
            //update the Q value
            double change = obs.reward + 0.9 * NextQMax - QValues[state][action];
            QValues[state][action] += 1.0 / Math.pow(occurrence[state][action], omega) * change;
            occurrence[state][action]++;
        }
        updateOptimalPolicy();
    }

    private ArrayList<ArrayList<Integer>> getInitialPerformed(){
        ArrayList<ArrayList<Integer>> performedActions = new ArrayList<>();
        for(int i = 0; i < 12; i ++){
            performedActions.add(i, new ArrayList<Integer>());
        }
        return performedActions;
    }

    private void updateOptimalPolicy() {
        for (int state = 0; state < 12; state++) {
            //we do not update the best action for the terminal state
            if (state == 4)
                continue;
            this.optimalPolicy[state] = bestAction(state, Collections.emptyList() );
        }
    }

    private int bestAction(int state, List<Integer> excludeActions) {
        int[] actions = this.towersOfHanoi.getAvailableActions(state);
        double maxValue = -Double.MAX_VALUE;
        Vector<Integer> bestActions = new Vector<>();
        for (int action : actions) {
            if ((!excludeActions.contains(action) || actions.length == excludeActions.size()) && QValues[state][action] > maxValue) {
                maxValue = QValues[state][action];
            }
        }
        for (int action : actions) {
            if (QValues[state][action] == maxValue)
                bestActions.add(action);
        }

        //We choose randomly from the actions with the same Q value
        //This is important to avoid infinite loops
        return chooseAction(bestActions);
    }

    private int chooseAction(int[] availableActions) {
        int rnd = new Random().nextInt(availableActions.length);
        return availableActions[rnd];
    }

    private int chooseAction(Vector<Integer> availableActions) {
        int rnd = new Random().nextInt(availableActions.size());
        return availableActions.get(rnd);
    }

    private double maxQ(int state) {
        int[] actions = this.towersOfHanoi.getAvailableActions(state);
        double maxValue = -Double.MAX_VALUE;
        for (int action : actions) {
            if (QValues[state][action] > maxValue)
                maxValue = QValues[state][action];
        }
        return maxValue;
    }

    public int[] getOptimalPolicy() {
        return optimalPolicy;
    }

    public double[][] getQValues() {
        return QValues;
    }
}
