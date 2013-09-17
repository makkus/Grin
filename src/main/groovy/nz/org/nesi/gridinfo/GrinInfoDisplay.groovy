package nz.org.nesi.gridinfo

import grisu.grin.YnfoManager
import grisu.grin.model.Grid
import grisu.jcommons.constants.Constants

class GrinInfoDisplay {

	private final GrinInfoDisplayParameters params;
    private final Grid grid;

	public GrinInfoDisplay(GrinInfoDisplayParameters params) {
		this.params = params


        if ( ! params.getPathOrUrl() ) {
            println '\nNo url or path to config file specified, please use the -c option...'
            System.exit(1)
        }

        def url = params.getPathOrUrl()
        println '\nUsing info config from: "'+url+'..."\n'
        def ym = new YnfoManager(url)

        grid = ym.getGrid()

        if ( ! params.getResourceToDisplay() ) {
            println '\nNo resource to display specified, please use at least one -r option...'
            System.exit(1)
        }
	}
	
	public void execute() {


        for ( String resource : params.getResourceToDisplay() ) {

            display(resource)

        }

        System.exit(0)
    }

    public void display(String resource) {

        println resource+":"

        resource = resource.toLowerCase()


        switch(resource) {
            case 'directory':
                for ( def dir : grid.getDirectorys().sort() ) {
                    println '\t'+dir.toString()
                }
                break;
            case 'queue':
                for ( def q : grid.getQueues().sort() ) {
                    println '\t'+q.toString()
                }
                break;
            case 'package':
                for ( def p : grid.getPackages().sort() ) {
                    if ( p.getVersion().toString() == Constants.NO_VERSION_INDICATOR_STRING ) {
                        continue;
                    }
                    println '\t'+p.getApplication().toString()+' ('+p.getVersion().toString()+'):'
                    for ( def q : grid.getResources(Queue.class, p).sort()) {
                        println '\t\t'+q.toString()
                    }
                }
            default:
                break;
        }
//
//		println 'Sites:'
//		for ( def site : grid.getSites().sort() ) {
//			println '\t'+site.toString()
//			//			printConnections(site)
//		}
//
//		println 'VOs:'
//		for ( def vo : grid.getVos().sort() ) {
//			println '\t'+vo.getVoName()
//			//			printConnections(vo)
//		}
//
//		println 'Groups:'
//		for ( def group : grid.getGroups().sort() ) {
//			println '\t' + group.getFqan()
//			//			printConnections(group)
//		}
//
//		println 'Filesystems:'
//		for ( def fs : grid.getFilesystems().sort() ) {
//			println '\t' + fs.toUrl()
//			//			printConnections(fs)
//		}
//
//		println 'Directories:'
//		for ( def dir : grid.getDirectorys().sort() ) {
//			println '\t' + dir.toUrl()
//			//			printConnections(dir)
//		}
//
//		println 'Gateways:'
//		for ( def gw : grid.getGateways().sort() ) {
//			println '\t' + gw.toString()
//			//			printConnections(gw)
//		}
//
//		println 'Queues:'
//		for ( def q : grid.getQueues().sort() ) {
//			println '\t' + q.toString()
//			//			printConnections(q)
//		}
//
//		println 'Applications:'
//		for ( def app : grid.getApplications().sort() ) {
//			println '\t' + app.getName()
//			//			printConnections(app)
//		}
//
//		println 'Versions:'
//		for ( def v : grid.getVersions().sort() ) {
//			println '\t' + v.getVersion()
//			//			printConnections(v)
//		}
//
//		println 'Packages:'
//		for ( def p : grid.getPackages().sort() ) {
//			println '\t' + p.getName()
//			//			printConnections(p)
//		}
//
//		System.exit(0)
	}
}
