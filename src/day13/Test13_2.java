package day13;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Test13_2 implements Runnable {
//	private static final String path = "day13_validation.txt";
	private static final String path = "day13-input1.txt";

	public static void main(String[] args) {
		new Test13_2().run();
	}
	
	public static class Packet implements Comparable<Packet>{
		JSONArray array;
		
		public Packet(JSONArray array) {
			super();
			this.array = array;
		}
		
		@Override
		public int compareTo(Packet o) {
			return compareArray(array, o.array);
		}
		
		@SuppressWarnings("unchecked")
		public int compareArray(JSONArray array0, JSONArray array1) {
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
						arrayComp= compareArray(temp, (JSONArray) o1);
					}else if(o1 instanceof Long) {
						temp=new JSONArray();
						temp.add(o1);
						arrayComp= compareArray((JSONArray) o0, temp);
					}else {
						arrayComp= compareArray((JSONArray) o0, (JSONArray) o1);
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
		Packet packet2;
		Packet packet6;
		try {
			packet2 = new Packet((JSONArray) parser.parse("[[2]]"));
			packet6=new Packet((JSONArray) parser.parse("[[6]]"));

			packets.add(packet2);
			packets.add(packet6);
			
			try (FileReader fr = new FileReader(inputFile); BufferedReader br = new BufferedReader(fr)) {
				String packet = br.readLine();
				while (packet != null) {
					if(packet.isBlank()) {
						packet = br.readLine();
						continue;
					}
					packets.add(new Packet((JSONArray) parser.parse(packet)));
					packet =br.readLine();
				}
				Collections.sort(packets);
				System.out.println((1+packets.indexOf(packet2))*(1+packets.indexOf(packet6)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
