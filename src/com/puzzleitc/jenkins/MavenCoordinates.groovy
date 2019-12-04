package com.puzzleitc.jenkins

class MavenCoordinates implements Serializable {
  String groupId
  String artifactId
  String version
  String packaging
  String classifier

  @com.cloudbees.groovy.cps.NonCPS
  String toString() {
    return "${groupId}:${artifactId}:${version}:${packaging}${classifier?.trim() ? ":${classifier}" : "" }"
  }

  @com.cloudbees.groovy.cps.NonCPS
  int hashCode() {
    final int prime = 31
    int result = 1
    result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode())
    result = prime * result + ((classifier == null) ? 0 : classifier.hashCode())
    result = prime * result + ((groupId == null) ? 0 : groupId.hashCode())
    result = prime * result + ((packaging == null) ? 0 : packaging.hashCode())
    result = prime * result + ((version == null) ? 0 : version.hashCode())
    return result
  }

  @com.cloudbees.groovy.cps.NonCPS
  boolean equals(Object obj) {
    if (this == obj)
        return true
    if (obj == null)
        return false
    if (getClass() != obj.getClass())
        return false
      MavenCoordinates other = (MavenCoordinates) obj
    if (artifactId == null) {
        if (other.artifactId != null)
            return false
    } else if (!artifactId.equals(other.artifactId))
        return false
    if (classifier == null) {
        if (other.classifier != null)
            return false
    } else if (!classifier.equals(other.classifier))
        return false
    if (groupId == null) {
        if (other.groupId != null)
            return false
    } else if (!groupId.equals(other.groupId))
        return false
    if (packaging == null) {
        if (other.packaging != null)
            return false
    } else if (!packaging.equals(other.packaging))
        return false
    if (version == null) {
        if (other.version != null)
            return false
    } else if (!version.equals(other.version))
        return false
    return true
  }
}
