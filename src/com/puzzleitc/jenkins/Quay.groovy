package com.puzzleitc.jenkins

import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Quay registry utility class.
 */
class Quay {
    static String API_PATH = 'api/v1'
    static String QUAY_URL = 'https://quay.io'
    static String REPOSITORY_PATH = API_PATH + '/' + 'repository'

    private final String credentialName
    private final String registryUrl
    private final Script script

    /**
     * This constructor sets the Jenkins 'script' class as the local script variable in order to execute steps (echo, etc).
     * Connecting to the default registry (quay.io)
     * @param script the script object (normally 'this')
     * @param credentialName the name of the Credential holding the login token
     */
    Quay(Script script, String credentialName) {
        this(script, credentialName, '')
    }

    /**
     * This constructor sets the Jenkins 'script' class as the local script variable in order to execute steps (echo, etc).
     * @param script the script object (normally 'this')
     * @param credentialName the name of the Credential holding the login token
     * @param registryUrl optional param to set the url of the registry (defaults to quay.io)
     */
    Quay(Script script, String credentialName, String registryUrl) {
        mandatoryParameter('credentialName', credentialName)
        this.script = script
        this.credentialName = credentialName
        if (registryUrl?.trim()) {
            this.registryUrl = registryUrl
        } else {
            this.registryUrl = QUAY_URL
        }
    }

    /**
     * Returns the url of the registry that is used.
     * @return the registry url
     */
    String getRegistryUrl() {
        return this.registryUrl
    }

    /**
     * Reads the tag of the given repository and returns the manifest digest (sha).
     * Returning null means tag not found.
     *
     * @param registryUserOrOrg user or organisation name that holds the repository
     * @param repository name of the image repository
     * @param tag name of the tag to get the sha of it's image
     * @return the sha of the image with that tag
     */
    String getTagManifest(String registryUserOrOrg, String repository, String tag) {
        mandatoryParameter('registryUserOrOrg', registryUserOrOrg)
        mandatoryParameter('repository', repository)
        mandatoryParameter('tag', tag)

        String url = registryUrl + '/' + REPOSITORY_PATH + '/' + registryUserOrOrg + '/' +
                repository + '/' + 'tag' + '/' + '?onlyActiveTags=true' + '&specificTag=' + tag
        this.script.withCredentials([this.script.string(credentialsId: this.credentialName, variable: 'bearerToken')]) {
            return doGetTagManifestRequest(url, tag, this.script.bearerToken)
        }
    }

    /**
     * Adds the given tag to the image referenced by the sha.
     *
     * @param registryUserOrOrg user or organisation name that holds the repository
     * @param repository name of the image repository
     * @param existingTagSha the manifest digest (sha) of the image to add the new tag
     * @param newTagName new tag for the image
     */
    void addTag(String registryUserOrOrg, String repository, String existingTagSha, String newTagName) {
        mandatoryParameter('registryUserOrOrg', registryUserOrOrg)
        mandatoryParameter('repository', repository)
        mandatoryParameter('existingTagSha', existingTagSha)
        mandatoryParameter('newTagName', newTagName)

        String url = registryUrl + '/' + REPOSITORY_PATH + '/' + registryUserOrOrg + '/' +
                repository + '/' + 'tag' + '/' + newTagName
        this.script.withCredentials([this.script.string(credentialsId: this.credentialName, variable: 'bearerToken')]) {
            doAddTagRequest(url, existingTagSha, newTagName, this.script.bearerToken)
        }
    }

    /**
     * Executes the request to read the sha of the image.
     * Extracted method to use @NonCPS
     *
     * @param url (api) to read the tag
     * @param tag that should be read
     * @param bearerToken login token
     * @return the sha of the image with that tag
     */
    @NonCPS
    private String doGetTagManifestRequest(String url, String tag, String bearerToken) {
        HttpURLConnection con = getRequest(url, bearerToken)
        int httpResult = con.getResponseCode()
        if (httpResult == HttpURLConnection.HTTP_OK) {
            String contentText = con.content.text
            con.disconnect()

            Object responseContent = new JsonSlurper().parseText(contentText)
            List tagsList = (List) responseContent.get("tags")
            switch (tagsList.size()) {
                case 0:
                    this.script.echo('Tag not found')
                    return null
                case 1:
                    String sha = tagsList[0].manifest_digest
                    this.script.echo('SHA found: ' + sha)
                    return sha
                default:
                    this.script.error('Tag "' + tag + '" not unique. Tag read returned more than one entry.')
            }
        } else {
            this.script.error("""
Tag '${tag}' read error.
HTTP response code: ${httpResult}
HTTP response message:
${con.getResponseMessage()}""")
        }
    }

    /**
     * Executes the request to add the tag.
     * Extracted method to use @NonCPS
     *
     * @param url (api) to add the tag
     * @param existingTagSha the manifest digest (sha) of the image to add the new tag
     * @param newTagName new tag for the image
     * @param bearerToken login token
     */
    @NonCPS
    private void doAddTagRequest(String url, String existingTagSha, String newTagName, String bearerToken) {
        HttpURLConnection con = putRequest(url, [manifest_digest: existingTagSha], bearerToken)
        int httpResult = con.getResponseCode()
        if (httpResult == HttpURLConnection.HTTP_OK || httpResult == HttpURLConnection.HTTP_CREATED) {
            String contentText = con.content.text
            con.disconnect()

            Object responseContent = new JsonSlurper().parseText(contentText)
            this.script.echo('Add tag result: ' + responseContent.toString())
        } else {
            this.script.error("""
Tag '${newTagName}' pointing to SHA '${existingTagSha}' creation error.
HTTP response code: ${httpResult}
HTTP response message:
${con.getResponseMessage()}""")
        }
    }

    /**
     * Prepare a GET request
     *
     * @param url to open the connection to
     * @param bearerToken login token
     * @return the http connection
     */
    @NonCPS
    private static HttpURLConnection getRequest(String url, String bearerToken) {
        URL targetURL = new URL(url)

        HttpURLConnection con = (HttpURLConnection) targetURL.openConnection()
        con.setDoOutput(true)
        con.setDoInput(true)
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        con.setRequestProperty("Accept", "application/json")
        con.setRequestProperty('Authorization', 'Bearer ' + bearerToken)
        return con
    }

    /**
     * Prepare a PUT request
     *
     * @param url to open the connection to
     * @param bodyContent map holding the body of the request. Will be transformed to JSON.
     * @param bearerToken login token
     * @return the http connection
     */
    @NonCPS
    private static HttpURLConnection putRequest(String url, Map bodyContent, String bearerToken) {
        URL targetURL = new URL(url)
        String body = JsonOutput.toJson(bodyContent)

        HttpURLConnection con = (HttpURLConnection) targetURL.openConnection()
        con.setDoOutput(true)
        con.setDoInput(true)
        con.setRequestMethod("PUT")
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        con.setRequestProperty("Accept", "application/json")
        con.setRequestProperty('Authorization', 'Bearer ' + bearerToken)

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream())
        wr.write(body.toString())
        wr.flush()
        return con
    }

    @NonCPS
    private static void mandatoryParameter(String parameterName, parameter) {
        if (!(parameter?.trim())) {
            throw new IllegalArgumentException("Missing parameter '${parameterName}'")
        }
    }

}
