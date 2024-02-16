package hu.ponte.hr.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.InputStream;

/**
 * @author zoltan
 */
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
	@Column(nullable = false)
	private String digitalSign;
	@Column(nullable = false, length = 3000000)
	private byte[] image;
}
