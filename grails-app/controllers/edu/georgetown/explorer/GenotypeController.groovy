package edu.georgetown.explorer

import org.springframework.web.multipart.MultipartHttpServletRequest
import edu.georgetown.explorer.VcfFileRecord
import edu.georgetown.explorer.VcfCall
import org.apache.commons.lang3.StringUtils;

class GenotypeController {
	
	def genotypeService, fileUploadService

    def index() { 
		
	}
	
	def selectGenotypeFileFlow = {
		getFileType {
			action {
				def inputFileType = params["inputFileType"]?.trim()
				
				if(inputFileType == "VCF") {
					selectVcf();
				}
				
				else if(inputFileType == "PLINK") {
					selectPlink();
				}
				
				else {
					afterUpload();
				}
			}
			on("afterUpload").to "afterUpload"
			on("selectVcf").to "selectVcf"
			on("selectPlink").to "selectPlink"
		}
		
		selectVcf {
			render(view: 'selectVcf')
			on("submit").to "uploadVcf"
		}
		
		selectPlink {
			render(view: 'selectPlink')
			on("submit").to "uploadPlink"
		}
		
		uploadVcf {
			action {
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
			}
			on("success").to "summary"
		}
		
		uploadPlink {
			action {
				MultipartHttpServletRequest mpr = (MultipartHttpServletRequest) request;
				List<String> files = fileUploadService.uploadPlinkFiles(mpr);

				GenotypeFileReader reader = GenotypeFileReader.getFileReader(new File(files.get(0)));
				reader.read();
				Individual[] individuals = reader.getIndividuals();
				
				def model = ["count":individuals.length, "samples":individuals];
				session["type"] = StringUtils.splitByCharacterTypeCamelCase(reader.getClass().getSimpleName())[0].toUpperCase();
				session["reader"] = reader;
				session["dir"] = new File(files.get(0)).getParent();
				return model;
			}
			on("success").to "afterUpload"
		}
		
		afterUpload {
			action {
				Class _class = session["reader"].getClass();
				String type = "";
				if(_class == PlinkFileReader.class) type = "PLINK";
				
				else if(_class == VcfFileReader.class) type = "VCF";
				return ["type":type]
			}
			
			on("success").to "summary"
		}
		
		summary {
			render(view: "summary")
		}
	}
	
	def selectGenotypeFlow = {
		getChromosome {
			action {
				flow.remove("mode");
				flow.remove("chromosome");
				flow.remove("samples");
				flow.remove("position");
				flow.remove("positions");
				flow.remove("reference");
				flow.remove("alternate");
				flow.remove("genotype_type");
				GenotypeFileReader reader = session["reader"];
				def chromosomes = reader.chromosomes;
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
				if(params["chromosome"]) flow["chromosome"] = params["chromosome"]
				flow.remove("position");
				flow.remove("samples");
				flow.remove("positions");
				flow.remove("reference");
				flow.remove("alternate");
				flow.remove("genotype_type");
			}
			
			on("success").to "typePosition"
			on("changeChromosome").to "getChromosome"
		}
		
		typePosition {
			render (view: 'select')
			on("submit").to "getGenotypes"
			on("changeMode").to "getRangeIndex"
			on("changeChromosome").to "getChromosome"
		}
		
		getRangeIndex {
			action {
				log.debug("inside getRangeIndex")
				flow["mode"] = "range";
				flow.remove("rangeIndex");
				flow.remove("positions");
				flow.remove("position");
				flow.remove("samples");
				flow.remove("positions");
				flow.remove("reference");
				flow.remove("alternate");
				flow.remove("genotype_type");
				
				GenotypeFileReader reader = session["reader"];
				def chromosome = flow["chromosome"];
				def positions = reader.getPositionsForChromosome(chromosome);
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
			on("changeChromosome").to "getChromosome"
		}
		
		getPositions {
			action {
				def positions = flow["groups"].get(params.int("rangeIndex"));
				def model = [positions:positions, "rangeIndex":params["rangeIndex"] ];
				
				return model;
			}
			on("success").to "selectPosition"
		}
		
		selectPosition {
			render(view: 'select')
			on("changeRangeIndex").to "getRangeIndex"
			on("submit").to "getGenotypes"
			on("changeChromosome").to "getChromosome"
		}
		
		getGenotypes {
			action {
				String position = params["position"]?: flow["position"];
				def chromosome = flow["chromosome"];
				flow.remove("genotype_type");
				flow.remove("samples");
				
				GenotypeFileReader reader = session["reader"];
				def record = reader.fetch(chromosome, position.toInteger());
				return ["position":position, "reference":record.reference, "alternate":record.alternates.join(",")]
			}
			on("success").to "showGenotypes"
		}
		
		showGenotypes {
			render(view: 'select')
			on("submit").to "getSamples"
			on("changeChromosome").to "getChromosome"
			on("changePositionWithRange").to "getPositions"
			on("changePositionWithType").to "typePosition"
		}
		
		getSamples {
			log.debug("enter");
			action {
				List<GenotypeCall> calls;
				def samples = [];
				GenotypeFileReader reader = session["reader"];
				String position = flow["position"];
				String chromosome = flow["chromosome"];
				GenotypeFileRecord<? extends GenotypeCall, ?> record = reader.fetch(chromosome, position.toInteger());
				if(params["genotype_type"] == "het") calls = record.getHets();
				
				else if(params["genotype_type"] == "hom_ref") calls = record.getHomRefs();
				
				else calls = record.getHomAlts();
				samples = calls?.collect {it.getIndividual().getId()};
				
				if(!samples || samples.size() == 0) samples << "None found"
				return ["genotype_type":params["genotype_type"], "samples":samples, "count": samples.size()]
			}
			on("success").to "showSamples"
		}
		
		showSamples {
			render(view: 'select')
			on("startOver").to "getChromosome"
			on("saveSamples").to "saveSamples"
			on("changeChromosome").to "getChromosome"
			on("changePosition").to "enterPosition"
			on("changeGenotype").to "getGenotypes"
		}
		
		saveSamples {
			action {
				def sampleName = params["samplename"];
				def savedSamples = session["savedSamples"]
				
				if(!savedSamples) savedSamples = [:];
				
				savedSamples[sampleName] = flow["samples"];
				session["savedSamples"] = savedSamples
				return ["samplename":sampleName]
			}
			on("success").to "saveSuccess"
		}
		
		saveSuccess {
			render(view: "saveSuccess")
		}
	}

}
