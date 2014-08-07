package it.bitz.cuke2customer

import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNRevision
import org.tmatesoft.svn.core.wc.SVNUpdateClient
import org.tmatesoft.svn.core.wc.SVNWCUtil

class SvnAdapterService implements VersionControlAdapter {

    String svnUser
    String svnPassword
    String svnUrl

    void checkoutLatestRevision(String destinationDirectory) {
        SVNClientManager clientManager = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(true), svnUser,
                                                                      svnPassword)
        SVNUpdateClient updateClient = clientManager.getUpdateClient()
        SVNRevision revision = SVNRevision.HEAD
        boolean recursive = true
        final File target = new File(destinationDirectory)
        log.info "checking out '$svnUrl' to '${target.getAbsolutePath()}'... "
        long revisionNumber = updateClient.doCheckout(SVNURL.parseURIEncoded(svnUrl), target, revision, revision,
                                                      recursive)
        log.info " done, checked out revision $revisionNumber"
    }
}
