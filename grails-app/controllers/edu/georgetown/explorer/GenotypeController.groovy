package edu.georgetown.explorer

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest
import edu.georgetown.explorer.VcfFileRecord
import edu.georgetown.explorer.VcfCall
import grails.converters.JSON
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

class GenotypeController {
	
	def genotypeService, fileUploadService
	def springSecurityService
	def grailsApplication

    def index() { 
		
	}
	
	def ajaxGetRsIdsByGeneSym() {
		String symbol = params["sym"]
		GenotypeFileReader reader = session["reader"]
		def rsIds = []
		List<Integer> allRsIds = reader.getAllRsIds()
		
		withHttp(uri: "http://"+grailsApplication.config.annotate.host+":"+grailsApplication.config.annotate.port) {
			def html = get(path : '/annotate/gene', contentType : groovyx.net.http.ContentType.JSON, query : [hgnc_sym:symbol, requested:'rsid']) { resp, json ->
				json["rsids"].each { rsId ->
					//Integer rsId = NumberUtils.toInt(StringUtils.removePattern(it, "[^0-9]"));
					if(allRsIds.contains(rsId)) rsIds << rsId
				}
			}
		 }
		render rsIds as JSON
	}
	
	def ajaxGetGenotypeByRsId() {
		String rsId = params["rs_id"]?.trim();
		GenotypeFileReader reader = session["reader"];
		GenotypeFileRecord record = null;
		def jsonObject = [:];
		
		if(rsId != null) {
			record = reader.fetchByRsId(rsId);
			jsonObject = genotypeService.getGenotypeFromRecord(record, session);
		}
		
		render jsonObject as JSON
	}
	
	def ajaxGetGenotypeByChromosomeAndPosition() {
		String chromosome = params["chromosome"]?.trim();
		int position = params.int("position");
		log.debug "chromosome is: ${chromosome} and position is: ${position}"
		GenotypeFileReader reader = session["reader"];
		GenotypeFileRecord record = null;
		def jsonObject = [:];
		
		if(chromosome && position) {
			record = reader.fetch(chromosome, position);
			jsonObject = genotypeService.getGenotypeFromRecord(record, session);
		}
		
		render jsonObject as JSON
	}
	
