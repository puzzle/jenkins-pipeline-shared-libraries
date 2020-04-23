import com.puzzleitc.jenkins.command.KustomizeCommand

def call(String resource) {
    echo "-- start kustomize build --"
    echo "resource: $resource"

    KustomizeCommand command = new KustomizeCommand();
    echo "Command output: ${command.execute()}";
}