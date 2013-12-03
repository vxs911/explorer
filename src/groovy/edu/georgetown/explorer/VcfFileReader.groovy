package edu.georgetown.explorer

public class VcfFileReader implements GenotypeFileReader {
	
	File file = null;
	HashMap<String, HashMap<Integer, VcfFileRecord>> records= new HashMap<String, HashMap<Integer, VcfFileRecord>>();
	HashMap<String, ArrayList<Integer>> sortedPositions = new HashMap<String, ArrayList<Integer>>();
	HashSet<String> chromosomes = new HashSet<String>();
	String[] samples = null

	public VcfFileReader(File file) {
		if(!file.exists()) throw new FileNotFoundException();
		this.file = file;
		String[] headers = null;
		Set<String> chromosomes = new HashSet<String>();
		List<VcfFileRecord> records = new ArrayList<VcfFileRecord>()
		
		file.eachLine {raw ->
			if(raw.startsWith("##")) {
				
			}
			
			else if(raw.startsWith("#")) {
				headers = raw.split("\t")
				this.samples = Arrays.copyOfRange(headers, 9, headers.length)
			}
			
			else {
				try {
					VcfFileRecord record = createShallowRecord(raw);
					records.add(record);
				} catch(Exception e) {
					println "Exception caught"				
				}

			}
		}		
		organizeRecords(records);
		this.chromosomes = this.records.keySet();
	}
	
	public VcfFileReader(String path) {
		this(new File(path));
	}
	
	private VcfFileRecord createShallowRecord(String raw) throws Exception {
		VcfFileRecord record = new VcfFileRecord();
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
	
	private void parseRecord(VcfFileRecord record) throws Exception {
		String[] line = record.line;
		//String chromosome = line[0].replaceAll("[a-zA-Z]", "");
		//Integer position = new Integer(line[1]);
		//String rsid = line[2];
		String reference = line[3];
		String[] alternates = line[4].split(":");
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
		//VcfCall test = new VcfCall();
		VcfCall[] calls = new VcfCall[this.samples.length];
		for (int i = 0; i < this.samples.length; i++) {
			VcfCall call = new VcfCall();
			call.name = this.samples[i];
			temp = line[i + 9].split(":");
			String[] alleles = temp[0].split("/");
			call.alleles[0] = new Integer(alleles[0]);
			call.alleles[1] = new Integer(alleles[1]);
			
			for(int j = 0; j < 2; j++) {
				if(call.alleles[j] == 0) call.bases[j] = reference
				else {
					call.bases[j] = alternates[call.alleles[j] - 1]
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
		record.alternates = alternates;
		record.quality = quality;
		record.filter = filter;
		record.info = info;
		record.format = format;
		record.setCalls(calls);
		record.shallow = false;
		
		//return record;
	}
	
	private void organizeRecords(List<VcfFileRecord> records) {
		records.each { record ->
			if(this.records.get(record.chromosome)) {
				this.records.get(record.chromosome).put(record.position, record);
			}
			
			else {
				HashMap<Integer, VcfFileRecord> allChromRecords = new HashMap<Integer, VcfFileRecord>();
				allChromRecords.put(record.position, record);
				this.records.put(record.chromosome, allChromRecords);
			}
		}
	}
	
	public VcfFileRecord fetch(String chromosome, int position) {
		VcfFileRecord record = this.records.get(chromosome).get(position);
		if(record.shallow) {
			 parseRecord(record);
		}
		
		return record;
	}
	
	public class VcfFileRecord implements GenotypeFileRecord {
		String chromosome;
		Integer position;
		String rsid;
		String reference;
		String[] alternates;
		Number quality;
		String filter;
		Map<String, String> info = [:];
		String[] format;
		private HashMap<String, VcfCall> calls = new HashMap<String, VcfCall>(1);
		private String[] line = null;
		boolean shallow = true;
		
		public setCalls(VcfCall[] calls) {
			this.calls = new HashMap<String, VcfCall>(calls.length * 2);
			for(int i = 0; i < calls.length; i++) {
				this.calls.put(calls[i].name, calls[i]);
			}
		}
		
		public getCalls() {
			return this.calls;
		}
		
		public VcfCall getGenotype(String name) {
			return this.calls.get(name);
		}
		
		public VcfCall[] getHets() {
			def result = []
			this.calls.each { sample, call ->
				if(call.isHet()) result << call
			}
			
			return result.toArray()
		}
		
		public VcfCall[] getHomAlts() {
			def result = []
			this.calls.each {sample, call ->
				if(call.isHomAlt()) result << call
			}
			
			return result.toArray()
		}
		
		public VcfCall[] getHomRefs() {
			def result = []
			this.calls.each {sample, call ->
				if(call.isHomRef()) result << call
			}
			
			return result.toArray()
		}
	}
	
	public class VcfCall implements GenotypeCall {
		
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
