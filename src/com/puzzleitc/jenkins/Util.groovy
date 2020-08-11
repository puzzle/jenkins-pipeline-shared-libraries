package com.puzzleitc.jenkins

class Util {
    static Map parseArgs(namedArgs, positionalArgs, List requiredParams, Map optionalParams = [:]) {

        int i = 0
        for (def item: requiredParams) {
            if (namedArgs.containsKey(item)) {
                if (i < positionalArgs.length) {
                    throw new IllegalArgumentException("Multiple values for argument '${item}'")
                }
            } else {
                if (i < positionalArgs.length) {
                    namedArgs[item] = positionalArgs[i]
                } else {
                    throw new IllegalArgumentException("Missing argument '${item}'")
                }
            }
            i++
        }

        for (def item: optionalParams) {
            if (namedArgs.containsKey(item.key)) {
                if (i < positionalArgs.length) {
                    throw new IllegalArgumentException("Multiple values for argument '${item.key}'")
                }
            } else {
                if (i < positionalArgs.length) {
                    namedArgs[item.key] = positionalArgs[i]
                } else {
                    namedArgs[item.key] = item.value
                }
            }
            i++
        }

        return namedArgs
    }
}
