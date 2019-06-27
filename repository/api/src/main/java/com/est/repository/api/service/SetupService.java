package com.est.repository.api.service;

import com.est.repository.api.exception.RepositoryException;

public interface SetupService {

    boolean isRepositorySetup() throws RepositoryException;

    void setupRepository() throws RepositoryException;
}