package edu.georgetown.explorer

import java.util.List;
import javax.servlet.http.HttpSession
import org.rosuda.REngine.Rserve.RConnection
import edu.georgetown.explorer.tabular.TabularFileReader
import grails.transaction.Transactional

@Transactional
class KmService {
	
	def grailsApplication
	
	//static scope = "flow";

	/*def getKmPoints(List survivalData) {
		List sortedSurvivalData = survivalData.sort {a, b ->
			a.survival <=> b.survival
		}
		
		def points = []
		int total = survivalData.size()
		float curSurvivalTime = 0.0, prevSurvivalTime = 0.0
		float fraction = 1.0
		int dead = 0
		
		for(int i = 0; i < total; i++) {
			if(i == 0) {
				points << ["x":curSurvivalTime, "y":fraction];
				dead++;
			}
			
			else {
				curSurvivalTime = sortedSurvivalData[i]["survival"];
				points << ["x":curSurvivalTime, "y": fraction]
				fraction = (total - dead)/total;
				points << ["x":curSurvivalTime, "y": fraction]
				dead++;		
			}
			
		}
		
		return points
	}*/
	
	def getKmPoints(HttpSession session, String survivalTimeVariableName, String survivalEventVariableName, List<String> requestedCohorts, List allCohorts) {			
		Map<String, Individual[]> samples = [:];
		def kmpoints = [:]
		RConnection re = session["rConnection"];
		GenotypeFileReader reader = session["reader"]
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		TabularFileReader tabFileReader = phenotypeFileReader.getTabularFileReader();
		String[] allCensoredRowIds = tabFileReader.getRowIdsWithCellValueEqualTo(survivalEventVariableName, "0");
		
		log.debug "cohort list size: "+requestedCohorts.size();
		
		requestedCohorts.each { name ->
			log.debug "name is: "+name
			samples[name] = allCohorts.find { it.name == name }["individuals"]
		}

		//re.eval("mydata <- read.table('${phenotypeFileName}', header=T, row.names=2)");
		
		samples.each { key, value ->
			//String[] ids = value.collect {it.id}.toArray(new String[0])
			if(value.length > 0) {
				//re.assign("samples", value);
				String[] sampleIds = value;
				re.assign("samples", sampleIds);
				//re.eval("samples=c(\"${value.join('\",\"')}\")")
				List<Float> survivalArr = new ArrayList<Float>();
				List<Integer> eventArr = new ArrayList<Integer>();
				
				for(int i = 0; i < value.length; i++) {
					survivalArr << tabFileReader.getCell(value[i], survivalTimeVariableName).getValueAsFloat();
					eventArr << tabFileReader.getCell(value[i], survivalEventVariableName).getValueAsInt();
				}
				re.eval("time=c(${survivalArr.join(',')})");
				re.eval("status=c(${eventArr.join(',')})");
				
				//re.eval("s=Surv(mydata[samples, survival], mydata[samples, event])");
				re.eval("s=Surv(time, status)");
				re.eval("km=survfit(s~1)");
				double[] times = re.eval("km\$time").asDoubles();
				double[] survs = re.eval("km\$surv").asDoubles();
				
				def points = [];
				float curSurv = 1.0f;
				float curTime = 0.0f;
				//int[] censored;
				//Individual[] censored = null;
				List<String> censored = null;
				
				for(int i = 0; i <= survs.length; i++) {
					//censored = survivalTimeContainer.filterIndividualsWithPhenotypeEqualTo(allCensored, survivalTimeContainer.createPhenotype(""+curTime));
					censored = new ArrayList<String>();
					
					allCensoredRowIds.each {
						if(tabFileReader.getCell(it, survivalTimeVariableName).getValueAsFloat() == curTime) censored << it
					}
					//censored = re.eval("events = mydata[mydata\$${survivalTimeVariableName} == ${curTime} & mydata\$${survivalEventVariableName} == 0, \"${survivalEventVariableName}\"]").asIntegers();
					if(i < survs.length) {
						points << ["x":curTime, "y":curSurv, "censored":censored.size()]
						censored = new ArrayList<String>();
						allCensoredRowIds.each {
							if(tabFileReader.getCell(it, survivalTimeVariableName).getValueAsFloat() == times[i]) censored << it
						}
						//censored = survivalTimeContainer.filterIndividualsWithPhenotypeEqualTo(allCensored, survivalTimeContainer.createPhenotype(""+times[i]));
						//censored = re.eval("events = mydata[mydata\$${survivalTimeVariableName} == ${times[i]} & mydata\$${survivalEventVariableName} == 0, \"${survivalEventVariableName}\"]").asIntegers();
						points << ["x":times[i], "y":curSurv, "censored":censored.size()]
						
						curSurv = survs[i];
						curTime = times[i];
					}
				}
				points << ["x":curTime, "y": curSurv, "censored":censored.size()];
				
				kmpoints[key] = points;
			}
		}
		
		return kmpoints;
	}
	
	def getPValue(HttpSession session, String survivalTimeVariableName, String survivalEventVariableName, List<String> requestedCohorts, List allCohorts) {
		RConnection re = session["rConnection"];
		GenotypeFileReader reader = session["reader"]
		PhenotypeFileReader phenotypeFileReader = session["phenotypeFileReader"];
		TabularFileReader tabFileReader = phenotypeFileReader.getTabularFileReader();
		Map<String, Individual[]> samples = [:];
		
		requestedCohorts.each { name ->
			log.debug "name is: "+name
			samples[name] = allCohorts.find { it.name == name }["individuals"]
		}
		List<String> all = reader.getIndividuals().collect {it.id};
		String[] groupTags = new String[all.size()];
		
		for(Map.Entry<String, String[]> entry : samples.entrySet()) {
			for(String i : entry.getValue()) {
				groupTags[all.indexOf(i)] = "'G"+requestedCohorts.indexOf(entry.getKey())+"'"
			}
		}
		
		log.debug "groupsTags size is: ${groupTags.length}"
		re.assign("groups", groupTags);
		
		Float[] allTimes = tabFileReader.getColumnAsFloatArray(survivalTimeVariableName);
		Float[] allStatus = tabFileReader.getColumnAsFloatArray(survivalEventVariableName);
		re.eval("time=c(${allTimes.join(',')})");
		re.eval("status=c(${allStatus.join(',')})");
		
		re.eval("s=Surv(time, status)");
		//re.eval("diff = survdiff(s ~ groups)");
		re.eval("diff = summary(coxph(s ~ groups))");
		//double pValue = re.eval("1 - pchisq(diff\$chisq, length(diff\$n) - 1)").asDouble();
		double pValue = re.eval("diff\$waldtest[3]").asDouble();
		return pValue
	}
	
}