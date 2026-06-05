package de.jaypi4c.demo.backend.model;

import java.util.UUID;

public record BookUpdateMessage(UUID jobId, String bookId, String status) {

    public String toJson() {
        return String.format("{\"bookId\":\"%s\",\"status\":\"%s\"}", bookId, status);
    }

}
