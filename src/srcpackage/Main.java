package srcpackage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
	
	public static void main(String[] args)
	{
		Environment towerOfHanoi=new Environment();
		Qlearning alg = new Qlearning(10000, towerOfHanoi);
		alg.learn();
		double[][] QValues = alg.getQValues();
		int[] OptimalPolicy = alg.getOptimalPolicy();



		System.out.println("\nQ-values");
		for(var q : QValues){
			for(var qv : q) {
				System.out.print(qv + "\t");
			}
			System.out.println();
		}


		System.out.println("\nOptimal Policy");
		for(var optPol : OptimalPolicy){
			System.out.println(optPol);
		}

		System.out.println("\nQ-values in optimal policy");
		for(int i = 0; i < OptimalPolicy.length; i++){
			System.out.println("State: " + i + "\tOptimal Policy: " + OptimalPolicy[i] + "\t Qvalue: " + getLargestElement(QValues[i]));
		}
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
}
