package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class ImportObservationsTask extends Task<Integer> {
    private static final Logger LOG = Logger.getLogger(ImportObservationsTask.class.getName());
    private final Path file;
    private final HttpEcologicalIncidentsClient api;
    public ImportObservationsTask(Path file, HttpEcologicalIncidentsClient api) {
        this.file = file;
        this.api = api;
    }

    @Override
    protected Integer call() throws Exception {
        List<String> lines = Files.readAllLines(file);
        int total = Math.max(1, lines.size() - 1), imported = 0;
        for (int i = 1; i < lines.size(); i++) {
            String[] p = lines.get(i).split(";");
            if (p.length != 7) {LOG.warning("Пропущено рядок " + (i + 1));continue;}
            Observation o = new Observation(
                    null,
                    Long.parseLong(p[0]),
                    p[1],
                    p[2],
                    Double.parseDouble(p[3]),
                    p[4],
                    p[5],
                    p[6]
            );
            api.addObservation(o);
            imported++;
            updateProgress(i, total);
            updateMessage("Імпортовано: " + imported + " із " + total);
        }
        return imported;
    }
}