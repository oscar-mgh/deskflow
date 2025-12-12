package com.github.oscarmgh.deskflow.exceptions.tickets;

public class FileUploadNotAllowedException extends RuntimeException {
    public FileUploadNotAllowedException() {
        super("Only PREMIUM users can upload files");
    }
}