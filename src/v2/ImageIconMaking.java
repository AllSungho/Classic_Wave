// ImageIconMaking Class
package v2;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageIconMaking {
	private String imageIconName;
	private String imageIconPath;
	private ImageIcon imageIcon;
	
	public ImageIconMaking(String imageName) {
		PathCheck imagePathCheck = new PathCheck(imageName);
		this.imageIconName = imagePathCheck.getFileName();
		this.imageIconPath = imagePathCheck.getFilePath();
		this.imageIcon = new ImageIcon(imagePathCheck.getFilePath());
		System.out.println(imagePathCheck.getFileName() + "의 이름을 가진 이미지 아이콘을 로드시켰습니다.");
	}
	
	public void exchange(int width, int height) {
		Image tempImage = this.imageIcon.getImage();
		Image changeTempImage = tempImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		this.imageIcon = new ImageIcon(changeTempImage);
	}
	
	public String getImageIconNamen() {
		return this.imageIconName;
	}
	
	public String getImageIconPath() {
		return this.imageIconPath;
	}
	
	public ImageIcon getImageIcon() {
		return this.imageIcon;
	}
}
