package nz.org.nesi.gridinfo

import grisu.grin.YnfoManager
import grisu.jcommons.model.info.Application
import grisu.jcommons.model.info.Module
import grisu.jcommons.model.info.Package
import grisu.jcommons.model.info.Version
import groovy.io.FileType

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.collect.ImmutableList
import com.google.common.collect.Maps
import com.google.common.collect.Sets

class ModuleFileManager implements PackageListCreator {
	
	static final Logger log = LoggerFactory.getLogger(ModuleFileManager.class);
	
	public static ModuleFileManager create() {
		
		def path = '/data/src/config/nesi-grid-info/examples/modules'
		
		return create(path, false)
	}
	
	public static ModuleFileManager create(def path, boolean is_app_structure, String filter=null) {

		File dir;
		if ( path instanceof String ) {
			dir = new File(path)
		} else if ( path instanceof File ) {
			dir = path
		} else {
			throw new RuntimeException("Invalid class: "+path)
		}

		def modules = []

		if ( ! is_app_structure ) {
			dir.eachFileRecurse (FileType.FILES) { file ->
				try {
					def m = new ModuleFile(file, path)
					modules << m
				} catch (Exception e) {
					log.error("Not adding module for '"+file.toString()+"': "+e.getLocalizedMessage())
				}
			}
		} else {
			dir.traverse(type: FileType.FILES) { file ->
				def p = file.getAbsolutePath()
				if ( p.contains(File.separator+"modules"+File.separator)) {
					def m = new ModuleFile(p, path)
					modules << m
				}
			}
		}
		
		
		ModuleFileManager m = new ModuleFileManager(modules, filter)
		return m
	}
	
	public static ModuleFileManager create(def dir) {

		return create(dir, false)
	}
	
	private final List<ModuleFile> modules
	
	private final SortedSet<Application> applications
	private final Map<Application, SortedSet<String>> modulesStrings = Maps.newTreeMap()
	private final Map<Application, List<ModuleFile>> moduleMap
	private final Set<Package> packages = Sets.newTreeSet()
	
	private final String filter

	public ModuleFileManager(List<ModuleFile> modules) {
		this(modules, null)
	}
	
	public ModuleFileManager(List<ModuleFile> modules, String filter) {
		
		if ( ! filter ) {
			this.modules = ImmutableList.copyOf(modules)
		} else {
			this.modules = modules.findAll { mf ->
				mf.isAvailableFor(filter)
			}
			
		}
		
		this.applications = this.modules.collect {
			Application.get(it.application)
		} as SortedSet

		moduleMap = this.modules.groupBy {
			return Application.get(it.getApplication())
		}
		
		this.applications.each { app ->
			def temp = []
			moduleMap[app].each { m ->
				Module mod = Module.create(m.getModuleString())
				temp << mod
				Package p = new Package(app, Version.get(m.getVersion()), Sets.newHashSet(), mod)
				packages << p
			}
			modulesStrings[app] = temp
		}
	}
	
	public Set<Package> getPackages() {
		return packages
	}
	
	public SortedSet<Application> getApplications() {
		
		return applications
	}
	
	public def getModuleMap() {
		return moduleMap
	}
	
	public SortedSet<Module> getModules(def application) {
		
		if ( application instanceof String) {
			application = Application.get(application)
		}
		return modulesStrings[application] as SortedSet
	}
	
	public String createInfoSystemStringForPackages(String alias) {
		
		def allVersions = [] as Set
		def allApplications = [] as Set
		def allModules = [] as Set
		
		packages.each { pkg ->
			def temp = pkg.getModule().getModule()
			temp = temp.replace("/", "_")
			temp = temp.replace("\\", "_")
			temp = temp.replace(".", "_")
			temp = temp.replace("-", "_")
			temp = temp.replace("+", "_")
			pkg.getModule().setAlias('_generated_'+alias+'_module_'+temp)
			allModules.add(pkg.getModule())
			temp = pkg.getApplication().getName()
			temp = temp.replace("/", "_")
			temp = temp.replace("\\", "_")
			temp = temp.replace(".", "_")
			temp = temp.replace("-", "_")
			temp = temp.replace("+", "_")
			temp = temp.replace(" ", "")
			pkg.getApplication().setAlias('_generated_'+alias+'_application_'+temp)
			allApplications.add(pkg.getApplication())
			temp = pkg.getVersion().getVersion()
			temp = temp.replace("/", "_")
			temp = temp.replace("\\", "_")
			temp = temp.replace(".", "_")
			temp = temp.replace("-", "_")
			temp = temp.replace("+", "_")
			temp = temp.replace(" ", "")
			pkg.getVersion().setAlias('_generated_'+alias+'_version_'+temp)
			allVersions.add(pkg.getVersion())
			temp = pkg.toString()
			temp = temp.replace("/", "_")
			temp = temp.replace("\\", "_")
			temp = temp.replace(".", "_")
			temp = temp.replace("-", "_")
			temp = temp.replace("+", "_")
			temp = temp.replace(" ", "")
			pkg.setAlias('_generated_'+alias+'_package_'+temp)
		}
		
		def result = "\n"
		allVersions.each { v ->
			result = result + v.getAlias() + " = " + "Version.get('"+v.toString()+"')\n"
		}
		result = result + "\n"
		
		allApplications.each { a ->
			result = result + a.getAlias() + " = " + "Application.get('"+a.getName()+"')\n"
		}
		result = result + "\n"

		allModules.each { m ->
			result = result + m.getAlias() + " = " + "Module.create('"+m.getModule()+"')\n"
		}
		result = result + "\n"
		
		def pkg_list = []
				
		packages.each { pkg ->
			result = result + pkg.getAlias() + " = new Package(\n"
			result = result + '\tapplication:'+pkg.getApplication().getAlias()+',\n'
			result = result + '\tmodule:'+pkg.getModule().getAlias()+',\n'
			result = result + '\tversion:'+pkg.getVersion().getAlias()+'\n'
			result = result + '\t)\n'
			pkg_list << pkg.getAlias()
		}
		result = result + "\n"
		
		result = result + alias + ' = [\n\t'
		result = result + pkg_list.join(',\n\t')
		result = result + '\n]\n'
		
		return result
		
	}
	

}
