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
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

@Service
public class PathPredictionService {

    private final static String CUSF_API_URL = "https://predict.cusf.co.uk/api/v1/";

    public LinkedHashMap<?, ?> predictPath(Position balloonPosition) {

        String getRequest = CUSF_API_URL + "?";

        getRequest += ("launch_latitude=" + balloonPosition.getLatitude());
        getRequest += ("&launch_longitude=" + balloonPosition.getLongitude());
        getRequest += ("&launch_datetime=" + DateTimeUtils.convertUTC3toUTC0(balloonPosition.getDateTime()) + "Z");
        getRequest += ("&ascent_rate=" + 5); //calculate
        getRequest += ("&burst_altitude=" + 30000); //calculate
        getRequest += ("&descent_rate=" + 5); //calculate


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
            }
            System.out.println(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return prediction;
    }

}
