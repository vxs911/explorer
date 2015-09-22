package edu.georgetown.explorer

import java.util.List;
import java.util.Random
import org.springframework.web.multipart.MultipartHttpServletRequest
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils
import edu.georgetown.explorer.VcfFileRecord
import edu.georgetown.explorer.VcfCall
import grails.converters.JSON
import org.apache.commons.io.FileUtils
import static grails.async.Promises.*

class HomeController {
	
	def homeService, fileUploadService, genotypeService
	def springSecurityService, messages

    def index() {
		List userFiles = UserFiles.findAllByUser(springSecurityService.getCurrentUser())
		Messages messages = new Messages();
		int messageId = messages.addMessage("Batch processing started");
		//session["batch_data"] = ["available":"yes", "ready":"no", "acknowledged":"no"]; //need to change logic
		//log.debug "ready is: "+session?.batch_data?.ready;
		homeService.processBatchJob(messages, messageId);
		//background();
		session["messages"] = messages;
		return ["userFiles":userFiles, "messages":messages.getAllMessages()]
	}
	
	def background() {
		def ctx = startAsync();
		log.debug "started";
		ctx.start {
			Thread.sleep(10 * 1000);
			log.debug("rendering message");
			Messages messages = session["messages"];
			messages.addMessage("Batch processinc complete");
			//session["batch_data"]["ready"] = true;
			render (status:200, text:"message from server");
			ctx.complete();
		}
		log.debug "ended";
	}
	
	def showSavedCohorts() {
		List savedCohorts = session["savedSampleSets"];
	}
	
	def start() {
		render(view:'start')
	}
	
	def loadSession() {
		String dir = params["dir"]?.trim();
		if(dir != session["dir"]) {
			UserFiles files = UserFiles.findByDir(dir);
			if(files) {
				log.debug "loadSession found userfiles"
				session["dir"] = dir;
				Map readers = genotypeService.readUploadedFiles(files);
				session["type"] = files.type;
				session["reader"] = readers?.reader;
				session["phenotypeFileReader"] = readers?.phenotypeFileReader;
				session["survivalTimeVariableName"] = null;
				session["survivalEventVariableName"] = null;
			}
			else {
				// SEND TO ERROR PAGE
			}
		}
		forward controller:'phenotype', action: "summary";
	}
	
	def deleteSession() {
		String dir = params["dir"]?.trim();
		log.debug("deleting session: "+dir)
		UserFiles files = UserFiles.findByDir(dir);
		try {
			files.delete();
			File fileDir = new File(fileUploadService.getDir(dir));
			FileUtils.deleteDirectory(fileDir);
			render (status:HttpServletResponse.SC_OK);
		} catch(Exception e) {
			log.error("error deleting user session")
		}		
	}
	
	def renameSession() {
		String dir = params["dir"]?.trim();
		String newName = params["new_name"]?.trim();
		log.debug("renaming session: "+dir)
		UserFiles files = UserFiles.findByDir(dir);
		try {
			files.identifier = newName;
			files.save(flush: true);
			render (status:HttpServletResponse.SC_OK);
		} catch(Exception e) {
			log.error("error deleting user session")
		}
	}
			
}