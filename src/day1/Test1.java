package day1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class Test1 implements Runnable{

	private static final String path="day1-input1.txt";
	
	public static void main(String[] args) {
		new Test1().run();
	}
	
	protected static class ElfInventory implements Comparable<ElfInventory>{
		ArrayList<Integer> calories;
		Integer id;
		
		public Integer getId() {
			return id;
		}

		public ElfInventory(int id) {
			this.id=id;
			calories=new ArrayList<>();
		}
		
		public Integer totalCalories() {
			int total=0;
			for(Integer cal:calories) {
				total+=cal;
			}
			return total;
		}

		@Override
		public int compareTo(ElfInventory arg0) {
			if(totalCalories()==arg0.totalCalories()) {
				return id.compareTo(arg0.id);
			}
			return -totalCalories().compareTo(arg0.totalCalories());
		}
		
		@Override
		public String toString() {
			return id+":"+totalCalories()+" cal";
		}
		
		public void addCalories(int calories) {
			this.calories.add(calories);
		}
	}

	@Override
	public void run() {
		File input=new File(path);
		TreeSet<ElfInventory> elves=new TreeSet<>();
		int count=0;
		ElfInventory elf=new ElfInventory(count);
		try(FileReader fr=new FileReader(input); BufferedReader br=new BufferedReader(fr)){
			String read=br.readLine();
			while(read!=null) {
				try {
					elf.addCalories(Integer.parseInt(read));
				}catch (NumberFormatException e) {
					count++;
					elves.add(elf);
					elf=new ElfInventory(count);
				}
				read=br.readLine();
			}
			elves.add(elf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(elves.first());
	}
}
