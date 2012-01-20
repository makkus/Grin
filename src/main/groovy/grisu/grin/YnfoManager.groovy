package grisu.grin

import grisu.grin.model.Grid
import grisu.grin.model.resources.*

import com.google.common.collect.Sets




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

		def ym = new YnfoManager('/home/markus/Workspaces/Goji/grin/src/main/resources/default_config.groovy')

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
		for ( def dir : grid.getDirectories() ) {
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

	public YnfoManager(def pathToConfig) {

		this(new ConfigSlurper().parse(new File(pathToConfig).toURL()))
	}


	public YnfoManager(ConfigObject config) {


		for (def e in config) {

			def name = e.key
			def object = e.value
			switch(object.class) {

				case Directory.class:
					grid.addDirectory(object)
					break
				case Package.class:
					if (! object.getGroups()) {
						Set<Group> temp = Sets.newHashSet()
						for (Queue q : object.getQueues()) {
							temp.addAll(q.getGroups())
						}
						object.setGroups(temp)
					}
					grid.addPackage(object)
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
