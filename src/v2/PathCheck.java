// PathCheck Class
package v2;

import java.io.File;

// 사용법 (3 ~ 5번은 선택 옵션이다)
// 1. 파일을 v2.resource 폴더에 넣는다.
// 2. PathCheck 객체이름 = new PathCheck(확장자를 포함한 파일 이름); -> 우선 이렇게 이 클래스의 객체를 생성한다.
// 3. 이름을 담을 변수 = 객체이름.getFileName(); -> 이렇게 변수에 파일 이름을 담을 수 있다.
// 4. 경로를 담을 변수 = 객체이름.getFilePath(); -> 이렇게 변수에 파일 경로를 담을 수 있다.
// 5. 파일을 담을 변수 = 객체이름.getFile(); -> 이렇게 변수에 파일 그 자체를 담을 수 있다.

// 특이사항
// 1. 파일 이름은 파일이 존재하지 않으면 파일 이름이 파일이름(존재하지 않는 파일)이 됨
// 2, 파일 경로, 파일이 존재하지 않으면 null로 표기됨
// 3. 파일 그 자체, 파일이 존재하지 않으면 null로 표기됨

public class PathCheck {
	private String fileName; // 파일 이름, 파일이 존재하지 않으면 파일 이름이 파일이름(존재하지 않는 파일)이 됨
	private String filePath; // 파일 경로, 파일이 존재하지 않으면 null로 표기됨
	private File file; // 파일 그 자체, 파일이 존재하지 않으면 null로 표기됨
	
	// 이름에 맞는 파일이 존재하는지 체크 후 존재한다면 멤버 변수를 최신화
	public PathCheck(String fileName) {
		String path = System.getProperty("user.dir");
		
		String fullPathMac = path + "/src/v2/resource/" + fileName;
		String fullPathWindows = path + "\\src\\v2\\resource\\" + fileName;
		
		File fileMac = new File(fullPathMac);
		File fileWindows = new File(fullPathWindows);

		if (fileMac.exists()) {
			System.out.println(fileName + "의 이름을 가진 파일을 찾았습니다.(Mac 환경)");
			this.fileName = fileName;
			this.filePath = fullPathMac;
			this.file = fileMac;
        }
		else if (fileWindows.exists()) {
			System.out.println(fileName + "의 이름을 가진 파일을 찾았습니다.(Windows 환경)");
			this.fileName = fileName;
			this.filePath = fullPathWindows;
			this.file = fileWindows;
		}
		else {
			this.fileName = fileName + "(존재하지 않는 파일)";
        	System.out.println("사용자의 컴퓨터에 " + fileName + "의 이름을 가진 파일이 존재하지 않습니다.");
        }
	}
	
	// 파일 이름을 얻고자 할 때 사용, 파일이 존재하지 않으면 null로 표기됨
	public String getFileName() {
		return this.fileName;
	}
	
	// 파일 경로를 얻고자 할 때 사용, 파일이 존재하지 않으면 null로 표기됨
	public String getFilePath() {
		return this.filePath;
	}
	
	// 파일 그 자체를 얻고자 할 때 사용, 파일이 존재하지 않으면 null로 표기됨
	public File getFile() {
		return this.file;
	}
	
}
