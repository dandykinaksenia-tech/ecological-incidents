package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class ResponseActionServlet extends HttpServlet {
    private final ResponseActionService service = new ResponseActionService();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            String action = req.getParameter("action");

            if ("delete".equals(action)) {
                service.delete(id(req));
            } else if ("add".equals(action) || "update".equals(action)) {
                Response_action value = new Response_action(
                        "update".equals(action) ? id(req) : null,
                        longValue(req.getParameter("incidentId")),
                        req.getParameter("title"),
                        req.getParameter("description"),
                        req.getParameter("responsiblePerson"),
                        LocalDateTime.parse(req.getParameter("plannedDate")),
                        dateValue(req.getParameter("completedDate")),
                        req.getParameter("status")
                );

                if ("add".equals(action)) {
                    service.add(value);
                } else {
                    service.update(value);
                }
            } else {
                throw new IllegalArgumentException("Невідома операція");
            }

            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), data);
    }

    private void writeError(HttpServletResponse resp, Exception e) throws IOException {
        boolean clientError = e instanceof IllegalArgumentException;
        int status = clientError ? HttpServletResponse.SC_BAD_REQUEST
                : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = clientError
                ? (e.getMessage() == null ? "Некоректний запит" : e.getMessage())
                : "Внутрішня помилка сервера";

        resp.setStatus(status);
        writeJson(resp, Map.of("error", message));
    }

    private long id(HttpServletRequest req) {
        String value = req.getParameter("id");
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("Не вказано id");
        return Long.parseLong(value);
    }

    private Long longValue(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("Не вказано ID екологічного інциденту");
        return Long.parseLong(value);
    }

    private LocalDateTime dateValue(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return LocalDateTime.parse(value);
    }
}