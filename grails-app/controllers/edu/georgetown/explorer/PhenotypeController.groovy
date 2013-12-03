package edu.georgetown.explorer

import org.springframework.web.multipart.MultipartHttpServletRequest

class PhenotypeController {
	
	def phenotypeService

    def index() { 
		if(session.phenotypeFileReader) render(view:'selectSampleSet')
	}
	
	def upload() {
		MultipartHttpServletRequest mpr = (MultipartHttpServletRequest) request;
		def file = mpr.getFile("phenotypeFile");
		def random = new Random();
		def calendar = new java.util.GregorianCalendar();
		String fileName = "phenotype"+calendar.get(Calendar.YEAR).toString()+calendar.get(Calendar.MONTH).toString()+calendar.get(Calendar.DATE).toString()+random.nextInt().toString()+".tsv";
		if(!file.empty) {
			File newFile = new File("/Users/varun/dev/explorer/uploads/"+fileName);
			file.transferTo(newFile);
		}
		
		PhenotypeFileReader phenotypeFileReader = phenotypeService.getPhenotypeReader(fileName)
		session.phenotypeFileReader = phenotypeFileReader
		def model = ["headers": phenotypeFileReader.headers]
		
		render(view: "describe", model: model)
	}
	
	def describe() {
		
	}
	
	def setDataColumns() {
		String sampleColumn = params["column-sample"];
		String survivalColumn = params["column-survival"]
		session["column-survival"] = survivalColumn
		
		PhenotypeFileReader reader = (PhenotypeFileReader) session.phenotypeFileReader;
		reader.parse(sampleColumn);
		//def samples = ["ID001", "ID002", "ID003"]
		//def samples = reader.phenotypes.keySet()
		render (view:'selectSampleSet')
	}
	
	def selectSampleSet() {
		String sampleSetName = params["sample-set-name"];
		log.debug("sampleSetName: "+sampleSetName)
		//log.debug(session["savedSamples"])
		String survivalColumn = session["column-survival"];
		def samples = session["savedSamples"].get(sampleSetName);
		log.debug(samples)
		PhenotypeFileReader reader = (PhenotypeFileReader) session.phenotypeFileReader;
		def survivalData = []
		samples.each {
			Float survivalTime = 0.0;
			try {
				survivalTime = new Float(reader.getPhenotype(it, survivalColumn));
				//log.debug(it+":"+survivalTime)
			} catch(NumberFormatException e) {
			
			}
			if(survivalTime > 0.0) survivalData << ["sample": it, "survival": survivalTime]
		}
		log.debug(survivalData)
		def points = phenotypeService.getKmPoints(survivalData)
		session.kmpoints = points
		def model = ["kmpoints": points]
				
		render(view: 'summary', model: model)
	}
	
	def plot() {
		
	}
}