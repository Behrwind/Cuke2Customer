package it.bitz.cuke2customer

import org.apache.commons.io.DirectoryWalker

class FeatureService {

    static scope = 'singleton'

    private Map features = [:]

    void loadFeatures(String featureDirectory) {
        log.info("loading features in directory '$featureDirectory'")
        features.clear()
        List<File> featureFiles = new FeatureFileDirectoryWalker().getFeatureFilesInDirectory(featureDirectory)
        featureFiles.each { file ->
            String fileContent = file.text
            features[fileContent.hashCode().toString()] = fileContent
            log.info("loaded feature '$file.name'")
        }
    }

    private class FeatureFileDirectoryWalker extends DirectoryWalker {

        List<File> getFeatureFilesInDirectory(String directory) {
            List<File> featureFiles = []
            walk(new File(directory), featureFiles)
            return featureFiles
        }

        @Override
        protected void handleFile(File file, int depth, Collection results) throws IOException {
            if(file.name.endsWith('.feature')) {
                results.add(file)
            }
        }
    }

    String getFeatureWithHashCode(String hashCode) {
        return features[hashCode]
    }

    List<String> search (List<String> searchTerms) {
        searchTerms.removeAll { it.trim() == '' }
        if (searchTerms.empty) return []
        return features.values().findAll { String feature ->
            containsAllSubStrings(feature, searchTerms)
        } as List<String>
    }

    private containsAllSubStrings (String string, List<String> subStrings) {
        boolean containsAll = true
        string = string.toLowerCase()
        subStrings.collect{it.toLowerCase()}.each {
            if (!string.contains(it)) {
                containsAll = false
            }
        }
        return containsAll
    }

}
