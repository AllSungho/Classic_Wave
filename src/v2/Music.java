// Music Class
package v2;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// 사용법 (3번은 선택 옵션이다)
// 1. wav 확장자의 음악 파일을 v2.resource 폴더에 넣는다
// 2. Music 객체이름 = new Music("확장자를 포함한 파일 이름"); -> 우선 이렇게 이 클래스의 객체를 생성한다.
// 3. 객체이름.musicSetCount(반복 횟수) -> 이렇게 해당을 음악을 몇 번 반복할건지 설정할 수 있다. (무한 반복은 0을 넣으면 된다)
// 4. 객체이름.musicStart() -> 이렇게 음악을 시작할 수 있다.
// 5. 객체이름.musicStop() -> 이렇게 음악을 종료시킬 수 있다.

public class Music {
	private String audioName;
	private File audioFile;
	private AudioInputStream ais;
	private Clip clip;

	// 음악을 로드시키는 생성자
	public Music(String audioName) {
		PathCheck musicPathCheck = new PathCheck(audioName);
		this.audioName = musicPathCheck.getFileName();
		this.audioFile = musicPathCheck.getFile();

		try {
			this.ais = AudioSystem.getAudioInputStream(this.audioFile);
			this.clip = AudioSystem.getClip();
			this.clip.open(this.ais);
			System.out.println(this.audioName + "의 이름을 가진 음악을 로드시켰습니다.");
		} catch (Exception ex) {
			System.out.println(this.audioName + "의 이름을 가진 음악을 로드시킬 수 없습니다.");
		}
	}

	// 음악을 몇 번 반복할건지 설정할 수 있는 메소드
	public void musicSetCount(int count) {
		try {
			this.clip.loop(count - 1);
			if (count == 0)
				System.out.println(this.audioName + "의 이름을 가진 음악의 재생횟수를 무한으로 변경하였습니다.");
			else if (count < 0)
				System.out.println(this.audioName + "의 이름을 가진 음악의 재생횟수를 0회 미만으로 변경할 수 없습니다.");
			else
				System.out.println(this.audioName + "의 이름을 가진 음악의 재생횟수를 " + count + "회로 변경하였습니다.");
		} catch (Exception ex) {
			System.out.println(this.audioName + "의 이름을 가진 음악의 재생횟수를 변경할 수 없습니다.");
		}
	}

	// 음악을 시작할 수 있는 메소드
	public void musicStart() {
		try {
			this.clip.start();
			System.out.println(this.audioName + "의 이름을 가진 음악을 재생합니다.");
		} catch (Exception ex) {
			System.out.println(this.audioName + "의 이름을 가진 음악을 재생시킬 수 없습니다.");
		}
	}

	// 음악을 멈출 수 있는 메소드
	public void musicStop() {
		try {
			this.clip.stop();
			System.out.println(this.audioName + "의 이름을 가진 음악을 정지시킵니다.");
		} catch (Exception ex) {
			System.out.println(this.audioName + "의 이름을 가진 음악을 정지시킬 수 없습니다.");
		}
	}
}
