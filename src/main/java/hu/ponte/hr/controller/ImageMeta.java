package hu.ponte.hr.controller;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author zoltan
 */
@Getter
@Builder
@Entity
public class ImageMeta
{
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String mimeType;
	@Column(nullable = false)
	private long size;
	@Column(nullable = false, unique = true)
	private String digitalSign;
}
