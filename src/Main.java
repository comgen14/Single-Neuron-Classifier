import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main (String args[]) throws IOException{
		ArrayList<String[]> dataTest = GetData(args[1]);
		ArrayList<String[]> dataTrain = GetData(args[0]);
		double[] weights = new double[dataTest.get(0).length];
		double[] tempWeights = new double[dataTest.get(0).length];
		for(int i = 0; i < weights.length; i++){
			weights[i] = 0;
		}
		int iterations = Integer.parseInt(args[3]);
		double trainingFactor = Double.parseDouble(args[2]);
		double currentAnswer = 0;
		double cClass = 0;
		double percent;
		
		currentAnswer = SigmoidUnit(dataTrain.get(1), weights);
		for(int i = 0; i < iterations; i++){
			int pos = i%(dataTrain.size() - 1) + 1;
			
			for(int j = 0; j < dataTrain.get(0).length; j++){
				tempWeights[j] = WeightAdjustment(dataTrain.get(pos), weights, trainingFactor, j);
			}
			System.out.format("After iteration %d: ", pos);
			for(int j = 0; j < weights.length-1; j++){
				weights[j] = tempWeights[j];
				System.out.format("w(%s) = %.4f, ", dataTrain.get(0)[j], weights[j]);
			}
			currentAnswer = SigmoidUnit(dataTrain.get(pos), weights);
			if(currentAnswer >= .5){
				if(dataTrain.get(pos)[dataTrain.get(pos).length - 1].compareTo("1") == 0){
					cClass++;
				}
			}else{
				if(dataTrain.get(pos)[dataTrain.get(pos).length - 1].compareTo("0") == 0){
					cClass++;
				}
			}
			System.out.format("output = %.4f\n", currentAnswer);
		}
		percent = cClass/iterations*100;
		System.out.format("\nAccuracy on training set (%d instances): %.2f%%\n", iterations, percent);
		
		cClass = 0;
		
		for(int i = 1; i <= dataTest.size()-1; i++){
			int pos = i;
			currentAnswer = SigmoidUnit(dataTest.get(pos), weights);
			if(currentAnswer >= .5){
				if(dataTest.get(pos)[dataTest.get(pos).length - 1].compareTo("1") == 0){
					cClass++;
				}
			}else{
				if(dataTest.get(pos)[dataTest.get(pos).length - 1].compareTo("0") == 0){
					cClass++;
				}
			}
		}
		percent = cClass/(dataTest.size()-1)*100;
		System.out.format("\nAccuracy on test set (%d instances): %.2f%%\n", dataTest.size()-1, percent);
	}
	
	public static double WeightAdjustment(String[] inputs, double[] weights, double tFactor, int pos){
		double output = 0;
		if(inputs[pos] == "?"){
			return weights[pos];
		}else{
			output = weights[pos] + tFactor*SigmaError(inputs, weights)*Integer.parseInt(inputs[pos])*(SigmoidUnit(inputs, weights)*(1-SigmoidUnit(inputs, weights)));
		}
		
		return output;
	}
	
	public static double SigmaError(String[] inputs, double[] weights){
		double output = 0.0;
		int l = inputs.length - 1;
		int pOutput = Integer.parseInt(inputs[l]);
	
		output = pOutput - SigmoidUnit(inputs, weights);
		
		return output;
	}
	
	public static double SigmoidUnit(String[] inputs, double[] weights){
		double sum = 0.0;
		double output = 0.0;
		
		for(int i = 0; i < inputs.length - 1; i++){
			if(inputs[i] == "?"){
				continue;
			}else{
			sum += Integer.parseInt(inputs[i])*weights[i];
			}
		}
		
		output = 1/(1 + Math.exp(-sum));
		
		return output;
	}
	
	public static ArrayList<String[]> GetData(String file) throws IOException{
		ArrayList<String[]> answer = new ArrayList<String[]>();
		String [] placeholder;
		String temp;
		File f = new File(file);
		BufferedReader br = new BufferedReader(new FileReader(f));
        while ((temp = br.readLine()) != null) {
			placeholder = temp.split(" ");
			answer.add(placeholder);
		}
		
		br.close();
		
		return answer;
		
	}
}
