package it.bitz.cuke2customer

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Config
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

class GitAdapterService implements VersionControlAdapter {

    boolean ignoreInvalidCertificates
    String gitUser
    String gitPassword
    String gitUrl

    void checkoutLatestRevision (String destinationDirectory) {
        initRepository (destinationDirectory)
        Git localRepository = getLocalGitRepository (destinationDirectory)
        configureRepository (localRepository)
        addOriginRemote (localRepository)
        fetchOrigin (localRepository)
        checkoutMaster (localRepository)
    }

    private void addOriginRemote (Git localRepository) {
        Config localConfig = localRepository.getRepository ().getConfig ()
        localConfig.setString ("remote", "origin", "url", gitUrl)
        localConfig.setString ("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*")
        localConfig.save ()
    }

    private static void checkoutMaster (Git localRepository) {
        localRepository.checkout ()
                .setName ('refs/remotes/origin/master')
                .call ()
    }

    private void configureRepository (Git localRepository) {
        Config localConfig = localRepository.getRepository ().getConfig ()
        if (ignoreInvalidCertificates) {
            localConfig.setBoolean ('http', null, 'sslVerify', false)
        }
        localConfig.save ()
    }

    private void fetchOrigin (Git localRepository) {
        localRepository.fetch ()
                .setRemote ('origin')
                .setCredentialsProvider (getCredentialsProvider ())
                .call ()
    }

    private static void initRepository (String destinationDirectory) {
        Git.init ()
                .setDirectory (new File (destinationDirectory))
                .call ()
    }

    private UsernamePasswordCredentialsProvider getCredentialsProvider () {
        return new UsernamePasswordCredentialsProvider (gitUser, gitPassword)
    }

    private static Git getLocalGitRepository (String destinationDirectory) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder ()
        Repository localRepository = builder.setGitDir (new File ("$destinationDirectory/.git")).readEnvironment ().findGitDir ().build ()
        return new Git (localRepository)
    }
}
