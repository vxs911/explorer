package edu.georgetown.explorer;

public interface GenotypeFileRecord {

	public String getChromosome();
	public void setChromosome(String chromosome);
	
	public Integer getPosition();
	public void setPosition(Integer position);
	
	public String getRsid();
	public void setRsid(String rsid);
	
	public String getReference();
	public void setReference(String reference);
	
	public String[] getAlternates();
	public void setAlternates(String[] alternates);
}