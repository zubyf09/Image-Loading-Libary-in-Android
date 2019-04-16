package com.mindvally.mindvellytest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable
{


	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("username")
	@Expose
	private String username;
	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("profile_image")
	@Expose
	private ProfileImage profileImage;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProfileImage getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(ProfileImage profileImage) {
		this.profileImage = profileImage;
	}


}