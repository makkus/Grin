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

test = new Group(
		vo = nz,
		fqan = '/nz/test'
		)

uoa = new Group(
		vo = nz,
		fqan = '/nz/uoa'
		)

uoa_civil_eng = new Group(
		vo = nz,
		fqan = '/nz/civil_engineering'
		)

uoa_mech_eng = new Group(
		vo = nz,
		fqan = '/nz/uoa/mechanical-engineering'
		)

uoa_math = new Group(
		vo = nz,
		fqan = '/nz/uoa/math'
		)

uoa_stats = new Group(
		vo = nz,
		fqan = '/nz/uoa/stats'
		)

uoa_comp_chem = new Group(
		vo = nz,
		fqan = '/nz/uoa/comp-chem'
		)

uoa_comp_evol = new Group(
		vo = nz,
		fqan = '/nz/uoa/comp-evol'
		)

uoa_eng_sci = new Group(
		vo = nz,
		fqan = '/nz/uoa/engineering-science'
		)

uoa_qoptics = new Group(
		vo = nz,
		fqan = '/nz/uoa/quantum-optics'
		)
		

uoa_virt_screening = new Group(
		vo = nz,
		fqan = '/nz/virtual-screening'
		)

uoa_acsrc = new Group(
		vo = nz,
		fqan = '/nz/virtual-screening/acsrc'
		)

uoa_vs_jobs = new Group(
		vo = nz,
		fqan = '/nz/virtual-screening/jobs'
		)

uoa_sbs = new Group(
		vo = nz,
		fqan = '/nz/virtual-screening/sbs-structural-biology'
		)

// filesystems
auckland_gram5_fs = new FileSystem(
		host:'gram5.ceres.auckland.ac.nz',
		site:auckland
		)

auckland_df_fs = new FileSystem(
		host:'df.auckland.ac.nz',
		site:auckland
		)

auckland_ng2_fs = new FileSystem(
		host:'ng2.auckland.ac.nz',
		site:auckland
		)

canterbury_ng1_fs = new FileSystem(
	host:'ng1.canterbury.ac.nz',
	site:canterbury
	)

canterbury_ng2_fs = new FileSystem(
	host:'ng1.canterbury.ac.nz',
	site:canterbury
	)


// directories (make sure to always have a trailing slash for the path element

auckland_home = new Directory(
		filesystem:auckland_gram5_fs,
		groups:[nesi,uoa_vs_jobs,uoa_mech_eng],
		path:"/~/",
		volatileDirectory:true
		)

auckland_df_home = new Directory(
		filesystem:auckland_df_fs,
		groups:[nesi],
		path:"/~/",
		volatileDirectory:false
		)

auckland_vs_group = new Directory(
		filesystem:auckland_gram5_fs,
		groups:[uoa_virt_screening],
		volatileDirectory:false,
		path:"/home/grid-vs/",
		shared:false
		)

auckland_acsrc_group = new Directory(
		filesystem:auckland_gram5_fs,
		groups:[uoa_acsrc],
		volatileDirectory:false,
		path:"/home/grid-acsrc/"
		)

auckland_sbs_group = new Directory(
		filesystem:auckland_gram5_fs,
		groups:[uoa_sbs],
		volatileDirectory:false,
		path:"/home/grid-sbs/"
		)

canterbury_ng1_home = new Directory(
	filesystem:canterbury_ng1_fs,
	groups:[nesi],
	path:"/~/"
	)

canterbury_ng2_home = new Directory(
	filesystem:canterbury_ng2_fs,
	groups:[nesi],
	path:"/~/"
	)

globus4 = Middleware.get("Globus", "4.0.0");
globus5 = Middleware.get("Globus", "5.0")
globus5_2 = Middleware.get("Globus", "5.2");

// gateways
gram5 = new Gateway(
	site:auckland,
	host:"gram5.ceres.auckland.ac.nz"
	)


canterbury_ng2 = new Gateway(
		site:canterbury,
		host:"ng2.canterbury.ac.nz",
		middleware:globus4
		)


canterbury_ng1 = new Gateway(
		site:canterbury,
		host:"ng1.canterbury.ac.nz",
		middleware:globus5
		)


// packages

unixcommands_5_2_1 = new Package(
		application:Application.get('UnixCommands'),
		version:Version.get('5.2.1'),
		executables:Executable.getList('ls', 'cat', 'diff', 'echo','pwd')
		)
		

beast_1_6_1 = new Package(
		application: Application.get('BEAST'),
		version:Version.get('1.6.1'),
		executables:[Executable.get('beast')]
		)

python_2_6 = new Package(
		application: Application.get('python'),
		version:Version.get('2.6'),
		executables:[Executable.get('python')]
		)

r_2_10_0 = new Package(
		application:Application.get('R'),
		version:Version.get('2.10.0'),
		executables:[Executable.get('R')]
		)

octave_3_0_5 = new Package(
		application:Application.get('Octave'),
		version:Version.get('3.0.5'),
		executables:[Executable.get('octave')]
		)

