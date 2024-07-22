package com.manager.filemanager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Document {

    private Type type;
    private String name;
    private String path;
    private String[] pathSegments;

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
