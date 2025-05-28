
// Main Class
package v2;

import java.awt.Color;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// 현재 요구 스펙
// 1. 144fps가 유지되는 컴퓨터 사양 (144fps 유지 실패 시 게임 속도 자체가 느려짐)
//    -> 게임 시작 후 Frame Rate 출력문으로 알 수 있음
//    -> 게임 완성 후 프레임에 따라서 게임 속도 조절하도록 수정 예정

public class Main extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L; // 직렬화와 관련... 따로 건드릴 필요 없음

	// 사용자 컴퓨터 화면 해상도
	private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width; // 가로
	private static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height; // 세로

	// 프로그램 화면 크기
	private static final double DISPLAY_MULTIPLE = 0.6; // 프로그램 크기 배수 값 조절
	private static final int PROGRAM_SCREEN_WIDTH = (int) (SCREEN_WIDTH * DISPLAY_MULTIPLE); // 가로
	private static final int PROGRAM_SCREEN_HEIGHT = (int) (((double) PROGRAM_SCREEN_WIDTH / 1512.0) * 982.0);

	// 음악
	private static Music mainMusic;
	private static Music gameMusic;

	// 플레이어 컨트롤 관련
	private static boolean moveLeft;
	private static boolean moveRight;
	private static boolean coolDown;
	private static boolean dashCheck;
	private static boolean rightDash;
	private static boolean leftDash;
	private static boolean rightDashing;
	private static boolean leftDashing;
	private static boolean jumpInput = false;
	private static boolean jumpCheck = false;
	private static boolean jumping = false;

	// 플레이어 체력
	private static int health = 100;
	private static double healthUpdate = 100.0;
	private static boolean healthGuard;

	// 플레이어 위치
	private static double locX = PROGRAM_SCREEN_WIDTH / 2;
	private static double locY = PROGRAM_SCREEN_HEIGHT / 3 * 2;

	// 플레이어 x축 관련
	private static double speedX = Main.PROGRAM_SCREEN_WIDTH * 0.004;
	private static double dashSpeed = Main.PROGRAM_SCREEN_WIDTH * 0.008;

	// 플레이어 y축 이동 관련
	private static double speedY = 0; // 플레이어 Y축 속도
	private static double accelerationY = Main.PROGRAM_SCREEN_HEIGHT * 0.018; // 플레이어 점프 가속도
	private static double gravity = Main.PROGRAM_SCREEN_HEIGHT * 0.00075; // 맵 안 중력

	// 공격 패턴 선정
	private static int attackNumber = 100;

	// 맵 설정
	private static double mapStartX = (PROGRAM_SCREEN_WIDTH * 0.2);
	private static double mapEndX = PROGRAM_SCREEN_WIDTH - (PROGRAM_SCREEN_WIDTH * 0.2);
	private static double mapStartY = (PROGRAM_SCREEN_WIDTH * 0.3);
	private static double mapEndY = ((PROGRAM_SCREEN_HEIGHT / 10) * 9); // 처음 맵 바닥 높이

	// 게임 상태 관련
	private int currentFrameRate; // 현재 프로그램 실행 중 프레임 속도
	private static boolean gameStatus; // 게임 진행 상태 (false : 게임 시작 안함 or 게임 끝남 / true : 게임 진행 중)
	private static boolean gamePause; // 게임 잠시 멈춤 상태

	// 게임 패턴1 전용 변수들
	private static int choiceNumber;
	private static int randomChoiceNumber;
	private static int countLeft = 0;
	private static int countRight = 0;
	Component arrowLeft;
	Component arrowRight;

	// 게임 패턴2 전용 변수들
	Component[] seong_attack;
	Component[] seong_attack2;

	// 게임 패턴3 전용 변수들
	Component stairsLeft1;
	Component stairsLeft2;
	Component stairsLeft3;
	Component stairsRight1;
	Component stairsRight2;
	Component stairsRight3;
	Component attackBeforeWidth;
	Component attackBeforeHeight;

	Component attackDamage1;
	int attackDamage1StartY;
	int attackDamage1EndY;

	Component attackDamage2;
	int attackDamage2StartX;
	int attackDamage2EndX;

	Component attackDamage3;
	int attackDamage3StartY;
	int attackDamage3EndY;

	Component attackDamage4;
	int attackDamage4StartX;
	int attackDamage4EndX;

	Component attackDamage5;
	int attackDamage5StartY;
	int attackDamage5EndY;

	Component attackDamage6;
	int attackDamage6StartX;
	int attackDamage6EndX;

	Component attackDamage7;
	int attackDamage7StartY;
	int attackDamage7EndY;

	Component monster1;
	double monster1X;

	Component monster2;
	double monster2X;
	double monster2Y;

	double bossX;
	double bossY;
	boolean bossCheck = true;

	// 게임 패턴4 전용 변수들
	private static int fireballSpeed = 2;
	private static int fireballSpeedX1;
	private static int fireballSpeedY1;
	private static int fireballSpeedX2;
	private static int fireballSpeedY2;
	private static int fireballSpeedX3;
	private static int fireballSpeedY3;
	private static int fireballSpeedX4;
	private static int fireballSpeedY4;
	Component Fireball1;
	Component Fireball2;
	Component Fireball3;
	Component Fireball4;

	// 게임 패턴6 전용 변수들
	Component invincible;
	Component Ghost;
	Component Leftstair1;
	Component Leftstair2;
	Component Rightstair1;
	Component Rightstair2;
	Component Centerstair1;
	Component SpeedUpPlayer;
	Component invinciblePlayer;
	Component greenmonster;
	Component[] monsterArray;
	int[] monsterArrayX;
	int[] monsterArrayY;
	static boolean isPlayerInvincible = false;
	static int invincibilityTimer = 0;
	static boolean isPlayerSpeedup = false;
	static int SpeedupTimer = 0;
	boolean monstermove = true;
	static int monsterspeed = 1;
	boolean checkInvincibility;
	boolean checkSpeedUp;

	// 보스 관련 변수
	private static int tempBoss = 0;
	private static boolean BossUpDownCheck;

	// Swing 컴포넌트 변수들
	JButton gameStartButton;
	JButton helpButton;
	JButton GoBackButton;
	JButton pauseButton;
	JButton restartButton;
	JButton goHomeButton;
	ImageMaking homeImage;
	ImageMaking HelpImage;
	JPanel Startpanel;
	JLabel Timer;
	Component playerDamageImage;
	Component playerImage;
	Component map1;
	Component map2;
	Component map3;
	Component map4;
	Component backHealthBar;
	Component healthBar;
	Component boss;
	Component damageField1;
	Component damageField2;
	Component Layer1;
	Component Layer2;
	JLabel resultScore;
	JLabel scoreList;

	// 점수판 변수들
	JLabel FirstScore;
	JLabel SecondScore;
	JLabel ThirdScore;
	JLabel FourthScore;
	JLabel FifthScore;
	int[] Score = new int[5];

	static Component playerDashImage;
	static Component playerDashImage2;

	public Main() {
		// 초기 설정
		JFrame tempFrame = new JFrame("타이틀 바 크기 구하기 예제");
		tempFrame.setVisible(true);
		setTitle("고전파(Classic Wave)");
		System.out.println();
		setSize(Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT + tempFrame.getInsets().top);
		tempFrame.dispose();
		// setUndecorated(true); // 타이틀 바 없애기
		setResizable(true);

		// 창을 화면 가운데에 배치
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 게임 시작 아이콘 생성
		ImageIconMaking gameStartButtonImageIcon1 = new ImageIconMaking("GameStartButton1.png");
		gameStartButtonImageIcon1.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.15));
		// 버튼 마우스 감지 시 이미지 변환을 위한 아이콘
		ImageIconMaking gameStartButtonImageIcon2 = new ImageIconMaking("GameStartButton2.png");
		gameStartButtonImageIcon2.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.15));
		// 게임 시작 버튼 생성
		this.gameStartButton = new JButton(gameStartButtonImageIcon1.getImageIcon());
		// 버튼 테두리 없음, 채우기 없음, 선택됐을때 테두리 없음
		gameStartButton.setBorderPainted(false);
		gameStartButton.setContentAreaFilled(false);
		gameStartButton.setFocusPainted(false);
		gameStartButton.setBounds((int) (Main.PROGRAM_SCREEN_HEIGHT * 0.01),
				Main.PROGRAM_SCREEN_HEIGHT - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05)
						- (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.15),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.2), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.15));
		// 마우스 감지시 이미지 변환 아이콘
		gameStartButton.setRolloverIcon(gameStartButtonImageIcon2.getImageIcon());
		// 버튼 추가
		add(gameStartButton);

		// 도움말 아이콘 생성
		ImageIconMaking helpButtonImageIcon1 = new ImageIconMaking("Helpbutton1.png");
		helpButtonImageIcon1.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.15));
		// 버튼 마우스 감지 시 이미지 변환을 위한 아이콘
		ImageIconMaking helpButtonImageIcon2 = new ImageIconMaking("Helpbutton2.png");
		helpButtonImageIcon2.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.15));
		// 도움말 버튼 생성
		this.helpButton = new JButton(helpButtonImageIcon1.getImageIcon());
		// 버튼 테두리 없음, 채우기 없음, 선택됐을때 테두리 없음
		helpButton.setBorderPainted(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setFocusPainted(false);
		helpButton.setBounds(Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.21),
				Main.PROGRAM_SCREEN_HEIGHT - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.2), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.15));
		// 마우스 감지시 이미지 변환 아이콘
		helpButton.setRolloverIcon(helpButtonImageIcon2.getImageIcon());
		// 버튼 추가
		add(helpButton);

		// 뒤로가기 아이콘 생성
		ImageIconMaking GoBackButtonImageIcon1 = new ImageIconMaking("GoBackButton1.png");
		GoBackButtonImageIcon1.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.1),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.08));
		// 버튼 마우스 감지 시 이미지 변환을 위한 아이콘
		ImageIconMaking GoBackButtonImageIcon2 = new ImageIconMaking("GoBackButton2.png");
		GoBackButtonImageIcon2.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.1),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.08));
		// 뒤로가기 버튼 생성
		this.GoBackButton = new JButton(GoBackButtonImageIcon1.getImageIcon());
		// 버튼 테두리 없음, 채우기 없음, 선택됐을때 테두리 없음
		GoBackButton.setBorderPainted(false);
		GoBackButton.setContentAreaFilled(false);
		GoBackButton.setFocusPainted(false);
		GoBackButton.setBounds((int) (Main.PROGRAM_SCREEN_HEIGHT * 0.01), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.01),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.08));
		// 마우스 감지시 이미지 변환 아이콘
		GoBackButton.setRolloverIcon(GoBackButtonImageIcon2.getImageIcon());
		// 버튼 추가
		add(GoBackButton);

		// 게임시작 블랙 패널
		this.Startpanel = new JPanel();
		Startpanel.setLayout(null);
		Startpanel.setBackground(Color.black);

		// 홈으로 돌아가기 아이콘 생성
		ImageIconMaking goHomeButtonImageIcon1 = new ImageIconMaking("goHomeImage.png");
		goHomeButtonImageIcon1.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 버튼 마우스 감지 시 이미지 변환을 위한 아이콘
		ImageIconMaking goHomeButtonImageIcon2 = new ImageIconMaking("goHomeImage2.png");
		goHomeButtonImageIcon2.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 홈으로 돌아가기 버튼 생성
		this.goHomeButton = new JButton(goHomeButtonImageIcon1.getImageIcon());
		// 버튼 테두리 없음, 채우기 없음, 선택됐을때 테두리 없음
		goHomeButton.setBorderPainted(false);
		goHomeButton.setContentAreaFilled(false);
		goHomeButton.setFocusPainted(false);
		goHomeButton.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.04), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.02),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 마우스 감지시 이미지 변환 아이콘
		goHomeButton.setRolloverIcon(goHomeButtonImageIcon2.getImageIcon());
		// 버튼 추가
		Startpanel.add(goHomeButton);
		goHomeButton.setVisible(false);

		// 일시정지 아이콘 생성
		ImageIconMaking pauseButtonImageIcon1 = new ImageIconMaking("pauseImage.png");
		pauseButtonImageIcon1.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 버튼 마우스 감지 시 이미지 변환을 위한 아이콘
		ImageIconMaking pauseButtonImageIcon2 = new ImageIconMaking("pauseImage2.png");
		pauseButtonImageIcon2.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 일시정지 버튼 생성
		this.pauseButton = new JButton(pauseButtonImageIcon1.getImageIcon());
		// 버튼 테두리 없음, 채우기 없음, 선택됐을때 테두리 없음
		pauseButton.setBorderPainted(false);
		pauseButton.setContentAreaFilled(false);
		pauseButton.setFocusPainted(false);
		pauseButton.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.12), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.02),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 마우스 감지시 이미지 변환 아이콘
		pauseButton.setRolloverIcon(pauseButtonImageIcon2.getImageIcon());
		// 버튼 추가
		Startpanel.add(pauseButton);
		pauseButton.setVisible(false);

		// 재시작 아이콘 생성
		ImageIconMaking restartButtonImageIcon1 = new ImageIconMaking("restartImage.png");
		restartButtonImageIcon1.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 버튼 마우스 감지 시 이미지 변환을 위한 아이콘
		ImageIconMaking restartButtonImageIcon2 = new ImageIconMaking("restartImage2.png");
		restartButtonImageIcon2.exchange((int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 재시작 버튼 생성
		this.restartButton = new JButton(restartButtonImageIcon1.getImageIcon());
		// 버튼 테두리 없음, 채우기 없음, 선택됐을때 테두리 없음
		restartButton.setBorderPainted(false);
		restartButton.setContentAreaFilled(false);
		restartButton.setFocusPainted(false);
		restartButton.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.12), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.02),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		// 마우스 감지시 이미지 변환 아이콘
		restartButton.setRolloverIcon(restartButtonImageIcon2.getImageIcon());
		// 버튼 추가
		Startpanel.add(restartButton);
		restartButton.setVisible(false);

		// 버튼이 리스너 작동하도록
		gameStartButton.addActionListener(this);
		helpButton.addActionListener(this);
		GoBackButton.addActionListener(this);
		goHomeButton.addActionListener(this);
		pauseButton.addActionListener(this);
		restartButton.addActionListener(this);

		// 뒤로가기 버튼 안보이게 하기
		GoBackButton.setVisible(false);

		// 게임시작 이후 시간 가는 타이머
		Timer = new JLabel("이성호", SwingConstants.CENTER);
		Font font = new Font("Aharoni 굵게", Font.BOLD, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		Timer.setFont(font);
		Timer.setForeground(Color.WHITE);
		Startpanel.add(Timer);
		Timer.setVisible(true);

		resultScore = new JLabel("안명근", SwingConstants.CENTER);
		resultScore.setOpaque(true);
		resultScore.setFont(font);
		resultScore.setBackground(Color.BLUE);
		resultScore.setForeground(Color.WHITE);
		Startpanel.add(resultScore);
		resultScore.setVisible(false);

		this.playerDamageImage = new ImageMaking("playerDamage.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
		playerDamageImage.setBackground(new Color(255, 0, 0, 0)); // 배경 투명색 적용
		playerDamageImage.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
		Startpanel.add(playerDamageImage);
		playerDamageImage.setVisible(false);

		// 무적상태 플레이어 아이콘
		this.invinciblePlayer = new ImageMaking("InvinciblePlayer.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
		invinciblePlayer.setBackground(new Color(255, 0, 0, 0)); // 배경 투명색 적용
		invinciblePlayer.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
		Startpanel.add(invinciblePlayer);
		invinciblePlayer.setVisible(false);

		// 이속 증가 상태 플레이어 아이콘
		this.SpeedUpPlayer = new ImageMaking("SpeedUpPlayer.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
		SpeedUpPlayer.setBackground(new Color(255, 0, 0, 0)); // 배경 투명색 적용
		SpeedUpPlayer.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
		Startpanel.add(SpeedUpPlayer);
		SpeedUpPlayer.setVisible(false);

		// 게임시작 패널에 추가되는 플레이어 아이콘
		this.playerImage = new ImageMaking("player.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
		playerImage.setBackground(new Color(255, 0, 0, 0)); // 배경 투명색 적용
		playerImage.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
		Startpanel.add(playerImage);

		// 게임시작 패널에 추가되는 플레이어 좌로 대쉬 아이콘
		playerDashImage = new ImageMaking("playerDash1.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.075),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.045));
		playerDashImage.setBackground(new Color(255, 0, 0, 0)); // 배경 투명색 적용
		playerDashImage.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.050 / 2),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.045 / 2), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.075),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.045));
		Startpanel.add(playerDashImage);
		playerDashImage.setVisible(false);

		// 게임시작 패널에 추가되는 플레이어 우로 대쉬 아이콘
		playerDashImage2 = new ImageMaking("playerDash2.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.075),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.045));
		playerDashImage2.setBackground(new Color(255, 0, 0, 0)); // 배경 투명색 적용
		playerDashImage2.setBounds(
				(int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.050 / 2 - Main.PROGRAM_SCREEN_WIDTH * 0.075),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.045 / 2), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.075),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.045));
		Startpanel.add(playerDashImage2);
		playerDashImage2.setVisible(false);

		// 레드바 구현
		damageField1 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
				(int) (Main.PROGRAM_SCREEN_HEIGHT));
		damageField1.setBackground(new Color(255, 0, 0, 0));
		damageField1.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT));
		Startpanel.add(damageField1);
		damageField1.setVisible(false);

		damageField2 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
				(int) (Main.PROGRAM_SCREEN_HEIGHT));
		damageField2.setBackground(new Color(255, 0, 0, 0));
		damageField2.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT));
		Startpanel.add(damageField2);
		damageField2.setVisible(false);

		// 체력바 구현
		healthBar = new ImageMaking("healthBar.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.065));
		healthBar.setBackground(new Color(255, 0, 0, 0));
		healthBar.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.25),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.065));
		Startpanel.add(healthBar);
		healthBar.setVisible(false);

		// 체력바 뒷면 구현
		backHealthBar = new ImageMaking("backHealthBar.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.065));
		backHealthBar.setBackground(new Color(255, 0, 0, 0));
		backHealthBar.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.25),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.065));
		Startpanel.add(backHealthBar);
		backHealthBar.setVisible(false);

		// 보스 구현
		boss = new ImageMaking("boss.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.2));
		boss.setBackground(new Color(255, 0, 0, 0));
		boss.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.11), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.2));
		Startpanel.add(boss);
		boss.setVisible(false);

		// 보스 공격 패턴 2 떨어지는 몬스터
		monster1 = new ImageMaking("monster.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
		monster1.setBackground(new Color(255, 0, 0, 0));
		monster1.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.465), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
		Startpanel.add(monster1);
		monster1.setVisible(false);

		monster2 = new ImageMaking("monster.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
		monster2.setBackground(new Color(255, 0, 0, 0));
		monster2.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.465), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
		Startpanel.add(monster2);
		monster2.setVisible(false);

		// 보스 공격 패턴 2 공격 필드 생성 전
		attackBeforeWidth = new ImageMaking("attackBeforeWidth.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		attackBeforeWidth.setBackground(new Color(255, 0, 0, 0));
		attackBeforeWidth.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		Startpanel.add(attackBeforeWidth);
		attackBeforeWidth.setVisible(false);

		attackBeforeHeight = new ImageMaking("attackBeforeHeight.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
				(int) (Main.PROGRAM_SCREEN_HEIGHT));
		attackBeforeHeight.setBackground(new Color(255, 0, 0, 0));
		attackBeforeHeight.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
				(int) (Main.PROGRAM_SCREEN_HEIGHT));
		Startpanel.add(attackBeforeHeight);
		attackBeforeHeight.setVisible(false);

		// 보스 공격 패턴 2 공격 필드 생성 후
		attackDamage1 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		attackDamage1.setBackground(new Color(255, 0, 0, 0));
		attackDamage1.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		Startpanel.add(attackDamage1);
		attackDamage1.setVisible(false);

		attackDamage2 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
				(int) (Main.PROGRAM_SCREEN_HEIGHT));
		attackDamage2.setBackground(new Color(255, 0, 0, 0));
		attackDamage2.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
		Startpanel.add(attackDamage2);
		attackDamage2.setVisible(false);

		attackDamage3 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		attackDamage3.setBackground(new Color(255, 0, 0, 0));
		attackDamage3.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		Startpanel.add(attackDamage3);
		attackDamage3.setVisible(false);

		attackDamage4 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
				(int) (Main.PROGRAM_SCREEN_HEIGHT));
		attackDamage4.setBackground(new Color(255, 0, 0, 0));
		attackDamage4.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
		Startpanel.add(attackDamage4);
		attackDamage4.setVisible(false);

		attackDamage5 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		attackDamage5.setBackground(new Color(255, 0, 0, 0));
		attackDamage5.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		Startpanel.add(attackDamage5);
		attackDamage5.setVisible(false);

		attackDamage6 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
				(int) (Main.PROGRAM_SCREEN_HEIGHT));
		attackDamage6.setBackground(new Color(255, 0, 0, 0));
		attackDamage6.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
		Startpanel.add(attackDamage6);
		attackDamage6.setVisible(false);

		attackDamage7 = new ImageMaking("damageField.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		attackDamage7.setBackground(new Color(255, 0, 0, 0));
		attackDamage7.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
		Startpanel.add(attackDamage7);
		attackDamage7.setVisible(false);

		// 보스 화염구 1
		Fireball1 = new ImageMaking("Fireball.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
		Fireball1.setBackground(new Color(255, 0, 0, 0));
		Fireball1.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
		Startpanel.add(Fireball1);
		Fireball1.setVisible(false);

		// 보스 화염구 2
		Fireball2 = new ImageMaking("Fireball.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
		Fireball2.setBackground(new Color(255, 0, 0, 0));
		Fireball2.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
		Startpanel.add(Fireball2);
		Fireball2.setVisible(false);

		// 보스 화염구 3
		Fireball3 = new ImageMaking("Fireball.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
		Fireball3.setBackground(new Color(255, 0, 0, 0));
		Fireball3.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
		Startpanel.add(Fireball3);
		Fireball3.setVisible(false);

		// 보스 화염구 4
		Fireball4 = new ImageMaking("Fireball.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
		Fireball4.setBackground(new Color(255, 0, 0, 0));
		Fireball4.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
		Startpanel.add(Fireball4);
		Fireball4.setVisible(false);

		// 무적 아이템
		invincible = new ImageMaking("invincible.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
		invincible.setBackground(new Color(255, 0, 0, 0));
		invincible.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
		Startpanel.add(invincible);
		invincible.setVisible(false);

		// 이속 증가 아이템
		Ghost = new ImageMaking("speedup.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
		Ghost.setBackground(new Color(255, 0, 0, 0));
		Ghost.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
				(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
		Startpanel.add(Ghost);
		Ghost.setVisible(false);

		// 플레이어 추적 몬스터
		this.monsterArray = new Component[5];
		for (int i = 0; i < 5; i++) {
			monsterArray[i] = new ImageMaking("greenmonster.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.04),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06));
			monsterArray[i].setBackground(new Color(255, 0, 0, 0));
			this.Startpanel.add(this.monsterArray[i]);
			this.monsterArray[i].setVisible(false);
		}

		// 왼쪽 화살표
		arrowLeft = new ImageMaking("arrowLeft.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		arrowLeft.setBackground(new Color(255, 0, 0, 0));
		arrowLeft.setBounds(
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)
						- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.25), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		Startpanel.add(arrowLeft);
		arrowLeft.setVisible(false);

		// 오른쪽 화살표
		arrowRight = new ImageMaking("arrowRight.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		arrowRight.setBackground(new Color(255, 0, 0, 0));
		arrowRight.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.25), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
		Startpanel.add(arrowRight);
		arrowRight.setVisible(false);

		// 기본 맵 구현
		// map1 = 상
		// map2 = 하
		// map3 = 좌
		// map4 = 우
		map1 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		map1.setBackground(new Color(255, 0, 0, 0));
		map1.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
				(int) mapStartY - ((int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
				(int) mapEndX - (int) mapStartX + (2 * (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025));
		Startpanel.add(map1);
		map1.setVisible(false);

		map2 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		map2.setBackground(new Color(255, 0, 0, 0));
		map2.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025), (int) mapEndY,
				(int) mapEndX - (int) mapStartX + (2 * (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025));
		Startpanel.add(map2);
		map2.setVisible(false);

		map3 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		map3.setBackground(new Color(255, 0, 0, 0));
		map3.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025), (int) mapStartY,
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025), (int) mapEndY - (int) mapStartY);
		Startpanel.add(map3);
		map3.setVisible(false);

		map4 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		map4.setBackground(new Color(255, 0, 0, 0));
		map4.setBounds((int) mapEndX, (int) mapStartY, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
				(int) mapEndY - (int) mapStartY);
		Startpanel.add(map4);
		map4.setVisible(false);

		// 계단 생성
		// 좌
		stairsLeft1 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		stairsLeft1.setBackground(new Color(255, 0, 0, 0));
		stairsLeft1.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(stairsLeft1);
		stairsLeft1.setVisible(false);

		stairsLeft2 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		stairsLeft2.setBackground(new Color(255, 0, 0, 0));
		stairsLeft2.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73) - (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(stairsLeft2);
		stairsLeft2.setVisible(false);

		stairsLeft3 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		stairsLeft3.setBackground(new Color(255, 0, 0, 0));
		stairsLeft3.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73) - (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(stairsLeft3);
		stairsLeft3.setVisible(false);
		// 우
		stairsRight1 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		stairsRight1.setBackground(new Color(255, 0, 0, 0));
		stairsRight1.setBounds(
				(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
						- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(stairsRight1);
		stairsRight1.setVisible(false);

		stairsRight2 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		stairsRight2.setBackground(new Color(255, 0, 0, 0));
		stairsRight2.setBounds(
				(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
						- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73) - (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(stairsRight2);
		stairsRight2.setVisible(false);

		stairsRight3 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		stairsRight3.setBackground(new Color(255, 0, 0, 0));
		stairsRight3.setBounds(
				(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
						- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73) - (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(stairsRight3);
		stairsRight3.setVisible(false);

		// 패턴 1 전용 코드
		this.seong_attack = new Component[15];
		for (int i = 0; i < 15; i++) {
			seong_attack[i] = new ImageMaking("jjangdol.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.01),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.02));
			seong_attack[i].setBackground(new Color(255, 0, 0, 0));
			this.Startpanel.add(this.seong_attack[i]);
			this.seong_attack[i].setVisible(false);
		}
		this.seong_attack2 = new Component[2];
		for (int i = 0; i < 2; i++) {
			seong_attack2[i] = new ImageMaking("jjangdol2.png", 0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03));
			seong_attack2[i].setBackground(new Color(255, 0, 0, 0));
			this.Startpanel.add(this.seong_attack2[i]);
			this.seong_attack2[i].setVisible(false);
		}

		// 패턴 6 전용 계단
		// 좌
		Leftstair1 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		Leftstair1.setBackground(new Color(255, 0, 0, 0));
		Leftstair1.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(Leftstair1);
		Leftstair1.setVisible(false);

		Leftstair2 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		Leftstair2.setBackground(new Color(255, 0, 0, 0));
		Leftstair2.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73) - (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(Leftstair2);
		Leftstair2.setVisible(false);
		// 우
		Rightstair1 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		Rightstair1.setBackground(new Color(255, 0, 0, 0));
		Rightstair1.setBounds(
				(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
						- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(Rightstair1);
		Rightstair1.setVisible(false);

		Rightstair2 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		Rightstair2.setBackground(new Color(255, 0, 0, 0));
		Rightstair2.setBounds(
				(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
						- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73) - (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(Rightstair2);
		Rightstair2.setVisible(false);

		// 중앙
		Centerstair1 = new ImageMaking("grayPrint.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		Centerstair1.setBackground(new Color(255, 0, 0, 0));
		Centerstair1.setBounds(
				(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.43))
						- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73) - (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
		Startpanel.add(Centerstair1);
		Centerstair1.setVisible(false);

		// 파일 가져오기
		String path = System.getProperty("user.dir");
		String filepath = path + "/src/JavaPoint.txt";
		try {
			FileReader fileReader = new FileReader(filepath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			for (int i = 0; i < 5; i++) {
				Score[i] = Integer.parseInt(bufferedReader.readLine());
			}
			bufferedReader.close();
		} catch (IOException e) {
			System.out.println("JavaPoint.txt 파일을 열 수 없습니다.");
		}

		// 게임 스코어
		Font font2 = new Font("Aharoni 굵게", Font.BOLD, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.033));
		// ================================================================================================================
		// ================================================================================================================
		FirstScore = new JLabel("1등 : ", SwingConstants.CENTER);
		FirstScore.setFont(font2);
		FirstScore.setOpaque(true);
		FirstScore.setForeground(Color.WHITE);
		FirstScore.setBackground(Color.BLACK);
		add(FirstScore);
		FirstScore.setVisible(true);
		FirstScore.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH / 2) - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.17) / 2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT / 2), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17),
				(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.09));
		if (((int) (Score[0] / 144) % 60) < 10 && ((int) (Score[0] / 144) / 60) < 10) {
			FirstScore.setText("1등  0" + ((int) (Score[0] / 144) / 60) + ":" + "0" + ((int) (Score[0] / 144) % 60));
		} else if (((int) (Score[0] / 144) % 60) >= 10 && ((int) (Score[0] / 144) / 60) < 10) {
			FirstScore.setText("1등  0" + ((int) (Score[0] / 144) / 60) + ":" + ((int) (Score[0] / 144) % 60));
		} else if (((int) (Score[0] / 144) % 60) < 10 && ((int) (Score[0] / 144) / 60) >= 10) {
			FirstScore.setText("1등  " + ((int) (Score[0] / 144) / 60) + ":" + "0" + ((int) (Score[0] / 144) % 60));
		} else
			FirstScore.setText("1등  " + ((int) (Score[0] / 144) / 60) + ":" + ((int) (Score[0] / 144) % 60));
		// ================================================================================================================
		// ================================================================================================================
		SecondScore = new JLabel("2등 : ", SwingConstants.CENTER);
		SecondScore.setFont(font2);
		SecondScore.setOpaque(true);
		SecondScore.setForeground(Color.WHITE);
		SecondScore.setBackground(Color.BLACK);
		add(SecondScore);
		SecondScore.setVisible(true);
		SecondScore.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH / 2) - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.17) / 2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT / 2) + (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.07),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.09));
		if (((int) (Score[1] / 144) % 60) < 10 && ((int) (Score[1] / 144) / 60) < 10) {
			SecondScore.setText("2등  0" + ((int) (Score[1] / 144) / 60) + ":" + "0" + ((int) (Score[1] / 144) % 60));
		} else if (((int) (Score[1] / 144) % 60) >= 10 && ((int) (Score[1] / 144) / 60) < 10) {
			SecondScore.setText("2등  0" + ((int) (Score[1] / 144) / 60) + ":" + ((int) (Score[1] / 144) % 60));
		} else if (((int) (Score[1] / 144) % 60) < 10 && ((int) (Score[1] / 144) / 60) >= 10) {
			SecondScore.setText("2등  " + ((int) (Score[1] / 144) / 60) + ":" + "0" + ((int) (Score[1] / 144) % 60));
		} else
			SecondScore.setText("2등  " + ((int) (Score[1] / 144) / 60) + ":" + ((int) (Score[1] / 144) % 60));
		// ================================================================================================================
		// ================================================================================================================
		ThirdScore = new JLabel("3등 : ", SwingConstants.CENTER);
		ThirdScore.setFont(font2);
		ThirdScore.setOpaque(true);
		ThirdScore.setForeground(Color.WHITE);
		ThirdScore.setBackground(Color.BLACK);
		add(ThirdScore);
		ThirdScore.setVisible(true);
		ThirdScore.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH / 2) - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.17) / 2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT / 2) + ((int) (Main.PROGRAM_SCREEN_HEIGHT * 0.07) * 2),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.09));
		if (((int) (Score[2] / 144) % 60) < 10 && ((int) (Score[2] / 144) / 60) < 10) {
			ThirdScore.setText("3등  0" + ((int) (Score[2] / 144) / 60) + ":" + "0" + ((int) (Score[2] / 144) % 60));
		} else if (((int) (Score[2] / 144) % 60) >= 10 && ((int) (Score[2] / 144) / 60) < 10) {
			ThirdScore.setText("3등  0" + ((int) (Score[2] / 144) / 60) + ":" + ((int) (Score[2] / 144) % 60));
		} else if (((int) (Score[2] / 144) % 60) < 10 && ((int) (Score[2] / 144) / 60) >= 10) {
			ThirdScore.setText("3등  " + ((int) (Score[2] / 144) / 60) + ":" + "0" + ((int) (Score[2] / 144) % 60));
		} else
			ThirdScore.setText("3등  " + ((int) (Score[2] / 144) / 60) + ":" + ((int) (Score[2] / 144) % 60));
		// ================================================================================================================
		// ================================================================================================================
		FourthScore = new JLabel("4등 : ", SwingConstants.CENTER);
		FourthScore.setFont(font2);
		FourthScore.setOpaque(true);
		FourthScore.setForeground(Color.WHITE);
		FourthScore.setBackground(Color.BLACK);
		add(FourthScore);
		FourthScore.setVisible(true);
		FourthScore.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH / 2) - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.17) / 2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT / 2) + ((int) (Main.PROGRAM_SCREEN_HEIGHT * 0.07) * 3),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.09));
		if (((int) (Score[3] / 144) % 60) < 10 && ((int) (Score[3] / 144) / 60) < 10) {
			FourthScore.setText("4등  0" + ((int) (Score[3] / 144) / 60) + ":" + "0" + ((int) (Score[3] / 144) % 60));
		} else if (((int) (Score[3] / 144) % 60) >= 10 && ((int) (Score[3] / 144) / 60) < 10) {
			FourthScore.setText("4등  0" + ((int) (Score[3] / 144) / 60) + ":" + ((int) (Score[3] / 144) % 60));
		} else if (((int) (Score[3] / 144) % 60) < 10 && ((int) (Score[3] / 144) / 60) >= 10) {
			FourthScore.setText("4등  " + ((int) (Score[3] / 144) / 60) + ":" + "0" + ((int) (Score[3] / 144) % 60));
		} else
			FourthScore.setText("4등  " + ((int) (Score[3] / 144) / 60) + ":" + ((int) (Score[3] / 144) % 60));
		// ================================================================================================================
		// ================================================================================================================
		FifthScore = new JLabel("5등 : ", SwingConstants.CENTER);
		FifthScore.setFont(font2);
		FifthScore.setOpaque(true);
		FifthScore.setForeground(Color.WHITE);
		FifthScore.setBackground(Color.BLACK);
		add(FifthScore);
		FifthScore.setVisible(true);
		FifthScore.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH / 2) - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.17) / 2),
				(int) (Main.PROGRAM_SCREEN_HEIGHT / 2) + ((int) (Main.PROGRAM_SCREEN_HEIGHT * 0.07) * 4),
				(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.09));
		if (((int) (Score[4] / 144) % 60) < 10 && ((int) (Score[4] / 144) / 60) < 10) {
			FifthScore.setText("5등  0" + ((int) (Score[4] / 144) / 60) + ":" + "0" + ((int) (Score[4] / 144) % 60));
		} else if (((int) (Score[4] / 144) % 60) >= 10 && ((int) (Score[4] / 144) / 60) < 10) {
			FifthScore.setText("5등  0" + ((int) (Score[4] / 144) / 60) + ":" + ((int) (Score[4] / 144) % 60));
		} else if (((int) (Score[4] / 144) % 60) < 10 && ((int) (Score[4] / 144) / 60) >= 10) {
			FifthScore.setText("5등  " + ((int) (Score[4] / 144) / 60) + ":" + "0" + ((int) (Score[4] / 144) % 60));
		} else
			FifthScore.setText("5등  " + ((int) (Score[4] / 144) / 60) + ":" + ((int) (Score[4] / 144) % 60));
		// ================================================================================================================
		// ================================================================================================================

		// 도움말 그리기
		this.HelpImage = new ImageMaking("HelpImage.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);

		// 배경화면 그리기
		this.homeImage = new ImageMaking("homeImage.png", 0, 0, Main.PROGRAM_SCREEN_WIDTH, Main.PROGRAM_SCREEN_HEIGHT);
		add(homeImage);
	}
	
	// 최대 144 프레임을 기준으로 프로그램을 딜레이 시킴
	public int delay(long beforeTime, long afterTime) {
		try {
			int delayTime = 7 - (int) (afterTime - beforeTime); // 1초를 144fps로 나누면 6.94444ms, 근사값인 7로 정함
			if (delayTime > 0) {
				Thread.sleep(delayTime);
				return 144; // 최대 프레임인 144FPS를 출력 중이라는 뜻
			} else
				return (int) (1000 / ((delayTime * -1) + 7)); // 현재 프레임 반환
		} catch (Exception e) {
			System.out.println("delay 메소드에서 오류가 발생하여 프로그램을 강제 종료합니다.");
			System.exit(0); // 오류가 발생하여 프로그램 강제 종료
			return -1; // 이 값은 실제로 전달되지 않음
		}
	}

	// 마우스 이벤트
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == gameStartButton) {
			System.out.println("스타트 버튼 정상작동");
			FirstScore.setVisible(false);
			SecondScore.setVisible(false);
			ThirdScore.setVisible(false);
			FourthScore.setVisible(false);
			FifthScore.setVisible(false);
			// 홈 패널 안보이게하기
			homeImage.setVisible(false);
			// 게임시작 버튼 안보이게하기
			gameStartButton.setVisible(false);
			// 도움말 버튼 안보이게하기
			helpButton.setVisible(false);

			// 게임시작(블랙 패널) 추가
			add(Startpanel);
			Startpanel.setVisible(true);

			// 맵 보이게 하기
			map1.setVisible(true);
			map2.setVisible(true);
			map3.setVisible(true);
			map4.setVisible(true);

			// 체력바 보이게 하기
			healthBar.setVisible(true);
			backHealthBar.setVisible(true);

			// 보스 보이게 하기
			boss.setVisible(true);

			// 홈으로 돌아가기, 일시정지 버튼 보이게 하기
			goHomeButton.setVisible(true);
			pauseButton.setVisible(true);

			// 게임 상태 변수 변경 (진행 중)
			Main.gameStatus = true;
		}
		if (e.getSource() == helpButton) {
			System.out.println("도움말 버튼 정상작동");
			FirstScore.setVisible(false);
			SecondScore.setVisible(false);
			ThirdScore.setVisible(false);
			FourthScore.setVisible(false);
			FifthScore.setVisible(false);
			// 홈 패널 안보이게하기
			homeImage.setVisible(false);
			// 게임시작 버튼 안보이게하기
			gameStartButton.setVisible(false);
			// 도움말 버튼 안보이게하기
			helpButton.setVisible(false);

			// 도움말 이미지 추가
			add(HelpImage);
			// 도움말 이미지 보이게 하기
			HelpImage.setVisible(true);
			// 뒤로가기 버튼 보이게하기
			GoBackButton.setVisible(true);

		}
		if (e.getSource() == GoBackButton) {
			System.out.println("뒤로가기 버튼 정상작동");
			FirstScore.setVisible(true);
			SecondScore.setVisible(true);
			ThirdScore.setVisible(true);
			FourthScore.setVisible(true);
			FifthScore.setVisible(true);
			// 도움말 패널 안보이게하기
			HelpImage.setVisible(false);
			// 뒤로가기 버튼 안보이게하기
			GoBackButton.setVisible(false);

			// 홈 패널 보이게하기
			homeImage.setVisible(true);
			// 게임시작 버튼 보이게하기
			gameStartButton.setVisible(true);
			// 도움말 버튼 보이게하기
			helpButton.setVisible(true);
		}
		if (e.getSource() == pauseButton) {
			Main.gamePause = true;
			restartButton.setVisible(true);
			pauseButton.setVisible(false);
		}
		if (e.getSource() == restartButton) {
			Main.gamePause = false;
			pauseButton.setVisible(true);
			restartButton.setVisible(false);
		}
		if (e.getSource() == goHomeButton) {
			FirstScore.setVisible(true);
			SecondScore.setVisible(true);
			ThirdScore.setVisible(true);
			FourthScore.setVisible(true);
			FifthScore.setVisible(true);
			if (Main.gamePause) {
				Main.gamePause = false;
				pauseButton.setVisible(true);
				restartButton.setVisible(false);
			}
			Main.gameStatus = false;
			goHomeButton.setVisible(false);
			pauseButton.setVisible(false);
			boss.setVisible(false);
			healthBar.setVisible(false);
			backHealthBar.setVisible(false);
			map1.setVisible(false);
			map2.setVisible(false);
			map3.setVisible(false);
			map4.setVisible(false);
			Startpanel.setVisible(false);
			helpButton.setVisible(true);
			gameStartButton.setVisible(true);
			homeImage.setVisible(true);
			Main.mainMusic = new Music("tempMusic.wav");
			Main.mainMusic.musicSetCount(0);
			Main.mainMusic.musicStart();
			Main.gameMusic.musicStop();
		}
	}

	// 키보드 이벤트
	public void keyboardEvent() {
		addKeyListener(new KeyAdapter() { // 키 이벤트
			@Override
			public void keyPressed(KeyEvent e) { // 키 눌렀을때
				switch (e.getKeyCode()) { // 키 코드로 스위치
				case KeyEvent.VK_LEFT:// 방향키(왼)눌렀을때
					if (!moveLeft) {
						moveLeft = true;
					}
					break;
				case KeyEvent.VK_RIGHT:// 방향키(오른)눌렀을때
					if (!moveRight) {
						moveRight = true;
					}
					break;
				case KeyEvent.VK_UP: // 방향키(위) 눌렀을때
					jumpInput = true;
					break;
				case KeyEvent.VK_SPACE: // 스페이스 바 눌렀을때
					dashCheck = true;
					break;
				default:
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) { // 키 코드로 스위치
				case KeyEvent.VK_LEFT:// 방향키(왼) 놓을 때
					moveLeft = false;
					break;
				case KeyEvent.VK_RIGHT:// 방향키(오른) 놓을 때
					moveRight = false;
					break;
				case KeyEvent.VK_UP:
					jumpInput = false;
					break;
				case KeyEvent.VK_SPACE: // 스페이스 바 눌렀을때
					dashCheck = false;
					break;
				default:
					break;
				}
			}
		});
	}

	public static void main(String[] args) {

		/* ------ 여기에 단일 횟수 실행 코드 작성(위) ----- */
		System.out.println("현재 사용자 화면 해상도: " + SCREEN_WIDTH + " * " + SCREEN_HEIGHT);
		System.out.println("프로그램 화면 크기: " + PROGRAM_SCREEN_WIDTH + " * " + PROGRAM_SCREEN_HEIGHT + " (배율: "
				+ DISPLAY_MULTIPLE + ")");
		Main.mainMusic = new Music("tempMusic.wav");
		Main.gameMusic = new Music("gameMusic.wav");
		mainMusic.musicSetCount(0);
		mainMusic.musicStart();

		Main main = new Main(); // Main 객체 생성

		main.setVisible(true);

		while (!Main.gameStatus) {
			main.currentFrameRate = main.delay(0, 0);
		}

		gameMusic.musicSetCount(0);
		gameMusic.musicStart();
		mainMusic.musicStop();

		main.keyboardEvent(); // 유저 키보드 입력 받기 시작

		/* ------ 여기에 단일 횟수 실행 코드 작성(아래) ----- */

		boolean timer = false;
		int count = 1; // 반복문이 돈 횟수

		// 게임 패턴1 변수
		int cooldownInteger = 1;
		int seong_count = 1;
		int arraw = 0;
		double seong_wh = 0;
		int[] seong_width = new int[15];
		int[] seong_height = new int[15];
		int[] seong_go = new int[2];

		// 게임 패턴6 변수
		// 랜덤으로 아이템 선택
		Random r = new Random();
		boolean invincibleFirst = r.nextBoolean();
		// 5개의 몬스터를 랜덤한 위치에 생성
		for (int i = 0; i < 5; i++) {
			int randomX = (int) (Math.random()
					* (Main.mapEndX - Main.mapStartX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1))) + (int) Main.mapStartX
					+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
			int randomY = (int) (Math.random()
					* (Main.mapEndY - Main.mapStartY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1))) + (int) Main.mapStartY
					+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);

			// 몬스터 초기 설정
			main.monstermove = false;
			main.monsterArray[i].setBounds(randomX, randomY, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.04),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06));
		}
		main.monsterArrayX = new int[5];
		main.monsterArrayY = new int[5];

		// 게임 프레임 틀
		while (true) {
			long beforeTime = System.currentTimeMillis(); // 반복문 돌기 전 시간 구함

			/* ----- 여기에 프레임마다 실행할 코드 작성(위) ----- */

			// 포커스를 항상 메인으로 설정
			main.requestFocus();

			// 플레이어 좌우 이동 구현
			if (!Main.coolDown) {
				if (Main.moveLeft) {
					Main.locX -= speedX;
				}
				if (Main.moveRight) {
					Main.locX += speedX;
				}
			}

			// 플레이어 점프 구현
			if (jumpInput) {
				if (!jumping) {
					jumpCheck = true; // 점프 중으로 설정
				}
			}
			if (Main.jumpCheck) {
				Main.speedY = Main.accelerationY * -1;
				Main.jumping = true;
				Main.jumpCheck = false;
			}

			// 맵 속 중력 적용
			if ((int) Main.locY < (int) Main.mapEndY) {
				Main.speedY += Main.gravity;
			}

			// 플레이어 Y 좌표 이동
			Main.locY += Main.speedY;

			// 플레이어 대쉬 입력 데이터 처리
			if (dashCheck) {
				if (!coolDown) {
					if (moveLeft && !moveRight) {
						leftDash = true;
						coolDown = true;
					} else if (!moveLeft && moveRight) {
						rightDash = true;
						coolDown = true;
					}
				}
			}

			// 플레이어 좌우 대쉬 구현
			if (Main.leftDash) {
				// 데이터 처리
				Main.leftDash = false;
				Main.leftDashing = true;
			}
			if (Main.leftDashing) {
				// 데이터 처리
				Main.locX -= dashSpeed;

				// 이펙트 처리
				Main.playerDashImage.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.050 / 2),
						(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.045 / 2),
						(int) (Main.PROGRAM_SCREEN_WIDTH * 0.075), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.045));
				Main.playerDashImage.setVisible(true);
			}

			if (Main.rightDash) {
				// 데이터 처리
				Main.rightDash = false;
				Main.rightDashing = true;
			}
			if (Main.rightDashing) {
				// 데이터 처리
				Main.locX += dashSpeed;

				// 이펙트 처리
				Main.playerDashImage2.setBounds(
						(int) ((Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.050 / 2) - Main.PROGRAM_SCREEN_WIDTH * 0.075),
						(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.045 / 2),
						(int) (Main.PROGRAM_SCREEN_WIDTH * 0.075), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.045));
				Main.playerDashImage2.setVisible(true);
			}

			// 5초마다 체력회복
			if ((count - 1) % (144 * 15) == 0) {
				System.out.println("체력 회복");
				int heal = 30 - (int)(count / (144 * 15) * 5);
				if (heal < 0)
					heal = 0;
				Main.health += heal;
				if (Main.health > 100) {
					Main.health = 100;
				}
			}

			// 플레이어 데미지 이펙트
			if (Main.health < (int) Main.healthUpdate)
				main.playerDamageImage.setVisible(true);
			else
				main.playerDamageImage.setVisible(false);

			if ((count - 1) % (144 * 15) == 0) {
				for (int i = 0; i < 15; i++) {
					main.seong_attack[i].setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.01),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.02));
					main.seong_attack[i].setVisible(false);
				}
				for (int i = 0; i < 2; i++) {
					main.seong_attack2[i].setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03));
					main.seong_attack2[i].setVisible(false);
				}
				seong_count = 0;
				arraw = 0;
			}

			// 공격 패턴 선정
			if ((count - 1) % (144 * 15) == 0) {
				System.out.println("attackNumber 재설정");
				while (true) {
					int tempRandom = (int) (Math.random() * 100) % 5;
					if (tempRandom != Main.attackNumber) {
						Main.attackNumber = tempRandom;
						break;
					}
				}
			}

			// 테스트 코드 (위) ---------------------------------------------------
