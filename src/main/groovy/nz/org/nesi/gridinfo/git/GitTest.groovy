package nz.org.nesi.gridinfo.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepository

String localPath;
Repository localRepo;
Git git;

localPath = "/home/markus/git_repo_test/Grin"
remotePath = "git://github.com/makkus/Grin.git"

Git.cloneRepository()
.setURI(remotePath)
.setDirectory(new File(localPath))
.call();

try {
	localRepo = new FileRepository(localPath + "/.git")
} catch (IOException e) {
	e.printStackTrace()
}
git = new Git(localRepo)

PullCommand pullCmd = git.pull()
try {
	pullCmd.call()
} catch (GitAPIException e) {
	e.printStackTrace()
}


