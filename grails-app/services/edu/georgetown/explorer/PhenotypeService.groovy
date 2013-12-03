package edu.georgetown.explorer

import grails.transaction.Transactional

@Transactional
class PhenotypeService {

    def getPhenotypeReader(String fileName) {
		log.debug("/Users/varun/dev/explorer/uploads/"+fileName)
		File file = new File("/Users/varun/dev/explorer/uploads/"+fileName);
		return new PhenotypeFileReader(file);
    }
	
	def getKmPoints(List survivalData) {
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
	}
}
