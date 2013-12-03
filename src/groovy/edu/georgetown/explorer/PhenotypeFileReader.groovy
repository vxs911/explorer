package edu.georgetown.explorer

import java.io.Serializable

class PhenotypeFileReader implements Serializable {
	
	File file = null;
	ArrayList<String> headers = new ArrayList<String>();
	int sampleColumnIndex = 0;
	HashMap<String, String[]> phenotypes = new HashMap<String, String[]>();

	public PhenotypeFileReader(File file) {
		if(!file.exists()) throw new FileNotFoundException();
		this.file = file;
		BufferedReader buff = new BufferedReader(new FileReader(file));
		String[] headers = buff.readLine().split("\t");
		this.headers = Arrays.asList(headers);
	}
	
	public void parse(String sampleColumn) {
		BufferedReader buff = new BufferedReader(new FileReader(file));
		int count = 0;
		this.sampleColumnIndex = this.headers.indexOf(sampleColumn)
		
		String raw = buff.readLine();
		
		while(raw != null) {
			String[] line = raw.split("\t");
			this.phenotypes.put(line[this.sampleColumnIndex], line);
			raw = buff.readLine();	
		}
	}
	
	public String getPhenotype(String sample, String phenotype) {
		int phenotypeColumnIndex = headers.indexOf(phenotype);
		return this.phenotypes[sample][phenotypeColumnIndex];
	}
		
}
