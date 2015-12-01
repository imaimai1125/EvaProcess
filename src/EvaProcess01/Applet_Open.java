package EvaProcess01;
/**
 *
 * @author imaimai
 */

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.TextField;
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.awt.image.RenderedImage;
//import javax.imageio.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import EvaProcess01.Cell.CellKind;
import EvaProcess01.Cell.ParticleKind;



public class Applet_Open extends Applet implements
				ActionListener,MouseListener, Runnable {

	private static final long serialVersionUID = 1L;
	/**
	 * @param args the command line arguments
	 */
	int step;
	int num_normal,num_haste,num_empty;
	Variable var;
	Rulecell rulecell;
	Random rnd;
	Thread runner;
	Button Start,Stop;
	String foldnameruslt;
	TextField Time;
	Label Charact;
	int y_0length = 200;
	int num_particles = 1;
	//粒子の充填率・急いでいる人たちの割合の初期化
	double empty = 0.0;
	double haste = 0.5;
	//ルール
	int sffrule = 1;
	int uprule = 3;
	/**************************************************************************
	 * 初期化メソッド!
	 ***************************************************************************/
	public void init(){
		/********************
		 *クラス初期化,実体化
		 ********************/
		//乱数初期化
		rnd = new Random();
		//変数初期化
		var = new Variable();
		rulecell = new Rulecell();
		//粒子初期配置
		rulecell.initialize(rnd, var,empty,haste,sffrule);
		//step数を初期化
		step = 0;
		//背景色の設定
		setBackground(Color.white);
		/***** テキストフィールドの作成TextField *****/
		Charact = new Label("急いでいる人 : "+num_haste /**ここをstepに応じて変えたい**/);
		Charact.setForeground(Color.black);
		add(Charact);
		Charact.setBounds(180,20,20,20);
		Charact = new Label("普通の人 : "+num_normal);
		Charact.setForeground(Color.black);
		add(Charact);

		//テキストフィールド（入力用）
		Charact = new Label("Time Interval");
		add(Charact);
		Time = new TextField();
		Time.setText(Integer.toString(var.time_interval));
		add(Time);
		Time.addActionListener(this);
		Time.addActionListener(this);
		/*********************
            ボタンの作成
		 *******************/
		Start = new Button("Start");
		add(Start);
		Start.setBounds(560,40,50,25);
		//ストップボタン
		Stop  = new Button("Stop");
		add(Stop);
		Stop.setBounds(620,40,50,25);
		//イベントリスナに関連付け
		Start.addActionListener(this);
		Stop.addActionListener(this);
		addMouseListener(this);
	}//初期化メソッド
	/*************************************************************************
	 * マウスの動きによる処理メソッド
	 * ***********************************************************************/
	public void mouseClicked(MouseEvent e){
		Point point = e.getPoint();
		int X = point.x/var.n;
		int Y = (point.y-(y_0length-10*10))/var.n;
		System.out.println("x:" + X + ",y:" + Y);
		System.out.println("セルの種類 : " + var.cell[Y][X].get_cellkind()
				+"		粒子の種類 : " + var.cell[Y][X].get_particlekind()
				+"		SFF値 : " + var.cell[Y][X].SFF
				+"		DFF値 : " + var.cell[Y][X].DFF);
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	/*************************************************************************
	 *ボタンに対するイベント処理メソッド
	 *************************************************************************/
	public void actionPerformed(ActionEvent ev1){
		//アニメーションスタート
		if(ev1.getSource() == Start){
			if(runner==null){
				runner = new Thread(this);
				runner.start();
			}
		}
		//アニメーションストップ
		if(ev1.getSource() == Stop){
			//ストップ時はstepは0に戻さずそのまま保持
			stop();
		}
		else if(ev1.getSource() == Time){
			var.time_interval = Integer.valueOf(Time.getText()).intValue();
		}
		//再描画
		repaint();
	}

	/************************************************************************
	 * 動作実行メソッド
	 ************************************************************************/
	public void run(){
		System.out.println("start");
		while(runner != null){
			//アップデート
			rulecell.update(uprule, rnd, var);
			step++;
//			System.out.println(num_haste+"	"+num_normal+"	"+num_particles);

			//ループを抜ける条件
			if(num_particles == 0){
				stop();
				System.out.println("Everyone can evacuate within "+ step);
				System.out.println(runner);
			}
			//再描画
			if(var.updaterule==1){
				//アニメーションの更新時間間隔
				repaint();
				try{Thread.sleep(var.time_interval);}
				catch(InterruptedException e){}
			}else {
				stop();
				System.out.println(step);
				step = 0;
				rnd = new Random();
				var = new Variable();
				//ネットワークを初期化
				rulecell = new Rulecell();
			}
		}
	}


	/*************************************************************************
	 * スタートメソッド
	 *************************************************************************/
	public void start(){
		runner = new Thread(this);
		runner.start();
	}
	/************************************************************************
	 * ストップメソッド
	 ***********************************************************************/
	public void stop(){
		runner = null;
	}
	/*************************************************************************
	 * 描画メソッド
	 *************************************************************************/
	public void paint(Graphics g){
		num_empty = 0;
		num_haste = 0;
		num_normal = 0;
		//ウィンドウサイズの取得
		Dimension size = getSize();
		int width = size.width;
		int height = size.height;
		var.n = Math.min(width,height)/Math.max(var.L,var.N)-2;
		//変数定義
		Image backGrp = null; //バッファ用の変数
		Graphics gbg;//実際に描画するときのGraphicsインスタンス
		//バッファの作成
		if(backGrp ==null) backGrp = createImage(width, height);
		gbg = backGrp.getGraphics();
		if(var.updaterule==1){
			for( int i=1; i<var.N;i++){
				for(int j=1;j<var.L;j++){
					gbg.setColor(Color.black);
					gbg.drawRect(var.n*j,y_0length-10*10+var.n*i,var.n*2,var.n*2);
				}
			}
			for(int i=1;i<=var.N;i++){
				for(int j=1; j<=var.L; j++){
					//急いでいる人
					if(var.cell[i][j].get_particlekind() == ParticleKind.HASTE){
						gbg.setColor(Color.red);
						gbg.fillOval(var.n*j+1,y_0length-10*10+var.n*i+1,var.n,var.n);
						num_haste++;
					}
					//急いでいない人
					if(var.cell[i][j].get_particlekind() == ParticleKind.NORMAL){
						gbg.setColor(Color.blue);
						gbg.fillOval(var.n*j+1,y_0length-10*10+var.n*i+1,var.n,var.n);
						num_normal++;
					}
					//出口
					if(var.cell[i][j].get_cellkind() == CellKind.EXIT){
						gbg.setColor(Color.black);
						gbg.fillRect(var.n*j+1,y_0length-10*10+var.n*i+1,var.n,var.n);
					}
					//障壁
					if(var.cell[i][j].get_cellkind() == CellKind.OBSTACLE){
						gbg.setColor(Color.green);
						gbg.fillRect(var.n*j+1,y_0length-10*10+var.n*i+1,var.n,var.n);
					}
				}
			}
			num_particles = num_haste + num_normal;

			//ステップ数と残り粒子数を表示
			gbg.setColor(Color.BLACK);
			gbg.drawString("Step",100, 50);
			gbg.drawString(step+" ",100, 75);
			gbg.setColor(Color.RED);
			gbg.drawString("Haste",150, 50);
			gbg.drawString(num_haste+" ",150, 75);
			gbg.setColor(Color.BLUE);
			gbg.drawString("Normal",200, 50);
			gbg.drawString(num_normal+" ",200, 75);
			gbg.setColor(Color.BLACK);
			gbg.drawString("sum",100, 50);
			gbg.drawString(num_haste+num_normal+" ",250, 75);
			//ダブルバッファの処理
			gbg.dispose();
			g.drawImage(backGrp,0,0,this);
		}
	}

	/***********************************************************************
	 * 表画面をクリアせずに行うpaint の呼び出しメソッド
	 **********************************************************************/
	public void update(Graphics g){
		paint(g);
	}
}