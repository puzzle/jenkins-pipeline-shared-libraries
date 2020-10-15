package com.puzzleitc.jenkins

class Util {
    static Map parseArgs(namedArgs, positionalArgs, List requiredParams, Map optionalParams = [:]) {

        // null can be incorrectly assigned to namedArgs or positionalArgs if there are no other named or positional arguments, fix it
        if (namedArgs == null) {
            namedArgs = [:]
            positionalArgs = ([null] as Object[]) + positionalArgs
        }
        if (positionalArgs == null) {
            positionalArgs = ([null] as Object[])
        }

        // If the last argument is a closure it always goes into the last parameter
        def positionalArgsCount = positionalArgs?.length
        if (positionalArgsCount && positionalArgs[-1] instanceof Closure) {
            println("closure")
            def lastKey = optionalParams.size() ? optionalParams.keySet().last() : requiredParams[-1]
            namedArgs[lastKey] = positionalArgs[-1]
            positionalArgsCount--
        }

        int i = 0
        for (def item: requiredParams) {
            if (namedArgs.containsKey(item)) {
                if (i < positionalArgsCount) {
                    throw new IllegalArgumentException("Multiple values for argument '${item}'")
                }
            } else {
                if (i < positionalArgsCount) {
                    namedArgs[item] = positionalArgs[i]
                } else {
                    throw new IllegalArgumentException("Missing argument '${item}'")
                }
            }
            i++
        }

        for (def item: optionalParams) {
            if (namedArgs.containsKey(item.key)) {
                if (i < positionalArgsCount) {
                    throw new IllegalArgumentException("Multiple values for argument '${item.key}'")
                }
            } else {
                if (i < positionalArgsCount) {
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
