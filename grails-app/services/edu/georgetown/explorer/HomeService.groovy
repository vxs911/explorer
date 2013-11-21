package edu.georgetown.explorer

import grails.transaction.Transactional

@Transactional
class HomeService {

    def processFile(String fileName) {
		log.debug("/Users/varun/dev/explorer/uploads/"+fileName)
		VCFile file = new VCFile("/Users/varun/dev/explorer/uploads/"+fileName)
		return file;
    }
	
	def createBins(List<Integer> numbers) {
		ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();
		int maxNumberOfBins = 100;
		int maxInEachBin = (numbers.size() > maxNumberOfBins) ? numbers.size()/maxNumberOfBins:numbers.size();
		int start = 0;
		int end = maxInEachBin;
		int count = 0;
		
		while(start < numbers.size()) {
			count++;
			groups.add((ArrayList)numbers.subList(start, end));
			start = end;
			end += maxInEachBin + count * maxInEachBin;
			if(end > numbers.size()) end = numbers.size();
		}
		
		return groups;
	}
	
	def List<Integer> getPositionsInRange(List<Integer> positions, int low, int high) {
		int fromIndex = Collections.binarySearch(positions, low);
		int toIndex = Collections.binarySearch(positions, high);
		
		return positions.subList(fromIndex, toIndex);
	}
	
	def matchSamples(File vcfFile) {
		vcfFile.eachLine {raw ->
			if(raw.startsWith("#")) {
				
			}
			
			else {
				String[] line = raw.split("\t");
			}
		}
	}
}
