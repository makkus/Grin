package nz.org.nesi.gridinfo

class GrinCreateCommand {

	private final PackageListCreator plc
	private final String id
	private final String infoTemplate

	public GrinCreateCommand(GrinCreateParameters params) {
		
		this(params.getModules(), params.isAppFolderStructure(), params.getFilter(), params.getAppNameList(), params.getPackageListIdentifier(), params.getInfoTemplate())
		
	}

	public GrinCreateCommand(def file_or_folder, boolean app_structure=false, String filter=null, String appNameList=null, String id=null, String infoTemplate) {
		
		if ( appNameList ) {
			Application.setApplicationNameList(appNameList)
		}


		if ( file_or_folder instanceof String ) {
			file_or_folder = new File(file_or_folder)
		}


		if ( file_or_folder.isFile() ) {
			if ( ! id ) {
				id = FilenameUtils.getBaseName(file_or_folder.getAbsolutePath())+'_pkgs'
			}
		} else {
			if ( ! id ) {
				println 'No package list id specified, please use the -i option'
				System.exit(1)
			}
		}
		
		this.id = id
		if ( infoTemplate instanceof String ) {
			this.infoTemplate = new File(infoTemplate)
		} else {
			this.infoTemplate = infoTemplate
		}

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
	
	public String createInfoSystemStringForPackages() {
		return plc.createInfoSystemStringForPackages(id)
	}
	
	public Set<Package> getPackages() {
		return plc.getPackages();
	}
	
	public void execute() {
		def packages_config_string = createInfoSystemStringForPackages()
		
				if ( infoTemplate ) {
					def template = infoTemplate.text
					int index = template.indexOf(id)
					if ( index < 0 ) {
						println 'No identifier "'+id+'" found in template: '+infoTemplate.toString()
						System.exit(1)
					}
					def replaced = template.replace('//'+id, packages_config_string)
					println replaced
				} else {
					println packages_config_string
				}
	}
}
