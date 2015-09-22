package edu.georgetown.explorer

import java.io.File;
import java.util.List;
import javax.servlet.http.HttpSession
import org.apache.commons.lang3.StringUtils

import grails.transaction.Transactional

@Transactional
class GenotypeService {
	
	def fileUploadService
	
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
	
	Map readUploadedFiles(UserFiles files) {
		String dir = files.dir;
		String[] phenotypeFileDesc = files.phenotypeFileDesc?.split(":");
		Map<PhenotypeFileReader.CONFIG, String> configMap = new HashMap<String, String>();
		if(phenotypeFileDesc && phenotypeFileDesc.length > 0) {
			phenotypeFileDesc.each {
				String[] temp = it.split("=");
				configMap[PhenotypeFileReader.CONFIG.create(temp[0])] = temp[1];
			}
			log.debug "readUploadedFiles: configMap is: ${configMap}"
			GenotypeFileReader reader = GenotypeFileReader.getFileReader(new File(fileUploadService.getGenotypeDir(dir)).listFiles()[0]);
			reader.read();
			PhenotypeFileReader phenotypeFileReader = reader.getPhenotypeFileReader(new File(fileUploadService.getPhenotypeDir(dir)).listFiles()[0]);
			phenotypeFileReader.configure(configMap);
			phenotypeFileReader.read();
			return ["reader":reader, "phenotypeFileReader":phenotypeFileReader]
		}
		log.debug "readUploadedFiles returning null";
		return null;
	}
	
	def getGenotypeFromRecord(GenotypeFileRecord record, HttpSession session) {
		
		def jsonObject = [:];
		List<GenotypeCall> calls = null;

		log.debug "record is: "+record.rsid
		log.debug "position is: "+record.position
		log.debug "chromosome is: "+record.chromosome;
		
		jsonObject["chromosome"] = record.chromosome;
		jsonObject["position"] = record.position;
		jsonObject["reference"] = record.reference;
		jsonObject["alternate"] = record.alternates.join(",");
		jsonObject["count"] = ["het":record.getHets().size(), "hom_ref":record.getHomRefs().size(),"hom_alt":record.getHomAlts().size()];
		
		def individuals = null;
		def tempSampleCohorts = [];
		
		calls = record.getHets();
		individuals = calls?.collect {it.getIndividual().getId()}.toArray();
		tempSampleCohorts << ["name":record.reference+"-"+record.alternates[0], "individuals":individuals];
		log.debug "temp1 size: "+individuals.length;
		
		calls = record.getHomRefs();
		individuals = calls?.collect {it.getIndividual().getId()}.toArray();
		tempSampleCohorts << ["name":record.reference+"-"+record.reference, "individuals":individuals];
		log.debug "temp2 size: "+individuals.length;
		
		calls = record.getHomAlts();
		individuals = calls?.collect {it.getIndividual().getId()}.toArray();
		tempSampleCohorts << ["name":record.alternates[0]+"-"+record.alternates[0], "individuals":individuals];
		log.debug "temp3 size: "+individuals.length;
				
		session["tempSampleCohorts"] = tempSampleCohorts;
		
		return jsonObject;
		
	}

}
