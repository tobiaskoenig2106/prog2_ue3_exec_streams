package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "ce0273e8aa2c45a4be9fb7de08df9da2";  //TODO add your api key

	public void process(String query, Country country, Category category) {
		System.out.println("Start process");

		//TODO implement Error handling

		//TODO load the news based on the parameters
		NewsApi newsApi = null;
		try {
			newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ(query)
					.setEndPoint(Endpoint.TOP_HEADLINES)
					.setSourceCountry(country)
					.setSourceCategory(category)
					.createNewsApi();
		}catch (Exception e){
			System.err.println(e.getMessage());
		}



		NewsResponse newsResponse = null;
		try {
			newsResponse = newsApi.getNews();
		}catch (Exception e){
			System.err.println(e.getMessage());
		}



		List<Article> articles = newsResponse.getArticles();

		System.out.println("Articles:");
		for (Article article:articles){
			System.out.println(article.toString());
		}
		System.out.println();

		//TODO implement methods for analysis
		if(!articles.isEmpty()){
			//a
			long count = articles.stream()
					.count();
			System.out.println("There are " + count + " articles.");

			//b
			String prov = articles.stream()
					.collect(Collectors.groupingBy(article -> article.getSource()
							.getName(),Collectors.counting()))
					.entrySet().stream()
					.max(Comparator.comparingInt(a -> Math.toIntExact(a.getValue()))).get().getKey();
			if(prov != null){
				System.out.println("The provider with the most puplished articles is: " +
						prov);
			}



			//c
			System.out.println("The shortest author by name is: " +
					articles.stream()
					.filter(article -> article.getAuthor() != null)
					.min(Comparator.comparingInt(article -> article.getAuthor().length()))
					.filter(article -> article.getAuthor() != null)
					.get().getAuthor());


			//d
			System.out.println("First article by length and alphabetic order is:" +
					articles.stream()
					.sorted(Comparator.comparingInt(a -> a.getTitle().length()))
					.sorted(Comparator.comparing(Article::getTitle))
					.collect(Collectors.toList()).get(0).getTitle());

		}

		//for (Article article : articles) {



			try {
				Article article = articles.get(0);
				URL url = new URL(article.getUrl());
				InputStream is = url.openStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				File file = new File(article.getTitle().substring(0, 10) + ".html");
				file.createNewFile();
				BufferedWriter wr =
						new BufferedWriter(new FileWriter(file));
				String line;
				while ((line = br.readLine()) != null) {
					wr.write(line);
				}
				br.close();
				wr.close();

			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		//}


		System.out.println("End process");
	}
	

	public Object getData() {
		
		return null;
	}
}
