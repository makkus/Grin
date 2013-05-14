package nz.org.nesi.gridinfo

import grisu.grin.YnfoManager
import grisu.jcommons.model.info.Package

import org.slf4j.Logger
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException

class InfoFileManager {
	
	public static final Logger myLogger = LoggerFactory.getLogger(InfoFileManager.class)
	
	public static Set<Package> createPackageList(def file_or_folder, boolean app_structure=false, String filter=null) {
		
		if ( YnfoManager.CURRENT_LOCAL_CONFIG ) {
			String current = new File(YnfoManager.CURRENT_LOCAL_CONFIG).getParent()
			
			if ( file_or_folder instanceof String && ! new File(file_or_folder).isAbsolute() ) {
				file_or_folder = current + File.separator + file_or_folder
				 myLogger.debug("Using relative file: "+file_or_folder)
			}
		}
		
		InfoFileManager ifm = new InfoFileManager(file_or_folder, app_structure, filter)
		return ifm.getPackages()
	}

	static main(def args) {

		GrinMainParameters mainParams = new GrinMainParameters()
		
		GrinCreateParameters createParams = new GrinCreateParameters()
		GrinTestParameters testParams = new GrinTestParameters()

		JCommander jc = new JCommander(mainParams)
		
		jc.setProgramName("grin")
		
		jc.addCommand("create", createParams)
		jc.addCommand("test", testParams)
		
		try {
			jc.parse(args)
		} catch (ParameterException pe) {

			println 'Error running grin: '+pe.getLocalizedMessage()+'\n'
			jc.usage()
			System.exit(1)
		}

		if ( "create" == jc.getParsedCommand() ) {
			GrinCreateCommand gcc = new GrinCreateCommand(createParams)
			gcc.execute()
			System.exit(0)
		} else if ( "test" == jc.getParsedCommand() ) {
			GrinConfigTester gct = new GrinConfigTester(testParams)
			gct.execute()
			System.exit(0)
		} else {
			
			jc.usage()
			System.exit(0)
		
		}
				

	}
	
	private final PackageListCreator plc
	
	public InfoFileManager(def file_or_folder, boolean app_structure=false, String filter=null) {

		if ( file_or_folder instanceof String ) {
			file_or_folder = new File(file_or_folder)
		}
		
		if ( file_or_folder.isFile() ) {
			plc = new ApplicationListManager(file_or_folder)
		} else {
			plc = ModuleFileManager.create(file_or_folder, app_structure, filter)
		}

	}
	
	public PackageListCreator getPackageListCreator() {
		return plc
	}
	
	public String createInfoSystemStringForPackages(String id) {
		return plc.createInfoSystemStringForPackages(id)
	}
	
	public Set<Package> getPackages() {
		return plc.getPackages();
	}
}
