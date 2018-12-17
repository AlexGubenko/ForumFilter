package telran.ashkelon2018fl.forum.dto;

import java.util.Set;

import lombok.Getter;

@Getter
public class NewPostDto {
	String title;
	String content;
	String author;
	Set<String> tags;

}
