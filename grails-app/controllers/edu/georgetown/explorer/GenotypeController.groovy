package edu.georgetown.explorer

import org.springframework.web.multipart.MultipartHttpServletRequest
import edu.georgetown.explorer.VcfFileReader.VcfFileRecord
import edu.georgetown.explorer.VcfFileReader.VcfCall

class GenotypeController {
	
	def genotypeService

    def index() { 
		
	}
	
	def upload() {
		MultipartHttpServletRequest mpr = (MultipartHttpServletRequest) request;
		def file = mpr.getFile("vcfFile");
		def random = new Random();
		def calendar = new java.util.GregorianCalendar();
		String fileName = calendar.get(Calendar.YEAR).toString()+calendar.get(Calendar.MONTH).toString()+calendar.get(Calendar.DATE).toString()+random.nextInt().toString()+".vcf";
		if(!file.empty) {
			File newFile = new File("/Users/varun/dev/explorer/uploads/"+fileName);
			file.transferTo(newFile);
		}
		
		def vcf = genotypeService.processFile(fileName)
		session.vcf = vcf
		def model = ["count":vcf.samples.length, "samples":vcf.samples]
		
		render(view: "summary", model: model)
	}
	
	def summary() {
		def vcf = session.vcf;
		def model = ["count":vcf.samples.length, "samples":vcf.samples]
		render(view: "summary", model: model)
	}
	
	def selectGenotypeFlow = {
		getChromosome {
			action {
				flow.remove("mode");
				flow.remove("samples");
				flow.remove("positions");
				flow.remove("reference");
				flow.remove("alternate");
				VcfFileReader vcf = request.session.vcf;
				def chromosomes = vcf.chromosomes.toList();
				[chromosomes:chromosomes]
			}
			on("success").to "selectChromosome"
		}
		
		selectChromosome {
			log.debug("inside selectChromosome")
			render (view:'select')
			on("submit").to "enterPosition"
		}
		
		afterChromosomeSelection {
			action {
				if(flow["mode"] == "range") {
					getRangeIndex()
				}
				
				else {
					flow["mode"] = "type"
					typePosition()
				}
			}
			on("typePosition").to "typePosition"
			on("getRangeIndex").to "getRangeIndex"
		}
		
		enterPosition {
			action {
				flow["mode"] = "type"
			}
			
			on("success").to "typePosition"
		}
		
		typePosition {
			render (view: 'select')
			on("submit").to "getGenotypes"
			on("changeMode").to "getRangeIndex"
		}
		
		getRangeIndex {
			action {
				log.debug("inside getRangeIndex")
				flow["mode"] = "range"
				VcfFileReader vcf = request.session.vcf;
				def chromosome = params.chromosome;
				def positions = vcf.getPositionsForChromosome(chromosome);
				def bins = genotypeService.createBins(positions);
				def groups = [:]
				for(int i = 0; i < bins.size(); i++) {
					groups[i] = bins[i];
				}
				return [groups: groups]

			}
			on("success").to "selectRange"
		}
		
		selectRange {
			render (view: 'select')
			on("submit").to "getPositions"
			on("changeMode").to "enterPosition"
		}
		
		getPositions {
			action {
				def positions = flow["groups"].get(params.int("rangeIndex"));
				[positions:positions]
			}
			on("success").to "selectPosition"
		}
		
		selectPosition {
			render(view: 'select')
			on("submit").to "getGenotypes"
		}
		
		getGenotypes {
			action {
				def position = params.int("position");
				def chromosome = params.chromosome;
				VcfFileReader vcf = request.session.vcf;
				def record = vcf.fetch(chromosome, position);
				return ["reference":record.reference, "alternate":record.alternates.join(",")]
			}
			on("success").to "showGenotypes"
		}
		
		showGenotypes {
			render(view: 'select')
			on("submit").to "getSamples"
		}
		
		getSamples {
			log.debug("enter");
			action {
				VcfCall[] calls;
				def samples = [];
				VcfFileReader vcf = request.session.vcf;
				def position = params.int("position");
				def chromosome = params.chromosome;
				VcfFileRecord record = vcf.fetch(chromosome, position);
				if(params.genotype_type == "het") calls = record.getHets();
				
				else if(params.genotype_type == "hom_ref") calls = record.getHomRefs();
				
				else calls = record.getHomAlts();
				samples = calls.collect {it.name};
				
				if(!samples || samples.size() == 0) samples << "None found"
				return ["samples":samples, "count": samples.size()]
			}
			on("success").to "showSamples"
		}
		
		showSamples {
			render(view: 'select')
			on("startOver").to "getChromosome"
			on("saveSamples").to "saveSamples"
		}
		
		saveSamples {
			action {
				def sampleName = params["name"];
				def savedSamples = session["savedSamples"]
				
				if(!savedSamples) savedSamples = [:];
				
				savedSamples[sampleName] = flow["samples"];
				session["savedSamples"] = savedSamples
			}
			on("success").to "saveSuccess"
		}
		
		saveSuccess {
			render(view: "saveSuccess")
		}
	}

}
