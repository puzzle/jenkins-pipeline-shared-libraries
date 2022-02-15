package groovy.integration;

import hudson.FilePath;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class LocalLibraryRetriever extends LibraryRetriever {

    private final hudson.FilePath dir;

    public LocalLibraryRetriever() {
        this.dir = new FilePath(new File(System.getProperty("user.dir")));
    }

    @Override
    public void retrieve(@NotNull String name, @NotNull String version, boolean changelog, @NotNull FilePath target, @NotNull Run<?, ?> run, @NotNull TaskListener listener) throws Exception {
        dir.copyRecursiveTo("src/**/*.groovy,vars/*.groovy,vars/*.txt,resources/", null, target);
    }

    @Override
    public void retrieve(@NotNull String name, @NotNull String version, @NotNull FilePath target, @NotNull Run<?, ?> run, @NotNull TaskListener listener) throws Exception {
        dir.copyRecursiveTo("src/**/*.groovy,vars/*.groovy,vars/*.txt,resources/", null, target);
    }
}
