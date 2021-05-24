package com.javaproject.nfe114v17.tmdbApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.javaproject.nfe114v17.movie.Movie;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TmdbApiClient {
    private final String apiKey;
    private final String baseUrl = "https://api.themoviedb.org/3";

    public TmdbApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public Movie getMovieById(int movieId) throws IOException, InterruptedException, NotFoundException{
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key="+apiKey;

        HttpClient client = buildClient();

        HttpRequest request = buildGetRequest(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new NotFoundException();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        Movie movie = mapper.readValue(response.body(), Movie.class);
        return movie;
    }

    private HttpRequest buildGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
    }

    public String searchMovie(String query) throws IOException, InterruptedException, NotFoundException{

        String url = new UrlBuilder(apiKey, baseUrl).route("/search/movie/").addParam("query",query).build();

        HttpClient client = buildClient();

        HttpRequest request = buildGetRequest(url);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() != 200) {
            throw new NotFoundException();
        }

        ObjectMapper mapper = new ObjectMapper();
        return response.body();

    }

    private HttpClient buildClient(){
        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

    }



}