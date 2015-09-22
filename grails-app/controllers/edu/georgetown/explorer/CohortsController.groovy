package edu.georgetown.explorer

class CohortsController {
	
	def index() {
		if(params["choice"]) {
			if(params["choice"].trim() == "symbol") {
				redirect(action:'genotype', fragment: 'geneDiv')
			}
			
			if(params["choice"].trim() == "chromosome") {
				redirect(action:'genotype', fragment: 'chromosomeDiv')
			}
			
			else if(params["choice"] == "rsid") {
				redirect(action:'genotype', fragment: 'rsidDiv')
			}
			
			else if(params["choice"] == "phenotype") {
				redirect(action:'phenotype')
			}
		}
	}

    def genotype() { 
		GenotypeFileReader reader = session["reader"];
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		List<String> chromosomes = reader.getChromosomes();
		def model = [:];
		
		if(phenotypeFileReader != null) {
			model["phenotypeNames"] = phenotypeFileReader.phenotypeNames;
			model["chromosomes"] = chromosomes;
		}
		
		else {
			flash.error = "Please select an existing session or upload files to create a new session";
			redirect (controller:'home', action:'index');
		}
			
		return model;
	}
	
	def phenotype() {
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		def model = [:];
		if(phenotypeFileReader != null) {
			model["phenotypeNames"] = phenotypeFileReader.phenotypeNames;
		}
		else {
			flash.error = "Please select an existing session or upload files to create a new session";
			redirect (controller:'home', action:'index');
		}
		
		return model
	}
	
	def quickstart() {
		
	}
}