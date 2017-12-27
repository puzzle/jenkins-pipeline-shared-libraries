package com.puzzleitc.jenkins

import groovy.json.JsonSlurper
import groovy.json.JsonOutput

/**
 * Docker Hub API utility class.
 */
class DockerHub {
    static String BASE_URL = "https://hub.docker.com/v2/"
    static String LOGIN_URL = BASE_URL + "users/login/"
    static String REPOSITORIES_URL = BASE_URL + "repositories/"

    private final script

    /**
     * This constructor sets the Jenkins 'script' class as the local script
     * variable in order to resolve execution steps (sh, withCredentials, etc).
     * @param script the script object
     */
    DockerHub(script) {
        this.script = script
    }

    /**
     * Does a login into the Docker Hub API and returns the resulting token.
     * Returning null means no successful login.
     *
     * @return the Docker Hub API login token
     */
    @com.cloudbees.groovy.cps.NonCPS
    String createLoginToken(String dockerHubUser, String dockerHubPwd) {
        String auth = JsonOutput.toJson([username: dockerHubUser, password: dockerHubPwd])

        URL loginUrl = new URL(DockerHub.LOGIN_URL)
        HttpURLConnection con = (HttpURLConnection) loginUrl.openConnection()
        con.setDoOutput(true)
        con.setDoInput(true)
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        con.setRequestProperty("Accept", "application/json")
        con.setRequestMethod("POST")

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream())
        wr.write(auth.toString())
        wr.flush()

        int httpResult = con.getResponseCode()
        if (httpResult == HttpURLConnection.HTTP_OK) {
            def responseContent = new JsonSlurper().parseText(con.content.text)
            return responseContent.token
        } else {
            script.echo "HTTP response code: " + httpResult
            script.echo "HTTP response message: " + con.getResponseMessage()
            return null
        }
    }

    /**
     * Reads all repositories of the given dockerHubUser and returns the names as list.
     * Returning null means no successful reading.
     *
     * @return a list of all repositories of the dockerHubUser.
     */
    @com.cloudbees.groovy.cps.NonCPS
    String readRepositoryNames(String dockerHubUser, String token) {
        def url = REPOSITORIES_URL + dockerHubUser
        URL repositoriesUrl = new URL(url)

        HttpURLConnection con = (HttpURLConnection) repositoriesUrl.openConnection()
        con.setDoOutput(true)
        con.setDoInput(true)
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        con.setRequestProperty("Accept", "application/json")
        con.setRequestProperty("Authorization", "JWT ${token}")

        int httpResult = con.getResponseCode()
        if (httpResult == HttpURLConnection.HTTP_OK) {
            def responseContent = new JsonSlurper().parseText(con.content.text)
            def size = responseContent.count
            List repositoryNames = new ArrayList(size)
            List repositoriesList = (List) responseContent.get("results")
            for (int i = 0; i < size; i++) {
                repositoryNames.add(repositoriesList[i].name)
            }
            return repositoryNames
        } else {
            script.echo "HTTP response code: " + httpResult
            script.echo "HTTP response message: " + con.getResponseMessage()
            return null
        }
    }

    /**
     * Reads all tags of the given repository and returns the names as list.
     * Returning null means no successful reading.
     *
     * @return a list of all tags of the repository.
     */
    @com.cloudbees.groovy.cps.NonCPS
    List readTags(String dockerHubUser, String repository, String token) {
        def url = REPOSITORIES_URL + dockerHubUser + "/" + repository + "/tags"
        URL tagsUrl = new URL(url)

        HttpURLConnection con = (HttpURLConnection) tagsUrl.openConnection()
        con.setDoOutput(true)
        con.setDoInput(true)
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        con.setRequestProperty("Accept", "application/json")
        con.setRequestProperty("Authorization", "JWT ${token}")

        int httpResult = con.getResponseCode()
        if (httpResult == HttpURLConnection.HTTP_OK) {
            def responseContent = new JsonSlurper().parseText(con.content.text)
            def size = responseContent.count
            List tagNames = new ArrayList(size)
            List tagsList = (List) responseContent.get("results")
            for (int i = 0; i < size; i++) {
                tagNames.add(tagsList[i].name)
            }
            return tagNames
        } else {
            script.echo "HTTP response code: " + httpResult
            script.echo "HTTP response message: " + con.getResponseMessage()
            return null
        }
    }

    /**
     * Reads all tags of the given repository and returns the names as list.
     * Returning null means no successful reading.
     *
     * @return a list of all tags of the repository.
     */
    @com.cloudbees.groovy.cps.NonCPS
    void deleteByTag(String dockerHubUser, String repository, String tagName, String token) {
        def url = REPOSITORIES_URL + dockerHubUser + "/" + repository + "/tags/" + tagName
        URL tagsUrl = new URL(url)

        HttpURLConnection con = (HttpURLConnection) tagsUrl.openConnection()
        con.setDoOutput(true)
        con.setDoInput(true)
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        con.setRequestProperty("Accept", "application/json")
        con.setRequestProperty("Authorization", "JWT ${token}")
        con.setRequestMethod("DELETE")

        int httpResult = con.getResponseCode()
        if (httpResult == HttpURLConnection.HTTP_NO_CONTENT) {
            script.echo "deleted tag: " + tagName
        } else {
            script.echo "HTTP response code: " + httpResult
            script.echo "HTTP response message: " + con.getResponseMessage()
        }
    }
}
