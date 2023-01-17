package ru.netology.i18n;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalizationServiceImplTest {
    @ParameterizedTest
    @MethodSource("sourceMethod")
    public void localeTest(Country country, String expectedMessage) {
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();
        String message = localizationService.locale(country);
        assertEquals(expectedMessage, message);

    }

    public static Stream<Arguments> sourceMethod() {
        return Stream.of(Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.USA, "Welcome"),
                Arguments.of(Country.BRAZIL, "Welcome"),
                Arguments.of(Country.GERMANY, "Welcome"));
    }

}
