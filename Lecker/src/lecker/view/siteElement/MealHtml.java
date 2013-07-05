package lecker.view.siteElement;



import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;

import lecker.model.data.Label;
import lecker.model.data.Meal;
import lecker.model.data.comments.Comment;
import lecker.presenter.Handler;
import lecker.presenter.servlet.CommentServlet;
import lecker.view.MainSiteElement;



public class MealHtml implements MainSiteElement {
	private final Meal MEAL;
	private final Comment[] comments;
	
	
	
	public MealHtml(String mealName) {
		this.MEAL = Handler.getInstance().getMealManager().getMeal(mealName);
		this.comments = MEAL.getComments().get();
	}
	
	
	
	@Override
	public String getCode(String remoteAddr, boolean isMobile) {
		//Variables
		StringBuilder builder = new StringBuilder();
		ArrayList<Image> images = new ArrayList<Image>();
		int[] ratings = new int[Comment.MAX_RATING + 1];
		for (Comment com: comments) {
			if (com.getImage() != null) {
				images.add(com.getImage());
			}
			try {
				ratings[com.getRating()]++;
			} catch (Exception exc) {
				ratings[0]++;
			}
		}
		//close middle div
		builder.append("<br/><br/></div>");
		
		builder.append("<div class='meal_left'>" +
			this.showImage(images.toArray(new Image[0])) + "<br/>" +
			this.showDescription() + "<br/>" +
			this.showRating(ratings) + "<br/>" +
			"</div><div class='meal_right'>" +
			"<div class='comment' id='insertAnker'>" +
				"<form id='comment' action='.' type='POST'>" +
					"<input type='hidden' name='Meal' value='" + MEAL.getName() + "'/>" +
					"<textarea rows='4' cols='100'  class='comment_comment' id='" + CommentServlet.PARAM_COMMENT + "'></textarea></br>" +
					"<input type='radio' name='" + CommentServlet.PARAM_RATING + "' value='1'>" +
					"<input type='radio' name='" + CommentServlet.PARAM_RATING + "' value='2'>" +
					"<input type='radio' name='" + CommentServlet.PARAM_RATING + "' value='3'>" +
					"<input type='radio' name='" + CommentServlet.PARAM_RATING + "' value='4'>" +
					"<input type='radio' name='" + CommentServlet.PARAM_RATING + "' value='5'>" +
					"<input type='submit' id='onLogin' value='Senden'/>" +
					"<div id='comment_response'></div>" +
				"</form>" +
			"</div>");
		for (Comment comment: comments) {
			builder.append(showComment(comment));
		}
		builder.append("<div/>");
		
		return builder.toString();
	}

	@Override
	public String getSkript(String remoteAddr, boolean isMobile) {
		return "$(function(){" +
					"$('#comment').submit(function() {" +
						"var formComment = $('#" + CommentServlet.PARAM_COMMENT + "').val();" +
						"var formRating = 0;" +
						"var ratingsCb = document.getElementsByName('" + CommentServlet.PARAM_RATING + "');" +
						"for ( var i = 0; i < ratingsCb.length; i++) {" +
							"if(ratingsCb[i].checked) {" +
								"formRating = i + 1;" +
							"}" +
						"}" +
						"$.ajax({" +
							"type:'POST'," +
						    "cache:false," +
							"url:'Comment'," +
							"data:{" + CommentServlet.PARAM_MEAL + ":'" + MEAL.getName() + "', " + CommentServlet.PARAM_COMMENT + ":formComment, " + CommentServlet.PARAM_RATING + ":formRating}," +
							"success:function(response){" +
								"if (response == '') {" +
									"document.getElementById('comment_response').innerHTML = 'Kommentar gespeichert';" +
								"} else {" +
									"document.getElementById('comment_response').innerHTML = response;" +
								"}" +
							"}" +
						"});" +
						"return false;" +
					"});" +
				"});";
	}



	@Override
	public String getTitle() {
		return MEAL.getName();
	}
	
	
	
	
	
	private String showImage(Image[] images) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<div class='meal_image'><img src='images/meals/template.png'></div>");

		return builder.toString();
	}
	
	private String showDescription() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(
				"<div class='meal_name'>" +
					MEAL.getName() +
				"</div>" +
				"<div class='meal_labels'>");
		for (Label label: MEAL.getLabels()) {
			builder.append("<img title='" + label.getName() + "' src='images/mealinfo/" + label.getName() + "_small.png'>  ");
		}
		builder.append(
				"</div>" +
				"<div class='meal_price'>" + 
					(MEAL.getPrice() / 100) + "." + (MEAL.getPrice() % 100) + " &#8364" +
				"</div>");

		return builder.toString();
	}
	
	private String showRating(int[] ratings) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<div class='meal_rating'>");
		int count = 0, rate = 0;
		for (int i = ratings.length - 1; i >= 0; --i) {
			count += ratings[i];
			rate += ratings[i] * i;
			for (int j = 0; j < ratings.length - 1; ++j) {
				if (j < i) {
					builder.append("<img src='images/star_green_small.png'>");
				} else {
					builder.append("<img src='images/star_gray_small.png'>");
				}
			}
			builder.append(" " + ratings[i] + "<br />");
		}
		if(count > 0) {
			builder.append("<div class='meal_rating_solution'> Ø " + new DecimalFormat("0.00").format(1.0 * rate / count) + "</div>");
		}
		builder.append("</div>");
		
		return builder.toString();
	}
	
	private String showComment(Comment comment) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(
				"<div class='meal_comment'>" +
					"<div class='meal_comment_image'>" +
					"</div>" +
					"<div class='meal_comment_content'>" +
						"<div class='meal_comment_content_header'>" +
							"<div class='meal_comment_content_header_user'>" +
								comment.getUserName() +
							"</div>" +
							"<div class='meal_comment_content_header_rating'>" +
							
							"</div>" +
						"</div>" +
						"<div class='meal_comment_content_body'>" +
							comment.getComment() +
						"</div>" +
					"</div>" +
				"</div>");

		return builder.toString();
	}
}
