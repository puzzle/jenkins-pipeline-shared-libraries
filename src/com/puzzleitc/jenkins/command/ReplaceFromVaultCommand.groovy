package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class ReplaceFromVaultCommand {

    private final PipelineContext ctx

    ReplaceFromVaultCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    Object execute() {
        ctx.info('-- replaceFromVault --')
        def text = ctx.stepParams.getRequired('text') as String
        def result = text
        while (parseVaultLookup(result).size() > 0) {
            def match = parseVaultLookup(result).get(0)
            if (match.path) {
                def replacedValue = ctx.lookupValueFromVault(match.path, match.key)
                result = result.substring(0, match.start) + replacedValue + result.substring(match.end, result.length())
            }
        }
        return result
    }

    private static List<VaultMatch> parseVaultLookup(String lookup) {
        def matcher = lookup =~ /(?m)\{\{\s*vault\.get\(\s*"([^"]+)",\s*"([^"]+)"\s*\)\s*\}\}/
        def result = []
        while (matcher.find()) {
            result.add(new VaultMatch(path: matcher.group(1), key: matcher.group(2), start: matcher.start(), end: matcher.end()))
        }
        return result
    }

    private static class VaultMatch {
        String path
        String key
        int start
        int end
    }

}
