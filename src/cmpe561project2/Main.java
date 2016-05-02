package cmpe561project2;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
//metu_sabanci_cmpe_561/train/turkish_metu_sabanci_train.conll
//metu_sabanci_cmpe_561/test/turkish_metu_sabanci_test_gold_sample.conll.txt


public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter the path of training file.. ");
		String pathtoTraining=br.readLine();
		HMMTrainer hmmTrainer = new HMMTrainer(pathtoTraining);
		hmmTrainer.parseTrainer();
		ArrayList listcpt=hmmTrainer.getCPOSTags();
		ArrayList listpt=hmmTrainer.getPOSTags();
		Collections.sort(listcpt);
		Collections.sort(listpt);
		String[][] arraycpt = new String[listcpt.size()+1][listcpt.size()+1];
		String[][] arraypt = new String[listpt.size()+1][listpt.size()+1];
		for(int i=1; i<listcpt.size()+1; i++){
			arraycpt[0][i]=(String) listcpt.get(i-1);
			arraycpt[i][0]=(String) listcpt.get(i-1);
		}
		for(int i=1; i<listpt.size()+1; i++){
			arraypt[0][i]=(String) listpt.get(i-1);
			arraypt[i][0]=(String) listpt.get(i-1);
		}
		for(int i = 1; i < listcpt.size()+1; i++)
		{
			for(int j = 1; j < listcpt.size()+1; j++)
			{
				if (arraycpt[i][j]==null)
					arraycpt[i][j]="0";  
			}
		}
		for(int i = 1; i < listpt.size()+1; i++)
		{
			for(int j = 1; j < listpt.size()+1; j++)
			{
				if (arraypt[i][j]==null)
					arraypt[i][j]="0";  
			}
		}

		System.out.println("Enter the path of test file.. ");
		String pathtoTest=br.readLine();
		HMMTrainer hmmTest = new HMMTrainer(pathtoTest);
		hmmTest.parseTest();

		System.out.println("CPOSTAG or POSTAG");
		String type=br.readLine();
		type.toUpperCase();
		hmmTrainer.viterbiAlg(type,hmmTest.wordlist);

		System.out.println("Enter the path of file gold standard file");
		String pathtoCompare=br.readLine();
		if(type.equals("CPOSTAG"))
			compare("output.txt",pathtoCompare,type,arraycpt);
		else if (type.equals("POSTAG"))
			compare("output.txt",pathtoCompare,type,arraypt);
		else{
			System.out.println("Invalid type!");
		}


		//compareForAccuracy("metu_sabanci_cmpe_561/outputCPOSTAG.txt","metu_sabanci_cmpe_561/validation/turkish_metu_sabanci_val.conll", "POSTAG");

	}

	public static void compare(String path1, String path2, String tagType, String [][] array) throws FileNotFoundException
	{
		//path to the output file
		File f1 = new File(path1);
		//path to the validation file
		File f2 = new File(path2);

		Scanner sc=new Scanner(f1);
		Scanner sc2=new Scanner(f2);
		double accuracyCPOSTag = 0.0;
		double accuracyPOSTag = 0.0;
		int numberOfWords=0;
		int numberOfCorrectTags=0;
		String currentWord="";
		String lineOutput="";
		outerloop:
			while (sc2.hasNextLine()&& sc.hasNextLine()){

				String lineValidation=sc2.nextLine();

				if(!currentWord.equals("_"))
					lineOutput= sc.nextLine();
				int count=0;
				while(lineValidation.equals("")&&lineOutput.equals("")){
					count++;
					if(sc2.hasNextLine()){
						lineValidation=sc2.nextLine();
						lineOutput=sc.nextLine();
					}
					if(count>1)
						break outerloop;
				}
				String[] lineParts=lineValidation.split("\\s");
				currentWord= lineParts[1];

				if(!currentWord.equals("_") ){
					numberOfWords++;
					String[] lineOutputParts=lineOutput.split("\\|");
					String word=lineOutputParts[0];
					String tag=lineOutputParts[1];
					if(tagType.equals("CPOSTAG"))
					{
						String CPOSTAG= lineParts[3];
						if(currentWord.equals(word) && CPOSTAG.equals(tag)){
							numberOfCorrectTags++;
						}

						else{
							int column=0;int row=0;
							for(int i=1; i<array.length; i++){

								if(tag.equals(array[0][i]))
									row=i;
								if(CPOSTAG.equals(array[0][i]))
									column=i;
							}
							String s=array[row][column];
							int value= Integer.parseInt(s);
							value++;
							array[row][column]=Integer.toString(value);
						}


					}else if(tagType.equals("POSTAG"))
					{
						String POSTAG= lineParts[4];
						if(currentWord.equals(word) && POSTAG.equals(tag)){
							numberOfCorrectTags++;
						}

						else{
							int column=0;int row=0;
							for(int i=1; i<array.length; i++){

								if(tag.equals(array[0][i]))
									row=i;
								if(POSTAG.equals(array[0][i]))
									column=i;
							}
							String s=array[row][column];
							int value= Integer.parseInt(s);
							value++;
							array[row][column]=Integer.toString(value);
						}

					}else
						System.out.println("Tag type does not exists!!");
				}
			} 
		accuracyCPOSTag=(double)numberOfCorrectTags/numberOfWords;
		accuracyPOSTag=(double)numberOfCorrectTags/numberOfWords;
		if(tagType.equals("CPOSTAG")){
			System.out.println("The accuracy of CPOSTAG is "+accuracyCPOSTag );
		}
		else if(tagType.equals("POSTAG")){
			System.out.println("The accuracy of POSTAG is "+accuracyPOSTag );
		}
		else{}
		System.out.println("------------------Confusion Matrix---------------");
		for(int i = 0; i < array.length; i++)
		{
			for(int j = 0; j < array.length; j++)
			{
				System.out.print(array[i][j]+" ");
			}
			System.out.println();
		}
	}


	// This function is for calculating the accuracy of known and unknown words.
	// The input file for the method is created in a different format.
	// Each line is in the format of  "word|tag isknown" 
	// This function gets the path of this output file and the validation set, 
	// and calculates the accuracies of known and unknown words in the system.
	// I will put the input file for this function in the directory. 
	public static void compareForAccuracy(String path1, String path2, String tagType) throws FileNotFoundException{
		//path to the output file
		File f1 = new File(path1);
		//path to the validation file
		File f2 = new File(path2);

		Scanner sc=new Scanner(f1);
		Scanner sc2=new Scanner(f2);
		double accuracyCPOSTag = 0.0;
		int trueKnown=0;
		int falseKnown=0;
		int trueUnknown=0;
		int falseUnknown=0;
		double accuracyPOSTag = 0.0;
		int numberOfWords=0;
		int numberOfCorrectTags=0;
		String currentWord="";
		String lineOutput="";
		outerloop:
			while (sc2.hasNextLine()&& sc.hasNextLine()){

				String lineValidation=sc2.nextLine();

				if(!currentWord.equals("_"))
					lineOutput= sc.nextLine();
				int count=0;
				while(lineValidation.equals("")&&lineOutput.equals("")){
					count++;
					if(sc2.hasNextLine()){
						lineValidation=sc2.nextLine();
						lineOutput=sc.nextLine();
					}
					if(count>1)
						break outerloop;
				}
				String[] lineParts=lineValidation.split("\\s");
				currentWord= lineParts[1];

				if(!currentWord.equals("_") ){
					numberOfWords++;
					String[] lineOutputParts=lineOutput.split("\\|");
					String word=lineOutputParts[0];
					String[] tagAndIsKnown=lineOutputParts[1].split(" ");
					String tag=tagAndIsKnown[0];
					String isKnown=tagAndIsKnown[1];
					if(tagType.equals("CPOSTAG"))
					{
						String CPOSTAG= lineParts[3];
						if(currentWord.equals(word) && CPOSTAG.equals(tag)){
							numberOfCorrectTags++;
							if(isKnown.equals("false"))
								trueUnknown++;
							else
								trueKnown++;
						}

						else{

							if(isKnown.equals("false"))
								falseUnknown++;
							else
								falseKnown++;
						}


					}else if(tagType.equals("POSTAG"))
					{
						String POSTAG= lineParts[4];
						if(currentWord.equals(word) && POSTAG.equals(tag)){
							numberOfCorrectTags++;
							if(isKnown.equals("false"))
								trueUnknown++;
							else
								trueKnown++;
						}

						else{

							if(isKnown.equals("false"))
								falseUnknown++;
							else
								falseKnown++;
						}

					}else
						System.out.println("Tag type does not exists!!");
				}
			} 
		accuracyCPOSTag=(double)numberOfCorrectTags/numberOfWords;
		accuracyPOSTag=(double)numberOfCorrectTags/numberOfWords;
		if(tagType.equals("CPOSTAG")){
			System.out.println("The accuracy of CPOSTAG is "+accuracyCPOSTag );
			System.out.println("The accuracy of known words is "+ (double)trueKnown/(trueKnown+falseKnown));
			System.out.println("The accuracy of unknown words is "+ (double)trueUnknown/(trueUnknown+falseUnknown));
		}
		else if(tagType.equals("POSTAG")){
			System.out.println("The accuracy of POSTAG is "+accuracyPOSTag );
			System.out.println("The accuracy of known words is "+(double) trueKnown/(trueKnown+falseKnown));
			System.out.println("The accuracy of unknown words is "+ (double)trueUnknown/(trueUnknown+falseUnknown));
		}
		else{}

	}
}