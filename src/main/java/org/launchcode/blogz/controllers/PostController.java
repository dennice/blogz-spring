package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		User author = getUserFromSession(request.getSession());
		
		// if title and body are not null create new instance of post //if they are null redirect to newpost with error
		if (title.equals("") || title.equals(null) || body.equals("") || body.equals(null)){
			String error_message = "fill all fields!";
			model.addAttribute("error", error_message);
			model.addAttribute("title", title);
			model.addAttribute("body", body);

			return "newpost";
		}
		//create new instance of post and save it to database.
		Post post = new Post(title, body, author);
		postDao.save(post);
		
		//get the id of post to render posts page via permalink
		int uid = post.getUid();
		String username = author.getUsername();
		return singlePost(username, uid, model);
		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		
		// get single post
		Post singlePost = postDao.findByUid(uid);
		// post in template
		model.addAttribute("post", singlePost);
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		User author = userDao.findByUsername(username);
		
		List<Post> posts = author.getPosts();
		model.addAttribute("posts", posts);
		return "blog";
	}
	
}
