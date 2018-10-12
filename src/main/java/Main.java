import java.util.EnumSet;

public class Main {

	private static int repetitions;

	public static void main(String[] args)
	{
		int[] episodeRanges = {10, 50, 100, 500, 1000, 5000};
		EnumSet<Qlearning.LearningRate> learningRates = EnumSet.of(
				Qlearning.LearningRate.LINEARTWENTY,
				Qlearning.LearningRate.LINEARFIFTY,
				Qlearning.LearningRate.ONEOVERNPLUSN,
				Qlearning.LearningRate.TWOOVERTWOPLUSN
			);


		for(Qlearning.LearningRate learningRate : learningRates){

			switch(learningRate){
				case LINEARFIFTY:
					System.out.println("Current learning rate = 0.5");
					break;
				case LINEARTWENTY:
					System.out.println("Current learning rate = 0.2");
					break;
				case ONEOVERN:
					System.out.println("Current learning rate = 1/n");
					break;
				case ONEOVERNPLUSN:
					System.out.println("Current learning rate = 1/1+n");
					break;
				case TWOOVERTWOPLUSN:
					System.out.println("Current learning rate = 2/2+n");
					break;
			}


			for( int episodes : episodeRanges ){

				repetitions = 1000;

				double[][] allQvalues = new double[repetitions][];

				int[] optimalPol = new int[0];
				for(int i = 0; i < repetitions; i++){
					Qlearning alg = runLearner(episodes, learningRate);
					double[][] QValues = alg.getQValues();
					optimalPol = alg.getOptimalPolicy();

					double[] maxValues = new double[QValues.length];
					for (int j = 0; j < maxValues.length; j++) {
						maxValues[j] = getLargestElement(QValues[j]);
					}

					allQvalues[i] = maxValues;
				}

				double[] averageQvalues = getAverageQvalue(allQvalues);

				System.out.println("\nAverage highest Q-Values for episodes set to: "  + episodes);
				for(double q : averageQvalues){
					System.out.print(q + "\t");
				}

				System.out.println("\nOptimal Policy");
				for(int optPol : optimalPol){
					System.out.println(optPol);
				}
			}
		}

	}

	public static Qlearning runLearner(int maxEpisode, Qlearning.LearningRate learningRate){
		Environment towerOfHanoi=new Environment();
		Qlearning alg = new Qlearning(maxEpisode, towerOfHanoi);
		alg.learn(learningRate);

		return alg;
	}

	public static double getLargestElement(double[] array){
		double max = array[0];
		for(double curr : array){
			if(max < curr) {
				max = curr;
			}
		}
		return max;
	}

	public static double[] getAverageQvalue(double[][] values){
		int size = values.length;

		double[] sum = new double[values[0].length];

		for(int i = 0; i < size; i++){
			for(int j =0; j < sum.length; j++){
				sum[j] += values[i][j];
			}
		}

		for(int i =0; i < sum.length; i++){
			sum[i] = sum[i] / size;
		}

		return sum;

	}
}
