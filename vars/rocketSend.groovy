def void call(String webHook, String message, String avatar = null, Boolean rawMessage = true) {
    Map data = [:] as Map
    data['text'] = message
    data['rawMessage'] = rawMessage

    if (avatar != null) {
        data['avatar'] = avatar
    }

    def curlCommand = 'curl ' +
            '     -X POST "' + webHook + '" ' +
            '     -H "Content-Type: application/json" ' +
            '     --data \'' + groovy.json.JsonOutput.toJson(data) + '\' '

    def returnCode = sh(script: curlCommand, returnStatus: true)

    if (returnCode != 0) {
        error("RocketChat notification failed!")
    } else {
        echo "RocketChat notification sent successfully"
    }
}