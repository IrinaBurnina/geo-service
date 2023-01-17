package ru.netology.sender;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageSenderImplTest {
    @ParameterizedTest
    @MethodSource("sourceMethod")
    public void sendTest(String expectedRealIp, String ip, String expectedMessage,
                         Location locationValue, String valueCountry, Country country) {
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(locationValue);
        LocalizationService local = Mockito.mock(LocalizationService.class);
        Mockito.when(local.locale(country))
                .thenReturn(valueCountry);
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, local);
        expectedHeaders.put(expectedRealIp, ip);
        String result = messageSender.send(expectedHeaders);
        assertEquals(expectedMessage, result);
    }

    public static Stream<Arguments> sourceMethod() {
        return Stream.of(Arguments.of("x-real-ip", "96.", "Welcome",
                        new Location("New York", Country.USA, null, 0), "Welcome", Country.USA),
                Arguments.of("x-real-ip", "172.", "Добро пожаловать",
                        new Location("Moscow", Country.RUSSIA, null, 0), "Добро пожаловать", Country.RUSSIA),
                Arguments.of("x-real-ip", "172.0.32.11", "Добро пожаловать",
                        new Location("Moscow", Country.RUSSIA, "Lenina", 15), "Добро пожаловать", Country.RUSSIA),
                Arguments.of("x-real-ip", "96.44.183.149", "Welcome",
                        new Location("New York", Country.USA, " 10th Avenue", 32), "Welcome", Country.USA),
                Arguments.of("x-real-ip", "127.0.0.1", "Welcome",
                        new Location(null, null, null, 0), "Welcome", null),
                Arguments.of("x-real-ip", "127.0.0.1", null,
                        new Location(null, null, null, 0), "Welcome", Country.USA));
    }
}
