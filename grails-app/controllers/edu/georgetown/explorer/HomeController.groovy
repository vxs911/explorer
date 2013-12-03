package edu.georgetown.explorer

import java.util.List;
import java.util.Random
import org.springframework.web.multipart.MultipartHttpServletRequest
import edu.georgetown.explorer.VcfFileReader.VcfFileRecord
import edu.georgetown.explorer.VcfFileReader.VcfCall

class HomeController {
	
	def homeService

    def index() {
		if(session.vcf) redirect (action:"summary")
	}
			
}