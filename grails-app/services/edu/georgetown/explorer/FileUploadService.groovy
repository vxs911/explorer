package edu.georgetown.explorer

import grails.transaction.Transactional

import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

@Transactional
class FileUploadService {
	
	def grailsApplication;

    public String createFullPath(String dirName, String fileName) {
		return grailsApplication.config.grails.uploadDir+File.separator+dirName+File.separator+fileName;
	}
	
	public String getDir(String uuid) {
		if(uuid) return grailsApplication.config.grails.uploadDir + File.separator + uuid;
		
		return null;
	}
	
	public String getGenotypeDir(String uuid) {
		if(uuid) return grailsApplication.config.grails.uploadDir + File.separator + uuid + File.separator + "genotype";
		
		return null;
	}
	
	public String getPhenotypeDir(String uuid) {
		if(uuid) return grailsApplication.config.grails.uploadDir + File.separator + uuid + File.separator + "phenotype";
		
		return null;
	}
	
	public File getPhenotypeFile(String uuid) {
		if(uuid) {
			String dir = getPhenotypeDir(uuid);
			File[] files = new File(dir).listFiles();
			return files[0];
		}
		
		return null;
	}
	
	public boolean uploadFile(MultipartHttpServletRequest mpr, String name, String dirName){
		MultipartFile file = mpr.getFile(name);
		
		String fileName = dirName+File.separator+file.originalFilename;
		try {
			file.transferTo(new File(fileName));
			return true
		} catch(IOException e) {
			return false;
		}
	}
	
	public boolean uploadFiles(MultipartHttpServletRequest mpr, String name, String dir) {
		List<MultipartFile> files = mpr.getFiles(name);
		
		try {
			for (MultipartFile file : files) {
				String fileName = dir+File.separator+file.originalFilename;
				file.transferTo(new File(fileName));
			}
			return true;
			
		} catch(IOException e) {
			return false;
		}
	}
	
	public String uploadPhenotypeFile(MultipartHttpServletRequest mpr, String dirName) {
		if(uploadFile(mpr, "phenotypeFile", dirName)) return mpr.getFile("phenotypeFile").getOriginalFilename();
		
		return null;
	}
	
	public String uploadPlinkFiles(MultipartHttpServletRequest mpr) {	
		String uuid = UUID.randomUUID().toString();
		String dirName = grailsApplication.config.grails.uploadDir + File.separator + uuid;
		new File(dirName).mkdir();
		new File(dirName + File.separator + "genotype").mkdir();
		new File(dirName + File.separator + "phenotype").mkdir();
		if(uploadFiles(mpr, "plink", dirName + File.separator + "genotype")) return uuid;
		return null;
	}
	
	public String uploadVcfFile(MultipartHttpServletRequest mpr) {
		String uuid = UUID.randomUUID().toString();
		String dirName = grailsApplication.config.grails.uploadDir + File.separator + uuid;
		new File(dirName).mkdir();
		new File(dirName + File.separator + "genotype").mkdir();
		new File(dirName + File.separator + "phenotype").mkdir();
		if(uploadFiles(mpr, "vcf", dirName + File.separator + "genotype")) return uuid;
		return null;
	}
	
}