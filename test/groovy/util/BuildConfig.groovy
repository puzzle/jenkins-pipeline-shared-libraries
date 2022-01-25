package groovy.util

// Interface used to mock openshift BuildConfig
interface BuildConfig {
    Build startBuild(String[] params)
}