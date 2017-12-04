package com.demo.si.file.process.exception;

import java.io.Serializable;
/**
 * @author Karthik Palanivelu
 */
public class SystemException extends RuntimeException implements Serializable {
    public SystemException(String message) {

        super(message);
    }

    public SystemException(String message, Throwable cause) {

        super(message, cause);
    }

    public SystemException(Throwable cause) {

        super(cause);
    }

}
