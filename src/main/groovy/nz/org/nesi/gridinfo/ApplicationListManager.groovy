package nz.org.nesi.gridinfo

import grisu.jcommons.model.info.Package

import com.google.common.base.Charsets
import com.google.common.collect.Sets;
import com.google.common.io.Files

class ApplicationListManager implements PackageListCreator {
	
	private final Set<Package> packages = Sets.newTreeSet()
	
	public ApplicationListManager(def apps) {
		
		if ( apps instanceof String ) {
			apps = new File(apps)
		}
		
		if ( apps instanceof File ) {
			apps = Files.readLines(apps, Charsets.UTF_8)
		}
		
		apps.each { app ->
			if ( app.trim() ) {
				def application = app.trim().split("/")[0]
				def version = app.trim().split("/")[1]
				Package p = new Package(application, version)
				packages << p
			}
		}
	}
	
	public String createInfoSystemStringForPackages(String alias) {
		
		def allVersions = [] as Set
		def allApplications = [] as Set
		
		packages.each { pkg ->
			def temp = pkg.getApplication().getName()
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

		def pkg_list = []
				
		packages.each { pkg ->
			result = result + pkg.getAlias() + " = new Package(\n"
			result = result + '\tapplication:'+pkg.getApplication().getAlias()+',\n'
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

	@Override
	public Set<String> getPackages() {
		return packages;
	}


}
