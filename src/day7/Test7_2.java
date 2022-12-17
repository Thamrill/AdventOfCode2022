package day7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;

public class Test7_2 implements Runnable{
	private static final String path = "day7-input1.txt";

	public static void main(String[] args) {
		new Test7_2().run();
	}
	
	public static class CustomFile{
		int size;
		String name;
		
		public CustomFile(int size, String name) {
			super();
			this.size = size;
			this.name = name;
		}



		public int getSize() {
			return size;
		}
	}
	
	public static class Directory extends CustomFile{
		TreeMap<String, Directory> directories;
		ArrayList<CustomFile> files;
		Directory parent;
		
		public Directory( String name) {
			super(0, name);
			files=new ArrayList<>();
			directories=new TreeMap<>();
		}

		@Override
		public int getSize(){
			int total=0;
			for(CustomFile file:files) {
				total+=file.getSize();
			}
			for(Directory file:directories.values()) {
				total+=file.getSize();
			}
			return total;
		}
		
		public void addFile(CustomFile cf) {
			files.add(cf);
		}
		
		public void addDirectory(Directory dir) {
			directories.put(dir.name, dir);
			dir.parent=this;
		}
		
		public Directory getDirectory(String name) {
			return directories.get(name);
		}
	}

	@Override
	public void run() {
		File input = new File(path);
		Directory root=null;
		ArrayList<Directory> directories=new ArrayList<>();
		int totalSpace=70000000;
		try (FileReader fr = new FileReader(input); BufferedReader br = new BufferedReader(fr)) {
			String read = br.readLine();
			Directory dir=new Directory("/");
			root=dir;
			read = br.readLine();
			boolean listing=false;
			while(read!=null) {
				String[] cmd=read.split(" ");
				if(read.startsWith("$")) {//command
					listing=false;
					switch (cmd[1].toLowerCase()){
					case "ls": 
						listing=true;
						break;
					case "cd":
						if(cmd[2].equals("..")) {
							dir=dir.parent;
						}else {
							dir=dir.getDirectory(cmd[2]);
						}
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + cmd[1].toLowerCase());
					}
					
				}else if(listing) {//listing files
					if(read.startsWith("dir")) {
						dir.addDirectory(new Directory(cmd[1]));
						directories.add(dir.getDirectory(cmd[1]));
					}else {
						dir.addFile(new CustomFile(Integer.parseInt(cmd[0]), cmd[1]));
					}
				}
				read=br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int availableSpace=totalSpace-root.getSize();
		int threshold=30000000-availableSpace;
		int size=Integer.MAX_VALUE;
		int dirSize;
		for(Directory dir:directories) {
			dirSize=dir.getSize();
			if(dirSize>=threshold&&dirSize<size) {
				size=dirSize;
				System.out.println(size);
			}
		}
		System.out.println(size);
	}
	
	public String print(Directory dir, int level) {
		String str="";
		String offset="\n";
		for(int ii=0; ii<level; ii++) {
			offset+="\t";
		}
		str=offset+dir.name+"("+dir.getSize()+")";
		for(Directory value:dir.directories.values()) {
			str+=print(value, level+1);
		}
		offset+="\t";
		for(CustomFile file:dir.files) {
			str+=offset+file.name+"("+file.getSize()+")";
		}
		return str;
	}
}
