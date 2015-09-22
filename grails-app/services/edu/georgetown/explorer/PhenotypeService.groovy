package edu.georgetown.explorer

import java.io.File;

import edu.georgetown.explorer.tabular.TabularFileReader
import grails.converters.JSON
import grails.transaction.Transactional
import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder

@Transactional
class PhenotypeService {

	/*int createTemporaryCohortInSession(HttpSession session, Individual[] individuals) {
		int random = new Random().next(4);
		def tempPhenotypeCohort = session["tempPhenotypeCohort"]?: [:];
		tempPhenotypeCohort[random] = individuals;
		session["tempPhenotypeCohort"] = tempPhenotypeCohort;
		
		return random;
	}*/
	
	int createTemporaryCohortInSession(HttpSession session, String[] individualIds) {
		int random = new Random().next(4);
		def tempPhenotypeCohort = session["tempPhenotypeCohort"]?: [:];
		tempPhenotypeCohort[random] = individualIds;
		session["tempPhenotypeCohort"] = tempPhenotypeCohort;
		
		return random;
	}
	
	public String[] getHeader(File file, String regex) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = reader.readLine();
			 if(line != null) {
				 return line.split(regex);
			 }
		} catch(IOException) {
			log.debug "caught exception closing phenotype file"
		} finally {
			reader.close();
		}
		 return null;
	}
	
	/*def getCohortBasedOnPhenotypeRange(HttpSession session, String phenotypeName, String minimum, String maximum) {
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		PhenotypeContainer container = phenotypeFileReader.getPhenotypeContainerByName(phenotypeName);
		Phenotype low = container.createPhenotype(minimum);
		Phenotype high = container.createPhenotype(maximum);
		Individual[] individuals = container.getIndividualsWithPhenotypeInRange(low, high);		
		int random = createTemporaryCohortInSession(session, individuals);		
		def jsonObject = [:];
		jsonObject["count"] = individuals.length;
		jsonObject["key"] = random;
		log.debug "${individuals.length} individuals found. Key: ${random}"
		
		return jsonObject;
	}*/
	
	public String[] getCohortBasedOnPhenotypeRange(PhenotypeFileReader phenotypeFileReader, String phenotypeName, Float minimum, Float maximum) {
		//PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		TabularFileReader tabFileReader = phenotypeFileReader.getTabularFileReader();
		String[] individualIds = tabFileReader.getRowIdsWithCellValuesInRange(phenotypeName, minimum, maximum);
		//int random = createTemporaryCohortInSession(session, rowIds);
		//def jsonObject = [:];
		//jsonObject["count"] = rowIds.length;
		//jsonObject["key"] = random;
		log.debug "${individualIds.length} individuals found"
		
		return individualIds;
	}
	
	/*def getCohortBasedOnPhenotypeValue(HttpSession session, String phenotypeName, String phenotypeValue) {
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		PhenotypeContainer container = phenotypeFileReader.getPhenotypeContainerByName(phenotypeName);
		Phenotype other = container.createPhenotype(phenotypeValue);
		Individual[] individuals = container.getIndividualsWithPhenotypeEqualTo(other);
		int random = createTemporaryCohortInSession(session, individuals);
		def jsonObject = [:];
		jsonObject["count"] = individuals.length;
		jsonObject["key"] = random;
		log.debug "${individuals.length} individuals found. Key: ${random}";
		
		return jsonObject;
	}*/
	
	public String[] getCohortBasedOnPhenotypeValue(PhenotypeFileReader phenotypeFileReader, String phenotypeName, String phenotypeValue) {
		log.debug "inside getCohortBasedOnPhenotypeValue"
		log.debug "phenotypeName: ${phenotypeName} and phenotypeValue: ${phenotypeValue}"
		//PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		TabularFileReader tabFileReader = phenotypeFileReader.getTabularFileReader();
		String[] individualIds = tabFileReader.getRowIdsWithCellValueEqualTo(phenotypeName, phenotypeValue);
		log.debug "rowIds is: ${individualIds}"
		//int random = createTemporaryCohortInSession(session, rowIds);
		//def jsonObject = [:];
		//jsonObject["count"] = rowIds.length;
		//jsonObject["key"] = random;
		log.debug "${individualIds.length} individuals found";
		
		return individualIds;
	}
	
	/*def filterCohortBasedOnPhenotypeRange(Individual[] individuals, HttpSession session, String phenotypeName, String minimum, String maximum) {
		log.debug "inside filterCohortBasedOnPhenotypeRange"
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		PhenotypeContainer container = phenotypeFileReader.getPhenotypeContainerByName(phenotypeName);
		Phenotype low = container.createPhenotype(minimum);
		Phenotype high = container.createPhenotype(maximum);
		List<Individual> filtered = new ArrayList<Individual>();
		
		individuals.each { individual ->
			Phenotype phenotype = container.getIndividualPhenotype(individual);
			if(phenotype.isLessThan(high) && phenotype.isGreaterThan(low)) filtered << individual
		}
		int random = createTemporaryCohortInSession(session, filtered.toArray(new Individual[0]));
		def jsonObject = [:];
		jsonObject["count"] = filtered.size();
		jsonObject["key"] = random;
		log.debug "${jsonObject.count} individuals found. Key: ${random}";
		
		return jsonObject;
	}*/
	
	public String[] filterCohortBasedOnPhenotypeRange(PhenotypeFileReader phenotypeFileReader, String[] individualIds, String phenotypeName, 
		Float minimum, Float maximum) {
		log.debug "inside filterCohortBasedOnPhenotypeRange"
		TabularFileReader tabFileReader = phenotypeFileReader.getTabularFileReader();
		
		if((individualIds == null) || (individualIds.length == 0)) return tabFileReader.getRowIdsWithCellValuesInRange(phenotypeName, minimum, maximum);		
		
		else {
			List<String> filteredIds = new ArrayList<String>();
			
			individualIds.each { individualId ->
				Float phenotypeValueFloat = tabFileReader.getCell(individualId, phenotypeName).getValueAsFloat();
				if((phenotypeValueFloat > minimum) && (phenotypeValueFloat < maximum)) filteredIds << individualId;
			}
			log.debug "${filteredIds.size()} individuals found";
			
			return filteredIds.toArray(new String[0]);
		}
	}
	
	/*def filterCohortBasedOnPhenotypeValue(Individual[] individuals, HttpSession session, String phenotypeName, String phenotypeValue) {
		log.debug "inside filterCohortBasedOnPhenotypeValue"
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		PhenotypeContainer container = phenotypeFileReader.getPhenotypeContainerByName(phenotypeName);
		Phenotype other = container.createPhenotype(phenotypeValue);
		List<Individual> filtered = new ArrayList<Individual>();
		
		individuals.each { individual ->
			Phenotype phenotype = container.getIndividualPhenotype(individual);
			if(phenotype.equals(other)) filtered << individual
		}
		int random = createTemporaryCohortInSession(session, filtered.toArray(new Individual[0]));
		def jsonObject = [:];
		jsonObject["count"] = filtered.size();
		jsonObject["key"] = random;
		log.debug "${jsonObject.count} individuals found. Key: ${random}";
		
		return jsonObject;
	}*/
	
	public String[] filterCohortBasedOnPhenotypeValue(PhenotypeFileReader phenotypeFileReader, String[] individualIds, String phenotypeName, String phenotypeValue) {
		log.debug "inside filterCohortBasedOnPhenotypeValue"
		TabularFileReader tabFileReader = phenotypeFileReader.getTabularFileReader();
		
		if((individualIds == null) || (individualIds.length == 0)) 
			return tabFileReader.getRowIdsWithCellValueEqualTo(phenotypeName, phenotypeValue);
		
		else {
			List<String> filteredIds = new ArrayList<String>();
			
			individualIds.each { individualId ->
				String rawPhenotypeValue = tabFileReader.getCell(individualId, phenotypeName).getValueAsRaw();
				if(rawPhenotypeValue == phenotypeValue) filteredIds << individualId;
			}
			log.debug "${filteredIds.size()} individuals found";
			
			return filteredIds.toArray(new String[0]);
		}
	}
}