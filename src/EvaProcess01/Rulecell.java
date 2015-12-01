package EvaProcess01;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Random;

import EvaProcess01.Cell.CellKind;
import EvaProcess01.Cell.ParticleKind;

/**
 *
 * @author imaimai
 * セル同士の関連と初期配置記述したもの
 */
public class Rulecell {
	/**************************************
	 * 粒子とセルの初期設定
	 * 出口をVariableの変数にしたがって構成
	 * SFFruleは1:ユークリッド 2:マンハッタン
	 **************************************/
	public void initialize(Random rnd, Variable var,double rate_empty, double rate_haste,double SFFrule){
		//rate_empty:全セル中の埋まっている割合
		//rate_haste: 粒子がある中で、hasteの割合
		double empty = rate_empty;
		double haste = (1-rate_empty)*rate_haste;
		//人のばら撒き
		//出口の配置
		for(int i=0; i<var.exit1_size;i++){
			var.cell[var.exit1_y][var.exit1_x+i] = new Cell(var,CellKind.EXIT,ParticleKind.EMPTY);
		}
		for(int i=0; i<var.exit2_size;i++){
			var.cell[var.exit2_y][var.exit2_x+1+i] = new Cell(var,CellKind.EXIT,ParticleKind.EMPTY);
		}
		//人の配置
//		for (int i=0;i<=var.N+1;i++){
//			for (int j=0;j<=var.L+1;j++){
//				var.celltemp_left[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
//				var.celltemp_right[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
//				var.celltemp_upper[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
//				var.celltemp_lower[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
//				var.celltemp_stay[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
//				if(var.cell[i][j] ==null){
//					double rand = rnd.nextDouble();//二種類の粒子で比べる
//					//空スペース
//					if (rand <= empty){
//						var.cell[i][j] = new Cell(var,CellKind.NORMAL,ParticleKind.EMPTY);
//					}
//					//急いでいる人
//					else if(rand > empty && rand <= empty+haste){
//						var.cell[i][j] = new Cell(var,CellKind.NORMAL,ParticleKind.HASTE);
//					}
//					//急いでいない人
//					else if(rand >empty+haste){
//						var.cell[i][j] = new Cell(var,CellKind.NORMAL,ParticleKind.NORMAL);
//					}
//				}
//				if(SFFrule ==1){
//					var.cell[i][j].SFF = SFF_Euclid(var,i,j);
//				}else if(SFFrule == 2){
//					var.cell[i][j].SFF = SFF_Manhattan(var,i,j);
//				}
//				//壁の設定(外枠)
//				if(i==0||i==var.N+1||j==0||j==var.L+1){
//					var.cell[i][j] = new Cell(var,CellKind.WALL,ParticleKind.EMPTY);
//					var.cell[i][j].SFF = var.N*var.L;
//				}
//			}
//		}//ヒトの配置
//		ヒトの配置その2
		for (int i=0;i<=var.N/2;i++){
			for (int j=0;j<=var.L+1;j++){
				var.celltemp_left[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_right[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_upper[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_lower[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_stay[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				if(var.cell[i][j] == null){
					double rand = rnd.nextDouble();//二種類の粒子で比べる
					//空スペース
					if (rand <= empty){
						var.cell[i][j] = new Cell(var,CellKind.NORMAL,ParticleKind.EMPTY);
					}
					//急いでいる人
					else if(rand > empty && rand <= empty+haste){
						var.cell[i][j] = new Cell(var,CellKind.NORMAL,ParticleKind.HASTE);
					}
					//急いでいない人
					else if(rand >empty+haste){
						var.cell[i][j] = new Cell(var,CellKind.NORMAL,ParticleKind.NORMAL);
					}
				}
				if(SFFrule ==1){
					var.cell[i][j].SFF = SFF_Euclid(var,i,j);
				}else if(SFFrule == 2){
					var.cell[i][j].SFF = SFF_Manhattan(var,i,j);
				}
				//壁の設定(外枠)
				if(i==0||i==var.N+1||j==0||j==var.L+1){
					var.cell[i][j] = new Cell(var,CellKind.WALL,ParticleKind.EMPTY);
					var.cell[i][j].SFF = var.N*var.L;
				}
			}
		}
		for (int i=var.N/2+1;i<=var.N+1;i++){
			for (int j=0;j<=var.L+1;j++){
				var.celltemp_left[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_right[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_upper[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_lower[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_stay[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				if(var.cell[i][j] == null){	
					var.cell[i][j] = new Cell(var,CellKind.NORMAL,ParticleKind.EMPTY);
					if(SFFrule ==1){
						var.cell[i][j].SFF = 
								SFF_Euclid(var,i,j);
					}else if(SFFrule == 2){
						var.cell[i][j].SFF = SFF_Manhattan(var,i,j);
					}
				}
				//壁の設定(外枠)
				if(i==0||i==var.N+1||j==0||j==var.L+1){
					var.cell[i][j] = new Cell(var,CellKind.WALL,ParticleKind.EMPTY);
					var.cell[i][j].SFF = var.N*var.L;
				}
			}
		}//ヒトの配置その２

		//Obstacleの設定(11/18)
		int aaa = 0;
		for(int i = 25; i< 49 ; i+=5){
			for(int j = 1; j <49 ; j +=5){
				Obstacle_Rect(var,i,j,aaa,aaa);
			}
		}

	}
	/**************************************
	 * 粒子のアップデート方法を記述
	 * 1:4近傍ランダム（SFF）
	 * 2:8近傍ランダム（SFF)
	 **************************************/
	public void update(int updaterule, Random rnd, Variable var){
		if(updaterule ==1){
			randomupdate1(rnd,var);
		}
		else if(updaterule ==2){
			randomupdate2(rnd,var);
		}
		else if(updaterule ==3){
			parallelupdate1(rnd,var);
		}
	}

	/**************************************
	 * 以下パーツ
	 **************************************/
	/**************************************
	 *obstacleの設置
	 *Obstacle_Rect( int y_at, int x_at,int y, int x)
	 *	長方形型：左端が(x_at,y_at),x*yの長さの辺を持つ長方形
	 **************************************/
	public void Obstacle_Rect(Variable var,int y_at, int x_at,int y, int x){
		for(int i = y_at; i <= y_at + y;i++){
			for(int j = x_at; j <= x_at + x;j++){
				var.cell[i][j] = new Cell(var,CellKind.OBSTACLE,ParticleKind.EMPTY);
				var.cell[i][j].SFF = var.N*var.L;
			}
		}
	}
	/**************************************
	 * Static Floor Field
	 * Dynamic Floor Field
	 * の作成（マンハッタンver,ユークリッドver)
	 * 障害物を考慮していない。
	 **************************************/
	public double SFF_Manhattan(Variable var,int i ,int j){
		//Static Floor Fieldの値を決定（出口二つのうちの近いほう）
		//i,jとxyは基本的に向きが違うので注意
		double distance[];
		distance = new double[var.exit1_size+var.exit2_size];
		for (int k=0;k<var.exit1_size;k++){
			distance[k] = Math.abs(var.exit1_x+k-j)+Math.abs(var.exit1_y-i);
		}
		for (int k=0;k<var.exit2_size;k++){
			distance[var.exit1_size+k] = Math.abs(var.exit2_x+k-j+1)+Math.abs(var.exit2_y-i);
		}
		double min = distance[0];
		for (int k=0;k<distance.length;k++){
			if (min>distance[k]){
				min = distance[k];
			}
		}
		return min;
	}
	public double SFF_Euclid(Variable var, int i, int j){
		//Static Floor Fieldの値を決定（出口二つのうちの近いほう）
		//i,jとxyは基本的に向きが違うので注意
		double distance[];
		distance = new double[var.exit1_size+var.exit2_size];
		for (int k=0;k<var.exit1_size;k++){
			distance[k] = Math.sqrt((var.exit1_x+k-j)*(var.exit1_x+k-j)+(var.exit1_y-i)*(var.exit1_y-i));
		}
		for (int k=0;k<var.exit2_size;k++){
			distance[var.exit1_size+k] = Math.sqrt((var.exit2_x+k-j+1)*(var.exit2_x+k-j+1)+(var.exit2_y-i)*(var.exit2_y-i));
		}
		double min = distance[0];
		for (int k=0;k<distance.length;k++){
			if (min>distance[k]){
				min = distance[k];
			}
		}
		return min;
	}
	public double DFF_update(Variable var,int i, int j){
		return (1-var.DFF_alpha)*(1-var.DFF_delta)+var.DFF_alpha*(1-var.DFF_delta)
				*(var.cell[i+1][j].DFF+var.cell[i-1][j].DFF+var.cell[i][j+1].DFF+var.cell[i][j-1].DFF);
	}
	/**************************************
	 * 粒子の移動ルール
	 ①x→x+jに移動，y→y+iに移動
	 ②移動先が開いていたら移動。あいてなかったら無理
	 ③移動先がEXITなら消える
	 9/17:なぜか使えない
	 **************************************/
	public void move_particles(Variable var,int y, int x, int i,int j){
		if(var.cell[y+i][x+j].particle == ParticleKind.EMPTY){
			var.cell[y+i][x+j].particle = var.cell[y][x].particle;
			var.cell[y+i][x+j].particle = ParticleKind.EMPTY;
		}
		if(var.cell[y+i][x+j].cell == CellKind.EXIT){
			var.cell[y+i][x+j].particle = ParticleKind.EMPTY;
		}
	}
	/**************************************
	 * セルのクリア
	 **************************************/
	public void clear_celltemp(Variable var){
		for (int i=1;i<=var.N;i++){
			for(int j=1;j<=var.L;j++){
				var.celltemp_left[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_right[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_upper[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_lower[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_stay[i][j] = new Cell(var, CellKind.NORMAL,ParticleKind.EMPTY);

			}
		}
	}
	/**************************************
	 * 粒子のアップデートルールリスト
	 **************************************/
	//近傍4, ランダムアップデート
	public void randomupdate1(Random rnd, Variable var){
		int rand_x = rnd.nextInt(var.L+1);
		int rand_y = rnd.nextInt(var.N+1);
		double[] neighbor = new double[4];	//近隣 0:← 1:→ 2:↑ 3:↓
		double[] prob = new double[4];		//遷移確率
		double sum = 0;
		double rand;		//0-1のランダム変数
		//近傍4セルの状態チェック
		//セルチェック（壁は除外）
		if(var.cell[rand_y][rand_x].particle !=ParticleKind.EMPTY){
			if (var.cell[rand_y][rand_x-1].cell != CellKind.WALL){
				neighbor[0] = Math.exp(-var.cell[rand_y][rand_x-1].SFF * var.cell[rand_y][rand_x].get_ks());
			}
			if (var.cell[rand_y][rand_x+1].cell != CellKind.WALL){
				neighbor[1] = Math.exp(-var.cell[rand_y][rand_x+1].SFF * var.cell[rand_y][rand_x].get_ks());
			}
			if (var.cell[rand_y-1][rand_x].cell != CellKind.WALL){
				neighbor[2] = Math.exp(-var.cell[rand_y-1][rand_x].SFF * var.cell[rand_y][rand_x].get_ks());
			}
			if (var.cell[rand_y+1][rand_x].cell != CellKind.WALL){
				neighbor[3] = Math.exp(-var.cell[rand_y+1][rand_x].SFF * var.cell[rand_y][rand_x].get_ks());
			}
			//規格化定数の設定
			for (int i=0; i<4;i++){
				sum+=neighbor[i];
			}
			//近隣が開いていたらそこに動ける遷移確率を算出
			for (int i=0; i<neighbor.length;i++){
				prob[i] = neighbor[i]/sum;
			}
			rand = Math.random();
			//遷移確率にしたがって動かす
			if (rand<prob[0]){//←に移動する確率
				if(var.cell[rand_y][rand_x-1].particle == ParticleKind.EMPTY){
					var.cell[rand_y][rand_x-1].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y][rand_x-1].cell == CellKind.EXIT){
					var.cell[rand_y][rand_x-1].particle = ParticleKind.EMPTY;
				}
			}
			//→に遷移する確率
			if(prob[0]<=rand&&rand<prob[0]+prob[1]){
				if(var.cell[rand_y][rand_x+1].particle == ParticleKind.EMPTY){
					var.cell[rand_y][rand_x+1].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y][rand_x+1].cell == CellKind.EXIT){
					var.cell[rand_y][rand_x+1].particle = ParticleKind.EMPTY;
				}
			}//↑に遷移する確率
			if(prob[0]+prob[1]<=rand &&rand<=prob[0]+prob[1]+prob[2]){
				if(var.cell[rand_y-1][rand_x].particle == ParticleKind.EMPTY){
					var.cell[rand_y-1][rand_x].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y-1][rand_x].cell == CellKind.EXIT){
					var.cell[rand_y-1][rand_x].particle = ParticleKind.EMPTY;
				}
			}
			//↓に遷移する確率
			if(prob[0]+prob[1]+prob[2]<=rand &&rand<=1){
				if(var.cell[rand_y+1][rand_x].particle == ParticleKind.EMPTY){
					var.cell[rand_y+1][rand_x].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y+1][rand_x].cell == CellKind.EXIT){
					var.cell[rand_y+1][rand_x].particle = ParticleKind.EMPTY;
				}
			}
		}
	}//randomupdate1
	//近傍8,ランダムアップデート
	public void randomupdate2(Random rnd, Variable var){
		int rand_x = 1+rnd.nextInt(var.L);
		int rand_y = 1+rnd.nextInt(var.N);
		double[] neighbor = new double[8];	//0:0° 1:45° 2:90° 3:135° 4:180° 5:225° 6:270° 7:315°
		double[] prob = new double[8];		//遷移確率
		double sum = 0;
//		double max = 0;
		double rand;		//0-1のランダム変数
		//近傍8セルの状態チェック
		//セルチェック（壁は除外）
		if(var.cell[rand_y][rand_x].particle !=ParticleKind.EMPTY){
			//0
			if (var.cell[rand_y][rand_x+1].cell != CellKind.WALL){
				neighbor[0] = Math.exp(-var.cell[rand_y][rand_x+1].SFF * var.cell[rand_y][rand_x].get_ks());
			}//45
			if (var.cell[rand_y+1][rand_x+1].cell != CellKind.WALL){
				neighbor[1] = Math.exp((-0.5-var.cell[rand_y+1][rand_x+1].SFF )* var.cell[rand_y][rand_x].get_ks());
			}//90
			if (var.cell[rand_y+1][rand_x].cell != CellKind.WALL){
				neighbor[2] = Math.exp(-var.cell[rand_y+1][rand_x].SFF * var.cell[rand_y][rand_x].get_ks());
			}//135
			if (var.cell[rand_y+1][rand_x-1].cell != CellKind.WALL){
				neighbor[3] = Math.exp((-0.5-var.cell[rand_y+1][rand_x-1].SFF )* var.cell[rand_y][rand_x].get_ks());
			}//180
			if (var.cell[rand_y][rand_x-1].cell != CellKind.WALL){
				neighbor[4] = Math.exp(-var.cell[rand_y][rand_x-1].SFF * var.cell[rand_y][rand_x].get_ks());
			}//225
			if (var.cell[rand_y-1][rand_x-1].cell != CellKind.WALL){
				neighbor[5] = Math.exp((-0.5-var.cell[rand_y-1][rand_x-1].SFF) * var.cell[rand_y][rand_x].get_ks());
			}//270
			if (var.cell[rand_y-1][rand_x].cell != CellKind.WALL){
				neighbor[6] = Math.exp(-var.cell[rand_y-1][rand_x].SFF * var.cell[rand_y][rand_x].get_ks());
			}//315
			if (var.cell[rand_y-1][rand_x+1].cell != CellKind.WALL){
				neighbor[7] = Math.exp((-0.5-var.cell[rand_y-1][rand_x+1].SFF )* var.cell[rand_y][rand_x].get_ks());
			}
			//規格化定数の設定
			for (int i=0; i<8;i++){
				sum+=neighbor[i];
			}
			//近隣が開いていたらそこに動ける遷移確率を算出
			for (int i=0; i<neighbor.length;i++){
				prob[i] = neighbor[i]/sum;
			}

			rand = Math.random();
			double probability = 0;
			//遷移確率にしたがって動かす
			//0
			if (rand<prob[0]){
				if(var.cell[rand_y][rand_x+1].particle == ParticleKind.EMPTY){
					var.cell[rand_y][rand_x+1].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y][rand_x+1].cell == CellKind.EXIT){
					var.cell[rand_y][rand_x+1].particle = ParticleKind.EMPTY;
				}
			}
			probability +=prob[0];
			//45
			if(probability<=rand&&rand<probability+prob[1]){
				if(var.cell[rand_y+1][rand_x+1].particle == ParticleKind.EMPTY){
					var.cell[rand_y+1][rand_x+1].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y+1][rand_x+1].cell == CellKind.EXIT){
					var.cell[rand_y+1][rand_x+1].particle = ParticleKind.EMPTY;
				}
			}
			probability +=prob[1];
			//90
			if(probability<=rand &&rand<=probability+prob[2]){
				if(var.cell[rand_y+1][rand_x].particle == ParticleKind.EMPTY){
					var.cell[rand_y+1][rand_x].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y+1][rand_x].cell == CellKind.EXIT){
					var.cell[rand_y+1][rand_x].particle = ParticleKind.EMPTY;
				}
			}
			probability +=prob[2];
			//135
			if(probability<=rand &&rand<=probability+prob[3]){
				if(var.cell[rand_y+1][rand_x-1].particle == ParticleKind.EMPTY){
					var.cell[rand_y+1][rand_x-1].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y+1][rand_x-1].cell == CellKind.EXIT){
					var.cell[rand_y+1][rand_x-1].particle = ParticleKind.EMPTY;
				}
			}
			probability +=prob[3];
			//180
			if (probability<rand&&rand<=probability+prob[4]){
				if(var.cell[rand_y][rand_x-1].particle == ParticleKind.EMPTY){
					var.cell[rand_y][rand_x-1].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y][rand_x-1].cell == CellKind.EXIT){
					var.cell[rand_y][rand_x-1].particle = ParticleKind.EMPTY;
				}
			}
			probability +=prob[4];
			//225
			if(probability<=rand&&rand<probability+prob[5]){
				if(var.cell[rand_y][rand_x-1].particle == ParticleKind.EMPTY){
					var.cell[rand_y][rand_x-1].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y][rand_x-1].cell == CellKind.EXIT){
					var.cell[rand_y][rand_x-1].particle = ParticleKind.EMPTY;
				}
			}
			probability +=prob[5];
			//270
			if(probability<=rand &&rand<=probability+prob[6]){
				if(var.cell[rand_y-1][rand_x].particle == ParticleKind.EMPTY){
					var.cell[rand_y-1][rand_x].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y-1][rand_x].cell == CellKind.EXIT){
					var.cell[rand_y-1][rand_x].particle = ParticleKind.EMPTY;
				}
			}
			probability +=prob[6];
			//315
			if(probability<=rand &&rand<=1){
				if(var.cell[rand_y-1][rand_x+1].particle == ParticleKind.EMPTY){
					var.cell[rand_y-1][rand_x+1].particle = var.cell[rand_y][rand_x].particle;
					var.cell[rand_y][rand_x].particle = ParticleKind.EMPTY;
				}
				if(var.cell[rand_y-1][rand_x+1].cell == CellKind.EXIT){
					var.cell[rand_y-1][rand_x+1].particle = ParticleKind.EMPTY;
				}
			}
		}
	}//randomupdate2


	//近傍4, パラレルアップデート
	public void parallelupdate1(Random rnd, Variable var){
		double[] neighbor = new double[4];	//近隣 0:← 1:→ 2:↑ 3:↓
		double[] prob = new double[4];		//遷移確率
		double rand;		//0-1のランダム変数
		double a,b,c,d;	//コンフリクト解消時に使う。
		//①すべてのセルに対して近傍4セルの状態チェック
		//②動きたいセルを決定する。
		for (int i=1;i<=var.N;i++){
			for(int j=1;j<=var.L;j++){
				double sum = 0.0;	//規格化定数
				neighbor[0] = 0;
				neighbor[1] = 0;
				neighbor[2] = 0;
				neighbor[3] = 0;
				//System.out.println(var.cell[i][j].particle+", "+var.cell[i][j].get_ks()+", "+var.cell[i][j].get_haste_index());
				if(var.cell[i][j].particle !=ParticleKind.EMPTY){
					if (var.cell[i][j-1].cell != CellKind.WALL){
						neighbor[0] = Math.exp(-var.cell[i][j-1].SFF * var.cell[i][j].get_ks());
					}
					if (var.cell[i][j+1].cell != CellKind.WALL){
						neighbor[1] = Math.exp(-var.cell[i][j+1].SFF * var.cell[i][j].get_ks());
					}
					if (var.cell[i-1][j].cell != CellKind.WALL){
						neighbor[2] = Math.exp(-var.cell[i-1][j].SFF * var.cell[i][j].get_ks());
					}
					if (var.cell[i+1][j].cell != CellKind.WALL){
						neighbor[3] = Math.exp(-var.cell[i+1][j].SFF * var.cell[i][j].get_ks());
					}
					//規格化定数の設定
					for (int k=0; k<4;k++){
						sum+=(double)neighbor[k];
					}
					//近隣が開いていたらそこに動ける遷移確率を算出
					for (int k=0; k<neighbor.length;k++){
						prob[k] = neighbor[k]/sum;
					}
					rand = Math.random();
					//遷移確率にしたがって動かす
					/*****************************************************
				 自分が動きたい方向を決定して，かつそのセルが空いていればtmpに移動
					 * 左ならtemp_leftのシート
					 * 右ならtemp_rightのシート
					 * 上ならtemp_upperのシート
					 * 下ならtemp_lowerのシートに入れる（かぶりを防ぐため）
					 *****************************************************/
					//←に移動する確率
					if (rand<prob[0]){
						if(var.cell[i][j-1].particle == ParticleKind.EMPTY){
							var.celltemp_left[i][j-1].particle = var.cell[i][j].particle;
						}else if(var.cell[i][j-1].particle != ParticleKind.EMPTY){
							var.celltemp_stay[i][j].particle = var.cell[i][j].particle;
						}
					}//→に遷移する確率
					else if(prob[0]<=rand&&rand<prob[0]+prob[1]){
						if(var.cell[i][j+1].particle == ParticleKind.EMPTY){
							var.celltemp_right[i][j+1].particle = var.cell[i][j].particle;
							}else if(var.cell[i][j+1].particle != ParticleKind.EMPTY){
							var.celltemp_stay[i][j].particle = var.cell[i][j].particle;
						}
					}//↑に遷移する確率
					else if(prob[0]+prob[1]<=rand &&rand<prob[0]+prob[1]+prob[2]){
						if(var.cell[i-1][j].particle == ParticleKind.EMPTY){
							var.celltemp_upper[i-1][j].particle = var.cell[i][j].particle;
							}else if(var.cell[i-1][j].particle != ParticleKind.EMPTY){
							var.celltemp_stay[i][j].particle = var.cell[i][j].particle;
						}
					}//↓に遷移する確率
					else{
						if(var.cell[i+1][j].particle == ParticleKind.EMPTY){
							var.celltemp_lower[i+1][j].particle = var.cell[i][j].particle;
							}else if(var.cell[i+1][j].particle != ParticleKind.EMPTY){
							var.celltemp_stay[i][j].particle = var.cell[i][j].particle;

						}
					}
				}
			}
		}
		for (int i=1;i<=var.N;i++){
			for(int j=1;j<=var.L;j++){
				var.cell[i][j].particle = ParticleKind.EMPTY;
			}
		}
		/*************************************************************
		 * コンフリクトを解消する作業
		 * left, right, upper, lowerのセルを重ね合わせて二つ以上入っていると
		 * conflictが発生しているので，プロトコルに従ってコンフリクト解消する
		 * ①4粒子がコンフリクトしている
		 * ②3粒子がコンフリクトしている
		 * ③2粒子がコンフリクトしている
		 * ④コンフリクトは内
		 *************************************************************/
		for (int i=1;i<=var.N;i++){
			for(int j=1;j<=var.L;j++){
				rand = Math.random();
				/***********************************************************
				 *排反な事象で場合分け
			①動いたかどうか
			②それぞれのtempcellに粒子が入っているか)2^4通り
				 ************************************************************/
				/***********************************************************
				 *4粒子衝突
				 ************************************************************/
				if(var.celltemp_right[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_right[i][j].get_haste_index();
					b = var.celltemp_upper[i][j].get_haste_index();
					c = var.celltemp_left[i][j].get_haste_index();
					d = var.celltemp_lower[i][j].get_haste_index();
					if(rand<a*(1-b)*(1-c)*(1-d)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}
					else if(rand>=a*(1-b)*(1-c)*(1-d)&&rand< a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)
							&&rand<a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)+(1-a)*(1-b)*c*(1-d)){
						//cは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)+(1-a)*(1-b)*c*(1-d)
							&&rand<a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)+(1-a)*(1-b)*c*(1-d)+(1-a)*(1-b)*(1-c)*d){
						//dは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
					}
					if(rand>a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)+(1-a)*(1-b)*c*(1-d)+(1-a)*(1-b)*(1-c)*d){
						//全て入れない→元に戻す
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}
				}
				/***********************************************************
				 *３粒子衝突
				 ************************************************************/
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_upper[i][j].get_haste_index();
					b = var.celltemp_left[i][j].get_haste_index();
					c = var.celltemp_lower[i][j].get_haste_index();
					if(rand < a*(1-b)*(1-c)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)){
						//bは入れる
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//cは入れる
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//どれも入れない
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}
				}
				else if(var.celltemp_right[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_left[i][j].get_haste_index();
					b = var.celltemp_lower[i][j].get_haste_index();
					c = var.celltemp_right[i][j].get_haste_index();
					if(rand < a*(1-b)*(1-c)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)){
						//bは入れる
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//cは入れる
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//どれも入れない
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
					}
				}
				else if(var.celltemp_right[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_lower[i][j].get_haste_index();
					b = var.celltemp_right[i][j].get_haste_index();
					c = var.celltemp_upper[i][j].get_haste_index();
					if(rand < a*(1-b)*(1-c)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)){
						//bは入れる
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//cは入れる
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//どれも入れない
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
					}
				}
				else if(var.celltemp_right[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_right[i][j].get_haste_index();
					b = var.celltemp_upper[i][j].get_haste_index();
					c = var.celltemp_left[i][j].get_haste_index();
					if(rand < a*(1-b)*(1-c)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//cは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//どれも入れない
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
					}

				}
				/***********************************************************
				 *２粒子衝突
				 ************************************************************/
				else if(var.celltemp_right[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_right[i][j].get_haste_index();
					b = var.celltemp_upper[i][j].get_haste_index();
					if(rand<a*(1-b)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
					}
				}
				else if(var.celltemp_right[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_right[i][j].get_haste_index();
					b = var.celltemp_left[i][j].get_haste_index();
					if(rand<a*(1-b)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
					}
				}
				else if(var.celltemp_right[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_right[i][j].get_haste_index();
					b = var.celltemp_lower[i][j].get_haste_index();
					if(rand<a*(1-b)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}
				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_upper[i][j].get_haste_index();
					b = var.celltemp_left[i][j].get_haste_index();
					if(rand<a*(1-b)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
					}

				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_upper[i][j].get_haste_index();
					b = var.celltemp_lower[i][j].get_haste_index();
					if(rand<a*(1-b)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}

				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					a = var.celltemp_left[i][j].get_haste_index();
					b = var.celltemp_lower[i][j].get_haste_index();
					if(rand<a*(1-b)){
						//aは入れる
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
					}
				}
				/***********************************************************
				 *衝突なし
				 ************************************************************/
				else if(var.celltemp_right[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_right[i][j].particle;
				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_left[i][j].particle;
				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
				}
				//粒子なし（１通り）
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle != ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_stay[i][j].particle;
				}
				//出口に来た粒子は削除
				if(var.cell[i][j].cell == CellKind.EXIT){
					var.cell[i][j].particle = ParticleKind.EMPTY;
				}
			}
		}
		clear_celltemp(var);
	}//void parallelupdate1


}