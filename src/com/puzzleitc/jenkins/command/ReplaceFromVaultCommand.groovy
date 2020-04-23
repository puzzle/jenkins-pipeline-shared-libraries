package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class ReplaceFromVaultCommand {

    private final String input
    private final PipelineContext ctx

    ReplaceFromVaultCommand(String input, PipelineContext ctx) {
        this.input = input
        this.ctx = ctx
    }

    String execute() {
        def result = input
        while (parseVaultLookup(result).size() > 0) {
            def match = parseVaultLookup(result).get(0)
            def path = match[0]
            def key = match[1]
            def start = match[2]
            def end = match[3]
            if (path) {
                ctx.withVault(vaultSecrets: [[path: path, engineVersion: 2, secretValues: [[envVar: 'secretValue', vaultKey: key]]]]) {
                    result = result.substring(0, start) + secretValue + result.substring(end, result.length())
                }
            }
        }
        return result
    }

    private static List<List> parseVaultLookup(String lookup) {
        def matcher = lookup =~ /(?m)\{\{\s*vault\.get\(\s*"([^"]+)",\s*"([^"]+)"\s*\)\s*\}\}/
        def result = []
        while (matcher.find()) {
            result.add([matcher.group(1), matcher.group(2), matcher.start(), matcher.end()])
        }
        return result
    }

}