gold_5_1 = new Package(
		application:Application.get("Gold"),
		version:Version.get('5.1'),
		executables:[Executable.get('parallel_gold_auto')]
		)

mr_bayes_3_1_2 = new Package(
		application:Application.get('MrBayes'),
		version:Version.get('3.1.2'),
		executables:[Executable.get('mb')]
		)
		
// queues

/** example:
<id> = new Queue(
		gateway:<gateway>,	// required
		name:'<queuename>',	// required
		groups: [<list_of_groups>], // at least one value required
		directories:[<list_of_directories>], // at least one value required
		packages:[<list_of_packages>], // optional, default is just generic package
		description:'Description of the queue', // optional, default: 'n/a'
		hosts:<number_of_hosts_for_this_queue>, // optional, default: Integer.MAX_VALUE
		memoryInBytes:<long_value_for_memory>, // optional, default: Long.MAX_VALUE
		noCpus:<total_no_of_cpus>, 				// optional, default: Integer.MAX_VALUE
		virtualMemoryInBytes:<long_value_for_memory>, // optional, default: Long.MAX_VALUE
		walltimeInMinutes:<walltime>, // optional, default: Integer.MAX_VALUE
		cpusPerHost:<min_number_of_cpus_per_host>, // optional, default: Integer.MAX_VALUE
		clockspeedInHz:<clockspeed>, // optional, default: Integer.MAX_VALUE
*/	

default_gram5 = new Queue(
		gateway:gram5,
		name:'default',
		groups:[nesi],
		directories:[auckland_home],
		packages:[python_2_6, r_2_10_0, octave_3_0_5],
		description:'Suitable for any jobs by NeSI members',
		hosts:21,
		noCpus:252,
		cpusPerHost:12,
		memoryInBytes:25265145118,
		virtualMemoryInBytes:25265145118
		)

uoa_gpu = new Queue(
		gateway:gram5,
		name:'gpu',
		groups:[nesi],
		directories:[auckland_home],
		description:'Suitable for any jobs using the CUDA GPU framework. If you would like to use an alternative framework please contact eresearch-admin@list.auckland.ac.nz',
		hosts:2,
		cpus:16,
		cpusPerHost:16,
		clockspeedInHz:2340000000,
		memoryInBytes:50637664419,
		virtualMemoryInBytes:50637664419
		)

uoa_gold_ce = new Queue(
		gateway:gram5,
		name:'gold',
		groups:[uoa_vs_jobs],
		directories:[auckland_home],
		packages:[gold_5_1]
		)
		
uoa_mech_ce = new Queue(
		gateway:gram5,
		name:'uoamech',
		groups:[uoa_mech_eng],
		directories:[auckland_home]
		)

uoa_math_ce = new Queue(
		gateway:gram5,
		name:'uoamath',
		groups:[uoa_math],
		directories:[auckland_home],
		hosts:1,
		cpus:40,
		cpusPerHost:40,
		memoryInBytes:541316203151,
		virtualMemoryInBytes:541316203151,
		clockspeedInHz:1950000000,
		description:'Queue for math node'
		)

uoa_stats_ce = new Queue(
		gateway:gram5,
		name:'uoastats',
		groups:[uoa_stats],
		directories:[auckland_home],
		hosts:1,
		cpus:40,
		cpusPerHost:40,
		memoryInBytes:541316203151,
		virtualMemoryInBytes:541316203151,
		clockspeedInHz:1950000000,
		description:'Queue for stats node'
		)

uoa_comp_evol_ce = new Queue(
		gateway:gram5,
		name:'uoaevol',
		groups:[uoa_comp_evol],
		directories:[auckland_home], 
		hosts:5,
		cpusPerHost:12,
		cpus:60,
		memoryInBytes:10468982784,
		virtualMemoryInBytes:10468982784, 
		clockspeedInHz:2600000000,
		description:'Queue for computational evolution cluster'
		)

uoa_comp_chem_ce = new Queue(
		gateway:gram5,
		name:'uoacompchem',
		groups:[uoa_comp_chem],
		directories:[auckland_home]
		)

uoa_eng_sci_ce = new Queue(
		gateway:gram5,
		name:'uoaengsci',
		groups:[uoa_eng_sci],
		directories:[auckland_home],
		hosts:2,
		cpusPerHost:12,
		cpus:24,
		memoryInBytes:25265145118,
		virtualMemoryInBytes:25265145118,
		clockspeedInHz:2610000000,
		description:'Queue for engineering sciences cluster'
		)

uoa_q_optics_ce = new Queue(
		gateway:gram5,
		name:'uoaqoptics',
		groups:[uoa_qoptics],
		directories:[auckland_home],
		hosts:4,
		cpusPerHost:12,
		cpus:48,
		memoryInBytes:25265145118,
		virtualMemoryInBytes:25265145118,
		clockspeedInHz:2610000000,
		description:'Queue for engineering sciences cluster'
		)

small_canterbury_ng2 = new Queue(
		gateway:canterbury_ng2,
		groups:[nesi],
		name:'small',
		directories:[canterbury_ng2_home]
		)