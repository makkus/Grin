package grisu.grin

import grisu.grin.model.Grid
import grisu.grin.model.resources.*
import grisu.jcommons.configuration.CommonGridProperties
import grisu.jcommons.constants.GridEnvironment
import grisu.jcommons.model.info.*




class YnfoManager  {

	static void printConnections(def res) {

		res.getConnections().each { c ->

			println '\t\t'+c.toString()

			//			AbstractResource.getRecursiveConnections(c).each { it ->
			//				println '\t\t\t'+c+': '+ it.toString()
			//			}
		}
	}

	static void main (args) {

		def ym = new YnfoManager('/home/markus/Workspaces/Goji/grin/src/main/resources/nesi.groovy')
		//		def ym = new YnfoManager(null)
		//		def ym = new YnfoManager('/home/markus/Workspaces/Goji/grin/src/test/resources/test_2_sites.config.groovy')

		Grid grid = ym.getGrid()

		println 'Sites:'
		for ( def site : grid.getSites() ) {
			println '\t'+site.toString()
			printConnections(site)
		}

		println 'VOs:'
		for ( def vo : grid.getVos() ) {
			println '\t'+vo.getVoName()
			printConnections(vo)
		}

		println 'Groups:'
		for ( def group : grid.getGroups() ) {
			println '\t' + group.getFqan()
			printConnections(group)
		}

		println 'Filesystems:'
		for ( def fs : grid.getFilesystems() ) {
			println '\t' + fs.getUrl()
			printConnections(fs)
		}

		println 'Directories:'
		for ( def dir : grid.getDirectorys() ) {
			println '\t' + dir.getUrl()
			printConnections(dir)
		}

		println 'Gateways:'
		for ( def gw : grid.getGateways() ) {
			println '\t' + gw.toString()
			printConnections(gw)
		}

		println 'Queues:'
		for ( def q : grid.getQueues() ) {
			println '\t' + q.toString()
			printConnections(q)
		}

		println 'Applications:'
		for ( def app : grid.getApplications() ) {
			println '\t' + app.getName()
			printConnections(app)
		}

		println 'Versions:'
		for ( def v : grid.getVersions() ) {
			println '\t' + v.getVersion()
			printConnections(v)
		}

		println 'Packages:'
		for ( def p : grid.getPackages() ) {
			println '\t' + p.getName()
			printConnections(p)
		}
	}




	def grid = new Grid()

	public YnfoManager() {
		this(null)
	}

	public YnfoManager(def pathToConfig) {

		def config

		// try to get config from environment
		if ( ! pathToConfig ) {
			pathToConfig = CommonGridProperties.getDefault().getGridInfoConfig()
		}

		// check whether there is default "grid.groovy" file somewhwere
		if ( ! pathToConfig ) {
			File file = GridEnvironment.getGridInfoConfigFile()
			if ( file.exists ) {
				pathToConfig = file.getAbsolutePath()
			}
		}

		if ( ! pathToConfig || "testbed".equals(pathToConfig) ) {
			InputStream is = getClass().getResourceAsStream('/testbed.groovy')
			config = new ConfigSlurper().parse(is.getText())
		} else if ("nesi".equals(pathToConfig)) {
			InputStream is = getClass().getResourceAsStream('/nesi.groovy')
			config = new ConfigSlurper().parse(is.getText())
		} else {
			config = new ConfigSlurper().parse(new File(pathToConfig).toURL())
		}



		for (def e in config) {

			def name = e.key
			def object = e.value

			if ( object instanceof AbstractResource ) {
				//println 'setting alias: '+name
				object.setAlias(name)
			}

			switch(object.class) {

				case Directory.class:
					grid.addDirectory(object)
					break

				case Queue.class:
					grid.addQueue(object)
					break
			}
		}

		grid.validate();
	}


	public Grid getGrid() {
		return grid;
	}
}
