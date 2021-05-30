package com.javaproject.nfe114v17.movie;

import com.javaproject.nfe114v17.tmdbApi.NotFoundException;
import com.javaproject.nfe114v17.tmdbApi.TmdbApiClient;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class MovieController2 {

    private final MovieService movieService;
    private final TmdbApiClient tmdbApiClient;

    public MovieController2(MovieService movieService, TmdbApiClient tmdbApiClient) {
        this.movieService = movieService;
        this.tmdbApiClient = tmdbApiClient;
    }

    @RequestMapping(value = "/movie2", method = RequestMethod.GET)
    public String getMovie(Model model) {
        List<Movie> movies = movieService.getMovie();
        model.addAttribute("movies", movies);
        return "movie";
    }

    @RequestMapping(value = "/search2", method = RequestMethod.GET)
    public String searchMovie() {
        return "searchform";
    }

    @RequestMapping(value = "/search2", method = RequestMethod.POST)
    public String searchMovie(HttpServletRequest request, Model model) throws IOException, InterruptedException, NotFoundException, JSONException {
        String query = request.getParameter("query");
        model.addAttribute("research", tmdbApiClient.searchMovie2(query));
        return "searchform";
    }

    @RequestMapping(value = "movie/{movieId}", method = RequestMethod.GET)
    public String getMovieById(@PathVariable String movieId, Model model) throws IOException, InterruptedException, NotFoundException {
        try {
            int parsedMovieId = Integer.parseInt(movieId);
            Movie movieById = tmdbApiClient.getMovieById(parsedMovieId);
            model.addAttribute("movie", movieById);
        } catch (NotFoundException e) {
            throw e;
        }
        return "movie";
    }
}
