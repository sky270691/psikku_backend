package com.psikku.backend.exception;

import javax.persistence.NonUniqueResultException;

public class DataAlreadyExistException extends NonUniqueResultException {

    public DataAlreadyExistException(String message) {
        super(message);
    }
}
