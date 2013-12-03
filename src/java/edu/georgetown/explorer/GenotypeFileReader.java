package edu.georgetown.explorer;

import java.util.List;
import java.util.Set;

public interface GenotypeFileReader {

	public List<Integer> getPositionsForChromosome(String chromosome);
	
	public Set<String> getChromosomes();
	
	public String[] getSamples();
	
	public GenotypeFileRecord fetch(String chromosome, int position);
}
