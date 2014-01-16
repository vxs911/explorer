package edu.georgetown.explorer

import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

@Transactional
class FileUploadService {
	
	def grailsApplication;

    public List<String> uploadPlinkFiles(MultipartHttpServletRequest mpr) {
		
		String dirName = grailsApplication.config.grails.uploadDir+File.separator+UUID.randomUUID().toString();
		new File(dirName).mkdir();
		List<String> files = uploadFiles(mpr, "plink", dirName);
		
		/*MultipartFile bim = null;
		MultipartFile tfam = null;
		MultipartFile bed = null;
		
		for (MultipartFile file : files) {
			if(file.originalFilename.endsWith(".bim")) bim = file;
			
			else if(file.originalFilename.endsWith(".tfam")) tfam = file;
			
			else if(file.originalFilename.endsWith(".bed")) bed = file;
		}
		
		log.debug("upload dir is: "+grailsApplication.config.grails.uploadDir);

		
		bim.transferTo(new File(dirName+File.separator+bim.originalFilename));
		tfam.transferTo(new File(dirName+File.separator+tfam.originalFilename));
		bed.transferTo(new File(dirName+File.separator+bed.originalFilename));
		
		log.debug(["bim":dirName+File.separator+bim.originalFilename, "tfam":dirName+File.separator+tfam.originalFilename, "bed":dirName+File.separator+bed.originalFilename]);
		
		return ["bim":dirName+File.separator+bim.originalFilename, "tfam":dirName+File.separator+tfam.originalFilename, "bed":dirName+File.separator+bed.originalFilename];
		*/
		return files;
	}
	
	public String uploadPhenotypeFile(MultipartHttpServletRequest mpr, String dir) {
		return uploadFile(mpr, "phenotypeFile", dir);
	}
	
	public List<String> uploadFiles(MultipartHttpServletRequest mpr, String name, String dir) {
		List<MultipartFile> files = mpr.getFiles(name);
		List<String> uploaded = new ArrayList<String>();
		
		for (MultipartFile file : files) {
			String fileName = dir+File.separator+file.originalFilename;
			file.transferTo(new File(fileName));
			uploaded << fileName;
		}
		return uploaded;
	}
	
	public String uploadFile(MultipartHttpServletRequest mpr, String name, String dir){
		return uploadFiles(mpr, name, dir).get(0);
	}
}