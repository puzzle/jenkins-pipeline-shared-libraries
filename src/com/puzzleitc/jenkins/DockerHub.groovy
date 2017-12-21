package com.puzzleitc.jenkins

import groovy.json.JsonSlurper
import groovy.json.JsonOutput

/**
 * Docker Hub API utility class.
 */
class DockerHub {
  static String baseUrl = "https://hub.docker.com/v2/"
  static String loginUrl = baseUrl + "users/login/"

  /**
   * Does a login into the Docker Hub API and returns the resulting token.
   *
   * @return the Docker Hub API login token
   */
  @com.cloudbees.groovy.cps.NonCPS
  static String createLoginToken(String dockerHubUser, String dockerHubPwd) {
    def auth = JsonOutput.toJson([username: dockerHubUser, password: dockerHubPwd])

    def URL loginUrlObject = new URL(DockerHub.loginUrl);

    HttpURLConnection con = (HttpURLConnection) loginUrlObject.openConnection();
    con.setDoOutput(true);
    con.setDoInput(true);
    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    con.setRequestProperty("Accept", "application/json");
    con.setRequestMethod("POST");

    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
    wr.write(auth.toString());
    wr.flush();

    //display what returns the POST request

    StringBuilder sb = new StringBuilder();
    int HttpResult = con.getResponseCode();

    println "ResponseCode: " + HttpResult
    println "ResponseMessage: " + con.getResponseMessage()

    if (HttpResult == HttpURLConnection.HTTP_OK) {
       def responseContent = new JsonSlurper().parseText(con.content.text)
       return responseContent.token

    } else {
       return null
    }
  }
}
