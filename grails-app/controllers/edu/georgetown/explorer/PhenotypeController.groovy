package edu.georgetown.explorer

import edu.georgetown.explorer.tabular.TabularFileReader
import grails.converters.JSON
import org.springframework.web.multipart.MultipartHttpServletRequest
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

class PhenotypeController {
	
	def phenotypeService, genotypeService, fileUploadService

    def index() { 
		
	}
	
	def upload() {
		MultipartHttpServletRequest mpr = (MultipartHttpServletRequest) request;		
		String dir = session["dir"];
		String fileName = fileUploadService.uploadPhenotypeFile(mpr, fileUploadService.getPhenotypeDir(dir));
		
		redirect (action:'summary')
	}
	
	def describe() {
		String dir = session["dir"];
		File phenotypeFile = fileUploadService.getPhenotypeFile(dir);
		String[] headers = phenotypeService.getHeader(phenotypeFile, "\t");
		
		return ["headers":headers];
	}
	
	def process() {
		boolean containsHeader = params.boolean("contains_header");
		int sampleColumn = params.int("sample_column");
		log.debug "containsHeader: ${containsHeader}, sampleColumn: ${sampleColumn}"
		Map<PhenotypeFileReader.CONFIG, String> configMap = new HashMap<String, String>();
		
		configMap.put(PhenotypeFileReader.CONFIG.CONTAINS_HEADER, containsHeader);
		configMap.put(PhenotypeFileReader.CONFIG.SAMPLE_COLUMN, sampleColumn);
		
		UserFiles files = UserFiles.findByDir(session["dir"]);
		files.phenotypeFileDesc = configMap.collect { key, value ->
			[key, value].join("=")
		}.join(":");
		
		log.debug "phenotypeFileDesc changed to: ${files.phenotypeFileDesc}"
	
		files.save(flush:true);
		Map readers = genotypeService.readUploadedFiles(files);
		
		if(readers) {
			session["type"] = files.type;
			session["reader"] = readers?.reader;
			session["phenotypeFileReader"] = readers?.phenotypeFileReader;
			session["survivalTimeVariableName"] = null;
		}
		
		forward (action: 'summary')
	}
	
	def summary() {
		String dir = session["dir"];
		File[] genotypeFiles = new File(fileUploadService.getGenotypeDir(dir)).listFiles();
		File[] phenotypeFiles = new File(fileUploadService.getPhenotypeDir(dir)).listFiles();
		
		return [genotypeFiles:genotypeFiles, phenotypeFiles:phenotypeFiles];
	}
	
	def ajaxGetPhenotypeDescriptionFromName() {
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		TabularFileReader tabFileReader = phenotypeFileReader.getTabularFileReader();
		String phenotypeName = params["phenotype_name"]?.trim();
		Float max = tabFileReader.getColumnMaxValueAsFloat(phenotypeName);
		Float min = tabFileReader.getColumnMinValueAsFloat(phenotypeName);
		log.debug "max is ${max} and min is ${min}"
		def jsonObject = [:];
		
		if((max != null) && (min != null)) {
			jsonObject["minimum"] = min;
			jsonObject["maximum"] = max;
		}
		
		jsonObject["uniqueValues"] = tabFileReader.getColumnUniqueRawValuesArray(phenotypeName);
		jsonObject["phenotypeName"] = phenotypeName;
		render jsonObject as JSON;
	}
	
	def ajaxGetCohortBasedOnPhenotype() {
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		Float minimum = params.float("minimum");
		Float maximum = params.float("maximum");
		String genotypeType = params["genotype_type"]?.trim();
		String phenotypeName = params["phenotype_name"]?.trim();
		String phenotypeValue = params["phenotype_value"]?.trim();
		
		def jsonObject = [:];
		log.debug "params is: "+params
		String[] individuals = null;
		
		if(genotypeType) {			
			if(genotypeType == "het") {
				individuals = session["tempSampleCohorts"].get(0)["individuals"];
			}
			
			else if(genotypeType == "hom_ref") {
				individuals = session["tempSampleCohorts"].get(1)["individuals"];
			}
			
			else {
				individuals = session["tempSampleCohorts"].get(2)["individuals"];
			}
		}
		
		if(phenotypeValue) {
			individuals = phenotypeService.filterCohortBasedOnPhenotypeValue(phenotypeFileReader, individuals, phenotypeName, phenotypeValue);
		}
		
		else if(maximum && minimum) {
			individuals = phenotypeService.filterCohortBasedOnPhenotypeRange(phenotypeFileReader, individuals, phenotypeName, minimum, maximum);
		}
		
		jsonObject["count"] = individuals.length;
		jsonObject["key"] = phenotypeService.createTemporaryCohortInSession(session, individuals);
		render jsonObject as JSON;
	}
	
	def ajaxSaveCohort() {
		int cohortKey = params.int("cohort_key");
		String cohortName = params["cohort_name"].trim();
		def savedSampleCohorts = session["savedSampleCohorts"] ?: [];
		savedSampleCohorts << ["name":cohortName, "individuals":session["tempPhenotypeCohort"][cohortKey]];
		
		log.debug "saved cohort, key ${cohortKey}, name: ${cohortName}"
		
		session["savedSampleCohorts"] = savedSampleCohorts;
		render(status:HttpServletResponse.SC_OK);
	}
	
	def selectPhenotypeFlow = {
		getNames {
			action {
				PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
				String phenotypeSurvival = params["phenotype-survival"];
				def phenotypeNames = []
				phenotypeFileReader.phenotypeNames.each {
					if(it != phenotypeSurvival) phenotypeNames << it
				}
				def model = ["phenotypeNames": phenotypeNames]
				return model
			}
			on("success").to "selectName"
		}
		selectName {
			render(view:'selectName')
			on("submit").to "getPhenotypeSummary"
		}
		
		getPhenotypeSummary {
			action {
				PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
				PhenotypeContainer container = phenotypeFileReader.getPhenotypeContainerByName(params["phenotype"]);
				Phenotype[] phenotypes = container.getExtremeValues();
				return ["minimum":phenotypes[0].getValue(), "maximum":phenotypes[1].getValue(), "phenotype":params["phenotype"]]
			}
			on("success").to "selectRange"
		}
		
		selectRange {
			render(view:'selectRange')
			on("submit").to "retrieveSampleSet"
		}
		
		retrieveSampleSet {
			action {
				PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
				PhenotypeContainer container = phenotypeFileReader.getPhenotypeContainerByName(flow["phenotype"]);
				Phenotype low = container.createPhenotype(params["minimum"]);
				Phenotype high = container.createPhenotype(params["maximum"]);
				Individual[] individuals = container.getIndividualsWithPhenotypeInRange(low, high);
				return ["individuals":individuals];
			}
			on("success").to "showSampleSet"
		}
		
		showSampleSet {
			render(view:'showCohort');
			on("submit").to "saveSampleCohort"
		}
		
		saveSampleCohort {
			action {
				String cohortName = params["cohort-name"];
				def savedSampleCohorts = session["savedSampleCohorts"] ?: [];
				savedSampleCohorts << ["name":cohortName, "individuals":flow["individuals"]];
				
				session["savedSampleCohorts"] = savedSampleCohorts;
				return ["name":cohortName]
			}
			on("success").to "summary"
		}
		
		summary {
			render(view:'summary')
		}
	}
	
	def plot() {
		
	}
}