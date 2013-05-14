package nz.org.nesi.gridinfo;

import com.beust.jcommander.Parameter;


public class GrinCreateParameters {
	
	@Parameter(names = {"-a", "--app-folder-structure"}, description = "use the app folder structure instead of module directory with only module")
	private boolean use_app_folder_structure = false;

	@Parameter(names = { "-p", "--path" }, description = "the path to the folder containing the modules or the file containing a list of applications (in the format <APP>/<VERSION>)", required = true)
	private String modules;
	
	@Parameter(names = {"-i", "--id"}, description = "the identifier used for the resulting list of packages (something like: 'akl_pgks')")
	private String package_list_identifier;
	
	@Parameter(names = {"-t", "--info-template"}, description = "read info template file and replace id with list of packages", required = false)
	private String infoTemplate;
	
	@Parameter(names = {"--application-name-list"}, description = "file of list of preferred application names")
	private String appnameList;
	
	@Parameter(names = {"--filter", "-f"}, description = "filter in case of app folder structure, e.g. 'pan' to only use modules with 'pan' in the path")
	private String filter;

	public boolean isAppFolderStructure() {
		return use_app_folder_structure;
	}
	
	public String getModules() {
		return modules;
	}
	
	public String getPackageListIdentifier() {
		return package_list_identifier;
	}
	
	public String getInfoTemplate() {
		return infoTemplate;
	}
	
	public String getAppNameList() {
		return appnameList;
	}
	
	public String getFilter() {
		return filter;
	}

}
