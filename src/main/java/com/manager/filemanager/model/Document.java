package com.manager.filemanager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Getter
@Setter
@Builder
public class Document {

    @Id
    private String id;
    private Type type;
    private String name;
    private String path;
    private String[] pathSegments;
    private Instant createdTime;
    private Instant accessTime;
    private Instant modifyTime;

    public static enum Type {
        FOLDER, FILE, OTHERS;

        public static Type fromType(int type) {
            return switch (type) {
                case 1 -> FILE;
                case 2 -> FOLDER;
                default -> OTHERS;
            };
        }
    }
}
