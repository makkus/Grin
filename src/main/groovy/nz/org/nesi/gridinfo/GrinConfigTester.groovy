package nz.org.nesi.gridinfo


import grisu.grin.YnfoManager
import grisu.grin.model.Grid

class GrinConfigTester {

	private final GrinTestParameters params;
	
	public GrinConfigTester(GrinTestParameters params) {
		this.params = params
	}
	
	public void execute() {

		if ( ! params.getPathOrUrl() ) {
			println '\nNo url or path to config file specified, please use the -c option...'
			System.exit(1)
		}

		def url = params.getPathOrUrl()
		println '\nUsing info config from: "'+url+'..."\n'
		def ym = new YnfoManager(url)

		Grid grid = ym.getGrid()

		println 'Sites:'
		for ( def site : grid.getSites().sort() ) {
			println '\t'+site.toString()
			//			printConnections(site)
		}

		println 'VOs:'
		for ( def vo : grid.getVos().sort() ) {
			println '\t'+vo.getVoName()
			//			printConnections(vo)
		}

		println 'Groups:'
		for ( def group : grid.getGroups().sort() ) {
			println '\t' + group.getFqan()
			//			printConnections(group)
		}

		println 'Filesystems:'
		for ( def fs : grid.getFilesystems().sort() ) {
			println '\t' + fs.toUrl()
			//			printConnections(fs)
		}

		println 'Directories:'
		for ( def dir : grid.getDirectorys().sort() ) {
			println '\t' + dir.toUrl()
			//			printConnections(dir)
		}

		println 'Gateways:'
		for ( def gw : grid.getGateways().sort() ) {
			println '\t' + gw.toString()
			//			printConnections(gw)
		}

		println 'Queues:'
		for ( def q : grid.getQueues().sort() ) {
			println '\t' + q.toString()
			//			printConnections(q)
		}

		println 'Applications:'
		for ( def app : grid.getApplications().sort() ) {
			println '\t' + app.getName()
			//			printConnections(app)
		}

		println 'Versions:'
		for ( def v : grid.getVersions().sort() ) {
			println '\t' + v.getVersion()
			//			printConnections(v)
		}

		println 'Packages:'
		for ( def p : grid.getPackages().sort() ) {
			println '\t' + p.getName()
			//			printConnections(p)
		}

		System.exit(0)
	}
}
