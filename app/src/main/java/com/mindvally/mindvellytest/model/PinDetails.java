package com.mindvally.mindvellytest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class PinDetails implements Serializable
{
	@SerializedName("id")
	@Expose
	private String  id;
	@SerializedName("created_at")
	@Expose
	private String  createdAt;
	@SerializedName("width")
	@Expose
	private int width;
	@SerializedName("height")
	@Expose
	private int height;
	@SerializedName("color")
	@Expose
	private String  color;
	@SerializedName("likes")
	@Expose
	private int likes;

	@SerializedName("liked_by_user")
	@Expose
	private boolean likedByUser;

	@SerializedName("user")
	@Expose
	private User user;

	@SerializedName("current_user_collections")
	@Expose
	private List<Object> currentUserCollections = null;

	@SerializedName("categories")
	@Expose
	private List<Category> categories = null;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public boolean isLikedByUser() {
		return likedByUser;
	}

	public void setLikedByUser(boolean likedByUser) {
		this.likedByUser = likedByUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Object> getCurrentUserCollections() {
		return currentUserCollections;
	}

	public void setCurrentUserCollections(List<Object> currentUserCollections) {
		this.currentUserCollections = currentUserCollections;
	}
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

}