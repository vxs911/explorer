package edu.georgetown.explorer

public class VCFile {
	
	File file = null;
	HashMap<String, HashMap<Integer, Record>> records= new HashMap<String, HashMap<Integer, Record>>();
	HashMap<String, List<Integer>> sortedPositions = new HashMap<String, List<Integer>>();
	Set<String> chromosomes = new HashSet<String>();
	String[] samples = null

	public VCFile(File file) {
		if(!file.exists()) throw new FileNotFoundException();
		this.file = file;
		String[] headers = null;
		Set<String> chromosomes = new HashSet<String>();
		List<Record> records = new ArrayList<Record>()
		
		file.eachLine {raw ->
			if(raw.startsWith("##")) {
				
			}
			
			else if(raw.startsWith("#")) {
				headers = raw.split("\t")
				this.samples = Arrays.copyOfRange(headers, 9, headers.length)
			}
			
			else {
				try {
					Record record = createShallowRecord(raw);
					records.add(record);
				} catch(Exception e) {
					println "Exception caught"				
				}

			}
		}		
		organizeRecords(records);
		this.chromosomes = this.records.keySet();
	}
	
	public VCFile(String path) {
		this(new File(path));
	}
	
	private Record createShallowRecord(String raw) throws Exception {
		Record record = new Record();
		String[] line = raw.split("\t");
		String chromosome = line[0].replaceAll("[a-zA-Z]", "");
		Integer position = new Integer(line[1]);
		String rsid = line[2];
		record.chromosome = chromosome;
		record.position = position;
		record.rsid = rsid;
		record.line = line;
		
		return record;
	}
	
	public List<Integer> getPositionsForChromosome(String chromosome) {
		List<Integer> positions = this.sortedPositions.get(chromosome);
		if(!positions) {
			positions = this.records.get(chromosome).keySet().sort();
			this.sortedPositions.put(chromosome, positions);
		}
		return positions;
	}
	
	private void parseRecord(Record record) throws Exception {
		String[] line = record.line;
		//String chromosome = line[0].replaceAll("[a-zA-Z]", "");
		//Integer position = new Integer(line[1]);
		//String rsid = line[2];
		String reference = line[3];
		String[] alternate = line[4].split(":");
		Number quality = new Float(line[5]);
		//if(line[5].indexOf(".") > -1) quality = new Float(line[5]);
		//else quality = new Integer(line[5]);
		String filter = line[6];
		String[] temp = line[7].split(";");
		Map<String, String> info = new HashMap<String, String>();
		temp.each {
			String[] infoField = it.split("=");
			info.put(infoField[0], infoField[1]);
		}
		String[] format = line[8].split(":");
		Call test = new Call();
		Call[] calls = new Call[this.samples.length];
		for (int i = 0; i < this.samples.length; i++) {
			Call call = new Call();
			call.name = this.samples[i];
			temp = line[i + 9].split(":");
			String[] alleles = temp[0].split("/");
			call.alleles[0] = new Integer(alleles[0]);
			call.alleles[1] = new Integer(alleles[1]);
			
			for(int j = 0; j < 2; j++) {
				if(call.alleles[j] == 0) call.bases[j] = reference
				else {
					call.bases[j] = alternate[call.alleles[j] - 1]
				}
			}
			
			call.type = call.alleles[0] + call.alleles[1];
			
			if(temp[0] != "./.") call.called = true;
			
			calls[i] = call;
		}
		//record.chromosome = chromosome;
		//record.position = position;
		//record.rsid = rsid;
		record.reference = reference;
		record.alternate = alternate;
		record.quality = quality;
		record.filter = filter;
		record.info = info;
		record.format = format;
		record.setCalls(calls);
		record.shallow = false;
		
		//return record;
	}
	
	private void organizeRecords(List<Record> records) {
		records.each { record ->
			if(this.records.get(record.chromosome)) {
				this.records.get(record.chromosome).put(record.position, record);
			}
			
			else {
				HashMap<Integer, Record> allChromRecords = new HashMap<Integer, Record>();
				allChromRecords.put(record.position, record);
				this.records.put(record.chromosome, allChromRecords);
			}
		}
	}
	
	public Record fetch(String chromosome, int position) {
		Record record = this.records.get(chromosome).get(position);
		if(record.shallow) {
			 parseRecord(record);
		}
		
		return record;
	}
	
	public class Record {
		String chromosome;
		Integer position;
		String rsid;
		String reference;
		String[] alternate;
		Number quality;
		String filter;
		Map<String, String> info = [:];
		String[] format;
		private HashMap<String, Call> calls = new HashMap<String, Call>(1);
		private String[] line = null;
		boolean shallow = true;
		
		public setCalls(Call[] calls) {
			this.calls = new HashMap<String, Call>(calls.length * 2);
			for(int i = 0; i < calls.length; i++) {
				this.calls.put(calls[i].name, calls[i]);
			}
		}
		
		public getCalls() {
			return this.calls;
		}
		
		public Call getGenotype(String name) {
			return this.calls.get(name);
		}
		
		public Call[] getHets() {
			def result = []
			this.calls.each { sample, call ->
				if(call.isHet()) result << call
			}
			
			return result.toArray()
		}
		
		public Call[] getHomAlts() {
			def result = []
			this.calls.each {sample, call ->
				if(call.isHomAlt()) result << call
			}
			
			return result.toArray()
		}
		
		public Call[] getHomRefs() {
			def result = []
			this.calls.each {sample, call ->
				if(call.isHomRef()) result << call
			}
			
			return result.toArray()
		}
	}
	
	public class Call {
		boolean called = false;
		int[] alleles = new int[2];
		String[] bases = new String[2];
		int type;
		String name;
		
		public boolean isHet() {
			return this.called && (this.alleles[0] != this.alleles[1])
		}
		
		public boolean isHomAlt() {
			return this.called && (this.alleles[0] == 1) && (this.alleles[0] == this.alleles[1])
		}
		
		public boolean isHomRef() {
			return this.called && (this.alleles[0] == 0) && (this.alleles[0] == this.alleles[1])
		}
	}
	
}
