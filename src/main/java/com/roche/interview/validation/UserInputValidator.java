package com.roche.interview.validation;

import com.roche.interview.exception.ApiRequestException;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface UserInputValidator extends Function<Optional<List<String>>, ValidationResult> {

    static boolean isIp(String ip) {
        return InetAddressValidator.getInstance().isValidInet4Address(ip);
    }

    static UserInputValidator hasValidIps() {
        return (list) -> {
            list.ifPresent(
                    (listOfIp) -> {
                        listOfIp.forEach((ip) -> {
                            if (!isIp(ip)) {
                                throw new ApiRequestException(ValidationResult.HAS_INVALID_IP_ADDRESS.name());
                            }
                        });
                    });
            return ValidationResult.VALID_INPUT;
        };
    }

    static UserInputValidator hasNotEmptyValues() {
        return (list) -> {
            list.ifPresent(
                    (listOfIp) -> {
                        listOfIp.forEach((ip) -> {
                            if (ip.trim().isEmpty()) {
                                throw new ApiRequestException(ValidationResult.HAS_EMPTY_VALUE.name());
                            }
                        });

                    });
            return ValidationResult.VALID_INPUT;
        };


    }

    static UserInputValidator hasAppropriateSize() {
        return (iplist) -> {
            iplist.ifPresent(
                    (list) -> {
                        if (list.size() < 1) {
                            throw new ApiRequestException(ValidationResult.INPUT_SIZE_IS_LESS_THAN_ONE.name());
                        } else if (list.size() > 5) {
                            throw new ApiRequestException(ValidationResult.INPUT_SIZE_IS_GREATER_THAN_FIVE.name());
                        }
                    });
            return ValidationResult.VALID_INPUT;
        };
    }

    default UserInputValidator and(UserInputValidator other) {
        return list -> {
            ValidationResult result = this.apply(list);
            return result.equals(ValidationResult.VALID_INPUT) ? other.apply(list) : result;
        };
    }
}