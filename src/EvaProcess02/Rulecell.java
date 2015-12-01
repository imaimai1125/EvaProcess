package EvaProcess02;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Random;

import EvaProcess02.Cell.CellKind;
import EvaProcess02.Cell.ParticleKind;

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
	int number = 1;//粒子の数

	public void initialize(Random rnd, Variable var,double rate_empty, double rate_haste,double SFFrule){
		//rate_empty:全セル中の埋まっている割合
		//rate_haste: 粒子がある中で、hasteの割合
		double empty = rate_empty;
		double haste = (1-rate_empty)*rate_haste;
		//人のばら撒き
		//出口の配置
		for(int i=0; i<var.exit1_size;i++){
			var.cell[var.exit1_y][var.exit1_x+i] = new Cell(0,var,CellKind.EXIT,ParticleKind.EMPTY);
		}
		for(int i=0; i<var.exit2_size;i++){
			var.cell[var.exit2_y][var.exit2_x+1+i] = new Cell(0,var,CellKind.EXIT,ParticleKind.EMPTY);
		}
		//人の配置
		for (int i=0;i<=var.N+1;i++){
			for (int j=0;j<=var.L+1;j++){
				var.celltemp_left[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_right[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_upper[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_lower[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_stay[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				if(var.cell[i][j] ==null){
					double rand = rnd.nextDouble();//二種類の粒子で比べる
					//空スペース
					if (rand <= empty){
						var.cell[i][j] = new Cell(0,var,CellKind.NORMAL,ParticleKind.EMPTY);
					}
					//急いでいる人
					else if(rand > empty && rand <= empty+haste){
						var.cell[i][j] = new Cell(number,var,CellKind.NORMAL,ParticleKind.HASTE);
						if(i!=0&&i!=var.N+1&&j!=0&&j!=var.L+1){
							number++;
						}
					}
					//急いでいない人
					else if(rand >empty+haste){
						var.cell[i][j] = new Cell(number,var,CellKind.NORMAL,ParticleKind.NORMAL);
						if(i!=0&&i!=var.N+1&&j!=0&&j!=var.L+1){
							number++;
						}
					}
				}
				if(SFFrule ==1){
					var.cell[i][j].SFF = SFF_Euclid(var,i,j);
				}else if(SFFrule == 2){
					var.cell[i][j].SFF = SFF_Manhattan(var,i,j);
				}if(i==0||i==var.N+1||j==0||j==var.L+1){
					var.cell[i][j] = new Cell(0,var,CellKind.WALL,ParticleKind.EMPTY);
					var.cell[i][j].SFF = var.N*var.L;
				}
			}
		}
		var.particle_num = number;
	}
	/**************************************
	 * 粒子のアップデート方法を記述
	 * 1:4近傍ランダム（SFF）
	 * 2:8近傍ランダム（SFF)
	 **************************************/
	public void update(int updaterule, Random rnd, Variable var){
		parallelupdate(rnd,var);
	}
	/**************************************
	 * 以下パーツ
	 **************************************/
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
	 * グループ的な効果を導入
	 *1の2に対する引力
	 *SFFを使って確率を更新する際にこの効果を導入する。
	 *（出口までの距離と独立に力が働くように設定してやりたい）
	 *force[0] : y方向, force[1] : x方向
	 **************************************/
	public double[] pair_effect(Variable var,int num1,int num2){
		double[] force = new double[2];
		double distance;
		//退出後は座標(0,0)になってしまうので、ソレは考慮しない
		//num1はあること前提なので、num2に対して条件付け
		if(var.get_coordinate(num2)[0]!=0){
			//distance = Math.sqrt((var.get_coordinate(num2)[0] - var.get_coordinate(num1)[0])^2
			//		+(var.get_coordinate(num2)[1] - var.get_coordinate(num1)[1])^2);
			distance = 1.0;
			force[0] = (var.get_coordinate(num2)[0] - var.get_coordinate(num1)[0]-1)/distance;
			force[1] = (var.get_coordinate(num2)[1] - var.get_coordinate(num1)[1]-1)/distance;
			return force;
		}else{
			force[0] = 0.0;
			force[1] = 0.0;
			return force;
		}
	}

	/**************************************
	 * セルのクリア
	 **************************************/
	public void clear_celltemp(Variable var){
		for (int i=1;i<=var.N;i++){
			for(int j=1;j<=var.L;j++){
				var.celltemp_left[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_right[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_upper[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_lower[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);
				var.celltemp_stay[i][j] = new Cell(0,var, CellKind.NORMAL,ParticleKind.EMPTY);

			}
		}
	}
	/**************************************
	 * 粒子のアップデートルールリスト
	 **************************************/
	//近傍4, パラレルアップデート
	public void parallelupdate(Random rnd, Variable var){
		double[] neighbor = new double[4];	//近隣 0:← 1:→ 2:↑ 3:↓
		double[] prob = new double[4];		//遷移確率
		double rand;		//0-1のランダム変数
		double a,b,c,d;	//コンフリクト解消時に使う。
		//DFFの設定をしてみる
		for (int i=1;i<=var.N;i++){
			for(int j=1;j<=var.L;j++){
				var.cell[i][j].DFF = DFF_update(var,i,j);
			}
		}
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
				//DFFも考えている。考えたくないときはkd = 0にする
				if(var.cell[i][j].particle !=ParticleKind.EMPTY){
					if (var.cell[i][j-1].cell != CellKind.WALL){
						neighbor[0] = Math.exp(-var.cell[i][j-1].SFF * var.cell[i][j].get_ks()
								-var.cell[i][j-1].DFF* var.cell[i][j].get_kd());
					}
					if (var.cell[i][j+1].cell != CellKind.WALL){
						neighbor[1] = Math.exp(-var.cell[i][j+1].SFF * var.cell[i][j].get_ks()
								-var.cell[i][j+1].DFF* var.cell[i][j].get_kd());
					}
					if (var.cell[i-1][j].cell != CellKind.WALL){
						neighbor[2] = Math.exp(-var.cell[i-1][j].SFF * var.cell[i][j].get_ks()
								-var.cell[i-1][j].DFF* var.cell[i][j].get_kd());
					}
					if (var.cell[i+1][j].cell != CellKind.WALL){
						neighbor[3] = Math.exp(-var.cell[i+1][j].SFF * var.cell[i][j].get_ks()
								-var.cell[i+1][j].DFF* var.cell[i][j].get_kd());
					}
					/***************************************
					 * pair effectの導入（10/10製作途中）
					 * 隣接するやつらはペアだとしようという設定
					 * ペアの決め方は発展の必要あり。
					 **************************************/
					double kp =1.0;//係数
					if(var.cell[i][j].get_number()%2 == 1){
						if(pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[0]>0){
							neighbor[3]*=Math.exp(kp*pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[0]);
						}else if(pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[0]<=0){
							neighbor[2]*=Math.exp(-kp*pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[0]);
						}
						if(pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[1]>0){
							neighbor[1]*=Math.exp(kp*pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[1]);
						}else if(pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[1]<0)
							neighbor[0]*=Math.exp(-kp*pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[1]);
					}
					if(var.cell[i][j].get_number()%2 == 0/*&var.cell[i][j].get_number()!=0*/){
						if(pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()-1)[0]>0){
							neighbor[3]*=Math.exp(kp*pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[0]);
						}else if(pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()-1)[0]<=0){
							neighbor[2]*=Math.exp(-kp*pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[0]);
						}
						if(pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()-1)[1]>0){
							neighbor[1]*=Math.exp(kp*pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[1]);
						}else if(pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()-1)[1]<0)
							neighbor[0]*=Math.exp(-kp*pair_effect(var,var.cell[i][j].get_number(),var.cell[i][j].get_number()+1)[1]);
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
					 * 10/5移動のとき同時に粒子番号も更新してあげる
					 *****************************************************/
					//←に移動する確率
					if (rand<prob[0]){
						if(var.cell[i][j-1].particle == ParticleKind.EMPTY){
							var.celltemp_left[i][j-1].particle = var.cell[i][j].particle;
							var.celltemp_left[i][j-1].set_number(var.cell[i][j].get_number());
						}else if(var.cell[i][j-1].particle != ParticleKind.EMPTY){
							var.celltemp_stay[i][j].particle = var.cell[i][j].particle;
							var.celltemp_stay[i][j].set_number(var.cell[i][j].get_number());
						}
					}//→に遷移する確率
					else if(prob[0]<=rand&&rand<prob[0]+prob[1]){
						if(var.cell[i][j+1].particle == ParticleKind.EMPTY){
							var.celltemp_right[i][j+1].particle = var.cell[i][j].particle;
							var.celltemp_right[i][j+1].set_number(var.cell[i][j].get_number());
						}else if(var.cell[i][j+1].particle != ParticleKind.EMPTY){
							var.celltemp_stay[i][j].particle = var.cell[i][j].particle;
							var.celltemp_stay[i][j].set_number(var.cell[i][j].get_number());
						}
					}//↑に遷移する確率
					else if(prob[0]+prob[1]<=rand &&rand<prob[0]+prob[1]+prob[2]){
						if(var.cell[i-1][j].particle == ParticleKind.EMPTY){
							var.celltemp_upper[i-1][j].particle = var.cell[i][j].particle;
							var.celltemp_upper[i-1][j].set_number(var.cell[i][j].get_number());
						}else if(var.cell[i-1][j].particle != ParticleKind.EMPTY){
							var.celltemp_stay[i][j].particle = var.cell[i][j].particle;
							var.celltemp_stay[i][j].set_number(var.cell[i][j].get_number());
						}
					}//↓に遷移する確率
					else{
						if(var.cell[i+1][j].particle == ParticleKind.EMPTY){
							var.celltemp_lower[i+1][j].particle = var.cell[i][j].particle;
							var.celltemp_lower[i+1][j].set_number(var.cell[i][j].get_number());
						}else if(var.cell[i+1][j].particle != ParticleKind.EMPTY){
							var.celltemp_stay[i][j].particle = var.cell[i][j].particle;
							var.celltemp_stay[i][j].set_number(var.cell[i][j].get_number());
						}
					}
				}
			}
		}
		for (int i=1;i<=var.N;i++){
			for(int j=1;j<=var.L;j++){
				var.cell[i][j].particle = ParticleKind.EMPTY;
				var.cell[i][j].set_number(0);
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
						//						aruthuthijtirah粒子番号を一緒に更新してやる
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
					}
					else if(rand>=a*(1-b)*(1-c)*(1-d)&&rand< a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)
							&&rand<a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)+(1-a)*(1-b)*c*(1-d)){
						//cは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_upper[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)+(1-a)*(1-b)*c*(1-d)
							&&rand<a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)+(1-a)*(1-b)*c*(1-d)+(1-a)*(1-b)*(1-c)*d){
						//dは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_lower[i][j].get_number());
					}
					if(rand>a*(1-b)*(1-c)*(1-d)+(1-a)*b*(1-c)*(1-d)+(1-a)*(1-b)*c*(1-d)+(1-a)*(1-b)*(1-c)*d){
						//全て入れない→元に戻す
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)){
						//bは入れる
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//cは入れる
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//どれも入れない
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)){
						//bは入れる
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_lower[i][j].get_number());
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//cは入れる
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_right[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//どれも入れない
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_lower[i][j].get_number());
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)){
						//bは入れる
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//cは入れる
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_upper[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//どれも入れない
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c) && rand<a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//cは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_left[i][j].get_number());
					}else if(rand>=a*(1-b)*(1-c)+(1-a)*b*(1-c)+(1-a)*(1-b)*c){
						//どれも入れない
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_upper[i][j].get_number());
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_left[i][j].get_number());
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i][j-1].particle = var.celltemp_right[i][j].particle;
						var.cell[i][j-1].set_number(var.celltemp_right[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_left[i][j].get_number());
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i+1][j].particle = var.celltemp_upper[i][j].particle;
						var.cell[i+1][j].set_number(var.celltemp_upper[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
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
						var.cell[i][j].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)&&rand<a*(1-b)+(1-a)*b){
						//bは入れる
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i][j].set_number(var.celltemp_lower[i][j].get_number());
					}else if(rand>=a*(1-b)+(1-a)*b){
						//どっちも入れない
						var.cell[i][j+1].particle = var.celltemp_left[i][j].particle;
						var.cell[i][j+1].set_number(var.celltemp_left[i][j].get_number());
						var.cell[i-1][j].particle = var.celltemp_lower[i][j].particle;
						var.cell[i-1][j].set_number(var.celltemp_lower[i][j].get_number());
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
					var.cell[i][j].set_number(var.celltemp_right[i][j].get_number());
				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_upper[i][j].particle;
					var.cell[i][j].set_number(var.celltemp_upper[i][j].get_number());
				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_left[i][j].particle;
					var.cell[i][j].set_number(var.celltemp_left[i][j].get_number());
				}
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle != ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle == ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_lower[i][j].particle;
					var.cell[i][j].set_number(var.celltemp_lower[i][j].get_number());
				}
				//粒子なし（１通り）
				else if(var.celltemp_right[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_upper[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_left[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_lower[i][j].particle == ParticleKind.EMPTY
						&& var.celltemp_stay[i][j].particle != ParticleKind.EMPTY){
					var.cell[i][j].particle = var.celltemp_stay[i][j].particle;
					var.cell[i][j].set_number(var.celltemp_stay[i][j].get_number());
				}
				//出口に来た粒子は削除
				int x;
				int y;
				if(var.cell[i][j].cell == CellKind.EXIT){
					var.cell[i][j].particle = ParticleKind.EMPTY;
					if(var.cell[i][j].get_number()!=0){
						x = var.cell[i][j].get_number()%var.L;
						y = var.cell[i][j].get_number()/var.L;
						//						System.out.println("初期値("+x+","+y+")の粒子が退出しました");

					}
				}
			}
		}
		clear_celltemp(var);
	}

}