//			Main.attackNumber = 5;
			// 테스트 코드 (아래) ---------------------------------------------------

			// 패턴에 맞는 공격 패턴 실행
			switch (Main.attackNumber) {
			case 0: // 첫 번째 패턴
				// 데미지 필드 생성
				if ((count - 1) % (144 * 15) == (144 * 0.5)) {
					main.damageField1.setVisible(true);
					main.damageField2.setVisible(true);
				}
				// 데미지 필드 삭제
				if ((count - 1) % (144 * 15) == (144 * 15 - 1)) {
					main.damageField1.setVisible(false);
					main.damageField2.setVisible(false);
				}
				// 데미지 필드 UI 최신화
				main.damageField1.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
						(int) mapStartY, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025), (int) mapEndY - (int) mapStartY);
				main.damageField2.setBounds((int) mapEndX, (int) mapStartY, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
						(int) mapEndY - (int) mapStartY);

				// 시작 좌우 공간 넓히기 (0초 ~ 0.5초 동안)
				if ((count - 1) % (144 * 15) >= 0 && (count - 1) % (144 * 15) < (144 * 0.5)) {
					Main.mapStartX -= Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapEndX += Main.PROGRAM_SCREEN_WIDTH * 0.002;
				}
				// 마무리 좌우 공간 좁히기 (14.5초 ~ 15초 동안)
				if ((count - 1) % (144 * 15) >= (144 * 14.5) && (count - 1) % (144 * 15) < (144 * 15)) {
					Main.mapStartX += Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapEndX -= Main.PROGRAM_SCREEN_WIDTH * 0.002;
				}

				// 장애물 랜덤으로 좌우 벽 중 하나 크기 줄이기 (1초 ~ 13초 동안)
				if ((count - 1) % (144 * 15) >= (144 * 1) && (count - 1) % (144 * 15) < (144 * 13)) {
					// 2초에 한번 줄이는 벽 선정
					if (((count - 1) % (144 * 15)) % (144 * 2) == 0) {
						Main.randomChoiceNumber = (int) (Math.random() * 100) % 2;
					}
					if (Main.randomChoiceNumber == 0) { // 0일 때 (좌)
						Main.mapStartX += Main.PROGRAM_SCREEN_WIDTH * 0.00043;
						Main.countLeft++;
					} else { // 1일 때 (우)
						Main.mapEndX -= Main.PROGRAM_SCREEN_WIDTH * 0.00043;
						Main.countRight++;
					}
				}

				// 줄어든 벽 크기 돌리기 (13초 ~ 14초 동안)
				if ((count - 1) % (144 * 15) >= (144 * 13) && (count - 1) % (144 * 15) < (144 * 14)) {
					Main.mapStartX -= ((Main.PROGRAM_SCREEN_WIDTH * 0.00043) * Main.countLeft) / (144 * 1);
					Main.mapEndX += ((Main.PROGRAM_SCREEN_WIDTH * 0.00043) * Main.countRight) / (144 * 1);
				}

				// 변수 초기화 (14.5초)
				if ((count - 1) % (144 * 15) == (144 * 14.5)) {
					Main.countLeft = 0;
					Main.countRight = 0;
				}

				// 왼쪽 벽에 닿으면 데미지
				if ((count - 1) % (144 * 15) >= (144 * 0.5)) {
					if (Main.locX <= (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) + 1) {
						if (!Main.healthGuard) {
							Main.health -= 10;
							Music scream = new Music("scream.wav");
							scream.musicStart();
						}
					}
					// 오른쪽 벽에 닿으면 데미지
					if (Main.locX >= (int) Main.mapEndX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1) {
						if (!Main.healthGuard) {
							Main.health -= 10;
							Music scream = new Music("scream.wav");
							scream.musicStart();
						}
					}
				}

				// 플레이어가 뒤쪽으로 당겨지게 만들기 (0.5초 ~ 14.5초 동안)
				if ((count - 1) % (144 * 15) >= (144 * 0.5) && (count - 1) % (144 * 15) < (144 * 14.5)) {
					if (((count - 1) % (144 * 15)) % (144 * 2) == 0) {
						Main.choiceNumber = (Main.choiceNumber + 1) % 2; // 0은 왼쪽, 1은 오른쪽
					}
					if (Main.choiceNumber == 0) { // 0일 때
						Main.locX -= Main.PROGRAM_SCREEN_WIDTH * 0.002;
						main.arrowLeft.setVisible(true);
						main.arrowRight.setVisible(false);
					} else { // 1일 때
						Main.locX += Main.PROGRAM_SCREEN_WIDTH * 0.002;
						main.arrowRight.setVisible(true);
						main.arrowLeft.setVisible(false);
					}
					// 화살표 표시 끄기
					if (((count - 1) % (144 * 15)) == (2087)) {
						main.arrowLeft.setVisible(false);
						main.arrowRight.setVisible(false);
					}
				}
				break;

			case 1: // 두 번째 패턴
				if (seong_count % 12 == 0 && seong_count < 180) {
					seong_width[arraw] = (int) (Math.random() * 10000)
							% (int) (Main.PROGRAM_SCREEN_WIDTH - mapStartX * 2) + (int) (mapStartX);
					seong_height[arraw] = (int) (Math.random() * 10000) % (int) (Main.PROGRAM_SCREEN_WIDTH * 0.2)
							+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.1);

					seong_go[0] = (int) (mapStartX);
					seong_go[1] = (int) (mapEndX);

					main.seong_attack2[0].setBounds((int) (mapStartX), (int) (mapEndY),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.01), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.01));
					main.seong_attack2[1].setBounds((int) (mapEndX), (int) (mapEndY),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.01), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.01));

					// 15개
					if (arraw < 15) {
						seong_wh = (((int) (Math.random() * 100)) % 17 + 78);
						seong_wh /= 100;
						main.seong_attack[arraw].setBounds(seong_width[arraw], seong_height[arraw],
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.01), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02));

						main.seong_attack2[0].setBounds(seong_go[0] - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
								(int) (mapEndY * seong_wh), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03));

						main.seong_attack2[1].setBounds(seong_go[1], (int) (mapEndY * seong_wh),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03));
						main.seong_attack[arraw].setVisible(true);
						main.seong_attack2[0].setVisible(true);
						main.seong_attack2[1].setVisible(true);
					}
					arraw++;
				}
				if (seong_count >= 180) {
					main.seong_attack2[0].setBounds(seong_go[0], (int) (mapEndY * seong_wh),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03));

					main.seong_attack2[1].setBounds(seong_go[1], (int) (mapEndY * seong_wh),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03));
					seong_go[0] += (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.009);
					seong_go[1] -= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.009);
					for (int i = 0; i < 15; i++) {
						main.seong_attack[i].setBounds(seong_width[i], seong_height[i],
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.01), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02));
						seong_height[i] += (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.009);
						if (Main.locX >= (seong_width[i] - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locX <= ((seong_width[i] + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.01))
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2))
								&& Main.locY >= (seong_height[i] - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locY <= ((seong_height[i] + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02))
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2))) {
							if (!Main.healthGuard) {
								Main.health -= 10;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					for (int i = 0; i < 2; i++) {
						if (Main.locX >= (seong_go[i] - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locX <= seong_go[i] + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03)
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2
								&& Main.locY >= ((int) (mapEndY * seong_wh)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locY <= (int) (mapEndY * seong_wh) + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.01)
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2) {
							if (!Main.healthGuard) {
								Main.health -= 10;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
				}

				if (seong_count > 324) {
					seong_count = 0;
					arraw = 0;
				}
				seong_count++;
				break;

			case 2: // 세 번째 패턴
				// 시작 좌우 공간 넓히기 및 계단 생성 (0초 ~ 0.5초 동안)
				if ((count - 1) % (144 * 15) >= 0 && (count - 1) % (144 * 15) < (144 * 0.5)) {
					if ((count - 1) % (144 * 15) == (144 * 0.5) - 1) {
						main.stairsLeft1.setVisible(true);
						main.stairsLeft2.setVisible(true);
						main.stairsLeft3.setVisible(true);
						main.stairsRight1.setVisible(true);
						main.stairsRight2.setVisible(true);
						main.stairsRight3.setVisible(true);
					}
					Main.mapStartX -= Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapEndX += Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapStartY -= Main.PROGRAM_SCREEN_WIDTH * 0.003;
				}

				// 마무리 좌우 공간 좁히기 및 계단 생성 (14.5초 ~ 15초 동안)
				if ((count - 1) % (144 * 15) >= (144 * 14.5) && (count - 1) % (144 * 15) < (144 * 15)) {
					if ((count - 1) % (144 * 15) == (144 * 14.5)) {
						main.stairsLeft1.setVisible(false);
						main.stairsLeft2.setVisible(false);
						main.stairsLeft3.setVisible(false);
						main.stairsRight1.setVisible(false);
						main.stairsRight2.setVisible(false);
						main.stairsRight3.setVisible(false);
					}
					Main.mapStartX += Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapEndX -= Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapStartY += Main.PROGRAM_SCREEN_WIDTH * 0.003;
				}

				// 계단 밟을 수 있게 데이터 처리
				if ((count - 1) % (144 * 15) >= (144 * 0.5) && (count - 1) % (144 * 15) < (144 * 14.5)) {
					if (Main.locX > (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15) // locX
							&& Main.locX < (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)
									+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17)) {
						if (Main.speedY > Main.gravity) {
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
						}
					}
					if (Main.locX > (int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17)) // locX
							- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15))
							&& Main.locX < (int) (Main.PROGRAM_SCREEN_WIDTH
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15))) {
						if (Main.speedY > Main.gravity) {
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
						}
					}
				}
				// 1.5초부터 14.5초까지 공격
				if ((count - 1) % (144 * 15) >= (144 * 1.5) && (count - 1) % (144 * 15) < (144 * 14.5)) {
					// 12.0초에 데미지 필드 모두 삭제
					if ((count - 1) % (144 * 15) == (144 * 12.0)) {
						main.attackDamage1.setVisible(false);
						main.attackDamage2.setVisible(false);
						main.attackDamage3.setVisible(false);
						main.attackDamage4.setVisible(false);
						main.attackDamage5.setVisible(false);
						main.attackDamage6.setVisible(false);
						main.attackDamage7.setVisible(false);
					}
					// 1 (가로)
					if ((count - 1) % (144 * 15) >= (144 * 1.5) && (count - 1) % (144 * 15) <= (144 * 2.3)) {
						if ((count - 1) % (144 * 15) == (144 * 1.5)) {
							main.attackBeforeWidth.setVisible(true);
						}
						if ((count - 1) % (144 * 15) == (int) (144 * 2.3)) {
							main.attackDamage1StartY = (int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025);
							main.attackDamage1EndY = (int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)
									+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05);
							main.attackDamage1.setBounds(0,
									(int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
									(int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
							main.attackBeforeWidth.setVisible(false);
						}
						main.attackBeforeWidth.setBounds(0,
								(int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
								(int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
					}
					if ((count - 1) % (144 * 15) == (144 * 2.5)) {
						main.attackDamage1.setVisible(true);
					}
					// 데미지
					if ((count - 1) % (144 * 15) >= (144 * 2.5) && (count - 1) % (144 * 15) <= (144 * 12.0)) {
						if (Main.locY >= main.attackDamage1StartY - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locY <= main.attackDamage1EndY
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 5;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					// 2 (세로)
					if ((count - 1) % (144 * 15) >= (144 * 2.5) && (count - 1) % (144 * 15) < (144 * 3.3)) {
						if ((count - 1) % (144 * 15) == (144 * 2.5)) {
							main.attackBeforeHeight.setVisible(true);
						}
						if ((count - 1) % (144 * 15) == (int) (144 * 3.3)) {
							main.attackDamage2StartX = (int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015);
							main.attackDamage2EndX = (int) Main.locX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015);
							main.attackDamage2.setBounds((int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015), 0,
									(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
							main.attackBeforeHeight.setVisible(false);
						}
						main.attackBeforeHeight.setBounds((int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015),
								0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
					}
					if ((count - 1) % (144 * 15) == (144 * 3.5)) {
						main.attackDamage2.setVisible(true);
					}
					// 데미지
					if ((count - 1) % (144 * 15) >= (144 * 3.5) && (count - 1) % (144 * 15) <= (144 * 12.0)) {
						if (Main.locX >= main.attackDamage2StartX - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locX <= main.attackDamage2EndX
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 5;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					// 3 (가로)
					if ((count - 1) % (144 * 15) >= (144 * 3.5) && (count - 1) % (144 * 15) <= (144 * 4.3)) {
						if ((count - 1) % (144 * 15) == (144 * 3.5)) {
							main.attackBeforeWidth.setVisible(true);
						}
						if ((count - 1) % (144 * 15) == (int) (144 * 4.3)) {
							main.attackDamage3StartY = (int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025);
							main.attackDamage3EndY = (int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)
									+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05);
							main.attackDamage3.setBounds(0,
									(int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
									(int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
							main.attackBeforeWidth.setVisible(false);
						}
						main.attackBeforeWidth.setBounds(0,
								(int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
								(int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
					}
					if ((count - 1) % (144 * 15) == (144 * 4.5)) {
						main.attackDamage3.setVisible(true);
					}
					// 데미지
					if ((count - 1) % (144 * 15) >= (144 * 4.5) && (count - 1) % (144 * 15) <= (144 * 12.0)) {
						if (Main.locY >= main.attackDamage3StartY - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locY <= main.attackDamage3EndY
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 5;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					// 4 (세로)
					if ((count - 1) % (144 * 15) >= (144 * 4.5) && (count - 1) % (144 * 15) < (144 * 5.3)) {
						if ((count - 1) % (144 * 15) == (144 * 4.5)) {
							main.attackBeforeHeight.setVisible(true);
						}
						if ((count - 1) % (144 * 15) == (int) (144 * 5.3)) {
							main.attackDamage4StartX = (int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015);
							main.attackDamage4EndX = (int) Main.locX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015);
							main.attackDamage4.setBounds((int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015), 0,
									(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
							main.attackBeforeHeight.setVisible(false);
						}
						main.attackBeforeHeight.setBounds((int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015),
								0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
					}
					if ((count - 1) % (144 * 15) == (144 * 5.5)) {
						main.attackDamage4.setVisible(true);
					}
					// 데미지
					if ((count - 1) % (144 * 15) >= (144 * 5.5) && (count - 1) % (144 * 15) <= (144 * 12.0)) {
						if (Main.locX >= main.attackDamage4StartX - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locX <= main.attackDamage4EndX
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 5;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					// 5 (가로)
					if ((count - 1) % (144 * 15) >= (144 * 5.5) && (count - 1) % (144 * 15) <= (144 * 6.3)) {
						if ((count - 1) % (144 * 15) == (144 * 5.5)) {
							main.attackBeforeWidth.setVisible(true);
						}
						if ((count - 1) % (144 * 15) == (int) (144 * 6.3)) {
							main.attackDamage5StartY = (int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025);
							main.attackDamage5EndY = (int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)
									+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05);
							main.attackDamage5.setBounds(0,
									(int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
									(int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
							main.attackBeforeWidth.setVisible(false);
						}
						main.attackBeforeWidth.setBounds(0,
								(int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
								(int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
					}
					if ((count - 1) % (144 * 15) == (144 * 6.5)) {
						main.attackDamage5.setVisible(true);
					}
					// 데미지
					if ((count - 1) % (144 * 15) >= (144 * 6.5) && (count - 1) % (144 * 15) <= (144 * 12.0)) {
						if (Main.locY >= main.attackDamage5StartY - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locY <= main.attackDamage5EndY
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 5;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					// 6 (세로)
					if ((count - 1) % (144 * 15) >= (144 * 6.5) && (count - 1) % (144 * 15) < (144 * 7.3)) {
						if ((count - 1) % (144 * 15) == (144 * 6.5)) {
							main.attackBeforeHeight.setVisible(true);
						}
						if ((count - 1) % (144 * 15) == (int) (144 * 7.3)) {
							main.attackDamage6StartX = (int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015);
							main.attackDamage6EndX = (int) Main.locX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015);
							main.attackDamage6.setBounds((int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015), 0,
									(int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
							main.attackBeforeHeight.setVisible(false);
						}
						main.attackBeforeHeight.setBounds((int) Main.locX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.015),
								0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03), (int) (Main.PROGRAM_SCREEN_HEIGHT));
					}
					if ((count - 1) % (144 * 15) == (144 * 7.5)) {
						main.attackDamage6.setVisible(true);
					}
					// 데미지
					if ((count - 1) % (144 * 15) >= (144 * 7.5) && (count - 1) % (144 * 15) <= (144 * 12.0)) {
						if (Main.locX >= main.attackDamage6StartX - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locX <= main.attackDamage6EndX
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 5;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					// 7 (가로)
					if ((count - 1) % (144 * 15) >= (144 * 7.5) && (count - 1) % (144 * 15) <= (144 * 8.3)) {
						if ((count - 1) % (144 * 15) == (144 * 7.5)) {
							main.attackBeforeWidth.setVisible(true);
						}
						if ((count - 1) % (144 * 15) == (int) (144 * 8.3)) {
							main.attackDamage7StartY = (int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025);
							main.attackDamage7EndY = (int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)
									+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05);
							main.attackDamage7.setBounds(0,
									(int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
									(int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
							main.attackBeforeWidth.setVisible(false);
						}
						main.attackBeforeWidth.setBounds(0,
								(int) Main.locY - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
								(int) (Main.PROGRAM_SCREEN_WIDTH), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
					}
					if ((count - 1) % (144 * 15) == (144 * 8.5)) {
						main.attackDamage7.setVisible(true);
					}
					// 데미지
					if ((count - 1) % (144 * 15) >= (144 * 8.5) && (count - 1) % (144 * 15) <= (144 * 12.0)) {
						if (Main.locY >= main.attackDamage7StartY - (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)
								&& Main.locY <= main.attackDamage7EndY
										+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.030) / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 5;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					if ((count - 1) % (144 * 15) >= (144 * 8.5) && ((count - 1) % (144 * 15) <= (144 * 12.0))) {
						if ((count - 1) % (144 * 15) == (144 * 8.5)) {
							main.monster1.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.465),
									(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07),
									(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
							main.monster2.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.465),
									(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07),
									(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
							main.monster1X = (Main.PROGRAM_SCREEN_WIDTH * 0.465);
							main.monster2X = (Main.PROGRAM_SCREEN_WIDTH * 0.465);
							main.monster2Y = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18);
							main.monster1.setVisible(true);
							main.monster2.setVisible(true);
						}
						if ((count - 1) % (144 * 15) > (144 * 8.5)) {
							if (Main.locX > main.monster1X + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.035)) {
								if ((Main.locX - main.monster1X
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.035) > Main.PROGRAM_SCREEN_WIDTH
												* 0.002))
									main.monster1X += Main.PROGRAM_SCREEN_WIDTH * 0.002;
							} else if (Main.locX < main.monster1X + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.035)) {
								if ((main.monster1X + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.035)
										- Main.locX > Main.PROGRAM_SCREEN_WIDTH * 0.002))
									main.monster1X -= Main.PROGRAM_SCREEN_WIDTH * 0.002;
							}
						}
						main.monster1.setBounds((int) main.monster1X, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
						if (((count - 1) % (144 * 15)) % (144 * 0.5) >= 0) {
							if (((count - 1) % (144 * 15)) % (144 * 0.5) == 0) {
								main.monster2X = main.monster1X;
								main.monster2Y = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18);
								main.monster2.setBounds((int) main.monster2X, (int) main.monster2Y,
										(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07),
										(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
							}
							main.monster2Y += Main.PROGRAM_SCREEN_HEIGHT * 0.01;
							main.monster2.setBounds((int) main.monster2X, (int) main.monster2Y,
									(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
							if (Main.locX > main.monster2X
									&& Main.locX < main.monster2X + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07)) {
								if (Main.locY > main.monster2Y
										&& Main.locY < main.monster2Y + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07)) {
									if (!Main.healthGuard) {
										Main.health -= 10;
										Music scream = new Music("scream.wav");
										scream.musicStart();
									}
								}
							}
						}
						if ((count - 1) % (144 * 15) == (144 * 12.0)) {
							main.monster1.setVisible(false);
							main.monster2.setVisible(false);
						}
					}
					// 보스 떨어짐
					if ((count - 1) % (144 * 15) >= (144 * 11.5) && (count - 1) % (144 * 15) <= (144 * 14.5)) {
						// 초기 설정
						if ((count - 1) % (144 * 15) == 144 * 11.5) {
							main.bossCheck = false;
							main.bossX = (int) (Main.PROGRAM_SCREEN_WIDTH * 0.5)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1);
							main.bossY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.11)
									+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.00007) * tempBoss);
						}
						// ---- X축 추적
						if (Main.locX > main.bossX + (Main.PROGRAM_SCREEN_WIDTH * 0.1)) {
							if ((Main.locX - main.bossX + (Main.PROGRAM_SCREEN_WIDTH * 0.1) > Main.PROGRAM_SCREEN_WIDTH
									* 0.0025))
								main.bossX += Main.PROGRAM_SCREEN_WIDTH * 0.0025;
						} else if (Main.locX < main.bossX + (Main.PROGRAM_SCREEN_WIDTH * 0.1)) {
							if ((main.bossX + (Main.PROGRAM_SCREEN_WIDTH * 0.1) - Main.locX > Main.PROGRAM_SCREEN_WIDTH
									* 0.0025))
								main.bossX -= Main.PROGRAM_SCREEN_WIDTH * 0.0025;
						}
						// ---- Y축 추적
						if (Main.locY > main.bossY + (Main.PROGRAM_SCREEN_WIDTH * 0.1)) {
							if ((Main.locY - main.bossY + (Main.PROGRAM_SCREEN_WIDTH * 0.1) > Main.PROGRAM_SCREEN_WIDTH
									* 0.0025))
								main.bossY += Main.PROGRAM_SCREEN_WIDTH * 0.0025;
						} else if (Main.locY < main.bossY + (Main.PROGRAM_SCREEN_WIDTH * 0.1)) {
							if ((main.bossY + (Main.PROGRAM_SCREEN_WIDTH * 0.1) - Main.locY > Main.PROGRAM_SCREEN_WIDTH
									* 0.0025))
								main.bossY -= Main.PROGRAM_SCREEN_WIDTH * 0.0025;
						}
						// ---- 추적 마무리
						main.boss.setBounds((int) main.bossX, (int) main.bossY, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.2));
						if (Main.locX > main.bossX
								&& Main.locX < main.bossX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.2)) {
							if (Main.locY > main.bossY
									&& Main.locY < main.bossY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.2)) {
								if (!Main.healthGuard) {
									Main.health -= 10;
									Music scream = new Music("scream.wav");
									scream.musicStart();
								}
							}
						}
						if ((count - 1) % (144 * 15) == (144 * 14.5) - 1) {
							main.bossCheck = true;
						}
					}

				}
				break;

			case 3: // 네 번째 패턴
				// 첫 번째 화염구 생성(시작 직후)
				if ((count - 1) % (144 * 15) == (144 * 0.5)) {
					// 랜덤한 위치에서 화염구 생성
					int randomX1 = (int) (Math.random()
							* (Main.mapEndX - Main.mapStartX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
							+ (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
					int randomY1 = (int) (Math.random()
							* (Main.mapEndY - Main.mapStartY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
							+ (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
					main.Fireball1.setBounds(randomX1, randomY1, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
					main.Fireball1.setVisible(true);

					// 화염구 이동 방향 설정
					fireballSpeedX1 = (Math.random() > 0.5) ? fireballSpeed : -fireballSpeed; // 랜덤하게 왼쪽 또는 오른쪽으로 이동
					fireballSpeedY1 = (Math.random() > 0.5) ? fireballSpeed : -fireballSpeed; // 랜덤하게 위 또는 아래로 이동
				}

				// 두 번째 화염구 생성(시작 후 4초 뒤)
				if ((count - 1) % (144 * 15) == (144 * 4)) {
					// 랜덤한 위치에서 화염구 생성
					int randomX2 = (int) (Math.random()
							* (Main.mapEndX - Main.mapStartX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
							+ (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
					int randomY2 = (int) (Math.random()
							* (Main.mapEndY - Main.mapStartY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
							+ (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
					main.Fireball2.setBounds(randomX2, randomY2, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
					main.Fireball2.setVisible(true);

					// 화염구 이동 방향 설정
					fireballSpeedX2 = (Math.random() > 0.5) ? fireballSpeed : -fireballSpeed; // 랜덤하게 왼쪽 또는 오른쪽으로 이동
					fireballSpeedY2 = (Math.random() > 0.5) ? fireballSpeed : -fireballSpeed; // 랜덤하게 위 또는 아래로 이동
				}

				// 세 번째 화염구 생성(시작 후 8초 뒤)
				if ((count - 1) % (144 * 15) == (144 * 8)) {
					// 랜덤한 위치에서 화염구 생성
					int randomX3 = (int) (Math.random()
							* (Main.mapEndX - Main.mapStartX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
							+ (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
					int randomY3 = (int) (Math.random()
							* (Main.mapEndY - Main.mapStartY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
							+ (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
					main.Fireball3.setBounds(randomX3, randomY3, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
					main.Fireball3.setVisible(true);

					// 화염구 이동 방향 설정
					fireballSpeedX3 = (Math.random() > 0.5) ? fireballSpeed : -fireballSpeed; // 랜덤하게 왼쪽 또는 오른쪽으로 이동
					fireballSpeedY3 = (Math.random() > 0.5) ? fireballSpeed : -fireballSpeed; // 랜덤하게 위 또는 아래로 이동
				}

				// 네 번째 화염구 생성(시작 후 12초 뒤)
				if ((count - 1) % (144 * 15) == (144 * 12)) {
					// 랜덤한 위치에서 화염구 생성
					int randomX4 = (int) (Math.random()
							* (Main.mapEndX - Main.mapStartX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
							+ (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
					int randomY4 = (int) (Math.random()
							* (Main.mapEndY - Main.mapStartY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
							+ (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
					main.Fireball4.setBounds(randomX4, randomY4, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
					main.Fireball4.setVisible(true);

					// 화염구 이동 방향 설정
					fireballSpeedX4 = (Math.random() > 0.5) ? fireballSpeed : -fireballSpeed; // 랜덤하게 왼쪽 또는 오른쪽으로 이동
					fireballSpeedY4 = (Math.random() > 0.5) ? fireballSpeed : -fireballSpeed; // 랜덤하게 위 또는 아래로 이동
				}

				// 화염구 이동
				main.Fireball1.setLocation(main.Fireball1.getX() + fireballSpeedX1,
						main.Fireball1.getY() + fireballSpeedY1);
				main.Fireball2.setLocation(main.Fireball2.getX() + fireballSpeedX2,
						main.Fireball2.getY() + fireballSpeedY2);
				main.Fireball3.setLocation(main.Fireball3.getX() + fireballSpeedX3,
						main.Fireball3.getY() + fireballSpeedY3);
				main.Fireball4.setLocation(main.Fireball4.getX() + fireballSpeedX4,
						main.Fireball4.getY() + fireballSpeedY4);

				// 화염구에 닿으면 벽에 튕겨나오도록 처리
				if (main.Fireball1.getX() <= Main.mapStartX
						|| main.Fireball1.getX() >= Main.mapEndX - main.Fireball1.getWidth()) {
					fireballSpeedX1 *= -1; // x 방향 반전
				}
				if (main.Fireball1.getY() <= Main.mapStartY
						|| main.Fireball1.getY() >= Main.mapEndY - main.Fireball1.getHeight()) {
					fireballSpeedY1 *= -1; // y 방향 반전
				}
				if (main.Fireball2.getX() <= Main.mapStartX
						|| main.Fireball2.getX() >= Main.mapEndX - main.Fireball2.getWidth()) {
					fireballSpeedX2 *= -1; // x 방향 반전
				}
				if (main.Fireball2.getY() <= Main.mapStartY
						|| main.Fireball2.getY() >= Main.mapEndY - main.Fireball2.getHeight()) {
					fireballSpeedY2 *= -1; // y 방향 반전
				}
				if (main.Fireball3.getX() <= Main.mapStartX
						|| main.Fireball3.getX() >= Main.mapEndX - main.Fireball3.getWidth()) {
					fireballSpeedX3 *= -1; // x 방향 반전
				}
				if (main.Fireball3.getY() <= Main.mapStartY
						|| main.Fireball3.getY() >= Main.mapEndY - main.Fireball3.getHeight()) {
					fireballSpeedY3 *= -1; // y 방향 반전
				}
				if (main.Fireball4.getX() <= Main.mapStartX
						|| main.Fireball4.getX() >= Main.mapEndX - main.Fireball4.getWidth()) {
					fireballSpeedX4 *= -1; // x 방향 반전
				}
				if (main.Fireball4.getY() <= Main.mapStartY
						|| main.Fireball4.getY() >= Main.mapEndY - main.Fireball4.getHeight()) {
					fireballSpeedY4 *= -1; // y 방향 반전
				}

				// 화염구에 닿으면 데미지
				if ((count - 1) % (144 * 15) >= (144 * 0.5) && (count - 1) % (144 * 15) <= (144 * 14)) {
					if (Main.locX >= main.Fireball1.getX() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
							&& Main.locX <= main.Fireball1.getX() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
									+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2)
							&& Main.locY >= main.Fireball1.getY() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
							&& Main.locY <= main.Fireball1.getY() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
									+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2)) {
						if (!Main.healthGuard) {
							Main.health -= 10;
							Music scream = new Music("scream.wav");
							scream.musicStart();
						}
					}
					if ((count - 1) % (144 * 15) >= (144 * 4.0) && (count - 1) % (144 * 15) <= (144 * 14)) {
						if (Main.locX >= main.Fireball2.getX() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
								&& Main.locX <= main.Fireball2.getX() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2)
								&& Main.locY >= main.Fireball2.getY() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
								&& Main.locY <= main.Fireball2.getY() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 10;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					if ((count - 1) % (144 * 15) >= (144 * 8.0) && (count - 1) % (144 * 15) <= (144 * 14)) {
						if (Main.locX >= main.Fireball3.getX() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
								&& Main.locX <= main.Fireball3.getX() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2)
								&& Main.locY >= main.Fireball3.getY() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
								&& Main.locY <= main.Fireball3.getY() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 10;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
					if ((count - 1) % (144 * 15) >= (144 * 12.0) && (count - 1) % (144 * 15) <= (144 * 14)) {
						if (Main.locX >= main.Fireball4.getX() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
								&& Main.locX <= main.Fireball4.getX() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2)
								&& Main.locY >= main.Fireball4.getY() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
								&& Main.locY <= main.Fireball4.getY() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
										+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2)) {
							if (!Main.healthGuard) {
								Main.health -= 10;
								Music scream = new Music("scream.wav");
								scream.musicStart();
							}
						}
					}
				}

				// 화염구 삭제
				if ((count - 1) % (144 * 15) == (144 * 14)) {
					main.Fireball1.setVisible(false);
					main.Fireball2.setVisible(false);
					main.Fireball3.setVisible(false);
					main.Fireball4.setVisible(false);
				}
				break;

			case 4: // 다섯 번째 패턴
				// 시작 좌우 공간 넓히기 및 계단 생성 (0초 ~ 0.5초 동안)
				if ((count - 1) % (144 * 15) >= 0 && (count - 1) % (144 * 15) < (144 * 0.5)) {
					if ((count - 1) % (144 * 15) == (144 * 0.5) - 1) {
						main.Leftstair1.setVisible(true);
						main.Leftstair2.setVisible(true);
						main.Rightstair1.setVisible(true);
						main.Rightstair2.setVisible(true);
						main.Centerstair1.setVisible(true);
					}
					Main.mapStartX -= Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapEndX += Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapStartY -= Main.PROGRAM_SCREEN_WIDTH * 0.003;
				}

				// 마무리 좌우 공간 좁히기 및 계단 생성 (14.5초 ~ 15초 동안)
				if ((count - 1) % (144 * 15) >= (144 * 14.5) && (count - 1) % (144 * 15) < (144 * 15)) {
					if ((count - 1) % (144 * 15) == (144 * 14.5)) {
						main.Leftstair1.setVisible(false);
						main.Leftstair2.setVisible(false);
						main.Rightstair1.setVisible(false);
						main.Rightstair2.setVisible(false);
						main.Centerstair1.setVisible(false);
					}
					Main.mapStartX += Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapEndX -= Main.PROGRAM_SCREEN_WIDTH * 0.002;
					Main.mapStartY += Main.PROGRAM_SCREEN_WIDTH * 0.003;
				}

				// 계단 밟을 수 있게 데이터 처리
				if ((count - 1) % (144 * 15) >= (144 * 0.5) && (count - 1) % (144 * 15) < (144 * 14.5)) {
					if (Main.locX > (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15) // locX
							&& Main.locX < (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)
									+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17)) {
						if (Main.speedY > Main.gravity) {
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
						}
					}
					if (Main.locX > (int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.43)) // locX
							- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15))
							&& Main.locX < (int) (Main.PROGRAM_SCREEN_WIDTH
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.41))) {
						if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
								- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
								- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
								&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
										+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
							Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
							Main.speedY = 0;
							Main.jumping = false;
						}
					}
					if (Main.locX > (int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17)) // locX
							- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15))
							&& Main.locX < (int) (Main.PROGRAM_SCREEN_WIDTH
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15))) {
						if (Main.speedY > Main.gravity) {
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
							if (Main.locY >= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
									- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
									- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
									&& Main.locY <= (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
											- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1
											+ (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03)) {
								Main.locY = (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
								Main.speedY = 0;
								Main.jumping = false;
							}
						}
					}
				}

				// 랜덤한 위치에서 아이템 생성
				int randomX1 = (int) (Math.random()
						* (Main.mapEndX - Main.mapStartX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
						+ (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
				int randomY1 = (int) (Math.random()
						* (Main.mapEndY - Main.mapStartY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
						+ (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
				int randomX2 = (int) (Math.random()
						* (Main.mapEndX - Main.mapStartX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
						+ (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
				int randomY2 = (int) (Math.random()
						* (Main.mapEndY - Main.mapStartY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
						+ (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);

				// 첫 번째 아이템 랜덤 생성(1초 생성 후 2.5초까지 유지)
				if ((count - 1) % (144 * 15) == (144 * 1)) {
					if (invincibleFirst) {
						// 무적 아이템 먼저 생성
						main.checkInvincibility = true;
						main.invincible.setBounds(randomX1, randomY1, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
						main.invincible.setVisible(true);
					} else {
						// 이속 증가 아이템 먼저 생성
						main.checkSpeedUp = true;
						main.Ghost.setBounds(randomX1, randomY1, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
						main.Ghost.setVisible(true);
					}
				}

				// 무적 아이템 삭제 (생성 2초 후)
				if ((count - 1) % (144 * 15) == (144 * 3)) {
					main.checkInvincibility = false;
					main.invincible.setVisible(false);
				}

				// 이속 증가 아이템 삭제 (생성 2초 후)
				if ((count - 1) % (144 * 15) == (144 * 3)) {
					main.checkSpeedUp = false;
					main.Ghost.setVisible(false);
				}

				// 이속 증가 아이템 생성 (8초에 생성)
				if ((count - 1) % (144 * 15) == (144 * 8)) {
					if (invincibleFirst) {
						main.checkSpeedUp = true;
						main.Ghost.setBounds(randomX2, randomY2, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
						main.Ghost.setVisible(true);
					} else {
						main.checkInvincibility = true;
						main.invincible.setBounds(randomX2, randomY2, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
						main.invincible.setVisible(true);
					}
				}

				// 무적 아이템 삭제 (생성 2초 후)
				if ((count - 1) % (144 * 15) == (144 * 10)) {
					main.checkInvincibility = false;
					main.invincible.setVisible(false);
				}

				// 이속 증가 아이템 삭제 (생성 2초 후)
				if ((count - 1) % (144 * 15) == (144 * 10)) {
					main.checkSpeedUp = false;
					main.Ghost.setVisible(false);
				}

				// 플레이어가 현재 무적 상태인지 확인
				if (isPlayerInvincible) {
					main.invinciblePlayer.setVisible(true);
					// 무적 타이머 감소
					invincibilityTimer--;

					// 무적 지속 시간이 종료되었는지 확인
					if (invincibilityTimer <= 0) {
						main.invinciblePlayer.setVisible(false);
						isPlayerInvincible = false;
					}
				}

				// 무적 아이템과 충돌 여부를 확인하고 무적 상태를 활성화
				if (main.checkInvincibility) {
					if (!isPlayerInvincible
							&& Main.locX >= main.invincible.getX() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
							&& Main.locX <= main.invincible.getX() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
									+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06) / 2
							&& Main.locY >= main.invincible.getY() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
							&& Main.locY <= main.invincible.getY() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)) {
						isPlayerInvincible = true;
						invincibilityTimer = 3 * 144;
						main.invincible.setVisible(false);
					}
				}

				if (isPlayerSpeedup) {
					main.SpeedUpPlayer.setVisible(true);
					if (!Main.coolDown) {
						if (Main.moveLeft) {
							Main.locX -= (speedX + (PROGRAM_SCREEN_WIDTH * 0.001));
						}
						if (Main.moveRight) {
							Main.locX += (speedX + (PROGRAM_SCREEN_WIDTH * 0.001));
						}
					}
					SpeedupTimer--;

					if (SpeedupTimer <= 0) {
						main.SpeedUpPlayer.setVisible(false);
						isPlayerSpeedup = false;
					}
				}

				// 이속 증가 활성화
				if (main.checkSpeedUp) {
					if (!isPlayerSpeedup
							&& Main.locX >= main.Ghost.getX() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
							&& Main.locX <= main.Ghost.getX() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
									+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2)
							&& Main.locY >= main.Ghost.getY() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)
							&& Main.locY <= main.Ghost.getY() + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)) {
						isPlayerSpeedup = true;
						SpeedupTimer = 3 * 144;
						main.Ghost.setVisible(false);
					}
				}

				// 플레이어를 추적하는 몬스터 이동 및 충돌 확인
				if ((count - 1) % (144 * 15) >= (144 * 2) && (count - 1) % (144 * 15) <= (144 * 14.5)) {
					for (int i = 0; i < 5; i++) {
						main.monsterArray[i].setVisible(true);
						if ((count - 1) % (144 * 15) == 144 * 2) {
							main.monstermove = false;
							// 몬스터의 초기 위치 설정
							main.monsterArrayX[i] = main.monsterArray[i].getX() + monsterspeed;
							main.monsterArrayY[i] = main.monsterArray[i].getY() + monsterspeed;
						}
						// 몬스터 간의 거리를 확인하여 겹치지 않도록 조절
						for (int j = 0; j < 5; j++) {
							if (i != j) {
								double distance = Math.sqrt(Math.pow(main.monsterArrayX[i] - main.monsterArrayX[j], 2)
										+ Math.pow(main.monsterArrayY[i] - main.monsterArrayY[j], 2));
								double minDistance = Main.PROGRAM_SCREEN_WIDTH * 0.06;

								if (distance < minDistance) {
									double angle = Math.atan2(main.monsterArrayY[i] - main.monsterArrayY[j],
											main.monsterArrayX[i] - main.monsterArrayX[j]);
									double newX = main.monsterArrayX[j] + minDistance * Math.cos(angle);
									double newY = main.monsterArrayY[j] + minDistance * Math.sin(angle);

									main.monsterArrayX[i] = (int) newX;
									main.monsterArrayY[i] = (int) newY;
								}
							}
						}
						// X축 추적
						if (Main.locX > main.monsterArrayX[i]) {
							if ((Main.locX - main.monsterArrayX[i] > Main.PROGRAM_SCREEN_WIDTH * 0.0025))
								main.monsterArrayX[i] += Main.PROGRAM_SCREEN_WIDTH * 0.0015;
						} else if (Main.locX < main.monsterArrayX[i]) {
							if ((main.monsterArrayX[i] - Main.locX > Main.PROGRAM_SCREEN_WIDTH * 0.0025))
								main.monsterArrayX[i] -= Main.PROGRAM_SCREEN_WIDTH * 0.0015;
						}

						// Y축 추적
						if (Main.locY > main.monsterArrayY[i]) {
							if ((Main.locY - main.monsterArrayY[i]) > Main.PROGRAM_SCREEN_WIDTH * 0.0025)
								main.monsterArrayY[i] += Main.PROGRAM_SCREEN_WIDTH * 0.0025;
						} else if (Main.locY < main.monsterArrayY[i]) {
							if ((main.monsterArrayY[i] - Main.locY > Main.PROGRAM_SCREEN_WIDTH * 0.0025))
								main.monsterArrayY[i] -= Main.PROGRAM_SCREEN_WIDTH * 0.0025;
						}
						// ---- 추적 마무리
						main.monsterArray[i].setBounds((int) main.monsterArrayX[i], (int) main.monsterArrayY[i],
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.04), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06));
						if ((count - 1) % (144 * 15) == (144 * 14.5)) {
							main.monstermove = true;
						}

						// 플레이어와 몬스터의 충돌 확인
						if (!isPlayerInvincible) {
							if (Main.locX >= main.monsterArray[i].getX() - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03 / 2)
									&& Main.locX <= main.monsterArray[i].getX()
											+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03 / 2)
											+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.04)
									&& Main.locY >= main.monsterArray[i].getY()
											- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03 / 2)
									&& Main.locY <= main.monsterArray[i].getY()
											+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03 / 2)
											+ (int) (Main.PROGRAM_SCREEN_WIDTH * 0.06)) {
								if (!Main.healthGuard) {
									Main.health -= 10;
									Music scream = new Music("scream.wav");
									scream.musicStart();
								}
							}
						}

						// 몬스터 비활성화
						if ((count - 1) % (144 * 15) == (144 * 14.5)) {
							main.monsterArray[i].setVisible(false);
							main.monstermove = true;
						}
					}
				}

				break;

			default: // 오류 발생
				System.out.println("Error(attackNumber) 발생! 프로그램 강제 종료 ...");
				System.exit(0);
			}

			// 맵 그래픽 최신화 (상하좌우)
			main.map1.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
					(int) mapStartY - ((int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
					(int) mapEndX - (int) mapStartX + (2 * (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
					(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025));
			main.map2.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025), (int) mapEndY,
					(int) mapEndX - (int) mapStartX + (2 * (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
					(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025));
			main.map3.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025), (int) mapStartY,
					(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025), (int) mapEndY - (int) mapStartY);
			main.map4.setBounds((int) mapEndX, (int) mapStartY, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
					(int) mapEndY - (int) mapStartY);

			// 플레이어 맵 밖 탈출 방지
			if (Main.locX <= (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2))
				Main.locX = (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) + 1;
			if (Main.locX >= (int) Main.mapEndX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2))
				Main.locX = (int) Main.mapEndX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
			if (Main.locY <= (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2)) { // 플레이어가 맵 천장에 머리를
																										// 박을 때
				Main.locY = (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) + 1;
				Main.speedY = 0;
			}
			if (Main.locY >= (int) Main.mapEndY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1) { // 플레이어가 맵 바닥에 닿을
																										// 때
				Main.locY = (int) Main.mapEndY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2) - 1;
				Main.speedY = 0;
				Main.jumping = false;
			}

			// 시간 최신화
			main.Timer.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.77), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.02),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.2), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.09));
			if (((int) (count / 144) % 60) < 10 && ((int) (count / 144) / 60) < 10) {
				main.Timer.setText("0" + ((int) (count / 144) / 60) + ":" + "0" + ((int) (count / 144) % 60));
			} else if (((int) (count / 144) % 60) >= 10 && ((int) (count / 144) / 60) < 10) {
				main.Timer.setText("0" + ((int) (count / 144) / 60) + ":" + ((int) (count / 144) % 60));
			} else if (((int) (count / 144) % 60) < 10 && ((int) (count / 144) / 60) >= 10) {
				main.Timer.setText(((int) (count / 144) / 60) + ":" + "0" + ((int) (count / 144) % 60));
			} else
				main.Timer.setText(((int) (count / 144) / 60) + ":" + ((int) (count / 144) % 60));

			// 플레이어 이미지 위치 최신화
			main.playerImage.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
					(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));

			main.playerDamageImage.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
					(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));

			main.invinciblePlayer.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
					(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));

			main.SpeedUpPlayer.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
					(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2),
					(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));

			if (cooldownInteger % ((int) (main.currentFrameRate * 0.2) + 1) == 0) { // 약 0.2초마다 대쉬 가능
				coolDown = false;
				cooldownInteger = 1;
				Main.leftDashing = false;
				Main.rightDashing = false;

				// 좌로 대쉬 이펙트 제거
				Main.playerDashImage.setVisible(false);
				// 우로 대쉬 이펙트 제거
				Main.playerDashImage2.setVisible(false);
			}

			if (Main.health != (int) Main.healthUpdate) {
				Main.healthGuard = true;
			} else {
				Main.healthGuard = false;
			}

			// 체력바 업데이트
			if (Main.healthUpdate != Main.health) {
				// 체력이 플러스가 될 경우 보정
				if (Main.healthUpdate < Main.health) {
					if (Main.healthUpdate + 1 >= Main.health) {
						Main.healthUpdate = Main.health;
					}
				}
				// 체력이 마이너스가 될 경우 보정
				if (Main.healthUpdate > Main.health) {
					if (Main.healthUpdate - 1 <= Main.health) {
						Main.healthUpdate = Main.health;
					}
				}
				Main.healthUpdate -= (Main.healthUpdate - Main.health) * 0.05;
			}

			// 체력바 UI 업데이트
			main.healthBar.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.25),
					(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03),
					(int) (((int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) * (healthUpdate / 100))),
					(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.065));

			// 대쉬 쿨타임
			if (cooldownInteger < 289 && coolDown == true) {
				cooldownInteger++; // 쿨 다운 진행
			}

			// 보스가 위 아래로 움직이도록
			if (main.bossCheck) {
				if (tempBoss == 144)
					BossUpDownCheck = true;
				else if (tempBoss == -144)
					BossUpDownCheck = false;
				if (BossUpDownCheck) {
					tempBoss -= 1;
				} else {
					tempBoss += 1;
				}
				if ((count - 1) % 2 == 0) {
					main.boss.setBounds(
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1),
							(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.11)
									+ (int) ((Main.PROGRAM_SCREEN_WIDTH * 0.00007) * tempBoss),
							(int) (Main.PROGRAM_SCREEN_WIDTH * 0.2), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.2));
				}
			}

			// 반복문 마무리
			long afterTime = System.currentTimeMillis(); // 반복문 돈 후 시간 구함
			main.currentFrameRate = main.delay(beforeTime, afterTime); // 딜레이 후 현재 프레임 속도 기록
			if (main.currentFrameRate <= 0) { // 프레임이 0 이하로 떨어지면 프로그램 강제 종료
				System.out.println("Frame rate가 0 이하로 떨어져 프로그램을 강제 종료합니다.");
				System.exit(0);
			}

			if (count % ((main.currentFrameRate * 1) + 1) == 0) { // 약 1초마다 실행
				System.out.println("Frame Rate: " + main.currentFrameRate + "FPS"); // 현재 프레임 속도 출력 (확인용)
				System.out.println("User x : " + Main.locX + ", y : " + Main.locY);
				System.out.println("Map Sx, Ex, Sy, Ey : " + (int) Main.mapStartX + ", " + (int) Main.mapEndX + ", "
						+ (int) Main.mapStartY + ", " + (int) Main.mapEndY);
				System.out.println("User health : " + Main.health);
				System.out.println("AttackNumber : " + Main.attackNumber);
				System.out.println("count : " + count);
				System.out.println("프로그램이 시작된지 " + (int) (((int) (count / 144)) / 60) + "분 "
						+ ((int) (count / 144)) % 60 + "초" + "\n");
			}
			count++;

			// 사망 처리
			if (Main.healthUpdate <= 0) {
				System.out.println("사망");
				Music gameover = new Music("gameover.wav");
				gameover.musicStart();
				Main.gameMusic.musicStop();
				Main.gameStatus = false;

				main.restartButton.setVisible(false);
				main.pauseButton.setVisible(false);

				// ================================================================

				int tempRank = 6;

				// 점수 비교 연산
				for (int i = 0; i < 5; i++) {
					if (count > main.Score[i]) {
						tempRank = i + 1;
						for (int j = 4; j > i && j > 0; j--) {
							main.Score[j] = main.Score[j - 1];
						}
						main.Score[i] = count;
						break;
					}
				}

				// 파일 저장하기 코드 // 체력이 0이 되어 게임이 끝났을 시
				try {
					String path = System.getProperty("user.dir");
					String filePath = path + "/src/JavaPoint.txt";
					FileWriter fileWriter = new FileWriter(filePath);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					for (int i = 0; i < 5; i++) {
						bufferedWriter.write(Integer.toString(main.Score[i]));
						bufferedWriter.newLine();
					}
					bufferedWriter.close();
					System.out.println("JavaPoint.txt 파일을 성공적으로 저장하였습니다.");
				} catch (IOException e) {
					System.out.println("JavaPoint.txt 파일을 열 수 없습니다.");
					e.printStackTrace();
				}
				// ================================================================
				if (((int) (main.Score[0] / 144) % 60) < 10 && ((int) (main.Score[0] / 144) / 60) < 10) {

					main.FirstScore.setText("1등 0" + ((int) (main.Score[0] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[0] / 144) % 60));

				} else if (((int) (main.Score[0] / 144) % 60) >= 10 && ((int) (main.Score[0] / 144) / 60) < 10) {

					main.FirstScore.setText(
							"1등 0" + ((int) (main.Score[0] / 144) / 60) + ":" + ((int) (main.Score[0] / 144) % 60));

				} else if (((int) (main.Score[0] / 144) % 60) < 10 && ((int) (main.Score[0] / 144) / 60) >= 10) {

					main.FirstScore.setText("1등 " + ((int) (main.Score[0] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[0] / 144) % 60));

				} else

					main.FirstScore.setText(
							"1등 " + ((int) (main.Score[0] / 144) / 60) + ":" + ((int) (main.Score[0] / 144) % 60));

				if (((int) (main.Score[1] / 144) % 60) < 10 && ((int) (main.Score[1] / 144) / 60) < 10) {

					main.SecondScore.setText("2등 0" + ((int) (main.Score[1] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[1] / 144) % 60));

				} else if (((int) (main.Score[1] / 144) % 60) >= 10 && ((int) (main.Score[1] / 144) / 60) < 10) {

					main.SecondScore.setText(
							"2등 0" + ((int) (main.Score[1] / 144) / 60) + ":" + ((int) (main.Score[1] / 144) % 60));

				} else if (((int) (main.Score[1] / 144) % 60) < 10 && ((int) (main.Score[1] / 144) / 60) >= 10) {

					main.SecondScore.setText("2등 " + ((int) (main.Score[1] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[1] / 144) % 60));

				} else

					main.SecondScore.setText(
							"2등 " + ((int) (main.Score[1] / 144) / 60) + ":" + ((int) (main.Score[1] / 144) % 60));

				if (((int) (main.Score[2] / 144) % 60) < 10 && ((int) (main.Score[2] / 144) / 60) < 10) {

					main.ThirdScore.setText("3등 0" + ((int) (main.Score[2] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[2] / 144) % 60));

				} else if (((int) (main.Score[2] / 144) % 60) >= 10 && ((int) (main.Score[2] / 144) / 60) < 10) {

					main.ThirdScore.setText(
							"3등 0" + ((int) (main.Score[2] / 144) / 60) + ":" + ((int) (main.Score[2] / 144) % 60));

				} else if (((int) (main.Score[2] / 144) % 60) < 10 && ((int) (main.Score[2] / 144) / 60) >= 10) {

					main.ThirdScore.setText("3등 " + ((int) (main.Score[2] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[2] / 144) % 60));

				} else

					main.ThirdScore.setText(
							"3등 " + ((int) (main.Score[2] / 144) / 60) + ":" + ((int) (main.Score[2] / 144) % 60));

				if (((int) (main.Score[3] / 144) % 60) < 10 && ((int) (main.Score[3] / 144) / 60) < 10) {

					main.FourthScore.setText("4등 0" + ((int) (main.Score[3] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[3] / 144) % 60));

				} else if (((int) (main.Score[3] / 144) % 60) >= 10 && ((int) (main.Score[3] / 144) / 60) < 10) {

					main.FourthScore.setText(
							"4등 0" + ((int) (main.Score[3] / 144) / 60) + ":" + ((int) (main.Score[3] / 144) % 60));

				} else if (((int) (main.Score[3] / 144) % 60) < 10 && ((int) (main.Score[3] / 144) / 60) >= 10) {

					main.FourthScore.setText("4등 " + ((int) (main.Score[3] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[3] / 144) % 60));

				} else

					main.FourthScore.setText(
							"4등 " + ((int) (main.Score[3] / 144) / 60) + ":" + ((int) (main.Score[3] / 144) % 60));

				if (((int) (main.Score[4] / 144) % 60) < 10 && ((int) (main.Score[4] / 144) / 60) < 10) {

					main.FifthScore.setText("5등 0" + ((int) (main.Score[4] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[4] / 144) % 60));

				} else if (((int) (main.Score[4] / 144) % 60) >= 10 && ((int) (main.Score[4] / 144) / 60) < 10) {

					main.FifthScore.setText(
							"5등 0" + ((int) (main.Score[4] / 144) / 60) + ":" + ((int) (main.Score[4] / 144) % 60));

				} else if (((int) (main.Score[4] / 144) % 60) < 10 && ((int) (main.Score[4] / 144) / 60) >= 10) {

					main.FifthScore.setText("5등 " + ((int) (main.Score[4] / 144) / 60) + ":" + "0"
							+ ((int) (main.Score[4] / 144) % 60));

				} else

					main.FifthScore.setText(
							"5등 " + ((int) (main.Score[4] / 144) / 60) + ":" + ((int) (main.Score[4] / 144) % 60));
				// ================================================================
				main.resultScore.setBounds(
						(int) (Main.PROGRAM_SCREEN_WIDTH / 2) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.4 / 2),
						(int) (Main.PROGRAM_SCREEN_HEIGHT / 2) - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.09 / 2),
						(int) (Main.PROGRAM_SCREEN_WIDTH * 0.4), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.09));

				String tempString = "랭크 : " + tempRank + " / ";
				if (tempRank > 5) {
					tempString = "랭크 : 없음 / ";
				}

				if (((int) (count / 144) % 60) < 10 && ((int) (count / 144) / 60) < 10) {
					main.resultScore.setText(
							tempString + "점수 : " + "0" + ((int) (count / 144) / 60) + ":" + "0" + ((int) (count / 144) % 60));
				} else if (((int) (count / 144) % 60) >= 10 && ((int) (count / 144) / 60) < 10) {
					main.resultScore
							.setText(tempString + "점수 : " + "0" + ((int) (count / 144) / 60) + ":" + ((int) (count / 144) % 60));
				} else if (((int) (count / 144) % 60) < 10 && ((int) (count / 144) / 60) >= 10) {
					main.resultScore
							.setText(tempString + "점수 : " + ((int) (count / 144) / 60) + ":" + "0" + ((int) (count / 144) % 60));
				} else
					main.resultScore.setText(tempString + "점수 : " + ((int) (count / 144) / 60) + ":" + ((int) (count / 144) % 60));
				main.resultScore.setVisible(true);
			}

			while (Main.gamePause) {
				main.currentFrameRate = main.delay(0, 0);
			}
			// 홈화면으로 돌아가기
			if (!Main.gameStatus) {
				while (true) {
					if (!Main.gameStatus) {
						main.currentFrameRate = main.delay(0, 0);
					} else {
						main.resultScore.setVisible(false);
						Main.gameMusic = new Music("gameMusic.wav");
						Main.gameMusic.musicSetCount(0);
						Main.gameMusic.musicStart();
						Main.mainMusic.musicStop();
						count = 1;
						// 플레이어 체력
						health = 100;
						healthUpdate = 100.0;
						healthGuard = false;

						// 플레이어 위치
						locX = PROGRAM_SCREEN_WIDTH / 2;
						locY = PROGRAM_SCREEN_HEIGHT / 3 * 2;

						// 플레이어 x축 관련
						speedX = Main.PROGRAM_SCREEN_WIDTH * 0.004;
						dashSpeed = Main.PROGRAM_SCREEN_WIDTH * 0.008;

						// 플레이어 y축 이동 관련
						speedY = 0; // 플레이어 Y축 속도
						accelerationY = Main.PROGRAM_SCREEN_HEIGHT * 0.018; // 플레이어 점프 가속도
						gravity = Main.PROGRAM_SCREEN_HEIGHT * 0.00075; // 맵 안 중력

						// 공격 패턴 선정
						attackNumber = 100;

						// 맵 설정
						mapStartX = (PROGRAM_SCREEN_WIDTH * 0.2);
						mapEndX = PROGRAM_SCREEN_WIDTH - (PROGRAM_SCREEN_WIDTH * 0.2);
						mapStartY = (PROGRAM_SCREEN_WIDTH * 0.3);
						mapEndY = ((PROGRAM_SCREEN_HEIGHT / 10) * 9); // 처음 맵 바닥 높이

						// 게임 상태 관련
						gamePause = false; // 게임 잠시 멈춤 상태

						// 게임 패턴1 전용 변수들
						choiceNumber = 0;
						randomChoiceNumber = 0;
						countLeft = 0;
						countRight = 0;

						main.attackDamage1StartY = 0;
						main.attackDamage1EndY = 0;

						main.attackDamage2StartX = 0;
						main.attackDamage2EndX = 0;

						main.attackDamage3StartY = 0;
						main.attackDamage3EndY = 0;

						main.attackDamage4StartX = 0;
						main.attackDamage4EndX = 0;

						main.attackDamage5StartY = 0;
						main.attackDamage5EndY = 0;

						main.attackDamage6StartX = 0;
						main.attackDamage6EndX = 0;

						main.attackDamage7StartY = 0;
						main.attackDamage7EndY = 0;

						main.monster1X = 0;

						main.monster2X = 0;
						main.monster2Y = 0;

						main.bossX = 0;
						main.bossY = 0;
						main.bossCheck = true;

						// 게임 패턴4 전용 변수들
						fireballSpeed = 2;
						fireballSpeedX1 = 0;
						fireballSpeedY1 = 0;
						fireballSpeedX2 = 0;
						fireballSpeedY2 = 0;
						fireballSpeedX3 = 0;
						fireballSpeedY3 = 0;
						fireballSpeedX4 = 0;
						fireballSpeedY4 = 0;

						// 게임 패턴6 전용 변수들
						isPlayerInvincible = false;
						invincibilityTimer = 0;
						isPlayerSpeedup = false;
						SpeedupTimer = 0;
						main.monstermove = true;
						monsterspeed = 1;
						main.checkInvincibility = false;
						main.checkSpeedUp = false;

						// 보스 관련 변수
						tempBoss = 0;
						BossUpDownCheck = false;

						main.playerDamageImage.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
						main.playerDamageImage.setVisible(false);

						// 무적상태 플레이어 아이콘
						main.invinciblePlayer.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
						main.invinciblePlayer.setVisible(false);

						// 이속 증가 상태 플레이어 아이콘
						main.SpeedUpPlayer.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));
						main.SpeedUpPlayer.setVisible(false);

						// 게임시작 패널에 추가되는 플레이어 아이콘
						main.playerImage.setBounds((int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.030 / 2),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.030 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.030), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.030));

						// 게임시작 패널에 추가되는 플레이어 좌로 대쉬 아이콘
						playerDashImage.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.050 / 2),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.045 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.075), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.045));
						playerDashImage.setVisible(false);

						// 게임시작 패널에 추가되는 플레이어 우로 대쉬 아이콘
						playerDashImage2.setBounds(
								(int) (Main.locX - Main.PROGRAM_SCREEN_WIDTH * 0.050 / 2
										- Main.PROGRAM_SCREEN_WIDTH * 0.075),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.045 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.075), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.045));
						playerDashImage2.setVisible(false);

						// 레드바 구현
						main.damageField1.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
								(int) (Main.PROGRAM_SCREEN_HEIGHT));
						main.damageField1.setVisible(false);

						main.damageField2.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
								(int) (Main.PROGRAM_SCREEN_HEIGHT));
						main.damageField2.setVisible(false);

						// 보스 구현
						main.boss.setBounds(
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.11), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.2));
						main.boss.setVisible(true);

						// 보스 공격 패턴 2 떨어지는 몬스터
						main.monster1.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.465),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
						main.monster1.setVisible(false);

						main.monster2.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.465),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.18), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.07),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.07));
						main.monster2.setVisible(false);

						// 보스 공격 패턴 2 공격 필드 생성 전
						main.attackBeforeWidth.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
						main.attackBeforeWidth.setVisible(false);

						main.attackBeforeHeight.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
								(int) (Main.PROGRAM_SCREEN_HEIGHT));
						main.attackBeforeHeight.setVisible(false);

						// 보스 공격 패턴 2 공격 필드 생성 후
						main.attackDamage1.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
						main.attackDamage1.setVisible(false);

						main.attackDamage2.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
								(int) (Main.PROGRAM_SCREEN_HEIGHT));
						main.attackDamage2.setVisible(false);

						main.attackDamage3.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
						main.attackDamage3.setVisible(false);

						main.attackDamage4.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
								(int) (Main.PROGRAM_SCREEN_HEIGHT));
						main.attackDamage4.setVisible(false);

						main.attackDamage5.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
						main.attackDamage5.setVisible(false);

						main.attackDamage6.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.03),
								(int) (Main.PROGRAM_SCREEN_HEIGHT));
						main.attackDamage6.setVisible(false);

						main.attackDamage7.setBounds(0, 0, (int) (Main.PROGRAM_SCREEN_WIDTH),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.05));
						main.attackDamage7.setVisible(false);

						// 보스 화염구 1
						main.Fireball1.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
						main.Fireball1.setVisible(false);

						// 보스 화염구 2
						main.Fireball2.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
						main.Fireball2.setVisible(false);

						// 보스 화염구 3
						main.Fireball3.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
						main.Fireball3.setVisible(false);

						// 보스 화염구 4
						main.Fireball4.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.1 / 2));
						main.Fireball4.setVisible(false);

						// 무적 아이템
						main.invincible.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
						main.invincible.setVisible(false);

						// 이속 증가 아이템
						main.Ghost.setBounds((int) (Main.locX + Main.PROGRAM_SCREEN_WIDTH * 0.5),
								(int) (Main.locY - Main.PROGRAM_SCREEN_HEIGHT * 0.5),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06 / 2));
						main.Ghost.setVisible(false);

						// 플레이어 추적 몬스터
						for (int i = 0; i < 5; i++) {
							main.monsterArray[i].setVisible(false);
						}

						// 왼쪽 화살표
						main.arrowLeft.setBounds(
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.25), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
						main.arrowLeft.setVisible(false);

						// 오른쪽 화살표
						main.arrowRight.setBounds(
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.5) + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.25), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.05),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.05));
						main.arrowRight.setVisible(false);

						// 기본 맵 구현
						// map1 = 상
						// map2 = 하
						// map3 = 좌
						// map4 = 우
						main.map1.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
								(int) mapStartY - ((int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
								(int) mapEndX - (int) mapStartX + (2 * (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025));
						main.map1.setVisible(true);

						main.map2.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025), (int) mapEndY,
								(int) mapEndX - (int) mapStartX + (2 * (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025)),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025));
						main.map2.setVisible(true);

						main.map3.setBounds((int) mapStartX - (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
								(int) mapStartY, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
								(int) mapEndY - (int) mapStartY);
						main.map3.setVisible(true);

						main.map4.setBounds((int) mapEndX, (int) mapStartY, (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.025),
								(int) mapEndY - (int) mapStartY);
						main.map4.setVisible(true);

						// 계단 생성
						// 좌
						main.stairsLeft1.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.stairsLeft1.setVisible(false);

						main.stairsLeft2.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.stairsLeft2.setVisible(false);

						main.stairsLeft3.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.stairsLeft3.setVisible(false);
						// 우
						main.stairsRight1.setBounds(
								(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.stairsRight1.setVisible(false);

						main.stairsRight2.setBounds(
								(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.stairsRight2.setVisible(false);

						main.stairsRight3.setBounds(
								(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.stairsRight3.setVisible(false);

						// 패턴 1 전용 코드
						for (int i = 0; i < 15; i++) {
							main.seong_attack[i].setVisible(false);
						}
						for (int i = 0; i < 2; i++) {
							main.seong_attack2[i].setVisible(false);
						}

						// 패턴 6 전용 계단
						// 좌
						main.Leftstair1.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.Leftstair1.setVisible(false);

						main.Leftstair2.setBounds((int) (Main.PROGRAM_SCREEN_WIDTH * 0.15),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.Leftstair2.setVisible(false);
						// 우
						main.Rightstair1.setBounds(
								(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73), (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.Rightstair1.setVisible(false);

						main.Rightstair2.setBounds(
								(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.17))
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 2),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.Rightstair2.setVisible(false);

						// 중앙
						main.Centerstair1.setBounds(
								(int) ((Main.PROGRAM_SCREEN_WIDTH - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.43))
										- (int) (Main.PROGRAM_SCREEN_WIDTH * 0.15)),
								(int) (Main.PROGRAM_SCREEN_HEIGHT * 0.73)
										- (int) ((Main.PROGRAM_SCREEN_HEIGHT * 0.15) * 1),
								(int) (Main.PROGRAM_SCREEN_WIDTH * 0.17), (int) (Main.PROGRAM_SCREEN_HEIGHT * 0.03));
						main.Centerstair1.setVisible(false);

						timer = false;

						// 게임 패턴1 변수
						cooldownInteger = 1;
						seong_count = 1;
						arraw = 0;
						seong_wh = 0;

						// 게임 패턴6 변수
						// 랜덤으로 아이템 선택
						r = new Random();
						invincibleFirst = r.nextBoolean();
						// 5개의 몬스터를 랜덤한 위치에 생성
						for (int i = 0; i < 5; i++) {
							int randomX = (int) (Math.random()
									* (Main.mapEndX - Main.mapStartX - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
									+ (int) Main.mapStartX + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);
							int randomY = (int) (Math.random()
									* (Main.mapEndY - Main.mapStartY - (int) (Main.PROGRAM_SCREEN_WIDTH * 0.1)))
									+ (int) Main.mapStartY + (int) (Main.PROGRAM_SCREEN_WIDTH * 0.02);

							// 몬스터 초기 설정
							main.monstermove = false;
							main.monsterArray[i].setBounds(randomX, randomY, (int) (Main.PROGRAM_SCREEN_WIDTH * 0.04),
									(int) (Main.PROGRAM_SCREEN_WIDTH * 0.06));
						}
						break;
					}
				}
			}
			/* ----- 여기에 프레임마다 실행할 코드 작성(아래) ----- */
		}

		/* ------ 여기에 단일 횟수 실행 코드 작성(위) ----- */

		/* ------ 여기에 단일 횟수 실행 코드 작성(아래) ----- */
	}
}
