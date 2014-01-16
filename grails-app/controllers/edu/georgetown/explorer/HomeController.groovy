package edu.georgetown.explorer

import java.util.List;
import java.util.Random
import org.springframework.web.multipart.MultipartHttpServletRequest
import edu.georgetown.explorer.VcfFileRecord
import edu.georgetown.explorer.VcfCall

class HomeController {
	
	def homeService

    def index() {
		//if(session.vcf) redirect (controller:'genotype' , action:"")
		if(session["savedSampleSets"]) {
			forward(action:"showSavedCohorts")
		}
	}
	
	def showSavedCohorts() {
		List savedCohorts = session["savedSampleSets"];
		
		
	}
			
}