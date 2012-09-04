import grisu.jcommons.model.info.*




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

auckland_df = new FileSystem(
		host:'df.auckland.ac.nz',
		site:auckland
		)

auckland_ng2 = new FileSystem(
		host:'ng2.auckland.ac.nz',
		site:auckland
		)

canterbury_df = new FileSystem(
		host:'df.canterbury.ac.nz',
		site:canterbury
		)


// directories
auckland_ng2_home = new Directory(
		filesystem:auckland_ng2,
		shared:true)

auckland_home = new Directory(
		filesystem:auckland_gram5,
		groups:[nesi],
		path:"/~/",
		volatileDirectory:true
		)

auckland_tmp = new Directory(
		filesystem:auckland_gram5,
		groups:[nesi],
		path:"/tmp",
		volatileDirectory:true
		)

auckland_df_home = new Directory(
		filesystem:auckland_df,
		groups:[nesi],
		path:"/~/",
		volatileDirectory:false
		)

canterbury_home = new Directory(
		filesystem:canterbury_df,
		groups:[nesi],
		path:"/~/"
		)

globus4 = Middleware.create("Globus", "4.0.0");
globus5 = Middleware.create("Globus", "5.0")
globus5_2 = Middleware.create("Globus", "5.2");

// gateways
gram5 = new Gateway(
		site:auckland,
		host:"gram5.ceres.auckland.ac.nz"
		)

ng2 = new Gateway(
		site:auckland,
		host:"ng2.auckland.ac.nz",
		middleware:globus4
		)
		
// applications
java = new Application(
		name:'Java'
		)

python = new Application(
		name:'Python'
		)

// executables
exe_java = Executable.create('java')
exe_javac = Executable.create('javac')
exe_python = Executable.create('python')

mod_java = Module.create('java')

// packages
// groups is optional, if not set, groups of all queues will be used
java15_nesi = new Package(
		application:java,
		version: new Version('1.5.0'),
		)

// packages
java15 = new Package(
		application:java,
		version:new Version('1.5.0'),
		)

java16 = new Package(
	application:java,
	version:new Version('1.6.0'),
	)

java17 = new Package(
	application:java,
	version:new Version('1.7.0'),
	)

java172 = new Package(
	application:java,
	version:new Version('1.7.0'),
	executables: [exe_java, exe_javac],
	module: mod_java
	)


python = new Package(
		application:python,
		version:new Version('2.6'),
		executables: [exe_python]
		)
		
// queues
default_gram5 = new Queue(
		gateway:gram5,
		name:'default',
		groups:[nesi],
		directories:[auckland_home],
		packages:[java17],
		noCpus:6,
		memory:1073741824
		)

default_ng2 = new Queue(
		gateway:ng2,
		groups:[Group.NO_VO_GROUP],
		name:'route@er171.ceres.auckland.ac.nz',
		directories:[auckland_ng2_home]
		)