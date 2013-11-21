package edu.georgetown.explorer

import java.util.List;
import java.util.Random
import org.springframework.web.multipart.MultipartHttpServletRequest
import edu.georgetown.explorer.VCFile.Record

class HomeController {
	
	def homeService

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
		
		def vcf = homeService.processFile(fileName)
		session.vcf = vcf
		def model = ["count":vcf.samples.length, "samples":vcf.samples]
		
		render(view: "summary", model: model)
	}
	
	def summary() {
		
	}
	
	def selectGenotypeFlow = {
		getChromosome {
			action {
				flow.remove("mode");
				flow.remove("samples");
				flow.remove("positions");
				flow.remove("reference");
				flow.remove("alternate");
				VCFile vcf = request.session.vcf;
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
				VCFile vcf = request.session.vcf;
				def chromosome = params.chromosome;
				def positions = vcf.getPositionsForChromosome(chromosome);
				def bins = homeService.createBins(positions);
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
				VCFile vcf = request.session.vcf;
				def record = vcf.fetch(chromosome, position);
				return ["reference":record.reference, "alternate":record.alternate.join(",")]
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
				def samples = [];
				VCFile vcf = request.session.vcf;
				def position = params.int("position");
				def chromosome = params.chromosome;
				Record record = vcf.fetch(chromosome, position);
				if(params.genotype_type == "het") samples = record.getHets();
				
				else if(params.genotype_type == "hom_ref") samples = record.getHomRefs();
				
				else samples = record.getHomAlts();
				return ["samples":samples.collect {it.name}, "count": samples.length]
			}
			on("success").to "showSamples"
		}
		
		showSamples {
			render(view: 'select')
			on("startOver").to "getChromosome"
		}
	}
	
	/*def selectGenotype() {
		def model = [:]
		VCFile vcf = session.vcf;
		String chromosome = "";
		Record record = null;
		Set<String> chromosomes = vcf.chromosomes;
		model["chromosomes"] = chromosomes;
		int position = 0;
		List<Integer> positions = [];
		List<List<Integer>> bins = [];
		
		if(params.keySet().containsAll(["chromosome"])) {

		}
		
		if(params.keySet().containsAll(["chromosome", "position"])) {
			if(params.position == "none") {
				chromosome = params.chromosome;
				positions = vcf.getPositionsForChromosome(params.chromosome);
				bins = homeService.createBins(positions);
				def groups = [:]
				for(int i = 0; i < bins.size(); i++) {
					groups[i] = bins[i];
				}
				model["groups"] = groups;
			}
			
			else {
				position = params.int("position");
				record = vcf.fetch(chromosome, position);
				model["reference"] = record.reference;
				model["alternate"] = record.alternate.join(",");
			}
		}
		
		if(params.keySet().containsAll(["chromosome", "rangeIndex"])) {
			model["positions"] = bins.get(params.int("rangeIndex"));
		}
		
		if(params.keySet().containsAll(["chromosome", "rangeIndex", "position"])) {
			position = params.int("position");
			record = vcf.fetch(chromosome, position);
			model["reference"] = record.reference;
			model["alternate"] = record.alternate.join(",");
		}
		
		if(params.keySet().containsAll(["chromosome", "rangeIndex", "position", "genotype_type"])) {
			def samples = [];
			if(params.genotype_type == "het") samples = record.getHets();
			
			else if(params.genotype_type == "hom_ref") samples = record.getHomRefs();
			
			else samples = record.getHomAlts();
			model["samples"] = samples.collect {it.name};
			model["count"] = samples.length;
		}
		
		render(view:"selectGenotype", model: model);
	}*/
}