package org.passau.visualizor.service;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.passau.visualizor.domain.Position;
import org.passau.visualizor.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class PathPredictionService {

    private final static String CUSF_API_URL = "https://predict.cusf.co.uk/api/v1/";

    private static final double BURST_ALTITUDE = 30000;

    private static Position lastPredictionPosition = null;
    private static boolean ascending = true;

    public LinkedHashMap<?, ?> predictPath(Position balloonPosition) {

        String getRequest = CUSF_API_URL + "?";

        //LocalDateTime utc0Time = DateTimeUtils.convertUTC3toUTC0(balloonPosition.getDateTime());

        Position lastPredictionPos = getLastPredictionPosition() == null ? null : getLastPredictionPositionCopy();

        if(balloonPosition.getAltitude() >= BURST_ALTITUDE) {
            setAscending(false);
        }

        if(lastPredictionPos != null && (!ascending ||
                (balloonPosition.getDateTime().toEpochSecond(ZoneOffset.ofHours(0)) > lastPredictionPos.getDateTime().toEpochSecond(ZoneOffset.ofHours(0))
                && balloonPosition.getAltitude() < lastPredictionPos.getAltitude())
        )) {
            balloonPosition = lastPredictionPos;
        }

        LocalDateTime utc0Time = balloonPosition.getDateTime();
        getRequest += ("launch_latitude=" + balloonPosition.getLatitude());
        getRequest += ("&launch_longitude=" + balloonPosition.getLongitude());
        getRequest += ("&launch_altitude=" + balloonPosition.getAltitude());
        getRequest += ("&launch_datetime=" + (utc0Time.getSecond() == 0 ? (utc0Time.toString() + ":00") : utc0Time) + "Z");
        getRequest += ("&ascent_rate=" + 5); //calculate
        getRequest += ("&burst_altitude=" + BURST_ALTITUDE); //calculate
        getRequest += ("&descent_rate=" + 5); //calculate

        //System.out.println(getRequest);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getRequest))
                .build();

        LinkedHashMap<?, ?> prediction = null;
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            JSONParser parser = new JSONParser(response.body());
            Object obj = parser.parse();
            if(obj instanceof LinkedHashMap && ((LinkedHashMap<?, ?>) obj).get("prediction") instanceof ArrayList) {
                DateTimeUtils.changeResultDateTimeToUTC3((ArrayList<?>) ((LinkedHashMap<?, ?>) obj).get("prediction"));
                prediction = (LinkedHashMap<?, ?>) obj;
            } else if(obj instanceof LinkedHashMap) {
                return (LinkedHashMap<?, ?>) obj;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (java.text.ParseException | ParseException e) {
            e.printStackTrace();
        }

        if(getLastPredictionPosition() == null || getLastPredictionPositionCopy().getDateTime().toEpochSecond(ZoneOffset.UTC) < balloonPosition.getDateTime().toEpochSecond(ZoneOffset.UTC)) {
            setLastPredictionPosition(balloonPosition);
        }

        return prediction;
    }

    public static synchronized void setLastPredictionPosition(Position position) {
        lastPredictionPosition = new Position(
                position.getDateTime(),
                position.getLatitude(),
                position.getLongitude(),
                position.getAltitude()
        );
    }

    public static synchronized boolean isAscending() {
        return ascending;
    }

    public static synchronized boolean setAscending(boolean asc) {
        return ascending = asc;
    }

    public static synchronized Position getLastPredictionPositionCopy() {
        return new Position(
                lastPredictionPosition.getDateTime(),
                lastPredictionPosition.getLatitude(),
                lastPredictionPosition.getLongitude(),
                lastPredictionPosition.getAltitude()
        );
    }

    public static synchronized Position getLastPredictionPosition() {
        return lastPredictionPosition;
    }
}
