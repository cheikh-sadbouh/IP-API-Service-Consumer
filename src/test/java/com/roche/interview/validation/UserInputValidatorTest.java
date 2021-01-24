package com.roche.interview.validation;

import com.roche.interview.exception.ApiRequestException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserInputValidatorTest {
    static Stream<Arguments> getValidIPList() {
        return Stream.of(Arguments.of(Arrays.asList("127.0.0.1", "8.8.8.8")));
    }

    static Stream<Arguments> getIpAddressList() {
        return Stream.of(Arguments.of(Arrays.asList("127.0.0.1", "8.8.8.8", "000.3.4.5", "45.$.6.9")));
    }

    static Stream<Arguments> getListWithEmptyValue() {
        return Stream.of(Arguments.of(Arrays.asList("127.0.0.1", "8.8.8.8", "", "    ")));
    }

    static Stream<Arguments> getListWithNoEmptyValues() {
        return Stream.of(Arguments.of(Arrays.asList("127.0.0.1", "8.8.8.8")));
    }

    static Stream<Arguments> getListWithNoElement() {
        return Stream.of(Arguments.of(Collections.emptyList()));
    }

    static Stream<Arguments> getListWithMoreThanFiveElements() {
        return Stream.of(Arguments.of(Arrays.asList("127.0.0.1", "8.8.8.8", "4.5.6.7", "90.23.4.5", "12.4.6.8", "5.9.0.12", "23.45.8.0")));
    }

    static Stream<Arguments> getListWithAppropriateSize() {
        return Stream.of(Arguments.of(Arrays.asList("127.0.0.1", "8.8.8.8", "34.7.8.12")));
    }

    @ParameterizedTest
    @CsvSource({
            "121.234.12.12",
            "8.8.8.8",
            "127.0.0.1"
    })
    void Should_Return_True_When_IpAddress_Is_Valid(String ipAddress) {
        assertThat(UserInputValidator.isIp(ipAddress)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "000.12.12.034",
            "8.8,8,8",
            "I.Am.not.an.ip",
            "127.0.0.ip",
            "23.#.56.2.1",
            "23.3.  .2.1"
    })
    void Should_Return_False_When_IpAddress_Is_Not_Valid(String ipAddress) {
        assertThat(UserInputValidator.isIp(ipAddress)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getValidIPList")
    void Should_Return_Valid_Input_When_All_Ips_Are_Valid(List<String> ipAddressList) {
        assertThat(UserInputValidator.hasValidIps().apply(Optional.of(ipAddressList))).isEqualTo(ValidationResult.VALID_INPUT);
    }

    @ParameterizedTest
    @MethodSource("getIpAddressList")
    void Should_Return_Throw_Exception_When_List_Contains_Invalid_IpAddress(List<String> ipAddressList) {

        assertThatExceptionOfType(ApiRequestException.class)
                .isThrownBy(() -> UserInputValidator.hasValidIps().apply(Optional.of(ipAddressList)))
                .withMessage(ValidationResult.HAS_INVALID_IP_ADDRESS.name());
    }

    @ParameterizedTest
    @MethodSource("getListWithEmptyValue")
    void Should_Throw_Exception_When_List_Contains_Empty_Values(List<String> ipAddressList) {
        assertThatExceptionOfType(ApiRequestException.class)
                .isThrownBy(() -> UserInputValidator.hasNotEmptyValues().apply(Optional.of(ipAddressList)))
                .withMessage(ValidationResult.HAS_EMPTY_VALUE.name());
    }

    @ParameterizedTest
    @MethodSource("getListWithNoEmptyValues")
    void Should_Return_Valid_Input_When_List_Contains_No_Empty_Values(List<String> ipAddressList) {
        assertThat(UserInputValidator.hasNotEmptyValues().apply(Optional.of(ipAddressList))).isEqualTo(ValidationResult.VALID_INPUT);

    }

    @ParameterizedTest
    @MethodSource("getListWithNoElement")
    void Should_Throw_Exception_When_List_Size_Is_Less_Than_One(List<String> ipAddressList) {
        assertThatExceptionOfType(ApiRequestException.class)
                .isThrownBy(() -> UserInputValidator.hasAppropriateSize().apply(Optional.of(ipAddressList)))
                .withMessage(ValidationResult.INPUT_SIZE_IS_LESS_THAN_ONE.name());
    }

    @ParameterizedTest
    @MethodSource("getListWithMoreThanFiveElements")
    void Should_Throw_Exception_When_List_Size_Is_More_Than_Five(List<String> ipAddressList) {
        assertThatExceptionOfType(ApiRequestException.class)
                .isThrownBy(() -> UserInputValidator.hasAppropriateSize().apply(Optional.of(ipAddressList)))
                .withMessage(ValidationResult.INPUT_SIZE_IS_GREATER_THAN_FIVE.name());
    }

    @ParameterizedTest
    @MethodSource("getListWithAppropriateSize")
    void Should_Return_Valid_Input_When_List_Size_Is_Between_OneAndFive(List<String> ipAddressList) {
        assertThat(UserInputValidator.hasAppropriateSize().apply(Optional.of(ipAddressList))).isEqualTo(ValidationResult.VALID_INPUT);

    }
}
