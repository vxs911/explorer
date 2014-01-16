package edu.georgetown.explorer

import org.springframework.web.multipart.MultipartHttpServletRequest

class PhenotypeController {
	
	def phenotypeService, fileUploadService

    def index() { 
		//if(session.phenotypeFileReader) render(view:'selectSampleSet')
	}
	
	def upload() {
		MultipartHttpServletRequest mpr = (MultipartHttpServletRequest) request;
		String fileName = fileUploadService.uploadPhenotypeFile(mpr, session["dir"]);
		
		PhenotypeFileReader phenotypeFileReader = session["reader"].getPhenotypeFileReader(new File(fileName), true);
		phenotypeFileReader.read();
		session["phenotypeFileReader"] = phenotypeFileReader
		
		def model = ["headers": phenotypeFileReader.phenotypeNames]
		
		render(view: "describe", model: model)
	}
	
	def describe() {
		
	}
	
	def setSurvivalPhenotype() {
		
		//String sampleColumn = params["column-sample"];
		String survivalPhenotype = params["phenotype-survival"]
		
		session["phenotype-survival"] = survivalPhenotype
		
		String dir = session["dir"];
		
		//def samples = ["ID001", "ID002", "ID003"]
		//def samples = reader.phenotypes.keySet()
		render (view:'summary', model:["files":new File(dir).listFiles()] );
	}
	
	def selectSampleSet() {
		def sampleSetNames = params.list("sample-set-name");
		log.debug("sampleSetName: "+sampleSetNames)
		//log.debug(session["savedSamples"])
		String survivalColumn = session["column-survival"];
		def samples = [:]
		def points = [:]
		sampleSetNames.each {
			samples[it] = session["savedSamples"].get(it)
		}
		//def samples = session["savedSamples"].get(sampleSetName);
		log.debug(samples)
		//PhenotypeFileReader reader = (PhenotypeFileReader) session.phenotypeFileReader;
		def survivalData = []
		samples.each { key, value ->
			value.each {
				Float survivalTime = 0.0;
				try {
					//survivalTime = new Float(reader.getPhenotype(it, survivalColumn));
					//log.debug(it+":"+survivalTime)
				} catch(NumberFormatException e) {
				
				}
				if(survivalTime > 0.0) survivalData << ["sample": it, "survival": survivalTime]
			}
			points[key] = phenotypeService.getKmPoints(survivalData)
		}
		//log.debug(survivalData)
		//def points = phenotypeService.getKmPoints(survivalData)
		session.kmpoints = points
		//log.debug(points)
		def model = ["kmpoints": points]
				
		render(view: 'plot', model: model)
	}
	
	def selectPhenotypeFlow = {
		getNames {
			action {
				PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
				def model = ["phenotypeNames": phenotypeFileReader.phenotypeNames]
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
			render(view:'showSampleSet');
			on("submit").to "saveSampleSet"
		}
		
		saveSampleSet {
			action {
				String sampleSetName = params["sampleSetName"];
				if(session["savedSampleCohorts"]) session["savedSampleCohorts"] << ["name":sampleSetName, "individuals":flow["individuals"]];
				
				else {
					session["savedSampleCohorts"] = [];
					session["savedSampleCohorts"] << ["name":sampleSetName, "individuals":flow["individuals"]];
				}
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