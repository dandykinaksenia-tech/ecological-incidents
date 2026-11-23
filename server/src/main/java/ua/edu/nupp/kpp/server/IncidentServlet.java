package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncidentServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(IncidentServlet.class.getName());
    private final IncidentService service = new IncidentService();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        try {
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String action = req.getParameter("action");
            if ("add".equals(action) || "update".equals(action)) {
                Incident value = new Incident(
                        "update".equals(action) ? id(req) : null,
                        req.getParameter("title"),
                        req.getParameter("description"),
                        req.getParameter("incidentType"),
                        longValue(req.getParameter("locationId")),
                        doubleValue(req.getParameter("f1")),
                        doubleValue(req.getParameter("f2")),
                        doubleValue(req.getParameter("f3")),
                        0,
                        IncidentStatus.REGISTERED,
                        LocalDateTime.now()
                );
                if ("add".equals(action)) {
                    service.add(value);
                } else {
                    service.update(value);
                }
            } else if ("transition".equals(action)) {
                String statusValue = req.getParameter("status");
                if (statusValue == null || statusValue.isBlank()) {
                    throw new IllegalArgumentException(
                            "Не задано статус інциденту");
                }
                service.changeStatus(
                        id(req),
                        IncidentStatus.valueOf(statusValue)
                );
            } else if ("delete".equals(action)) {

                service.delete(id(req));

            } else {
                throw new IllegalArgumentException("Невідома операція");
            }
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }
    private void writeJson(HttpServletResponse resp,
                           Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), data);
    }
    private void writeError(HttpServletResponse resp,
                            Exception e) throws IOException {
        LOG.log(Level.SEVERE, "Помилка під час обробки запиту", e);
        boolean clientError = e instanceof IllegalArgumentException;
        int status = clientError
                ? HttpServletResponse.SC_BAD_REQUEST
                : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        String message = clientError
                ? (e.getMessage() == null
                ? "Некоректний запит"
                : e.getMessage())
                : "Внутрішня помилка сервера";

        resp.setStatus(status);
        writeJson(resp, Map.of("error", message));
    }
    private long id(HttpServletRequest req) {
        String value = req.getParameter("id");
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Не задано ID інциденту");
        }
        return Long.parseLong(value);
    }
    private Long longValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Long.parseLong(value);
    }
    private double doubleValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Числове значення не може бути порожнім");
        }

        return Double.parseDouble(value);
    }
}