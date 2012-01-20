import grisu.grin.model.resources.*




// sites

auckland = new Site(
	name = 'Auckland'
	)

canterbury = new Site(
	name = 'Canterbury'
	)

// vos

nz = new VO(
	voName = 'nz',
	host = 'voms.bestgrid.org',
	port = 15000,
	hostDN = '/C=NZ/O=The University of Auckland/OU=BeSTGRID/CN=voms.bestgrid.org'
	)

// groups
nesi = new Group(
		vo = nz,
		fqan = "/nz/nesi"
		)

grid_dev = new Group(
		vo = nz,
		fqan = "/nz/grid-dev"
		)

// filesystems
auckland_gram5 = new FileSystem(
		host:'gram5.ceres.auckland.ac.nz',
		protocol:'gsiftp',
		port:2811,
		site:auckland
		)

canterbury_df = new FileSystem(
		host:'df.canterbury.ac.nz',
		site:canterbury
		)


// directories
auckland_home = new Directory(
		filesystem:auckland_gram5,
		groups:[nesi],
		path:"/~/"
		)

auckland_tmp = new Directory(
		filesystem:auckland_gram5,
		groups:[nesi],
		path:"/tmp"
		)

canterbury_home = new Directory(
		filesystem:canterbury_df,
		groups:[nesi],
		path:"/~/"
		)

// gateways
gram5 = new Gateway(
		site:auckland,
		host:"gram5.ceres.auckland.ac.nz"
		)
		
// queues
default_gram5 = new Queue(
		gateway:gram5,
		name:'defaultQueue',
		groups:[nesi],
		directories:[auckland_home]
		)

// applications
java = new Application(
		name:'Java'
		)

python = new Application(
		name:'Python'
		)

// packages
// groups is optional, if not set, groups of all queues will be used
java15_nesi = new Package(
		application:java,
		version: new Version('1.5.0'),
		groups: [nesi],
		queues: [default_gram5]
		)

// packages
//java15 = new Package(
//		application:java,
//		version:new Version('1.5.0'),
//		queues: [default_gram5]
//		)
//
//java16 = new Package(
//	application:java,
//	version:new Version('1.6.0'),
//	queues: [default_gram5]
//	)

//java17 = new Package(
//	application:java,
//	version:new Version('1.7.0'),
//	queues: [default_gram5],
//	groups: [grid_dev]
//	)

java172 = new Package(
	application:java,
	version:new Version('1.7.0'),
	groups: [nesi],
	queues: [default_gram5]
	)


python = new Package(
		application:python,
		version:new Version('2.6'),
		queues: [default_gram5]
		)
		
