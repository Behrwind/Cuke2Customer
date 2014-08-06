package it.bitz.cuke2customer.view

import org.apache.commons.io.DirectoryWalker

class FeatureMenuRenderer extends DirectoryWalker {

    Appendable out
    Closure createLink

    void renderFeatureMenu(String startDirectory) {
        walk(new File (startDirectory), null)
    }

    @Override
    protected void handleStart(File startDirectory, Collection results) throws IOException {
        out << '<div class="featureMenu"><ul class="collapsibleList">'
    }

    @Override
    protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
        return directory.name != '.svn'
    }

    @Override
    protected void handleDirectoryStart(File directory, int depth, Collection results) throws IOException {
        if (depth == 0) return
        boolean bold = depth == 1
        out << "<li class=\"directory\">${bold ? '<span style="font-weight: bold;">' : ''}$directory.name${bold ? '</span>' : ''}"
        out << '<ul>'
    }

    @Override
    protected void handleFile(File file, int depth, Collection results) throws IOException {
        if (file.name.endsWith('.feature')) {
            String url = createLink (controller: 'feature', action: 'showFeature', params: [featureHashCode: file.text.hashCode()])
            out << "<li class=\"featureLink\"><a href=\"$url\">${file.name - '.feature'}</a></li>"
        }
    }

    @Override
    protected void handleDirectoryEnd(File directory, int depth, Collection results) throws IOException {
        if (depth == 0) return
        out << '</ul>'
        out << '</li>'
    }

    @Override
    protected void handleEnd(Collection results) throws IOException {
        out << '</ul></div>'
    }
}
