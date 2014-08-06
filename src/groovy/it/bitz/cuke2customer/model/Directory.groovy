package it.bitz.cuke2customer.model

class Directory {

    String name
    String path
    boolean topLevel

    Set<Directory> subDirectories = []
    Set<Feature> features = []

    boolean hasSubItems () {
        return hasSubDirectories () || hasFeatures ()
    }

    boolean hasSubDirectories () {
        return !subDirectories.empty
    }

    boolean hasFeatures () {
        return !features.empty
    }

    boolean hasContent () {
        return !features?.empty || subDirectories?.any {it.hasContent()}
    }

}
