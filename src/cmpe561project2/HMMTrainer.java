package cmpe561project2;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class HMMTrainer {
	Scanner scanner;
	HashMap<String, Integer> tagCountsCPosTag = new HashMap<String, Integer>();
	HashMap<String, HashMap<String, Integer>> wordCountsCPosTag = new HashMap<String, HashMap<String,Integer>>();
	HashMap<String, HashMap<String, Integer>> tagBigramCountsCPosTag = new HashMap<String, HashMap<String,Integer>>();
	HashMap<String, HashMap<String, Integer>> tagForWordCountsCPosTag = new HashMap<String, HashMap<String, Integer>>();

	HashMap<String, Integer> tagCountsPosTag = new HashMap<String, Integer>();
	HashMap<String, HashMap<String, Integer>> wordCountsPosTag = new HashMap<String, HashMap<String,Integer>>();
	HashMap<String, HashMap<String, Integer>> tagBigramCountsPosTag = new HashMap<String, HashMap<String,Integer>>();
	HashMap<String, HashMap<String, Integer>> tagForWordCountsPosTag = new HashMap<String, HashMap<String, Integer>>();
	ArrayList<String> wordlist= new ArrayList<String>();
	ArrayList<String> term_tag= new ArrayList<String>();
	String mostFreqCPosTag = "";
	int mostFreqCPosTagCount = 0;

	String mostFreqPosTag = "";
	int mostFreqPosTagCount = 0;

	int numTrainingCPosBigrams = 0;
	int numTrainingPosBigrams = 0;
	FileWriter writer;
	
	//constructor with path
	public HMMTrainer(String pathToTrainingSet) throws IOException {
		File file = new File(pathToTrainingSet);
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//parses the training file located at metu_sabanci_cmpe_561/train/turkish_metu_sabanci_train.conll
	public void parseTrainer()
	{
		String line = "";
		String preCPosTag="";
		String prePosTag="";
		outerloop:
			while(scanner.hasNextLine())
			{
				//scanner gets the line
				line=scanner.nextLine();
				//counts the number of blank lines, it there is just one blank line it continues parsing the file.
				// if more than one blank line exists then, it stops parsing 
				int count=0;
				while(line.equals("")){
					count++;
					if(scanner.hasNextLine()){
						line=scanner.nextLine();
					}
					if(count>1)
						break outerloop;
				}
				// splits line into parts
				String[] lineParts=line.split("\\s");
				//gets the word itself
				String currentWord= lineParts[1];
				String stemmed= lineParts[2];
				//if the word is an non word character sequence then discard it.
				boolean flag=true;
				if(currentWord.equals("_")&&stemmed.equals("_"))
				{
					flag=false;
				}
				//if it is an actual word
				if(flag)
				{
					// if it is a starting word, add <s> character to beginning of it.
					String id=lineParts[0];
					if(id.equals("1"))
					{
						wordlist.add("<s>");
					}
					//then add the word itself to the list
					if(!currentWord.equals("_"))
						wordlist.add(currentWord);
					else if (currentWord.equals("_") && !stemmed.equals("_")){
						wordlist.add(stemmed);
						currentWord=stemmed;
					}
					else
					{

					}
					//gets the CPosTag and processes it.
					String currentCPosTag=lineParts[3]; 
					add(tagCountsCPosTag, currentCPosTag);
					add(wordCountsCPosTag, currentCPosTag, currentWord);
					add(tagBigramCountsCPosTag, preCPosTag, currentCPosTag);
					add(tagForWordCountsCPosTag, currentWord, currentCPosTag);
					if(tagCountsCPosTag.get(currentCPosTag) >= mostFreqCPosTagCount){
						mostFreqCPosTagCount = tagCountsCPosTag.get(currentCPosTag);
						mostFreqCPosTag = currentCPosTag;
					}
					preCPosTag=currentCPosTag;
					numTrainingCPosBigrams++;
					//gets the PosTag and processes it.
					String currentPosTag=lineParts[4]; 
					add(tagCountsPosTag, currentPosTag);
					add(wordCountsPosTag, currentPosTag, currentWord);
					add(tagBigramCountsPosTag, prePosTag, currentPosTag);
					add(tagForWordCountsPosTag, currentWord, currentPosTag);
					if(tagCountsPosTag.get(currentPosTag) >= mostFreqPosTagCount){
						mostFreqPosTagCount = tagCountsPosTag.get(currentPosTag);
						mostFreqPosTag = currentPosTag;
					}
					prePosTag=currentPosTag;
					numTrainingPosBigrams++;
				}
			}
		/*for(String tag:tagCountsPosTag.keySet()){
			System.out.print(tag+", ");
		}*/
		//System.out.println(wordlist.toString());
		scanner.close();
	}
	public ArrayList<String> getCPOSTags(){
		ArrayList <String>tags= new ArrayList<String>();
		for(String tag:tagCountsCPosTag.keySet()){
			tags.add(tag);
		}
		return tags;
	}
	public ArrayList<String> getPOSTags(){
		ArrayList <String>tags= new ArrayList<String>();
		for(String tag:tagCountsPosTag.keySet()){
			tags.add(tag);
		}
		return tags;
	}
	public void parseTest()
	{
		String line = "";
		String preCPosTag="";
		String prePosTag="";
		boolean start=true;
		outerloop:
			while(scanner.hasNextLine())
			{
				//scanner gets the line
				line=scanner.nextLine();
				//counts the number of blank lines, it there is just one blank line it continues parsing the file.
				// if more than one blank line exists then, it stops parsing 
				int count=0;
				while(line.equals("")){
					count++;
					if(scanner.hasNextLine()){
						line=scanner.nextLine();
						start=true;
					}
					if(count>1)
						break outerloop;
				}
				// splits line into parts
				String[] lineParts=line.split("\\s");
				//gets the word itself
				String currentWord= lineParts[1];

				//if the word is an non word character sequence then discard it.
				boolean flag=true;
				if(currentWord.equals("_"))
				{
					flag=false;
				}
				//if it is an actual word
				if(flag)
				{
					// if it is a starting word, add <s> character to beginning of it.
					String id=lineParts[0];
					if(id.equals("1")||start)
					{
						wordlist.add("<s>");
						start=false;
					}
					//then add the word itself to the list
					wordlist.add(currentWord);

					//gets the CPosTag and processes it.
					String currentCPosTag=lineParts[3]; 
					add(tagCountsCPosTag, currentCPosTag);
					add(wordCountsCPosTag, currentCPosTag, currentWord);
					add(tagBigramCountsCPosTag, preCPosTag, currentCPosTag);
					add(tagForWordCountsCPosTag, currentWord, currentCPosTag);
					if(tagCountsCPosTag.get(currentCPosTag) >= mostFreqCPosTagCount){
						mostFreqCPosTagCount = tagCountsCPosTag.get(currentCPosTag);
						mostFreqCPosTag = currentCPosTag;
					}
					preCPosTag=currentCPosTag;
					numTrainingCPosBigrams++;
					//gets the PosTag and processes it.
					String currentPosTag=lineParts[4]; 
					add(tagCountsPosTag, currentPosTag);
					add(wordCountsPosTag, currentPosTag, currentWord);
					add(tagBigramCountsPosTag, prePosTag, currentPosTag);
					add(tagForWordCountsPosTag, currentWord, currentPosTag);
					if(tagCountsPosTag.get(currentPosTag) >= mostFreqPosTagCount){
						mostFreqPosTagCount = tagCountsPosTag.get(currentPosTag);
						mostFreqPosTag = currentPosTag;
					}
					prePosTag=currentPosTag;
					numTrainingPosBigrams++;
				}
			}
		//System.out.println(wordlist.toString());
		scanner.close();
	}
	public void viterbiAlg (String tagType, ArrayList<String> listofAllWords) throws IOException
	{
		File f = new File("output.txt");
		writer = new FileWriter(f);
		tagType = tagType.toUpperCase();
		//if the calculations according to CPOSTAG
		if (tagType.equals("CPOSTAG"))
		{
			HashMap<String, Term> prevMap = null;
			//goes over all the terms in the training list of words
			for(int i=0; i<listofAllWords.size(); i++){
				HashMap<String, Term> subMap = new HashMap<String,Term>();
				String term =listofAllWords.get(i);

				// if the term is at the start of the sentence
				if(term.equals("<s>")){
					Term t = new Term(listofAllWords.get(i+1), 1.0, "<s>", null);
					subMap.put(listofAllWords.get(i+1), t);
				}
				else
				{
					//if we have seen the word before in our 
					if(tagForWordCountsCPosTag.containsKey(term))
					{
						HashMap<String, Integer> tagcounts = tagForWordCountsCPosTag.get(term);
						for(String tag : tagcounts.keySet()){
							Term t=termProcessCPosTag(term, tag, prevMap);
							t.isknown=true;
							subMap.put(tag, t);               
						}
					}
					else
					{
						// if it is not in our training tags then choose the most frequent tag
						Term t=termProcessCPosTag(term, mostFreqCPosTag, prevMap);
						t.isknown=false;
						subMap.put(mostFreqCPosTag, t);
					}
					// if we reach the end of the list or end of the sentence
					if((i == listofAllWords.size()-1)||listofAllWords.get(i+1).equals("<s>") ){
						trace(subMap);
					}
				}
				prevMap=subMap;
			}
		}
		//if the calculations according to POSTAG
		if(tagType.equals("POSTAG"))
		{
			HashMap<String, Term> prevMap = null;
			//goes over all the terms in the training list of words
			for(int i=0; i<listofAllWords.size(); i++){
				HashMap<String, Term> subMap = new HashMap<String,Term>();
				String term =listofAllWords.get(i);

				// if the term is at the start of the sentence
				if(term.equals("<s>")){

					Term t = new Term(listofAllWords.get(i+1), 1.0, "<s>", null);
					subMap.put(listofAllWords.get(i+1), t);
				}
				else
				{
					//if we have seen the word before in our 
					if(tagForWordCountsPosTag.containsKey(term))
					{
						HashMap<String, Integer> tagcounts = tagForWordCountsPosTag.get(term);
						for(String tag : tagcounts.keySet()){
							Term t=termProcessPosTag(term, tag, prevMap);
							t.isknown=true;
							subMap.put(tag,t );               
						}
					}
					else
					{
						// if it is not in our training tags then choose the most frequent tag
						Term t=termProcessPosTag(term, mostFreqPosTag, prevMap);
						t.isknown=false;
						subMap.put(mostFreqPosTag, t);
					}
					// if we reach the end of the list or end of the sentence
					if((i == listofAllWords.size()-1)||listofAllWords.get(i+1).equals("<s>") ){
						trace(subMap);
					}
				}
				prevMap=subMap;
			}
		}
		for(String s:term_tag)
		{
			writer.write(s);
		}
		writer.close();
	}

	//for CPosTag
	private Term termProcessCPosTag(String term, String CPostag, HashMap<String, Term> prevMap) {
		Term t = new Term(term,CPostag);
		double maxProb = 0.0;
		for(String prevTag : prevMap.keySet()){
			Term prevTerm = prevMap.get(prevTag);
			double prevProb = prevTerm.probability;
			prevProb *= priorProbCPosTag(prevTag, CPostag);
			if(prevProb >= maxProb){
				maxProb = prevProb;
				t.previous = prevTerm;
			}
		}
		t.probability = maxProb * likelihoodCPosTag(CPostag, term);
		return t;
	}
	public double likelihoodCPosTag(String tag, String word)
	{

		return (double) counts(wordCountsCPosTag,tag,word) / (double) counts(tagCountsCPosTag,tag);
	}
	public double priorProbCPosTag(String tag1, String tag2){

		return (double) counts(wordCountsCPosTag,tag1,tag2) / (double) counts(tagCountsCPosTag,tag1);

	}
	//for PosTag
	public Term termProcessPosTag(String term, String Postag, HashMap<String, Term> prevMap) {
		Term t = new Term(term,Postag);
		double maxProb = 0.0;
		for(String prevTag : prevMap.keySet()){
			Term prevTerm = prevMap.get(prevTag);
			double prevProb = prevTerm.probability;
			prevProb *= priorProbPosTag(prevTag, Postag);
			if(prevProb >= maxProb){
				maxProb = prevProb;
				t.previous = prevTerm;
			}
		}
		t.probability = maxProb * likelihoodPosTag(Postag, term);
		return t;
	}

	public double likelihoodPosTag(String tag, String word)
	{

		return (double) counts(wordCountsPosTag,tag,word) / (double) counts(tagCountsPosTag,tag);
	}
	public double priorProbPosTag(String tag1, String tag2){

		return (double) counts(wordCountsPosTag,tag1,tag2) / (double) counts(tagCountsPosTag,tag1);

	}
	private void add(HashMap<String, Integer> map, String key1){
		if(map.containsKey(key1)){
			map.put(key1, map.get(key1)+1);
		} else{
			map.put(key1, 1);
		}       
	}   

	// performs map[key1][key2]++;
	private void add(HashMap<String, HashMap<String, Integer>> map, String key1, String key2){
		if(map.containsKey(key1)){
			add(map.get(key1),key2);
		} else {
			HashMap<String, Integer> subMap = new HashMap<String, Integer>();
			subMap.put(key2, 1);
			map.put(key1, subMap);
		}
	}
	private void trace(HashMap<String, Term> subMap) throws IOException {
		Term t = new Term("", "");
		for(String key : subMap.keySet()){
			Term currentNode = subMap.get(key);
			if(currentNode.probability >= t.probability){
				t = currentNode;
			}
		}

		Stack<Term> stack = new Stack<Term>();
		while(t != null){
			stack.push(t);
			t = t.previous;
		}

		while(!stack.isEmpty()){
			t = stack.pop();
			term_tag.add(t.name + "|" + t.tag + "\n");
		}
		term_tag.add("\n");
	}
	private int counts(HashMap<String, Integer> map, String key){
		return (map.containsKey(key)) ? map.get(key) : 0;
	}

	private int counts(HashMap<String, HashMap<String,Integer>> map, String key1, String key2){
		return (map.containsKey(key1))? counts(map.get(key1), key2) : 0;
	}


	private double counts(HashMap<String, Double> map, String key){
		return (map.containsKey(key)) ? map.get(key) : 0.0;
	}

	private double counts(HashMap<String, HashMap<String,Double>> map, String key1, String key2){
		return (map.containsKey(key1))? counts(map.get(key1), key2) : 0.0;
	}

}
