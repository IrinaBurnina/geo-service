package ru.netology.geo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GeoServiceImplTest {
    @ParameterizedTest
    @MethodSource("sourceMethod")
    public void byIpParamTest(Location expectedLocation, String ip) {
        GeoServiceImpl geoService = new GeoServiceImpl();
        Location resultIp = geoService.byIp(ip);
        assertEquals(expectedLocation, resultIp);
    }

    @Test
    @NullAndEmptySource
    public void byIpStringNullTest() {
        String ip = "k";
        GeoServiceImpl geoService = new GeoServiceImpl();
        Location resultIp = geoService.byIp(ip);
        assertNull(resultIp);
    }

    public static Stream<Arguments> sourceMethod() {
        return Stream.of(Arguments.of(new Location("Moscow", Country.RUSSIA, "Lenina", 15), "172.0.32.11"),
                Arguments.of(new Location(null, null, null, 0), "127.0.0.1"),
                Arguments.of(new Location("New York", Country.USA, " 10th Avenue", 32), "96.44.183.149"),
                Arguments.of(new Location("Moscow", Country.RUSSIA, null, 0), "172."),
                Arguments.of(new Location("New York", Country.USA, null, 0), "96."));
    }

    @Test
    public void byCoordinatesParamTest() {
        double latitude = 0.0;
        double longitude = 0.0;
        GeoServiceImpl geoService = new GeoServiceImpl();
        assertThrows(RuntimeException.class, () -> geoService.byCoordinates(latitude, longitude));
    }

}
