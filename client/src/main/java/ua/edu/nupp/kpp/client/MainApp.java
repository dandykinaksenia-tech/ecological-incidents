package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.util.logging.Logger;

public class MainApp extends Application {

    private static final Logger LOG = Logger.getLogger(MainApp.class.getName());
    private final HttpEcologicalIncidentsClient api = new HttpEcologicalIncidentsClient();

    private final TableView<Location> locationsTable = new TableView<>();
    private final TableView<Incident> incidentsTable = new TableView<>();
    private final TableView<Observation> observationsTable = new TableView<>();
    private final TableView<ResponseAction> responseActionsTable = new TableView<>();

    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane(
                new Tab("Локації", locationPane()),
                new Tab("Екологічні інциденти", incidentPane()),
                new Tab("Спостереження", observationPane()),
                new Tab("Заходи реагування", responseActionPane())
        );

        tabs.getTabs().forEach(t -> t.setClosable(false));

        stage.setScene(new Scene(tabs, 1000, 650));
        stage.setTitle("Система реєстрації та опрацювання екологічних інцидентів");
        stage.show();

        refreshAll();
    }

    private Pane locationPane() {
        locationsTable.getColumns().setAll(
                col("ID", "id"),
                col("Назва", "name"),
                col("Область", "region"),
                col("Адреса", "address"),
                col("Широта", "latitude"),
                col("Довгота", "longitude"),
                col("Опис", "description")
        );

        TextField name = new TextField();
        TextField region = new TextField();
        TextField address = new TextField();
        TextField latitude = new TextField();
        TextField longitude = new TextField();
        TextField description = new TextField();

        Button add = new Button("Додати");
        Button update = new Button("Змінити");
        Button delete = new Button("Видалити");
        Button refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            api.addLocation(readLocation(null, name, region, address, latitude, longitude, description));
            refreshLocations();
        }));

        update.setOnAction(e -> safe(() -> {
            Location location = selected(locationsTable);
            api.updateLocation(readLocation(location.getId(), name, region, address, latitude, longitude, description));
            refreshLocations();
        }));

        delete.setOnAction(e -> safe(() -> {
            api.deleteLocation(selected(locationsTable).getId());
            refreshAll();
        }));

        refresh.setOnAction(e -> refreshLocations());

        locationsTable.getSelectionModel().selectedItemProperty().addListener((o, a, l) -> {
            if (l != null) {
                name.setText(l.getName());
                region.setText(l.getRegion());
                address.setText(l.getAddress());
                latitude.setText("" + l.getLatitude());
                longitude.setText("" + l.getLongitude());
                description.setText(l.getDescription());
            }
        });

        VBox form = new VBox(6,
                new Label("Назва"), name,
                new Label("Область"), region,
                new Label("Адреса"), address,
                new Label("Широта"), latitude,
                new Label("Довгота"), longitude,
                new Label("Опис"), description,
                new HBox(8, add, update, delete, refresh)
        );

        return layout(locationsTable, form);
    }

    private Pane incidentPane() {
        incidentsTable.getColumns().setAll(
                col("ID", "id"),
                col("Назва", "title"),
                col("Тип інциденту", "incidentType"),
                col("ID локації", "locationId"),
                col("F1", "f1"),
                col("F2", "f2"),
                col("F3", "f3"),
                col("Інтегральний показник", "integralScore"),
                col("Стан", "status"),
                col("Опис", "description")
        );

        TextField title = new TextField();
        TextField description = new TextField();
        TextField incidentType = new TextField();
        TextField locationId = new TextField();
        TextField f1 = new TextField();
        TextField f2 = new TextField();
        TextField f3 = new TextField();

        ComboBox<IncidentStatus> status = new ComboBox<>(
                FXCollections.observableArrayList(IncidentStatus.values())
        );

        Button add = new Button("Додати");
        Button update = new Button("Змінити");
        Button delete = new Button("Видалити");
        Button refresh = new Button("Оновити");
        Button change = new Button("Змінити стан");

        add.setOnAction(e -> safe(() -> {
            api.addIncident(readIncident(null, title, description, incidentType, locationId, f1, f2, f3,
                    IncidentStatus.REGISTERED, null, null));
            refreshIncidents();
        }));

        update.setOnAction(e -> safe(() -> {
            Incident incident = selected(incidentsTable);
            api.updateIncident(readIncident(incident.getId(), title, description, incidentType, locationId,
                    f1, f2, f3, incident.getStatus(), incident.getIntegralScore(), incident.getCreatedAt()));
            refreshIncidents();
        }));

        delete.setOnAction(e -> safe(() -> {
            api.deleteIncident(selected(incidentsTable).getId());
            refreshAll();
        }));

        refresh.setOnAction(e -> refreshIncidents());

        change.setOnAction(e -> safe(() -> {
            Incident incident = selected(incidentsTable);

            if (status.getValue() == null) {
                throw new IllegalArgumentException("Оберіть новий стан");
            }

            api.changeIncidentStatus(incident.getId(), status.getValue());
            refreshIncidents();
        }));

        incidentsTable.getSelectionModel().selectedItemProperty().addListener((o, a, i) -> {
            if (i != null) {
                title.setText(i.getTitle());
                description.setText(i.getDescription());
                incidentType.setText(i.getIncidentType());
                locationId.setText("" + i.getLocationId());
                f1.setText("" + i.getF1());
                f2.setText("" + i.getF2());
                f3.setText("" + i.getF3());
                status.setValue(i.getStatus());
            }
        });

        VBox form = new VBox(6,
                new Label("Назва"), title,
                new Label("Опис"), description,
                new Label("Тип інциденту"), incidentType,
                new Label("ID локації"), locationId,
                new Label("F1"), f1,
                new Label("F2"), f2,
                new Label("F3"), f3,
                new HBox(8, add, update, delete, refresh),
                new Label("Новий стан"), status,
                change
        );

        return layout(incidentsTable, form);
    }

    private Pane observationPane() {
        observationsTable.getColumns().setAll(
                col("ID", "id"),
                col("ID інциденту", "incidentId"),
                col("Дата спостереження", "observationDate"),
                col("Показник", "indicatorName"),
                col("Значення", "indicatorValue"),
                col("Одиниця вимірювання", "unit"),
                col("Опис", "description"),
                col("Джерело", "source")
        );

        TextField incidentId = new TextField();
        TextField date = new TextField();
        TextField indicator = new TextField();
        TextField value = new TextField();
        TextField unit = new TextField();
        TextField description = new TextField();
        TextField source = new TextField();

        ProgressBar progress = new ProgressBar(0);
        Label status = new Label("Імпорт не запущено");

        Button add = new Button("Додати");
        Button update = new Button("Змінити");
        Button delete = new Button("Видалити");
        Button refresh = new Button("Оновити");
        Button importCsv = new Button("Імпортувати CSV");

        add.setOnAction(e -> safe(() -> {
            api.addObservation(readObservation(null, incidentId, date, indicator, value, unit, description, source));
            refreshObservations();
        }));

        update.setOnAction(e -> safe(() -> {
            Observation observation = selected(observationsTable);
            api.updateObservation(readObservation(observation.getId(), incidentId, date, indicator, value,
                    unit, description, source));
            refreshObservations();
        }));

        delete.setOnAction(e -> safe(() -> {
            api.deleteObservation(selected(observationsTable).getId());
            refreshObservations();
        }));

        refresh.setOnAction(e -> refreshObservations());

        observationsTable.getSelectionModel().selectedItemProperty().addListener((o, a, v) -> {
            if (v != null) {
                incidentId.setText("" + v.getIncidentId());
                date.setText(v.getObservationDate().toString());
                indicator.setText(v.getIndicatorName());
                value.setText("" + v.getIndicatorValue());
                unit.setText(v.getUnit());
                description.setText(v.getDescription());
                source.setText(v.getSource());
            }
        });

        importCsv.setOnAction(e -> {
            ImportObservationsTask task = new ImportObservationsTask(
                    Path.of("sample-data", "observations.csv"), api
            );

            progress.progressProperty().bind(task.progressProperty());
            status.textProperty().bind(task.messageProperty());

            importCsv.setDisable(true);

            task.setOnSucceeded(x -> {
                progress.progressProperty().unbind();
                status.textProperty().unbind();
                importCsv.setDisable(false);

                status.setText("Імпортовано: " + task.getValue());

                refreshObservations();
            });

            task.setOnFailed(x -> {
                progress.progressProperty().unbind();
                status.textProperty().unbind();
                importCsv.setDisable(false);
                progress.setProgress(0);
                status.setText("Імпорт не виконано");
                showError(task.getException());
            });

            task.setOnCancelled(x -> {
                progress.progressProperty().unbind();
                status.textProperty().unbind();
                importCsv.setDisable(false);
                status.setText("Імпорт скасовано");
                refreshObservations();
            });

            new Thread(task, "csv-observation-import").start();
        });

        VBox form = new VBox(6,
                new Label("ID інциденту"), incidentId,
                new Label("Дата спостереження (YYYY-MM-DDTHH:MM:SS)"), date,
                new Label("Назва показника"), indicator,
                new Label("Значення"), value,
                new Label("Одиниця вимірювання"), unit,
                new Label("Опис"), description,
                new Label("Джерело"), source,
                new HBox(8, add, update, delete, refresh),
                importCsv, progress, status
        );

        return layout(observationsTable, form);
    }

    private Pane responseActionPane() {
        responseActionsTable.getColumns().setAll(
                col("ID", "id"),
                col("ID інциденту", "incidentId"),
                col("Назва заходу", "title"),
                col("Відповідальна особа", "responsiblePerson"),
                col("Запланована дата", "plannedDate"),
                col("Дата завершення", "completedDate"),
                col("Статус", "status"),
                col("Опис", "description")
        );

        TextField incidentId = new TextField();
        TextField title = new TextField();
        TextField description = new TextField();
        TextField responsible = new TextField();
        TextField plannedDate = new TextField();
        TextField completedDate = new TextField();
        TextField status = new TextField();

        Button add = new Button("Додати");
        Button update = new Button("Змінити");
        Button delete = new Button("Видалити");
        Button refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            api.addResponseAction(readResponseAction(null, incidentId, title, description,
                    responsible, plannedDate, completedDate, status));
            refreshResponseActions();
        }));

        update.setOnAction(e -> safe(() -> {
            ResponseAction action = selected(responseActionsTable);
            api.updateResponseAction(readResponseAction(action.getId(), incidentId, title, description,
                    responsible, plannedDate, completedDate, status));
            refreshResponseActions();
        }));

        delete.setOnAction(e -> safe(() -> {
            api.deleteResponseAction(selected(responseActionsTable).getId());
            refreshResponseActions();
        }));

        refresh.setOnAction(e -> refreshResponseActions());

        responseActionsTable.getSelectionModel().selectedItemProperty().addListener((o, a, v) -> {
            if (v != null) {
                incidentId.setText("" + v.getIncidentId());
                title.setText(v.getTitle());
                description.setText(v.getDescription());
                responsible.setText(v.getResponsiblePerson());
                plannedDate.setText(v.getPlannedDate());
                completedDate.setText(v.getCompletedDate() == null ? "" : v.getCompletedDate());
                status.setText(v.getStatus());
            }
        });

        VBox form = new VBox(6,
                new Label("ID інциденту"), incidentId,
                new Label("Назва заходу"), title,
                new Label("Опис"), description,
                new Label("Відповідальна особа"), responsible,
                new Label("Запланована дата"), plannedDate,
                new Label("Дата завершення"), completedDate,
                new Label("Статус"), status,
                new HBox(8, add, update, delete, refresh)
        );

        return layout(responseActionsTable, form);
    }

    private Location readLocation(Long id, TextField name, TextField region, TextField address,
                                  TextField latitude, TextField longitude, TextField description) {
        return new Location(
                id,
                name.getText(),
                region.getText(),
                address.getText(),
                Double.parseDouble(latitude.getText().replace(',', '.')),
                Double.parseDouble(longitude.getText().replace(',', '.')),
                description.getText()
        );
    }

    private Incident readIncident(Long id, TextField title, TextField description, TextField incidentType,
                                  TextField locationId, TextField f1, TextField f2, TextField f3,
                                  IncidentStatus status, Double integralScore, String createdAt) {
        return new Incident(
                id,
                title.getText(),
                description.getText(),
                incidentType.getText(),
                Long.parseLong(locationId.getText()),
                Double.parseDouble(f1.getText().replace(',', '.')),
                Double.parseDouble(f2.getText().replace(',', '.')),
                Double.parseDouble(f3.getText().replace(',', '.')),
                integralScore == null ? 0 : integralScore,
                status == null ? IncidentStatus.REGISTERED : status,
                createdAt
        );
    }

    private Observation readObservation(Long id, TextField incidentId, TextField date, TextField indicator,
                                        TextField value, TextField unit, TextField description, TextField source) {
        return new Observation(
                id,
                Long.parseLong(incidentId.getText()),
                date.getText(),
                indicator.getText(),
                Double.parseDouble(value.getText().replace(',', '.')),
                unit.getText(),
                description.getText(),
                source.getText()
        );
    }

    private ResponseAction readResponseAction(Long id, TextField incidentId, TextField title,
                                              TextField description, TextField responsible,
                                              TextField plannedDate, TextField completedDate,
                                              TextField status) {
        return new ResponseAction(
                id,
                Long.parseLong(incidentId.getText()),
                title.getText(),
                description.getText(),
                responsible.getText(),
                plannedDate.getText(),
                completedDate.getText(),
                status.getText()
        );
    }

    private void refreshAll() {
        refreshLocations();
        refreshIncidents();
        refreshObservations();
        refreshResponseActions();
    }

    private void refreshLocations() {
        safe(() -> locationsTable.setItems(
                FXCollections.observableArrayList(api.getLocations())
        ));
    }

    private void refreshIncidents() {
        safe(() -> incidentsTable.setItems(
                FXCollections.observableArrayList(api.getIncidents())
        ));
    }

    private void refreshObservations() {
        safe(() -> observationsTable.setItems(
                FXCollections.observableArrayList(api.getObservations())
        ));
    }

    private void refreshResponseActions() {
        safe(() -> responseActionsTable.setItems(
                FXCollections.observableArrayList(api.getResponseActions())
        ));
    }

    private <T> T selected(TableView<T> table) {
        T value = table.getSelectionModel().getSelectedItem();

        if (value == null) {
            throw new IllegalArgumentException("Виберіть запис");
        }

        return value;
    }

    private void safe(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
            showError(e);
        }
    }

    private void showError(Throwable e) {
        Alert dialog = new Alert(
                Alert.AlertType.ERROR,
                e == null ? "Помилка" : e.getMessage(),
                ButtonType.OK
        );

        dialog.showAndWait();
    }

    private Pane layout(TableView<?> table, Pane form) {
        SplitPane split = new SplitPane(table, form);
        split.setDividerPositions(0.68);

        BorderPane root = new BorderPane(split);
        root.setPadding(new Insets(10));

        return root;
    }

    private <S, T> TableColumn<S, T> col(String title, String property) {
        TableColumn<S, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    public static void main(String[] args) {
        launch(args);
    }
}