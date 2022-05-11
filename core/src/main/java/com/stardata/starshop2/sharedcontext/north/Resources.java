package com.stardata.starshop2.sharedcontext.north;

import com.stardata.starshop2.sharedcontext.exception.ApplicationDomainException;
import com.stardata.starshop2.sharedcontext.exception.ApplicationInfrastructureException;
import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;


public class Resources {
    private static final Logger logger = LoggerFactory.getLogger(Resources.class.getName());

    private Resources(String requestType) {
        this.requestType = requestType;
    }

    private final String requestType;
    private HttpStatus successfulStatus;
    private HttpStatus errorStatus;
    private HttpStatus failedStatus;

    public static Resources with(String requestType) {
        return new Resources(requestType);
    }

    public Resources onSuccess(HttpStatus status) {
        this.successfulStatus = status;
        return this;
    }

    public Resources onError(HttpStatus status) {
        this.errorStatus = status;
        return this;
    }

    public Resources onFailed(HttpStatus status) {
        this.failedStatus = status;
        return this;
    }

    public <T> ResponseEntity<T> execute(Supplier<T> supplier) {
        try {
            T entity = supplier.get();
            return new ResponseEntity<>(entity, successfulStatus);
        } catch (ApplicationValidationException ex) {
            logger.warn(String.format("The request of %s is invalid", requestType));
            return new ResponseEntity<>(errorStatus);
        } catch (ApplicationDomainException ex) {
            logger.warn( String.format("Exception raised %s REST Call", requestType));
            return new ResponseEntity<>(failedStatus);
        } catch (ApplicationInfrastructureException ex) {
            logger.error( String.format("Fatal exception raised %s REST Call", requestType));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> execute(Runnable runnable) {
        try {
            runnable.run();
            return new ResponseEntity<>(successfulStatus);
        } catch (ApplicationValidationException ex) {
            logger.warn(String.format("The request of %s is invalid", requestType));
            return new ResponseEntity<>(errorStatus);
        } catch (ApplicationDomainException ex) {
            logger.warn( String.format("Exception raised %s REST Call", requestType));
            return new ResponseEntity<>(failedStatus);
        } catch (ApplicationInfrastructureException ex) {
            logger.error( String.format("Fatal exception raised %s REST Call", requestType));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}