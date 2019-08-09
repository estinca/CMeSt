package com.est.repository.api.exception;

import com.est.core.common.error.CoreException;

public class RepositoryException extends CoreException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RepositoryException(RepositoryError error) {
        super(error);
    }

    public RepositoryException(RepositoryError error, Throwable cause) {
        super(error, cause);
    }
}
