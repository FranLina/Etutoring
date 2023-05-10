package com.flb.etutoring.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Controller
public class MapController {
    String url = "AIzaSyCO6Ef_22ttWTuk0Q6ThjiLnSMkUURlDPk";

    @GetMapping("/map")
    public String showMap(Model model, HttpServletRequest request) {
        String apiKey = "AIzaSyCO6Ef_22ttWTuk0Q6ThjiLnSMkUURlDPk";
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, "1600 Amphitheatre Parkway, Mountain View, CA")
                    .await();
            model.addAttribute("latitude", results[0].geometry.location.lat);
            model.addAttribute("longitude", results[0].geometry.location.lng);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "map";
    }
}
