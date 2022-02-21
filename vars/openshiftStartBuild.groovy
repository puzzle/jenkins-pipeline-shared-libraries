def call(Map args = [:]) {

    echo "Hello world 2!"

    ansiColor('xterm') {
        echo "Hello world 2!"
    }

    //OpenshiftStartBuildCommand command = new OpenshiftStartBuildCommand(new JenkinsPipelineContext(this, args))
    //command.execute()
}