package com.est.repository.api.service;

import java.sql.SQLException;

import com.est.repository.api.exception.RepositoryException;

public interface SetupService {

    boolean isRepositorySetup() throws SQLException;

    void setupRepository() throws RepositoryException;
}