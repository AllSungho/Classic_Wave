// ImageMaking Class
package v2;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

// 객체를 만든 후 프레임 위에 add(객체명)으로 이미지를 프레임 위에 올릴 수 있음
// 예) ImageMaking tempImage = new ImageMaking("tempImage.png", 0, 0, 100, 100);
//     add(tempImage);

public class ImageMaking extends JPanel {
	static final long serialVersionUID = 1L; // 직렬화와 관련... 따로 건드릴 필요 없음
	private Image thisImage; // 이미지 변수
	private int locX, locY, width, height; // 이미지 위치 및 크기 변수
	private String fileName;

	// 이름에 맞는 이미지를 검색 후 이미지를 그릴 준비를 하는 생성자
	public ImageMaking(String imageName, int locX, int locY, int width, int height) {
		this.locX = locX;
		this.locY = locY;
		this.width = width;
		this.height = height;
		PathCheck imagePathCheck = new PathCheck(imageName);
		this.thisImage = new ImageIcon(imagePathCheck.getFilePath()).getImage();
		this.fileName = imagePathCheck.getFileName();
		System.out.println(this.fileName + "의 이름을 가진 이미지를 로드시켰습니다.");
	}

	// 그릴 이미지 크기 및 위치 변경
	public void exchange(int locX, int locY, int width, int height) {
		this.locX = locX;
		this.locY = locY;
		this.width = width;
		this.height = height;
		System.out.println(this.fileName + "의 이름을 가진 그릴 이미지의 크기를 " + width + "*" + height + "로 변경시켰습니다.");
	}
	
	// 이미지 반환
	public Image getImage() {
		return this.thisImage;
	}

	// 밑 세개 메소드는 따로 숙지할 필요 없음, 사용안해도 알아서 동작함
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(this.thisImage, locX, locY, width, height, null);
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}
}