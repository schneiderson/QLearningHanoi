public class Main {

	private static int repetitions;

	public static void main(String[] args)
	{
		int[] episodeRanges = {10, 50, 100, 500, 1000, 5000};
		double[] omegas = {0.5, .7, .9, 1.0};

		for( double omega : omegas){

			System.out.println("Current Omega value: " + omega);

			for( int episodes : episodeRanges){

				repetitions = 10;

				double[][] allQvalues = new double[repetitions][];

				int[] optimalPol = new int[0];
				for(int i = 0; i < repetitions; i++){
					Qlearning alg = runLearner(episodes, omega);
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

	public static Qlearning runLearner(int maxEpisode, double omega){
		Environment towerOfHanoi=new Environment();
		Qlearning alg = new Qlearning(maxEpisode, towerOfHanoi);
		alg.learn(omega);

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