	def ajaxSaveCohort() {
		String genotypeType = params["genotype_type"]?.trim();
		String name = params["cohort_name"].trim();
		List<GenotypeCall> calls = null;
		String[] individualIds = null;
		
		if(genotypeType == "het") {
			individualIds = session["tempSampleCohorts"].get(0)["individuals"];
		}
		
		else if(genotypeType == "hom_ref") {
			individualIds = session["tempSampleCohorts"].get(1)["individuals"];
		}
		
		else {
			individualIds = session["tempSampleCohorts"].get(2)["individuals"];
		}
		
		def savedSampleCohorts = session["savedSampleCohorts"] ?: [];
		
		savedSampleCohorts << ["name":name, "individuals":individualIds];
		
		session["savedSampleCohorts"] = savedSampleCohorts;
		log.debug "saved ${name} cohort of type ${genotypeType} with ${individualIds.length} individuals";
		log.debug "individuals are: "+individualIds
		
		render(status:HttpServletResponse.SC_OK);
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
				session["reader"] = null;
				session["type"] = null;
				session["dir"] = null;
				session["phenotypeFileReader"] = null;
				session["survivalTimeVariableName"] = null;
				
				MultipartHttpServletRequest mpr = (MultipartHttpServletRequest) request;
				String dir = fileUploadService.uploadVcfFile(mpr);
				log.debug "dir is: "+dir
				GenotypeFileReader reader = GenotypeFileReader.getFileReader(new File(fileUploadService.getGenotypeDir(dir)).listFiles()[0]);
				session["dir"] = dir;
				
				UserFiles userFiles = new UserFiles();
				userFiles.user = springSecurityService.getCurrentUser();
				userFiles.dir = dir;
				userFiles.type = StringUtils.splitByCharacterTypeCamelCase(reader.getClass().getSimpleName())[0].toUpperCase();;
				userFiles.identifier = (params["identifier"] ?: userFiles.dir).trim();
				userFiles.phenotypeFileDesc = null;
				userFiles.dateCreated = new Date();
				userFiles.lastUpdated = new Date();
				userFiles.save(flush: true);
				flow["type"] = userFiles.type;
				
			}
			on("success").to "summary"
		}
		
		uploadPlink {
			action {
				session["reader"] = null;
				session["type"] = null;
				session["dir"] = null;
				session["phenotypeFileReader"] = null;
				session["survivalTimeVariableName"] = null;
				
				MultipartHttpServletRequest mpr = (MultipartHttpServletRequest) request;
				String dir = fileUploadService.uploadPlinkFiles(mpr);
				log.debug "dir is: "+dir
				GenotypeFileReader reader = GenotypeFileReader.getFileReader(new File(fileUploadService.getGenotypeDir(dir)).listFiles()[0]);
				session["dir"] = dir;
				
				UserFiles userFiles = new UserFiles();
				userFiles.user = springSecurityService.getCurrentUser();
				userFiles.dir = dir;
				userFiles.type = StringUtils.splitByCharacterTypeCamelCase(reader.getClass().getSimpleName())[0].toUpperCase();;
				userFiles.identifier = (params["identifier"] ?: userFiles.dir).trim();
				userFiles.phenotypeFileDesc = null;
				userFiles.dateCreated = new Date();
				userFiles.lastUpdated = new Date();
				userFiles.save(flush: true);
				flow["type"] = userFiles.type;
			}
			on("success").to "summary"
		}
		
		afterUpload {
			action {
				/*Class _class = session["reader"].getClass();
				String type = "";
				if(_class == PlinkFileReader.class) type = "PLINK";
				
				else if(_class == VcfFileReader.class) type = "VCF";
				return ["type":type]*/
			}
			
			on("success").to "summary"
		}
		
		summary {
			render(view: "summary")
		}
	}
	
	def selectGenotypeFlow = {
		getRsid {
			action {
				def context = flow.persistenceContext;
				flow.clear();
				flow.persistenceContext = context;
			}
			on("success").to "typeRsid"
		}
		typeRsid {
			render(view: "select")
			on("submit").to "getGenotypes"
			on("doNotKnow").to "getChromosome"
		}
		getChromosome {
			action {
				def context = flow.persistenceContext;
				flow.clear();
				flow.persistenceContext = context;
				
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
				flow.remove("individuals");
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
				flow.remove("reference");
				flow.remove("alternate");
				flow.remove("genotype_type");
				flow.remove("individuals");
				
				Integer rangeIndex = flow["rangeIndex"]?: params.int("rangeIndex")
				def positions = flow["groups"].get(rangeIndex);
				def model = [positions:positions, "rangeIndex":rangeIndex];
				
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
				GenotypeFileRecord record = null;
				GenotypeFileReader reader = session["reader"];
				String rsId = flow["rsId"]?: params["rs_id"]?.trim();
				if(rsId != null) {
					record = reader.fetchByRsId(rsId);
					flow["rsId"] = rsId;
					log.debug "record is: "+record.rsid
					log.debug "position is: "+record.position
				}
				
				else {
					String position = flow["position"]?: params["position"] ;
					String chromosome = flow["chromosome"];
					record = reader.fetch(chromosome, NumberUtils.toInt(position));
					flow["position"] = position;
				}

				flow.remove("genotype_type");
				flow.remove("individuals");
				 
				flow["reference"] = record.reference;
				flow["alternate"] = record.alternates.join(",");	
				flow["count"] = ["het":record.getHets().size(), "hom_ref":record.getHomRefs().size(),"hom_alt":record.getHomAlts().size()];			
			}
			on("success").to "showGenotypes"
		}
		
		showGenotypes {
			render(view: 'select')
			on("submit").to "getSamples"
			on("saveSamples").to "getSamples"
			on("changeRsId").to "getRsid"
			on("changeChromosome").to "getChromosome"
			on("changeRangeIndex").to "getRangeIndex"
			on("changePositionWithRange").to "getPositions"
			on("changePositionWithType").to "typePosition"
			on("createKmPlot").to "createKmData"
		}
		
		getSamples {
			action {
				log.debug("enter getSamples");
				log.debug params
				List<GenotypeCall> calls;
				GenotypeFileReader reader = session["reader"];

				GenotypeFileRecord<? extends GenotypeCall, ?> record = null;
				if(flow["rsId"]) {
					record = reader.fetchByRsId(flow["rsId"]);
				}
				else {
					String position = flow["position"];
					String chromosome = flow["chromosome"];
					record = reader.fetch(chromosome, NumberUtils.toInt(position));
				}
				if(params["genotype_type"] == "het") calls = record.getHets();
				
				else if(params["genotype_type"] == "hom_ref") calls = record.getHomRefs();
				
				else calls = record.getHomAlts();
				def individuals = calls?.collect {it.getIndividual()}.toArray();
				//def model = ["genotype_type":params["genotype_type"]];
				//if(individuals && individuals.length > 0) model["individuals"] = individuals;  
				
				def cohortName = params["cohort-name"];
				def savedSampleCohorts = session["savedSampleCohorts"] ?: [];
				
				savedSampleCohorts << ["name":cohortName, "individuals":individuals];
				session["savedSampleCohorts"] = savedSampleCohorts
				
				flow["cohortName"] = cohortName;		
				//return model;
			}
			on("success").to "saveSuccess"
		}
		
		showSamples {
			render(view: 'select')
			on("startOver").to "getChromosome"
			on("saveSamples").to "saveSamples"
			on("changeRsId").to "getRsid"
			on("changeChromosome").to "getChromosome"
			on("changePositionWithRange").to "getPositions"
			on("changePosition").to "enterPosition"
			on("changeGenotype").to "getGenotypes"
		}
		
		saveSamples {
			action {
				def cohortName = params["cohort-name"];
				def savedSampleCohorts = session["savedSampleCohorts"] ?: [];
				
				savedSampleCohorts << ["name":cohortName, "individuals":flow["individuals"]];
				session["savedSampleCohorts"] = savedSampleCohorts
				return ["cohortName":cohortName]
			}
			on("success").to "saveSuccess"
		}
		
		createKmData {
			action {
				List<GenotypeCall> calls = null;
				GenotypeFileReader reader = session["reader"];
				GenotypeFileRecord<? extends GenotypeCall, ?> record = null;
				
				if(flow["rsId"]) {
					record = reader.fetchByRsId(flow["rsId"]);
				}
				else {
					String position = flow["position"];
					String chromosome = flow["chromosome"];
					record = reader.fetch(chromosome, NumberUtils.toInt(position));
				}
				
				def individuals = null;
				def tempSampleCohorts = [];
				
				calls = record.getHets();
				individuals = calls?.collect {it.getIndividual()}.toArray();
				tempSampleCohorts << ["name":record.reference+"-"+record.alternates[0], "individuals":individuals];
				log.debug "temp1 size: "+individuals.length;
				
				calls = record.getHomRefs();
				individuals = calls?.collect {it.getIndividual()}.toArray();
				tempSampleCohorts << ["name":record.reference+"-"+record.reference, "individuals":individuals];
				log.debug "temp2 size: "+individuals.length;
				
				calls = record.getHomAlts();
				individuals = calls?.collect {it.getIndividual()}.toArray();
				tempSampleCohorts << ["name":record.alternates[0]+"-"+record.alternates[0], "individuals":individuals];	
				log.debug "temp3 size: "+individuals.length;
				
				//log.debug "temp cohorts: "+tempSampleCohorts
				
				session["tempSampleCohorts"] = tempSampleCohorts;
				
			}
			on("success").to "createKmPlot"
		}
		
		createKmPlot {
			redirect (controller:"km", action:'index', params: ["cohort-type": "temp"])
		}
		
		saveSuccess {
			render(view: "saveSuccess")
		}
	}

}
