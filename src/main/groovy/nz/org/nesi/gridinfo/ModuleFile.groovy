package nz.org.nesi.gridinfo

import org.apache.commons.io.FilenameUtils;

class ModuleFile {
	
	public static String parseApplication() {
		
	}
	
	private final File moduleFile
	private final File moduleFolder
	
	private final String application
	private final String version
	private final String moduleString
	
	public ModuleFile(def path, def moduleFolderPath=null) {
		if (path instanceof File) {
			moduleFile = path
		} else if (path instanceof String) {
			moduleFile = new File(path)
		} else {
			throw new RuntimeException("Not a file: "+path)
		}
		
		if ( ! moduleFolderPath ) {
			moduleString = (moduleFile.getAbsolutePath() - ( moduleFile.getParentFile().getParent() + File.separator )).replace(File.separator+'modules', '')
		} else {
		
			if (moduleFolderPath instanceof File) {
				moduleFolder = moduleFolderPath
			} else if (moduleFolderPath instanceof String) {
				moduleFolder = new File(moduleFolderPath)
			} else {
				throw new RuntimeException("Not a file: "+path)
			}
		
			int index_of_app_folder = moduleFile.getAbsolutePath().indexOf(File.separator, moduleFolder.getAbsolutePath().size()+1)
			String path_app_folder = moduleFile.getAbsolutePath().substring(0, index_of_app_folder)
			String app = FilenameUtils.getName(path_app_folder)
			moduleString = app + File.separator + FilenameUtils.getName(moduleFile.getAbsolutePath()) 
		}
		
		application = moduleString.split(File.separator)[0]
		version = moduleString.split(File.separator)[1]

	}
	
	public boolean isAvailableFor(String queue) {
		if ( !moduleFolder ) {
			return false
		} else {
			return moduleFile.getAbsolutePath().contains(queue)
		}
	}

	public File getModuleFile() {
		return moduleFile
	}
	
	public String getModuleString() {
		return moduleString
	}
	
	
	public String getApplication() {
		return application
	}
	
	public String getVersion() {
		return version
	}
	
	@Override
	public String toString() {
		return getModuleString()
	}
}
