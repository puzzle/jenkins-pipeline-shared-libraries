package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext
import groovy.json.JsonSlurper

class KubeVersionCommand {

    private final PipelineContext ctx

    KubeVersionCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info('-- kubeVersion --')
        def server = ctx.stepParams.getOptional('server') as String

        server = (server != null) ? server.replace('insecure://', 'https://') : 'https://kubernetes.default.svc'

        def version = readVersion(server)
        ctx.echo("${server} version: ${version.major}.${version.minor}")

        return "${version.major}.${version.minor}"
    }

    Object readVersion(String server) {
        def url = "${server}/version"
        HttpURLConnection con
        try {
            con = connect(url)
        } catch (Throwable t) {
            printError('readVersion', t)
        }
        int httpResult = con.getResponseCode()
        if (httpResult == HttpURLConnection.HTTP_OK) {
            def version = new JsonSlurper().parseText(con.content.text)
            return version
        } else {
            ctx.echo( "HTTP response code: " + httpResult)
            ctx.echo( "HTTP response message: " + con.getResponseMessage())
            ctx.fail("kubeVersion:readVersion failure, see previous HTTP messages")
        }
    }

    private HttpURLConnection connect(String url) {
        try {
            URL targetURL = new URL(url)
            HttpURLConnection con = (HttpURLConnection) targetURL.openConnection()
            con.setDoOutput(true)
            con.setDoInput(true)
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            con.setRequestProperty("Accept", "application/json")
            return con
        } catch (Throwable t) {
            printError('connect', t)
        }
    }

    private void printError(String functionName, Throwable t) {
        ctx.echo("-- kubeVersion:${functionName} exception --")
        ctx.echo(t.getMessage())
        throw t
    }
}
