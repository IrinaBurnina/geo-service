package ru.netology.sender;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageSenderImplTest {
    @ParameterizedTest
    @MethodSource("sourceMethod")
    public void sendTest(String expectedRealIp, String ip, String expectedMessage,
                         Location locationValue, String valueCountry) {
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        GeoServiceMock geoService = new GeoServiceMock();
        geoService.setValue(locationValue);
        LocalizationServiceMock local = new LocalizationServiceMock();
        local.setValueCountry(valueCountry);
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, local);
        expectedHeaders.put(expectedRealIp, ip);
        String result = messageSender.send(expectedHeaders);
        assertEquals(expectedMessage, result);

    }

    public class GeoServiceMock implements GeoService {
        private Location value;

        @Override
        public Location byIp(String ip) {
            return value;
        }

        @Override
        public Location byCoordinates(double latitude, double longitude) {
            return value;
        }

        public void setValue(Location value) {
            this.value = value;
        }
    }

    public class LocalizationServiceMock implements LocalizationService {
        private String locale;

        @Override
        public String locale(Country country) {
            return locale;
        }

        public void setValueCountry(String valueCountry) {
            this.locale = valueCountry;
        }
    }

    public static Stream<Arguments> sourceMethod() {
        return Stream.of(Arguments.of("x-real-ip", "96.", "Welcome",
                        new Location("New York", Country.USA, null, 0), "Welcome"),
                Arguments.of("x-real-ip", "172.", "Добро пожаловать",
                        new Location("Moscow", Country.RUSSIA, null, 0), "Добро пожаловать"),
                Arguments.of("x-real-ip", "172.0.32.11", "Добро пожаловать",
                        new Location("Moscow", Country.RUSSIA, "Lenina", 15), "Добро пожаловать"),
                Arguments.of("x-real-ip", "96.44.183.149", "Welcome",
                        new Location("New York", Country.USA, " 10th Avenue", 32), "Welcome"),
                Arguments.of("x-real-ip", "127.0.0.1", "Welcome",
                        new Location("Brazilia", Country.BRAZIL, "Paranariver", 12), "Welcome"),
                Arguments.of("x-real-ip", null, "Welcome",
                        new Location(null, null, null, 0), "Welcome"));
    }
}
