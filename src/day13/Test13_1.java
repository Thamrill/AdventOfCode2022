package day13;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class Test13_1 implements Runnable {
	private static final String path = "day13-input1.txt";

	public static void main(String[] args) {
		new Test13_1().run();
	}
	
	public static class Packet{
		JSONArray left;
		JSONArray right;
		public Packet(JSONArray left, JSONArray right) {
			super();
			this.left = left;
			this.right = right;
		}
		
		public boolean isInOrder() {
			
			return isArraySmaller(left, right)<0;
		}
		
		@SuppressWarnings("unchecked")
		public int isArraySmaller(JSONArray array0, JSONArray array1) {
			int minSize=Math.min(array0.size(), array1.size());
			JSONArray temp;
			for(int ii=0; ii<minSize; ii++) {
				Object o0=array0.get(ii);
				Object o1=array1.get(ii);
				if((o0 instanceof Long)&&(o1 instanceof Long)) {
					if(Long.compare((long) o0, (long) o1)<0) {
						return -1;
					}else if(Long.compare((long) o0, (long) o1)>0) {
						return +1;
					}
				}else {
					int arrayComp=0;
					if(o0 instanceof Long) {
						temp=new JSONArray();
						temp.add(o0);
						arrayComp= isArraySmaller(temp, (JSONArray) o1);
					}else if(o1 instanceof Long) {
						temp=new JSONArray();
						temp.add(o1);
						arrayComp= isArraySmaller((JSONArray) o0, temp);
					}else {
						arrayComp= isArraySmaller((JSONArray) o0, (JSONArray) o1);
					}
					if(arrayComp!=0) {
						return arrayComp;
					}
				}
			}
			return Integer.compare(array0.size(), array1.size());
		}
	}
	
	

	@Override
	public void run() {
		File inputFile=new File(path);
		JSONParser parser=new JSONParser();
		ArrayList<Packet> packets=new ArrayList<>();
		try (FileReader fr = new FileReader(inputFile); BufferedReader br = new BufferedReader(fr)) {
			String left = br.readLine();
			String right = br.readLine();
			br.readLine();
			while (left != null) {
				packets.add(new Packet((JSONArray) parser.parse(left), (JSONArray) parser.parse(right)));
				left = br.readLine();
				right = br.readLine();
				br.readLine();
			}
			int sum=0;
			for(int ii=0; ii<packets.size(); ii++) {
				if(packets.get(ii).isInOrder()) {
					sum+=ii+1;
				}
			}
			System.out.println(sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
