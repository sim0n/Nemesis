package dev.sim0n.anticheat.log;

import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

@Getter
public class Log {
    private final long timestamp;

    private final UUID uuid;
    private final String check, data;

    private final int vl;

    public Log(long timestamp, UUID uuid, String check, String data, int vl) {
        this.timestamp = timestamp;

        this.uuid = uuid;
        this.check = check;
        this.data = data;
        this.vl = vl;
    }

    public Log(UUID uuid, String check, String data, int vl) {
        this.timestamp = System.currentTimeMillis();

        this.uuid = uuid;
        this.check = check;
        this.data = data;
        this.vl = vl;
    }

    public static Document toDocument(Log log) {
        Document document = new Document();

        document.put("time", log.getTimestamp());
        document.put("uuid", log.getUuid().toString());
        document.put("check", log.getCheck());
        document.put("data", log.getData());
        document.put("vl", log.getVl());

        return document;
    }

    public static Log fromDocument(Document document) {
        return new Log(
                document.getLong("time"), UUID.fromString(document.getString("uuid")),
                document.getString("check"), document.getString("data"),
                document.getInteger("vl")
        );
    }
}

