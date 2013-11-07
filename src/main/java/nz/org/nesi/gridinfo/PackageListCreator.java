package nz.org.nesi.gridinfo;

import java.util.Set;

public interface PackageListCreator {
	
	public String createInfoSystemStringForPackages(String package_id);
	
	public Set<String> getPackages();

}
