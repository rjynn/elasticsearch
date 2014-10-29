package ca.ualberta.ssrg.movies;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.ssrg.androidelasticsearch.R;
import ca.ualberta.ssrg.movies.es.ESMovieManager;
import ca.ualberta.ssrg.movies.es.Movie;
import ca.ualberta.ssrg.movies.es.IMovieManager;

public class AddActivity extends Activity {

	private IMovieManager movieManager;

	// Thread that close the activity after finishing add
	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		movieManager = new ESMovieManager();
	}

	public void save(View view) {
		TextView id = (TextView) findViewById(R.id.detailsIdText);
		TextView title = (TextView) findViewById(R.id.detailsTitleText);
		TextView director = (TextView) findViewById(R.id.detailsDirectorText);
		TextView year = (TextView) findViewById(R.id.detailsYearText);
		TextView genre = (TextView) findViewById(R.id.detailsGenreText);
		
		// Create movie object
		Movie newMovie = new Movie();
		newMovie.setId(Integer.parseInt(id.getText().toString()));
		newMovie.setTitle(title.getText().toString());
		newMovie.setDirector(director.getText().toString());
		newMovie.setYear(Integer.parseInt(year.getText().toString()));
		
		String genresString = genre.getText().toString();
		String[] genresArray = genresString.split(",");
		List<String> genres = Arrays.asList(genresArray);
		newMovie.setGenres(genres);
		
		// Execute the thread
		Thread thread = new AddThread(newMovie);
		thread.start();
	}
	
	class AddThread extends Thread {
		private Movie movie;

		public AddThread(Movie movie) {
			this.movie = movie;
		}

		@Override
		public void run() {
			movieManager.addMovie(movie);
			
			// Give some time to get updated info
			try {
				Thread.sleep(500); //this thread sleep is to make sure that can wait for a bit. should probably have a ui that says loading
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			runOnUiThread(doFinishAdd);
		}
	}
}


