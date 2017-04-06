package in.ac.kuvempu.dailynews.network;

import java.util.List;

import in.ac.kuvempu.dailynews.model.Article;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by raghav on 4/5/2017.
 */

public interface NewsApiEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

//    @GET("movie/top_rated")
//    Call<Article> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("articles")
    Call<Article> getNews(@Query("source") String source, @Query("sort") String sort, @Query("apiKey") String apiKey);

//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
