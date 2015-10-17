package it.bitz.cuke2customer

import org.eclipse.jgit.api.CreateBranchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

class GitAdapterService implements VersionControlAdapter {

    String gitUser
    String gitPassword
    String gitUrl

    void checkoutLatestRevision (String destinationDirectory) {
        if (!doesDestinationDirectoryExist (destinationDirectory)) {
            createDestinationDirectory (destinationDirectory)
        }
        if (repositoryHasBeenCloned (destinationDirectory)) {
            fetchRepository (destinationDirectory)
        } else {
            cloneRepository (destinationDirectory)
        }
        if (doesMasterBranchExist (destinationDirectory)) {
            checkoutMaster (destinationDirectory)
        } else {
            checkoutNewMaster (destinationDirectory)
        }
        pullMaster (destinationDirectory)
    }

    private static void checkoutMaster (String destinationDirectory) {
        FileRepository localRepository = new FileRepository ("$destinationDirectory/.git")
        Git git = new Git (localRepository)
        git.checkout ().setName ("master").call ()
    }

    private static void checkoutNewMaster (String destinationDirectory) {
        FileRepository localRepository = new FileRepository ("$destinationDirectory/.git")
        Git git = new Git (localRepository)
        git.checkout ().setCreateBranch (true).setName ("master")
                .setUpstreamMode (CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                .setStartPoint ("origin/master").call ()
    }

    private void cloneRepository (String destinationDirectory) {
        FileRepository localRepository = new FileRepository ("$destinationDirectory/.git")
        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider (gitUser, gitPassword)
        Git git = new Git (localRepository)
        git.cloneRepository ()
                .setDirectory (new File (destinationDirectory))
                .setURI (gitUrl)
                .setCredentialsProvider (credentialsProvider)
                .call ()
    }

    private static void createDestinationDirectory (String destinationDirectory) {
        new File (destinationDirectory).mkdir ()
    }

    private static boolean doesMasterBranchExist (String destinationDirectory) {
        FileRepository localRepository = new FileRepository ("$destinationDirectory/.git")
        Git git = new Git (localRepository)
        List branches = git.branchList ().call ()
        return 'refs/heads/master' in branches?.name
    }

    private static boolean doesDestinationDirectoryExist (String destinationDirectory) {
        return new File (destinationDirectory).exists ()
    }

    private void fetchRepository (String destinationDirectory) {
        FileRepository localRepository = new FileRepository ("$destinationDirectory/.git")
        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider (gitUser, gitPassword)
        Git git = new Git (localRepository)
        git.fetch ()
                .setCredentialsProvider (credentialsProvider)
                .call ()
    }

    private void pullMaster (String destinationDirectory) {
        FileRepository localRepository = new FileRepository ("$destinationDirectory/.git")
        UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider (gitUser, gitPassword)
        Git git = new Git (localRepository)
        git.pull ()
                .setCredentialsProvider (credentialsProvider)
                .call ()
    }

    private static boolean repositoryHasBeenCloned (String destinationDirectory) {
        return new File ("$destinationDirectory/.git").exists ()
    }
}
