import com.puzzleitc.jenkins.command.KustomizeCommand

def call(String resource) {
    echo "-- start kustomize build --"
    echo "resource: $resource"

    KustomizeCommand command = new KustomizeCommand(resource);
    echo "Command output: ${command.execute()}";
}