package edu.georgetown.explorer

import javax.servlet.http.HttpServletResponse;

import grails.converters.JSON
import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.Rserve.RConnection;

class KmController {
	
	def kmService
	def fileUploadService
	
	def test() {
		RConnection re = session["rConnection"];
		//File[] phenotypeFiles = new File(fileUploadService.getPhenotypeDir(session.dir)).listFiles();
		//log.debug("dir is: ${phenotypeFiles[0].absolutePath}")
		//re.eval("mydata <- read.table('${phenotypeFiles[0].absolutePath}', header=T, row.names=2)");
		/*log.debug(re.eval("names(mydata)").asStrings()[0]);
		String[] newSamples = ["SID001", "SID003"].toArray(new String[0]);
		String[] newPhenotypes = ["ostime"].toArray(new String[0]);
		
		re.assign("samples", newSamples);
		re.assign("phenotypes", newPhenotypes);
		double[] strExp = re.eval("mydata[samples, phenotypes]").asDoubles();*/
		log.debug re.eval("mydata")
		//re.close();
		
		render "success"
	}
	
	def explore() {
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		GenotypeFileReader reader = session["reader"];
		
		if(!phenotypeFileReader) {
			redirect (controller:'home', action:'index')
		}
		
		else {
			["headers": phenotypeFileReader.getPhenotypeNames(), "chromosomes": reader.getChromosomes()]
		}		
	}
	
	def index() {
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		if(phenotypeFileReader != null) {
			def model = ["headers": phenotypeFileReader.getPhenotypeNames()];
			if(params["cohort-type"]) model["cohortType"] = params["cohort-type"];
			if(params["cohort-name"]) model["cohortName"] = params["cohort-name"];
			return model;
		}
		
		else {
			flash.error = "Please select an existing session or upload files to create a new session";
			redirect (controller:'home', action:'index')
		}
	}
	
	def ajaxcheckSurvivalPhenotypeSet() {		
		if(session["survivalTimeVariableName"]) render("yes");
		else render("no");
	}
	
	def ajaxGetKmPoints() {
		log.debug "ajaxGetKmPoints: "+params
		List<String> cohorts = params.list("cohort-name");
		String cohortType = params["cohort-type"]?: "saved";
		log.debug "cohortType: "+cohortType
		def kmpoints = [:];
		
		if(cohortType == "temp") cohorts = [];
		
		def allCohorts = (cohortType == "saved") ? session["savedSampleCohorts"] : session["tempSampleCohorts"];
		log.debug "truth: "+cohorts.size()
		cohorts = (cohorts?.size() > 0) ? cohorts : allCohorts.collect { it.name };
		
		File[] phenotypeFiles = new File(fileUploadService.getPhenotypeDir(session.dir)).listFiles();
		
		log.debug("cohortName: "+cohorts);
		log.debug "cohorts size: "+cohorts.size();
		log.debug "allCohorts: "+allCohorts.collect { it.name };
		
		String survivalTimeVariableName = session["survivalTimeVariableName"]?:params["survival-time-variable-name"];
		String survivalEventVariableName = session["survivalEventVariableName"]?:params["survival-event-variable-name"];

		log.debug("file path: "+phenotypeFiles[0].absolutePath)
		
		kmpoints = kmService.getKmPoints(session, survivalTimeVariableName, survivalEventVariableName, cohorts, allCohorts);
		def json = [:]
		json["kmpoints"] = kmpoints
		if(cohortType == "temp") json["pValue"] = kmService.getPValue(session, survivalTimeVariableName, survivalEventVariableName, cohorts, allCohorts);
		render json as JSON;
	}
	
	def setSurvivalPhenotype() {
		log.debug "inside setSurvivalPhenotype()";
		log.debug params
		String survivalTimeVariableName = session["survivalTimeVariableName"]?:params["survival-time-variable-name"];
		String survivalEventVariableName = session["survivalEventVariableName"]?:params["survival-event-variable-name"];
		
		if(params["save-survival-preference"]) {
			session["survivalTimeVariableName"] = survivalTimeVariableName;
			session["survivalEventVariableName"] = survivalEventVariableName;
		}
		
		render (status:HttpServletResponse.SC_OK);
	}

	def createKmPlotFlow = {
		/*checkIfSurvivalPhenotypeSet {
			action {
				PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
				List<String> cohorts = params.list("cohort-name");
				String cohortType = params["cohortType"]?: "saved";
				log.debug "cohort type: "+cohortType
				log.debug "cohort names: "+cohorts
				log.debug(phenotypeFileReader.phenotypeNames);
				flow["headers"] = phenotypeFileReader.phenotypeNames;
				flow["cohorts"] = cohorts;
				flow["cohortType"] = cohortType;
				//if(session["survivalTimeVariableName"]) return success();
				//return error();				
			}
			on("success").to "calculate"
			on("error").to "setSurvivalColumn"
		}
		setSurvivalColumn {
			render (view:'describe')
			on("submit").to "calculate"
		}*/
			
		calculate {
			action {
				log.debug "calculate params: "+params
				String cohortType = params["cohort-type"]?: "saved";
				List<String> cohorts = params.list("cohort-name");
				def allCohorts = (cohortType == "saved") ? session["savedSampleCohorts"] : session["tempSampleCohorts"];
				
				cohorts = (cohorts && cohorts?.size() > 0) ? cohorts : allCohorts.collect { it.name };
				
				File[] phenotypeFiles = new File(fileUploadService.getPhenotypeDir(session.dir)).listFiles();
				
				RConnection re = session["rConnection"];
				
				log.debug("cohortName: "+cohorts);
				
				String survivalTimeVariableName = session["survivalTimeVariableName"]?:params["survival-time-variable-name"];
				String survivalEventVariableName = session["survivalEventVariableName"]?:params["survival-event-variable-name"];
				
				if(params["save-survival-preference"]) {
					session["survivalTimeVariableName"] = survivalTimeVariableName;
					session["survivalEventVariableName"] = survivalEventVariableName;
				}

				log.debug("file path: "+phenotypeFiles[0].absolutePath)				
				
				def model = ["kmpoints": 
					kmService.getKmPoints(re, phenotypeFiles[0].absolutePath, survivalTimeVariableName, survivalEventVariableName, cohorts, allCohorts)]
				return model;
			}
			on("success").to "plot"
		}
		
		plot {
			render(view: 'plot')
		}

	}
}
