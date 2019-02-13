package org.servantscode.photo;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public class Photo {
    private String guid;
    private String fileType;
    private InputStream bytes;

    public Photo() {}

    public Photo(String guid, String fileType, InputStream bytes) {
        this.guid = guid;
        this.fileType = fileType;
        this.bytes = bytes;
    }

    // ----- Accessors -----
    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public InputStream getBytes() { return bytes; }
    public void setBytes(InputStream bytes) { this.bytes = bytes; }
}
